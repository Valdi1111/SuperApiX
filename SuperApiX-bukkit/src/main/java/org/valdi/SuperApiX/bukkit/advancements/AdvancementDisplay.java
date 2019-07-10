package org.valdi.SuperApiX.bukkit.advancements;

import com.google.gson.annotations.SerializedName;
import javax.annotation.Nullable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.valdi.SuperApiX.bukkit.advancements.managers.AbstractAdvancement;
import org.valdi.SuperApiX.bukkit.nms.JSONMessage;

public class AdvancementDisplay {
    @SerializedName(value="icon")
    private Material iconID;
    private transient ItemStack icon;
    private JSONMessage title;
    private JSONMessage description;
    private AdvancementFrame frame;
    private boolean showToast;
    private boolean announceChat;
    private transient AdvancementVisibility vis;
    private String backgroundTexture;
    private float x = 0.0f;
    private float y = 0.0f;
    private float tabWidth = 0.0f;
    private float tabHeight = 0.0f;
    private transient AbstractAdvancement positionOrigin;
    @SerializedName(value="visibility")
    public String visibilityIdentifier = "VANILLA";

    public AdvancementDisplay(Material icon, JSONMessage title, JSONMessage description, AdvancementFrame frame, boolean showToast, boolean announceChat, AdvancementVisibility visibility) {
        this.icon = new ItemStack(icon);
        this.iconID = icon;
        this.title = title;
        this.description = description;
        this.frame = frame;
        this.showToast = showToast;
        this.announceChat = announceChat;
        this.setVisibility(visibility);
    }

    public AdvancementDisplay(Material icon, String title, String description, AdvancementFrame frame, boolean showToast, boolean announceChat, AdvancementVisibility visibility) {
        this.icon = new ItemStack(icon);
        this.iconID = icon;
        if (title.contains("\u00a7")) {
            title = String.valueOf(title) + "\u00a7a";
        }
        this.title = new JSONMessage("{\"text\":\"" + title.replaceAll("\"", "\\\"") + "\"}");
        this.description = new JSONMessage("{\"text\":\"" + description.replaceAll("\"", "\\\"") + "\"}");
        this.frame = frame;
        this.showToast = showToast;
        this.announceChat = announceChat;
        this.setVisibility(visibility);
    }

    public AdvancementDisplay(Material icon, JSONMessage title, JSONMessage description, AdvancementFrame frame, String backgroundTexture, boolean showToast, boolean announceChat, AdvancementVisibility visibility) {
        this.icon = new ItemStack(icon);
        this.iconID = icon;
        this.title = title;
        this.description = description;
        this.frame = frame;
        this.backgroundTexture = backgroundTexture;
        this.showToast = showToast;
        this.announceChat = announceChat;
        this.setVisibility(visibility);
    }

    public AdvancementDisplay(Material icon, String title, String description, AdvancementFrame frame, String backgroundTexture, boolean showToast, boolean announceChat, AdvancementVisibility visibility) {
        this.icon = new ItemStack(icon);
        this.iconID = icon;
        if (title.contains("\u00a7")) {
            title = String.valueOf(title) + "\u00a7a";
        }
        this.title = new JSONMessage("{\"text\":\"" + title.replaceAll("\"", "\\\"") + "\"}");
        this.description = new JSONMessage("{\"text\":\"" + description.replaceAll("\"", "\\\"") + "\"}");
        this.frame = frame;
        this.backgroundTexture = backgroundTexture;
        this.showToast = showToast;
        this.announceChat = announceChat;
        this.setVisibility(visibility);
    }

    public AdvancementDisplay(ItemStack icon, JSONMessage title, JSONMessage description, AdvancementFrame frame, boolean showToast, boolean announceChat, AdvancementVisibility visibility) {
        this.icon = icon;
        this.iconID = icon.getType();
        this.title = title;
        this.description = description;
        this.frame = frame;
        this.showToast = showToast;
        this.announceChat = announceChat;
        this.setVisibility(visibility);
    }

    public AdvancementDisplay(ItemStack icon, String title, String description, AdvancementFrame frame, boolean showToast, boolean announceChat, AdvancementVisibility visibility) {
        this.icon = icon;
        this.iconID = icon.getType();
        if (title.contains("\u00a7")) {
            title = String.valueOf(title) + "\u00a7a";
        }
        this.title = new JSONMessage("{\"text\":\"" + title.replaceAll("\"", "\\\"") + "\"}");
        this.description = new JSONMessage("{\"text\":\"" + description.replaceAll("\"", "\\\"") + "\"}");
        this.frame = frame;
        this.showToast = showToast;
        this.announceChat = announceChat;
        this.setVisibility(visibility);
    }

