# Realm Resource Extensions

This module provides custom realm-level resource providers for Keycloak, extending the default functionality with additional REST endpoints.

## Current Providers

### TOTP Validation Provider

A REST endpoint for validating Time-based One-Time Passwords (TOTP) without requiring the standard Keycloak authentication flow.

#### Features

- Validates TOTP codes against user's stored credentials
- Supports both Bearer token and Identity Cookie authentication
- Returns detailed validation results
- Handles various error cases gracefully

#### API Reference

**Endpoint:** `<keycloak_server>/realms/<realm_name>/validate-totp`

**Method:** POST

**Request Headers:**
- `Content-Type: application/json`
- `Authorization: Bearer <token>` (or valid Identity Cookie)

**Request Body:**
```json
{
    "realm": "your-realm-name",
    "otpCode": "123456"
}
```

**Response:**
```json
{
    "valid": true|false,
    "message": "Status message"
}
```

**Possible Response Codes:**
- 200: Validation successful
- 400: Bad request (missing/invalid parameters)
- 401: Authentication required
- 403: Invalid TOTP code
- 404: Realm not found
- 500: Internal server error

#### Error Handling

The endpoint handles various error cases:
- Missing/invalid authentication
- Non-existent realm
- User without configured TOTP
- Invalid TOTP code
- Internal processing errors

#### Security Considerations

- Requires valid user authentication (Bearer token or Identity Cookie)
- Only validates TOTP for the authenticated user
- Returns consistent error messages to prevent information leakage

## Building

```bash
mvn clean install
```

The JAR will be generated in `target/realm-resource-extensions-1.0.jar`

## Dependencies

- Keycloak Core (`${version.kc}`)
- Keycloak Server SPI
- Keycloak Server SPI Private
- Keycloak Model Infinispan
- RESTEasy Reactive Common
- Keycloak Services
- Lombok

## Installation

1. Build the module
2. Copy `target/realm-resource-extensions-1.0.jar` to Keycloak's `providers` directory
3. Restart Keycloak

## Development Guide

To add a new realm resource provider:

1. Create provider class implementing `RealmResourceProvider`
2. Create factory class implementing `RealmResourceProviderFactory`
3. Create endpoint class with JAX-RS annotations
4. Register factory in `META-INF/services/org.keycloak.services.resource.RealmResourceProviderFactory`

## Contributing

When contributing:
1. Follow existing code style
2. Add appropriate error handling
3. Document new endpoints
4. Update this README with new provider details