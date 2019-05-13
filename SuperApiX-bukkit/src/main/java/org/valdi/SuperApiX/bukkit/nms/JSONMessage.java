package org.valdi.SuperApiX.bukkit.nms;

import org.valdi.SuperApiX.bukkit.SuperApiBukkit;

public class JSONMessage {
    private final String json;

    public JSONMessage(String json) {
        this.json = json;
    }

    public String getJson() {
        return this.json;
    }

    public Object getBaseComponent() {
        return SuperApiBukkit.getInstance().getNmsProvider().getGeneralUtils().map(u -> u.getBaseComponentFromJson(this)).orElse(null);
    }

    public String toString() {
        return this.json;
    }
}

