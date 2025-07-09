# Password Hash Providers

This module contains custom password hash providers for Keycloak, enabling support for additional password hashing algorithms beyond the default set.

## Available Providers

### SHA-256 Password Hash Provider

A password hash provider that implements SHA-256 hashing for password verification. This is primarily intended for migration and compatibility with legacy systems that use SHA-256 for password storage.

- **Provider ID:** `SHA-256`
- **Features:**
  - Verifies passwords hashed with SHA-256
  - Supports configurable hash iterations
  - Integrates with Keycloak's password policy framework
  - Singleton implementation for efficiency
  - Does **not** support password encoding (verification only)

#### Implementation
- `Sha256PasswordHashProvider`: Main provider class implementing password verification
- `Sha256PasswordHashProviderFactory`: Factory for creating provider instances
- `NotImplementedException`: Exception thrown for unsupported operations (e.g., encoding)

#### Registration
The provider is registered via the Java SPI in:
```
src/main/resources/META-INF/services/org.keycloak.credential.hash.PasswordHashProviderFactory
```

#### Limitations
- Not recommended for new password storage (use PBKDF2, Bcrypt, or Argon2 for new systems)
- Intended for migration and legacy compatibility only

## Usage

1. **Build the module:**
   ```bash
   mvn clean install
   ```
2. **Deploy the JAR:**
   Copy `target/password-hash-providers-1.0.jar` to your Keycloak `providers/` directory.
3. **Restart Keycloak**

## Development

- Follow the structure in `src/main/java/org/keycloak/providers/passwordhash/`
- Register new providers in the appropriate SPI file under `META-INF/services/`
- Add documentation for each new provider

## License

This module is part of the Keycloak Extensions project and is licensed under the MIT License. 