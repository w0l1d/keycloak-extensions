# Realm Resource Extensions

Custom realm-level REST endpoints extending Keycloak's API functionality. This module implements the Realm Resource Provider SPI to add new REST endpoints to Keycloak realms.

## Current Providers

### [TOTP Validator](../docs/providers/totp-validator.md)
REST endpoint for validating TOTP codes without standard authentication flow.
- Standalone TOTP validation
- Support for both Bearer token and Cookie authentication
- Detailed validation responses

### [Global Logout](../docs/providers/global-logout.md)
Centralized session termination endpoint for multi-application environments.
- Cross-application logout
- OIDC compliant logout flow
- Secure session termination

### [SHA-256 Password Hash](../docs/providers/sha256-password-hash.md)
Password hash provider implementing SHA-256 hashing for password verification.
- Legacy system migration support
- Configurable hash iterations
- Compatible with Keycloak's password policy framework
- Now located in the [password-hash-providers module](../password-hash-providers/README.md)

## Provider Development

See these guides for development:
- [General Provider Guidelines](../docs/development/guidelines.md)
- [Provider Types Overview](../docs/development/provider-types.md)
- [Realm Provider Specific Guide](./docs/development.md)

## Implementation Notes

### Common Features
- Bearer token and Identity Cookie authentication
- Comprehensive error handling
- Detailed logging
- Security-first approach

### Structure
```
src/main/java/org/keycloak/rest/
├── totpvalidator/
│   ├── TOTPValidationResourceProvider.java
│   ├── TOTPValidationEndpoint.java
│   └── TOTPValidationResourceProviderFactory.java
└── logout/
    ├── GlobalLogoutResourceProvider.java
    ├── GlobalLogoutEndpoint.java
    └── GlobalLogoutResourceProviderFactory.java
```

Note: The SHA-256 Password Hash Provider has been moved to the separate `password-hash-providers` module. See its [README](../password-hash-providers/README.md) for usage and details.

## Quick Start

1. Build:
   ```