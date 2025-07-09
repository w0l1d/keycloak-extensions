# SHA-256 Password Hash Provider

A Keycloak password hash provider that implements SHA-256 hashing for password verification. This provider allows Keycloak to verify passwords that have been hashed using the SHA-256 algorithm.

## Use Cases

### Ideal For
- Migrating from legacy systems that use SHA-256 password hashing
- Verifying passwords from external systems that use SHA-256
- Compatibility with existing SHA-256 hashed password databases
- Systems requiring SHA-256 compliance for password verification

### Not Recommended For
- New password storage (more secure algorithms like PBKDF2 are preferred)
- High-security environments requiring modern password hashing
- Systems without legacy SHA-256 password requirements

## Implementation Details

### Key Components
- `Sha256PasswordHashProvider`: Main provider class implementing password verification
- `Sha256PasswordHashProviderFactory`: Provider factory for creating instances

### Features
- Verifies passwords hashed with SHA-256
- Supports configurable hash iterations
- Compatible with Keycloak's password policy framework
- Singleton implementation for efficiency

### Limitations
- Does not support password encoding (only verification)
- Primarily intended for migration and compatibility scenarios

## Configuration

### Provider ID
The provider is registered with the ID: `SHA-256`

### Default Settings
- Default hash iterations: 1

### Password Policy Integration
The provider integrates with Keycloak's password policy system and respects the configured hash iterations.

## Security Considerations

### Strength Assessment
- SHA-256 is a cryptographic hash function, not a password hashing function
- Lacks important features like salting and computational difficulty adjustment
- Vulnerable to rainbow table and brute force attacks compared to modern alternatives

### Recommendations
- Use only for verifying existing SHA-256 hashed passwords
- Consider migrating to more secure algorithms (PBKDF2, Bcrypt, Argon2) for new passwords
- Increase iterations when possible to improve security

## Implementation Example

### Verification Process
```java
private boolean verify(final String rawPassword, final String hash, final int iterations) {
    String encodedHash = rawPassword;
    for (int i = 0; i < iterations; i++) {
        encodedHash = sha256(encodedHash);
    }
    return encodedHash.equalsIgnoreCase(hash);
}
```

## Best Practices

### Integration Guidelines
1. Use only for verification of existing passwords
2. Implement a migration strategy to more secure algorithms
3. Set appropriate iteration counts based on security requirements
4. Consider additional security measures (MFA, login monitoring)

### Migration Strategy
1. Enable this provider for verification
2. When users authenticate successfully, rehash their password with a more secure algorithm
3. Gradually transition all users to the more secure algorithm
4. Eventually disable this provider once migration is complete

## Troubleshooting

### Common Issues
1. Verification failures
   - Check salt configuration
   - Verify iteration count matches original hash
   - Ensure case sensitivity is handled correctly
2. Performance concerns
   - Adjust iteration count appropriately
   - Monitor authentication times

### Logging
The provider uses SLF4J logging:
- Debug level for detailed verification steps
- Info level for successful verifications
- Warn level for verification failures