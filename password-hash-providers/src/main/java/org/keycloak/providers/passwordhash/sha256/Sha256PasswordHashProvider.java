package org.keycloak.providers.passwordhash.sha256;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.credential.PasswordCredentialModel;

import static io.quarkus.runtime.util.HashUtil.sha256;
import static java.lang.String.format;
import static org.keycloak.providers.passwordhash.sha256.Sha256PasswordHashProviderFactory.DEFAULT_ITERATIONS;
import static org.keycloak.providers.passwordhash.sha256.Sha256PasswordHashProviderFactory.PROVIDER_ID;


@Slf4j
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Sha256PasswordHashProvider implements PasswordHashProvider {

    public static Sha256PasswordHashProvider getInstance() {
        return new Sha256PasswordHashProvider();
    }

    @Override
    public boolean policyCheck(final PasswordPolicy policy, final PasswordCredentialModel credential) {
        log.debug("Sha256PasswordHashProvider policyCheck");
        final int policyHashIterations =
                policy.getHashIterations() == -1 ? DEFAULT_ITERATIONS : policy.getHashIterations();

        return credential.getPasswordCredentialData().getHashIterations() == policyHashIterations
                && PROVIDER_ID.equalsIgnoreCase(credential.getPasswordCredentialData().getAlgorithm());
    }

    @Override
    public PasswordCredentialModel encodedCredential(final String rawPassword, final int iterations) {
        log.debug("Sha256PasswordHashProvider encodedCredential");
        throw new NotImplementedException(format("{0} encoding not implemented! " +
                "Implementation only required if we want to store the password in {0}", PROVIDER_ID));
    }


    @Override
    public void close() {
        // NOOP
    }

    @Override
    public boolean verify(final String rawPassword, final PasswordCredentialModel credential) {
        log.debug("Sha256PasswordHashProvider verify");
        final String hash = credential.getPasswordSecretData().getValue();
        final String salt = new String(credential.getPasswordSecretData().getSalt());

        final int iterations = credential.getPasswordCredentialData().getHashIterations();
        return verify(salt + rawPassword, hash, iterations);
    }

    private boolean verify(final String rawPassword, final String hash, final int iterations) {
        String encodedHash = rawPassword;
        for (int i = 0; i < iterations; i++) {
            encodedHash = sha256(encodedHash);
        }
        return encodedHash.equalsIgnoreCase(hash);
    }

}