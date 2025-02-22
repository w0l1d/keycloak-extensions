package org.keycloak.rest.logout;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

@Slf4j
public class GlobalLogoutResourceProvider implements RealmResourceProvider {
    private final KeycloakSession session;

    public GlobalLogoutResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return new GlobalLogoutEndpoint(session);
    }

    @Override
    public void close() {
        // NOOP
    }
}