    public AdvancementDisplay(ItemStack icon, JSONMessage title, JSONMessage description, AdvancementFrame frame, String backgroundTexture, boolean showToast, boolean announceChat, AdvancementVisibility visibility) {
        this.icon = icon;
        this.iconID = icon.getType();
        this.title = title;
        this.description = description;
        this.frame = frame;
        this.backgroundTexture = backgroundTexture;
        this.showToast = showToast;
        this.announceChat = announceChat;
        this.setVisibility(visibility);
    }

    public AdvancementDisplay(ItemStack icon, String title, String description, AdvancementFrame frame, String backgroundTexture, boolean showToast, boolean announceChat, AdvancementVisibility visibility) {
        this.icon = icon;
        this.iconID = icon.getType();
        if (title.contains("\u00a7")) {
            title = String.valueOf(title) + "\u00a7a";
        }
        this.title = new JSONMessage("{\"text\":\"" + title.replaceAll("\"", "\\\"") + "\"}");
        this.description = new JSONMessage("{\"text\":\"" + description.replaceAll("\"", "\\\"") + "\"}");
        this.frame = frame;
        this.backgroundTexture = backgroundTexture;
        this.showToast = showToast;
        this.announceChat = announceChat;
        this.setVisibility(visibility);
    }

    public ItemStack getIcon() {
        if (this.icon == null && this.iconID != null) {
            this.icon = new ItemStack(this.iconID);
        }
        return this.icon;
    }

    public JSONMessage getTitle() {
        return this.title;
    }

    public JSONMessage getDescription() {
        return this.description;
    }

    public AdvancementFrame getFrame() {
        return this.frame;
    }

    public boolean isToastShown() {
        return this.showToast;
    }

    public boolean isAnnouncedToChat() {
        // FIXME: implement a disable method for chat announces
        if (this.announceChat /*&& CrazyAdvancements.isAnnounceAdvancementMessages()*/) {
            return true;
        }
        return false;
    }

    @Nullable
    public String getBackgroundTexture() {
        return this.backgroundTexture;
    }

    public void setBackgroundTexture(@Nullable String backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float generateX() {
        if (this.getPositionOrigin() == null) {
            return this.x;
        }
        return this.getPositionOrigin().getDisplay().generateX() + this.x;
    }

    public float generateY() {
        if (this.getPositionOrigin() == null) {
            return this.y;
        }
        return this.getPositionOrigin().getDisplay().generateY() + this.y;
    }

    public float getTabWidth() {
        return this.tabWidth;
    }

    public float getTabHeight() {
        return this.tabHeight;
    }

    public AdvancementVisibility getVisibility() {
        return this.vis != null ? this.vis : AdvancementVisibility.VANILLA;
    }

    public boolean isVisible(Player player, AbstractAdvancement advancement) {
        AdvancementVisibility visibility = this.getVisibility();
        if (!(visibility.isVisible(player, advancement) || advancement.isGranted(player) || visibility.isAlwaysVisibleWhenAdvancementAfterIsVisible() && advancement.isAnythingGrantedAfter(player))) {
            return false;
        }
        return true;
    }

    public AbstractAdvancement getPositionOrigin() {
        return this.positionOrigin;
    }

    public void setIcon(Material icon) {
        this.icon = new ItemStack(icon);
        this.iconID = icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
        this.iconID = icon.getType();
    }

    public void setTitle(JSONMessage title) {
        this.title = title;
    }

    public void setTitle(String title) {
        if (title.contains("\u00a7")) {
            title = String.valueOf(title) + "\u00a7a";
        }
        this.title = new JSONMessage("{\"text\":\"" + title.replaceAll("\"", "\\\"") + "\"}");
    }

    public void setDescription(JSONMessage description) {
        this.description = description;
    }

    public void setDescription(String description) {
        this.description = new JSONMessage("{\"text\":\"" + description.replaceAll("\"", "\\\"") + "\"}");
    }

    public void setFrame(AdvancementFrame frame) {
        this.frame = frame;
    }

    public void setShowToast(boolean showToast) {
        this.showToast = showToast;
    }

    public void setAnnounceChat(boolean announceChat) {
        this.announceChat = announceChat;
    }

    public void setVisibility(AdvancementVisibility visibility) {
        this.vis = visibility;
        this.visibilityIdentifier = this.getVisibility().getName();
    }

    public void setCoordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setTabHeight(float tabHeight) {
        this.tabHeight = tabHeight;
    }

    public void setTabWidth(float tabWidth) {
        this.tabWidth = tabWidth;
    }

    public void setPositionOrigin(AbstractAdvancement positionOrigin) {
        this.positionOrigin = positionOrigin;
    }

}

