# Keycloak Provider Types

This document provides an overview of the different types of providers available in Keycloak and their use cases.

## Overview

Keycloak's functionality can be extended through various provider types:

1. Realm Resource Providers
2. Authentication Providers
3. Event Listeners
4. User Storage Providers
5. Protocol Mappers
6. Policy Providers
7. Action Token Handlers
8. Theme Resource Providers

## Realm Resource Providers

### Purpose
Extend Keycloak's REST API with custom endpoints at the realm level.

### Use Cases
- Custom authentication flows
- Additional user management endpoints
- Integration with external systems
- Custom token validation
- Session management extensions

### Implementation
```java
public interface RealmResourceProvider extends Provider {
    Object getResource();
}

public interface RealmResourceProviderFactory 
        extends ProviderFactory<RealmResourceProvider> {
    String getId();
}
```

### Examples in This Project
1. [TOTP Validator](../realm-resource-extensions/docs/providers/totp-validator.md)
    - Custom TOTP code validation
    - REST API endpoint for verification

2. [Global Logout](../realm-resource-extensions/docs/providers/global-logout.md)
    - Centralized logout mechanism
    - Session termination across applications

## Authentication Providers

### Purpose
Create custom authentication mechanisms and flows.

### Use Cases
- Biometric authentication
- Hardware token integration
- Custom MFA implementations
- Risk-based authentication
- Social identity provider integration

### Implementation
```java
public interface Authenticator extends Provider {
    void authenticate(AuthenticationFlowContext context);
    void action(AuthenticationFlowContext context);
    boolean requiresUser();
    boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user);
    void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user);
}
```

## Event Listeners

### Purpose
React to Keycloak events for auditing, monitoring, or integration.

### Use Cases
- Audit logging
- Security monitoring
- User activity tracking
- Integration with SIEM systems
- Custom notifications

### Implementation
```java
public interface EventListenerProvider extends Provider {
    void onEvent(Event event);
    void onEvent(AdminEvent event, boolean includeRepresentation);
}
```

## User Storage Providers

### Purpose
Integrate external user databases with Keycloak.

### Use Cases
- Legacy system integration
- Custom database connections
- LDAP/Active Directory extension
- Custom user attributes
- Federated user management

### Implementation
```java
public interface UserStorageProvider extends Provider {
    UserModel getUserById(String id, RealmModel realm);
    UserModel getUserByUsername(String username, RealmModel realm);
    UserModel getUserByEmail(String email, RealmModel realm);
}
```

## Protocol Mappers

### Purpose
Customize token claims and user attribute mapping.

### Use Cases
- Custom token claims
- Attribute transformation
- Conditional claim inclusion
- Role mapping
- Scope-based claims

### Implementation
```java
public interface ProtocolMapper {
    String getProtocol();
    String mapClaim(AccessToken token, ProtocolMapperModel mappingModel, UserSessionModel userSession);
}
```

## Policy Providers

### Purpose
Implement custom authorization policies.

### Use Cases
- Time-based access control
- Geographic restrictions
- Resource-based permissions
- Custom rule evaluation
- Dynamic policy decisions

### Implementation
```java
public interface PolicyProvider {
    void evaluate(PolicyEvaluationContext context);
    void close();
}
```

## Action Token Handlers

### Purpose
Handle custom action tokens for specific workflows.

### Use Cases
- Password reset flows
- Email verification
- Identity verification
- Custom registration flows
- Time-limited actions

### Implementation
```java
public interface ActionTokenHandler<T extends JsonWebToken> {
    void handleToken(T token, ActionTokenContext<T> context);
    Class<T> getTokenClass();
}
```

## Theme Resource Providers

### Purpose
Extend Keycloak's theming capabilities.

### Use Cases
- Custom login pages
- Branded user interfaces
- Custom email templates
- Dynamic theme selection
- Resource localization

### Implementation
```java
public interface ThemeResourceProvider extends Provider {
    InputStream getResourceAsStream(String path);
    Set<String> getResources(String path);
}
```

## Provider Development Process

1. **Choose Provider Type**
    - Identify extension point
    - Review use cases
    - Check existing implementations

2. **Implementation Steps**
    - Create provider class
    - Implement required interfaces
    - Create factory class
    - Register provider

3. **Testing Requirements**
    - Unit tests for provider
    - Integration with Keycloak
    - Performance testing
    - Security validation

4. **Documentation**
    - Technical documentation
    - Usage examples
    - Security considerations
    - Configuration guide