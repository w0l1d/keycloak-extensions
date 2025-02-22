# TOTP Validation Provider

A Keycloak realm resource provider that enables TOTP (Time-based One-Time Password) validation through a REST API endpoint. This provider allows applications to validate TOTP codes without going through the standard Keycloak authentication flow.

## Use Cases

### Ideal For
- Applications requiring standalone TOTP validation
- Mobile apps with custom authentication flows
- Systems integrating with legacy TOTP implementations
- API-first architectures needing TOTP validation

### Not Recommended For
- Standard web applications using Keycloak's login flow
- Systems without existing TOTP configuration
- Public-facing TOTP validation services

## API Reference

### Endpoint
```
POST <keycloak_server>/realms/<realm_name>/validate-totp
```

### Authentication
The endpoint supports two authentication methods:
- Bearer token
- Identity Cookie

### Request Format
Headers:
```
Content-Type: application/json
Authorization: Bearer <token>
```

Body:
```json
{
    "realm": "your-realm-name",
    "otpCode": "123456"
}
```

### Response Format
```json
{
    "valid": true|false,
    "message": "Status message"
}
```

### Status Codes
- `200` Validation successful
- `400` Bad request (missing/invalid parameters)
- `401` Authentication required
- `403` Invalid TOTP code
- `404` Realm not found
- `500` Internal server error

## Security Considerations

### Authentication & Authorization
- Requires valid user authentication
- Only validates TOTP for the authenticated user
- Supports both token and cookie authentication
- Validates realm access permissions

### Error Handling
- Consistent error messages to prevent information leakage
- No detailed error information in production
- Proper logging of validation attempts
- Rate limiting recommended (not included)

### Data Protection
- No TOTP secrets exposed via API
- No storage of TOTP codes
- Uses Keycloak's secure credential storage

## Implementation Details

### Key Components
- `TOTPValidationResourceProvider`: Main provider class
- `TOTPValidationEndpoint`: REST endpoint implementation
- `TOTPValidationResourceProviderFactory`: Provider factory

### Dependencies
- Keycloak Core
- Keycloak Server SPI
- RESTEasy Reactive

### Configuration
No additional configuration required beyond standard TOTP setup in Keycloak.

## Best Practices

### Integration Guidelines
1. Always use HTTPS
2. Implement client-side rate limiting
3. Add request logging
4. Handle all error cases
5. Validate input thoroughly

### Error Handling
```java
try {
    // Validate TOTP
    return Response.ok(new ValidationResult(true, "Valid TOTP code")).build();
} catch (AuthenticationException e) {
    return Response.status(Response.Status.UNAUTHORIZED).build();
} catch (InvalidTOTPException e) {
    return Response.status(Response.Status.FORBIDDEN).build();
}
```

## Testing

### Test Cases
1. Valid TOTP code
2. Invalid TOTP code
3. Expired TOTP code
4. Missing authentication
5. Invalid authentication
6. Rate limiting (if implemented)

### Example Curl Request
```bash
curl -X POST \
  https://keycloak.example.com/realms/your-realm/validate-totp \
  -H 'Authorization: Bearer <your-token>' \
  -H 'Content-Type: application/json' \
  -d '{
    "realm": "your-realm",
    "otpCode": "123456"
}'
```

## Troubleshooting

### Common Issues
1. Authentication errors
    - Verify token/cookie validity
    - Check realm name
2. Invalid TOTP codes
    - Verify time synchronization
    - Check code format
3. Configuration problems
    - Ensure TOTP is enabled for user
    - Verify provider installation

### Logging
The provider uses SLF4J logging:
- Debug level for detailed validation steps
- Info level for successful validations
- Warn level for validation failures
- Error level for system errors