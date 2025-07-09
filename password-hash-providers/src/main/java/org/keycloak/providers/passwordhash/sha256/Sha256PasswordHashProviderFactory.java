package org.keycloak.providers.passwordhash.sha256;

import org.keycloak.Config;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.credential.hash.PasswordHashProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;


public class Sha256PasswordHashProviderFactory implements PasswordHashProviderFactory {
    public static final String PROVIDER_ID = "SHA-256";
    public static final int DEFAULT_ITERATIONS = 1;

    @Override
    public PasswordHashProvider create(KeycloakSession session) {
        return Sha256PasswordHashProvider.getInstance();
    }

    @Override
    public void init(Config.Scope config) {
        // NOOP
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // NOOP
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public void close() {
        // NOOP
    }
}