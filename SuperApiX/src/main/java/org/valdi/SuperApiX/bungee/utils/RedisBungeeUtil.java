package org.valdi.SuperApiX.bungee.utils;

import java.util.Optional;
import java.util.UUID;

import com.imaginarycode.minecraft.redisbungee.RedisBungee;

public final class RedisBungeeUtil {

    /**
     * Looks up a UUID from username via RedisBungee's uuid cache.
     *
     * @param username the username to lookup
     * @return a uuid, if present
     */
    public static Optional<UUID> lookupUuid(String username) {
        return Optional.ofNullable(RedisBungee.getApi()).map(a -> a.getUuidFromName(username, true));
    }

    public static Optional<String> lookupUsername(UUID uuid) {
        return Optional.ofNullable(RedisBungee.getApi()).map(a -> a.getNameFromUuid(uuid, true));
    }

    private RedisBungeeUtil() {}

}
