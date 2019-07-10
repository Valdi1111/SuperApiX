package org.valdi.SuperApiX.bukkit.nms.base;

import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.nms.JSONMessage;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public interface IGeneralUtils<K, V> {

    SuperKey spaceKeyFromMinecraft(K key) throws VersionUnsupportedException;

    K minecraftKeyFromSpace(SuperKey key) throws VersionUnsupportedException;

    V getBaseComponentFromJson(String json);

    default V getBaseComponentFromJson(JSONMessage json) {
        return this.getBaseComponentFromJson(json.getJson());
    }

    int getTicks();

}
