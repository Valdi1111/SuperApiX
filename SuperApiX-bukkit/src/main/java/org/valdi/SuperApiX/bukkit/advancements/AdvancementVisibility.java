package org.valdi.SuperApiX.bukkit.advancements;

import java.util.Arrays;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.advancements.managers.AbstractAdvancement;

public abstract class AdvancementVisibility {
    public static final AdvancementVisibility ALWAYS = new AdvancementVisibility("ALWAYS") {

        @Override
        public boolean isVisible(Player player, AbstractAdvancement advancement) {
            return true;
        }

    };

    public static final AdvancementVisibility PARENT_GRANTED = new AdvancementVisibility("PARENT_GRANTED") {

        @Override
        public boolean isVisible(Player player, AbstractAdvancement advancement) {
            if (advancement.isGranted(player)) {
                return true;
            }
            AbstractAdvancement parent = advancement.getParent();
            if (parent != null && !parent.isGranted(player)) {
                return false;
            }
            return true;
        }

    };

    public static final AdvancementVisibility VANILLA = new AdvancementVisibility("VANILLA") {

        @Override
        public boolean isVisible(Player player, AbstractAdvancement advancement) {
            if (advancement.isGranted(player)) {
                return true;
            }
            AbstractAdvancement parent = advancement.getParent();
            if (parent != null && !parent.isGranted(player)) {
                AbstractAdvancement grandParent = parent.getParent();
                if (grandParent != null && grandParent.getParent() != null && !grandParent.isGranted(player)) {
                    return false;
                }
                return true;
            }
            return true;
        }

    };

    public static final AdvancementVisibility HIDDEN = new AdvancementVisibility("HIDDEN") {

        @Override
        public boolean isVisible(Player player, AbstractAdvancement advancement) {
            return advancement.isGranted(player);
        }

    };

    private final String name;

    public AdvancementVisibility() {
        this.name = "CUSTOM";
    }

    private AdvancementVisibility(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract boolean isVisible(Player player, AbstractAdvancement advancement);

    public boolean isAlwaysVisibleWhenAdvancementAfterIsVisible() {
        return true;
    }

    public static AdvancementVisibility byName(String name) {
        for (AdvancementVisibility visibility : Arrays.asList(ALWAYS, PARENT_GRANTED, VANILLA, HIDDEN)) {
            if (!visibility.getName().equalsIgnoreCase(name)) continue;
            return visibility;
        }
        return VANILLA;
    }

    /*public AdvancementVisibility(String string, AdvancementVisibility advancementVisibility) {
        AdvancementVisibility advancementVisibility2;
        advancementVisibility2(string);
    }*/

}

