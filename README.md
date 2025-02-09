# Keycloak Extensions Project

A collection of custom Keycloak extensions providing additional functionality and integrations. The project uses a modular structure for easy management and extension.

## Project Structure

```
keycloak-extensions/
├── pom.xml                      # Parent POM managing dependencies and versions
└── realm-resource-extensions/   # Module for realm-specific resource extensions
    ├── pom.xml
    └── src/
```

## Requirements

- Java 21
- Maven 3.8+
- Keycloak 26.1.0+

## Building

To build all extensions:

```bash
mvn clean install
```

This builds each module and generates JARs in their respective `target` directories.

## Available Extensions

### Realm Resource Extensions

Currently includes:
- TOTP Validation Provider: REST endpoint for validating Time-based One-Time Passwords
- More providers coming soon...

See [realm-resource-extensions/README.md](realm-resource-extensions/README.md) for detailed documentation.

## Deployment

1. Build the project using Maven
2. Copy the generated JAR(s) from `<module>/target/` to your Keycloak server's `providers` directory
3. Restart Keycloak

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.


## Support

For issues, questions, or contributions, please open an issue in the repository.