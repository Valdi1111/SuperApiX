package org.valdi.SuperApiX.bukkit.nms.base;

import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.nms.JSONMessage;

public interface IGeneralUtils<K, V> {

    SuperKey spaceKeyFromMinecraft(K key);

    K minecraftKeyFromSpace(SuperKey key);

    V getBaseComponentFromJson(String json);

    default V getBaseComponentFromJson(JSONMessage json) {
        return this.getBaseComponentFromJson(json.getJson());
    }

    int getTicks();

}
