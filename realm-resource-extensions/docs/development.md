# Realm Resource Provider Development Guide

This guide focuses on implementing realm resource providers for Keycloak, using patterns and examples from our implementations.

## Provider Structure

A realm resource provider consists of three main components:

### 1. Provider Class
```java
@Slf4j
public class YourResourceProvider implements RealmResourceProvider {
    private final KeycloakSession session;

    public YourResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public Object getResource() {
        return new YourEndpoint(session);
    }

    @Override
    public void close() {
        // Cleanup resources if needed
    }
}
```

### 2. Factory Class
```java
public class YourResourceProviderFactory implements RealmResourceProviderFactory {
    public static final String PROVIDER_ID = "your-endpoint-name";

    @Override
    public RealmResourceProvider create(KeycloakSession session) {
        return new YourResourceProvider(session);
    }

    @Override
    public void init(Config.Scope config) {
        // Initialize any configurations
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
```

### 3. Endpoint Class
```java
@Slf4j
public class YourEndpoint {
    private final KeycloakSession session;
    private final AuthenticationManager.AuthResult auth;

    public YourEndpoint(KeycloakSession session) {
        this.session = session;
        this.auth = authenticateSession(session);
    }

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleRequest() {
        if (auth == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        // Implementation
    }
}
```

## Common Implementation Patterns

### Authentication
```java
private AuthenticationManager.AuthResult authenticateSession(KeycloakSession session) {
    // Try Bearer token first
    AuthenticationManager.AuthResult authResult = 
        new AppAuthManager.BearerTokenAuthenticator(session).authenticate();
    
    if (authResult == null) {
        // Fallback to Identity Cookie
        authResult = new AppAuthManager()
            .authenticateIdentityCookie(session, session.getContext().getRealm());
    }
    return authResult;
}
```

### Response Building
```java
private Response buildSuccessResponse(Object entity) {
    return Response.ok()
        .entity(entity)
        .type(MediaType.APPLICATION_JSON)
        .build();
}

private Response buildErrorResponse(String message, Response.Status status) {
    return Response.status(status)
        .entity(new ErrorResponse(message))
        .type(MediaType.APPLICATION_JSON)
        .build();
}
```

### Error Handling
```java
try {
    // Implementation
    return buildSuccessResponse(result);
} catch (AuthenticationException e) {
    log.warn("Authentication failed: {}", e.getMessage());
    return Response.status(Response.Status.UNAUTHORIZED).build();
} catch (Exception e) {
    log.error("Unexpected error: {}", e.getMessage(), e);
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
}
```

## Integration with Keycloak

### Provider Registration
Create file: `META-INF/services/org.keycloak.services.resource.RealmResourceProviderFactory`
```
org.keycloak.rest.yourprovider.YourResourceProviderFactory
```

### Accessing Keycloak Services
```java
// User operations
UserModel user = session.users().getUserById(realm, userId);

// Realm operations
RealmModel realm = session.getContext().getRealm();

// Client operations
ClientModel client = session.clients().getClientById(realm, clientId);
```

## Best Practices

### 1. Security
- Always validate authentication
- Use appropriate scopes/roles
- Sanitize input/output
- Follow principle of least privilege

### 2. Error Handling
- Use appropriate status codes
- Provide meaningful error messages
- Log sufficient details
- Handle all edge cases

### 3. Performance
- Use efficient queries
- Implement proper caching
- Release resources promptly
- Monitor response times

### 4. Testing
- Unit test core logic
- Test error cases
- Verify authentication
- Test with various inputs

## Example Provider: TOTP Validator

```java
@POST
@Path("/validate")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response validateTOTP(ValidationRequest request) {
    try {
        // Validate authentication
        if (auth == null || auth.getUser() == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        // Validate input
        if (!isValidRequest(request)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // Process request
        boolean isValid = validateCode(request.getCode(), auth.getUser());
        
        // Return response
        return Response.ok(new ValidationResult(isValid)).build();
    } catch (Exception e) {
        log.error("TOTP validation failed", e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
```

## Real-World Examples

Refer to our implementations:
1. [TOTP Validator](./providers/totp-validator.md)
2. [Global Logout](./providers/global-logout.md)

## Troubleshooting

### Common Issues
1. Provider not loading
    - Check registration file
    - Verify provider ID
    - Check JAR placement

2. Authentication failures
    - Verify token validity
    - Check cookie settings
    - Validate realm settings

3. Response errors
    - Check content types
    - Verify JSON formatting
    - Validate response entities