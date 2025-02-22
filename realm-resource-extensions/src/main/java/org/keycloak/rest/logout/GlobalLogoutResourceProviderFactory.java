package org.keycloak.rest.logout;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;


class GlobalLogoutResourceProviderFactory implements RealmResourceProviderFactory {
    public static final String PROVIDER_ID = "global-logout";

    @Override
    public RealmResourceProvider create(KeycloakSession session) {
        return new GlobalLogoutResourceProvider(session);
    }

    @Override
    public void init(org.keycloak.Config.Scope config) {
        // NOOP
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // NOOP
    }

    @Override
    public void close() {
        // NOOP
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}