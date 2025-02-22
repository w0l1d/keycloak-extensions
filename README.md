# Keycloak Extensions

A collection of custom Keycloak providers and extensions focusing on realm resource providers.

## Project Overview

This project provides additional functionality for Keycloak through custom providers:

- **TOTP Validator**: REST endpoint for validating TOTP codes
- **Global Logout**: Centralized session termination across multiple applications

## Requirements

- Java 21
- Maven 3.8+
- Keycloak 26.1.0+

## Project Structure

```
├── docs/
│   └── development/
│       ├── guidelines.md
│       └── provider-types.md
├── realm-resource-extensions/
│   ├── docs/
│   │   └── providers/
│   │       ├── totp-validator.md
│   │       └── global-logout.md
│   └── src/
│       └── main/
│           ├── java/
│           │   └── org/
│           │       └── keycloak/
│           │           └── rest/
│           │               ├── totpvalidator/
│           │               │   ├── TOTPValidationEndpoint.java
│           │               │   ├── TOTPValidationResourceProvider.java
│           │               │   └── TOTPValidationResourceProviderFactory.java
│           │               └── logout/
│           │                   ├── GlobalLogoutEndpoint.java
│           │                   ├── GlobalLogoutResourceProvider.java
│           │                   └── GlobalLogoutResourceProviderFactory.java
│           └── resources/
│               └── META-INF/
│                   └── services/
│                       └── org.keycloak.services.resource.RealmResourceProviderFactory
├── pom.xml
└── LICENSE.txt
```

## Available Providers

### TOTP Validator

A REST endpoint for validating TOTP codes without going through the standard Keycloak authentication flow.

**Endpoint**: `POST /realms/{realm-name}/validate-totp`

See [TOTP Validator Documentation](realm-resource-extensions/docs/providers/totp-validator.md) for details.

### Global Logout

Centralized session termination across multiple applications sharing the same authentication realm.

**Endpoint**: `GET /realms/{realm-name}/global-logout`

See [Global Logout Documentation](realm-resource-extensions/docs/providers/global-logout.md) for details.

## Building and Installation

1. Build the project:
   ```bash
   mvn clean install
   ```

2. Copy the JAR file to Keycloak's providers directory:
   ```bash
   cp realm-resource-extensions/target/realm-resource-extensions-1.0.jar $KEYCLOAK_HOME/providers/
   ```

3. Restart Keycloak

## Development

For detailed development guidelines and documentation:

- [Development Guidelines](docs/development/guidelines.md)
- [Provider Types Overview](docs/development/provider-types.md)

### Adding New Providers

1. Create provider classes following the structure in `realm-resource-extensions/src/main/java/org/keycloak/rest/`
2. Register the provider factory in `META-INF/services/org.keycloak.services.resource.RealmResourceProviderFactory`
3. Add documentation in `realm-resource-extensions/docs/providers/`

## Dependencies

- Keycloak Core: 26.1.0
- Quarkus RESTEasy: 3.15.1
- Lombok: 1.18.36

## Security Considerations

- All endpoints require proper authentication
- Supports both Bearer token and Identity Cookie authentication
- Implements proper session validation
- Follows Keycloak's security best practices

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see [LICENSE.txt](LICENSE.txt) for details.

## Authors

- Walid Ahdouf (ahdoufwalid@gmail.com)