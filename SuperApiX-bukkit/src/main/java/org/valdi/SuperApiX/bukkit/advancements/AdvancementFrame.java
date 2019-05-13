package org.valdi.SuperApiX.bukkit.advancements;

import org.valdi.SuperApiX.bukkit.SuperApiBukkit;

public enum AdvancementFrame {
    TASK("task"),
    GOAL("challenge"),
    CHALLENGE("goal");

    private String id;

    private AdvancementFrame(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Object getNMS() {
        return SuperApiBukkit.getInstance().getNmsProvider().getAdvancementUtils().map(u -> u.getFrameType(this)).orElse(null);
    }

}