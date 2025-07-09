# Global Logout Extension for Keycloak

A Keycloak extension that provides centralized session termination across multiple applications sharing the same authentication realm.

## Use Cases & Considerations

### Ideal For
- Multiple applications sharing Keycloak authentication requiring simultaneous logout
- Regulated environments needing immediate session termination
- Systems requiring custom post-logout flows or cleanup operations

### Not Recommended For
- Single application deployments where standard Keycloak logout suffices
- Performance-critical systems where additional HTTP calls should be minimized
- Environments with non-standard session management requirements

## Security Risks & Limitations

### Primary Risks
- **Race Conditions**: Concurrent logout requests may affect session termination
- **Token Exposure**: ID tokens in logout flow could be intercepted
- **Network Issues**: Partial session termination if some systems are inaccessible
- **Client Compatibility**: May not work with clients not following OIDC logout standards

### Mitigations
- Uses atomic operations for session handling
- Implements secure token transmission
- Validates redirect URIs against whitelist
- Maintains comprehensive logging

## Implementation Details

### Endpoint
```
GET <keycloak_server>/realms/<realm_name>/global-logout
```

### Authentication
- Bearer token or Identity Cookie

### Client ID Configuration
The extension uses the hardcoded client ID "account-console" for several reasons:
- It's a default Keycloak client available in all realms
- Provides consistent post-logout user experience
- Ensures users can access their account settings after logout
- Avoids security risks of configurable redirect endpoints

If you need to use a different client ID, modify the `GlobalLogoutEndpoint.java` file:
```java
// Change this line in initiateGlobalLogout()
ClientModel client = session.clients().getClientByClientId(realm, "your-client-id");
```

### Installation
1. Build: `mvn clean install`
2. Deploy: Copy JAR to Keycloak's providers directory
3. Restart Keycloak

### Best Practices
- Enable HTTPS for all communications
- Configure proper token timeouts
- Monitor logout operations for suspicious patterns
- Maintain whitelist of valid redirect URIs

## License
MIT License - See LICENSE.txt for details