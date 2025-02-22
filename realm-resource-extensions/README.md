# Realm Resource Extensions

Custom realm-level REST endpoints extending Keycloak's API functionality. This module implements the Realm Resource Provider SPI to add new REST endpoints to Keycloak realms.

## Current Providers

### [TOTP Validator](./docs/providers/totp-validator.md)
REST endpoint for validating TOTP codes without standard authentication flow.
- Standalone TOTP validation
- Support for both Bearer token and Cookie authentication
- Detailed validation responses

### [Global Logout](./docs/providers/global-logout.md)
Centralized session termination endpoint for multi-application environments.
- Cross-application logout
- OIDC compliant logout flow
- Secure session termination

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

## Quick Start

1. Build:
   ```bash
   mvn clean install
   ```
2. Deploy: Copy `target/realm-resource-extensions-1.0.jar` to Keycloak's providers directory
3. Restart Keycloak

## Dependencies

All dependencies are managed through the parent POM:
- Keycloak Core (${version.kc})
- Keycloak Server SPI
- RESTEasy Reactive