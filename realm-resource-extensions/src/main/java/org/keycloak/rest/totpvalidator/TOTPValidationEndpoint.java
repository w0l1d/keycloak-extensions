/*
 * The MIT License
 * Copyright © 2025 Walid Ahdouf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.keycloak.rest.totpvalidator;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.NoCache;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.credential.OTPCredentialProvider;
import org.keycloak.credential.OTPCredentialProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.OTPCredentialModel;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;

import static java.util.Objects.isNull;

@Slf4j
@Path("")
public class TOTPValidationEndpoint {

    private final KeycloakSession session;

    public TOTPValidationEndpoint(KeycloakSession session) {
        this.session = session;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    public Response validateTOTP(TOTPValidationRequest request) {
        try {
            RealmModel realm = session.realms().getRealmByName(request.realm());
            if (realm == null) {
                return buildErrorResponse("Realm not found", Response.Status.NOT_FOUND);
            }

            UserModel user = authenticateSession();
            if (user == null) {
                return buildErrorResponse("Invalid or missing authentication");
            }

            OTPCredentialModel otpCredential = getCachedOtpCredential(user);
//            OTPCredentialModel otpCredential =
//                    getCredentialProvider().getDefaultCredential(session, session.getContext().getRealm(), user);
            if (otpCredential == null) {
                return buildErrorResponse("User has no configured TOTP");
            }

            boolean isValid = getCredentialProvider().isValid(realm,
                    user,
                    new UserCredentialModel(otpCredential.getId(), OTPCredentialModel.TYPE, request.otpCode()));
            if (!isValid) {
                return buildErrorResponse("OTP is invalid", Response.Status.FORBIDDEN);
            }

            return Response.ok(new TOTPValidationResponse(true, "TOTP is valid")).build();

        } catch (Exception e) {
            log.error("Error validating TOTP", e);
            return buildErrorResponse("Internal validation error");
        }
    }

    private UserModel authenticateSession() {
        AuthenticationManager.AuthResult authResult =
                new AppAuthManager.BearerTokenAuthenticator(session).authenticate();
        if (isNull(authResult)) {
            // Try to authenticate by identity cookie if bearer token is not present
            authResult = new AppAuthManager().authenticateIdentityCookie(session, session.getContext().getRealm());
            if (isNull(authResult)) {
                log.error("Could not authenticate user by bearer token or identity cookie");
            }
        }
        return authResult.getUser();
    }

    private OTPCredentialModel getCachedOtpCredential(UserModel user) {
        return ((OTPCredentialModel) user.credentialManager()
                .getStoredCredentialsByTypeStream(OTPCredentialModel.TYPE)
                .findFirst()
                .orElse(null));
    }



    private OTPCredentialProvider getCredentialProvider() {
        return (OTPCredentialProvider) session.getProvider(CredentialProvider.class,
                OTPCredentialProviderFactory.PROVIDER_ID);
    }

    private Response buildErrorResponse(String message, Response.Status status) {
        return Response.status(status)
                .entity(new TOTPValidationResponse(false, message))
                .build();
    }

    private Response buildErrorResponse(String message) {
        return buildErrorResponse(message, Response.Status.BAD_REQUEST);
    }

    private record TOTPValidationRequest(String realm, String otpCode) {
    }

    private record TOTPValidationResponse(boolean valid, String message) {
    }
}
