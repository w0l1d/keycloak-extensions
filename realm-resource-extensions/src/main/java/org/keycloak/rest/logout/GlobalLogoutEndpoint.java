package org.keycloak.rest.logout;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.NoCache;
import org.keycloak.models.ClientModel;
import org.keycloak.models.ClientSessionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.keycloak.representations.IDToken;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.services.util.DefaultClientSessionContext;
import org.keycloak.util.TokenUtil;

import java.net.URI;

@Slf4j
public class GlobalLogoutEndpoint {
    private final KeycloakSession session;
    private final AuthenticationManager.AuthResult auth;

    public GlobalLogoutEndpoint(KeycloakSession session) {
        this.session = session;
        this.auth = authenticateSession(session);
    }

    private AuthenticationManager.AuthResult authenticateSession(KeycloakSession session) {
        AuthenticationManager.AuthResult authResult =
                new AppAuthManager.BearerTokenAuthenticator(session).authenticate();

        if (authResult == null) {
            authResult = new AppAuthManager()
                    .authenticateIdentityCookie(session, session.getContext().getRealm());
        }
        return authResult;
    }

    @GET
    @Path("")
    @NoCache
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response initiateGlobalLogout() {
        if (auth == null || auth.getUser() == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        RealmModel realm = session.getContext().getRealm();
        ClientModel client = session.clients().getClientByClientId(realm, "account-console");
        session.getContext().setClient(client);

        URI redirectURI = buildLogoutRedirectURI(realm, client);
        return Response.seeOther(redirectURI).build();
    }

    private URI buildLogoutRedirectURI(RealmModel realm, ClientModel client) {
        URI baseRedirectURI = UriBuilder.fromUri(session.getContext().getAuthServerUrl())
                .path(client.getBaseUrl())
                .build();

        IDToken idToken = generateIdToken(client);

        return UriBuilder.fromUri(session.getContext().getAuthServerUrl())
                .path("/realms/%s/protocol/openid-connect/logout".formatted(realm.getName()))
                .queryParam("post_logout_redirect_uri", baseRedirectURI)
                .queryParam("client_id", client.getClientId())
                .queryParam("id_token_hint", session.tokens().encode(idToken))
                .build();
    }

    private IDToken generateIdToken(ClientModel client) {
        ClientSessionContext clientSessionCtx = DefaultClientSessionContext
                .fromClientSessionScopeParameter(
                        auth.getSession().getAuthenticatedClientSessionByClient(client.getId()),
                        session
                );

        IDToken idToken = new IDToken();
        idToken.id(KeycloakModelUtils.generateId());
        idToken.type(TokenUtil.TOKEN_TYPE_ID);
        idToken.subject(auth.getUser().getId());
        idToken.audience(client.getClientId());
        idToken.issuedNow();
        idToken.issuedFor(client.getClientId());
        idToken.issuer(auth.getToken().getIssuer());
        idToken.setNonce(clientSessionCtx.getAttribute(OIDCLoginProtocol.NONCE_PARAM, String.class));
        idToken.setSessionId(auth.getToken().getSessionId());
        idToken.exp(auth.getToken().getExp());

        return idToken;
    }
}
