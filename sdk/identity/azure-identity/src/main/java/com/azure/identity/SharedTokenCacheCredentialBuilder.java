// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.identity;

/**
 * Fluent credential builder for instantiating a {@link SharedTokenCacheCredential}.
 *
 * @see SharedTokenCacheCredential
 */
public class SharedTokenCacheCredentialBuilder extends AadCredentialBuilderBase<SharedTokenCacheCredentialBuilder> {
    private String username;

    /**
     * Sets the username for the account.
     *
     * @param username The username for the account.
     *
     * @return The updated SharedTokenCacheCredentialBuilder object.
     */
    public SharedTokenCacheCredentialBuilder username(String username) {
        this.username = username;
        return this;
    }

    /**
     * Allows to use an unprotected file specified by <code>cacheFileLocation()</code> instead of
     * Gnome keyring on Linux. This is restricted by default.
     *
     * @return An updated instance of this builder.
     */
    public SharedTokenCacheCredentialBuilder allowUnencryptedCache() {
        this.identityClientOptions.allowUnencryptedCache();
        return this;
    }

    /**
     * Creates a new {@link SharedTokenCacheCredentialBuilder} with the current configurations.
     *
     * @return a {@link SharedTokenCacheCredentialBuilder} with the current configurations.
     */
    public SharedTokenCacheCredential build() {
        return new SharedTokenCacheCredential(username, clientId, tenantId,
                identityClientOptions.enablePersistentCache());
    }
}
