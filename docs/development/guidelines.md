# Keycloak Provider Development Guidelines

This guide covers general principles and practices for developing Keycloak providers, regardless of type.

## Provider Development Lifecycle

1. **Design**
    - Identify the appropriate provider type
    - Define the provider's interface and functionality
    - Consider security implications
    - Plan for testing and validation

2. **Implementation**
    - Follow Keycloak's provider patterns
    - Implement required interfaces
    - Add proper error handling
    - Include comprehensive logging

3. **Testing**
    - Unit tests for provider logic
    - Integration tests with Keycloak
    - Security testing
    - Performance testing when relevant

4. **Documentation**
    - Technical documentation
    - Security considerations
    - Installation guide
    - Usage examples

## Common Implementation Patterns

### Basic Provider Structure
```java
public class YourProvider implements ProviderFactory<Provider>, Provider {
    private KeycloakSession session;
    
    @Override
    public void init(Config.Scope config) {
        // Initialize provider
    }
    
    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // Post-initialization if needed
    }
}
```

### Session Handling
```java
public class SessionAwareProvider {
    private final KeycloakSession session;
    
    public SessionAwareProvider(KeycloakSession session) {
        this.session = session;
    }
    
    protected void validateSession() {
        if (session == null || session.getContext() == null) {
            throw new IllegalStateException("Invalid session state");
        }
    }
}
```

## Security Guidelines

1. **Authentication & Authorization**
    - Always validate user permissions
    - Use Keycloak's security context
    - Implement proper token validation

2. **Data Handling**
    - Validate all input
    - Sanitize output
    - Protect sensitive data

3. **Error Handling**
    - Use appropriate error responses
    - Avoid exposing internal details
    - Maintain security in error states

## Testing Requirements

### Unit Tests
- Test provider in isolation
- Mock Keycloak dependencies
- Cover error cases
- Validate security checks

### Integration Tests
- Test with running Keycloak
- Verify provider registration
- Test full functionality
- Check error handling

## Project Structure

1. **Source Organization**
   ```
   src/
   ├── main/
   │   ├── java/
   │   │   └── org/keycloak/providers/
   │   └── resources/
   │       └── META-INF/services/
   └── test/
       └── java/
   ```

2. **Documentation Organization**
   ```
   docs/
   ├── providers/
   │   └── provider-specific-docs.md
   ├── development.md
   └── security.md
   ```

## Code Style

1. **Java Conventions**
    - Follow Keycloak coding style
    - Use consistent naming
    - Document public APIs
    - Use appropriate logging

2. **Provider Patterns**
    - Implement proper lifecycle
    - Handle resources correctly
    - Use appropriate visibility
    - Follow SPI contracts

## Troubleshooting

1. **Common Issues**
    - Provider not loading
    - Session handling problems
    - Authentication issues
    - Configuration problems

2. **Debugging Tips**
    - Enable debug logging
    - Use proper exception handling
    - Check provider registration
    - Verify configurations

## Additional Resources

1. **Keycloak Documentation**
    - SPI documentation
    - Provider interfaces
    - Security guidelines

2. **Example Providers**
    - Reference implementations
    - Best practices
    - Common patterns