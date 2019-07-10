package org.valdi.SuperApiX.bukkit.nms.v1_13_R2.advancements;

import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.advancements.managers.AbstractAdvancement;
import org.valdi.SuperApiX.bukkit.advancements.AdvancementDisplay;
import org.valdi.SuperApiX.bukkit.advancements.AdvancementFrame;
import org.valdi.SuperApiX.bukkit.nms.JSONMessage;

import javax.annotation.Nullable;
import java.util.*;

public class Advancement extends AbstractAdvancement<net.minecraft.server.v1_13_R2.Advancement, AdvancementProgress, Criterion> {

    public Advancement(SuperApiBukkit plugin, @Nullable AbstractAdvancement parent, SuperKey name, AdvancementDisplay display) {
        super(plugin, parent, name, display);
    }

    @Override
    public void setCriteria(int criteria) {
        this.criteria = criteria;
        HashMap<String, Criterion> advCriteria = new HashMap<>();
        for(int i = 0; i < getCriteria(); i++) {
            advCriteria.put("criterion." + i, new Criterion(() -> new MinecraftKey(SuperKey.MINECRAFT, "impossible")));
        }
        this.saveCriteria(advCriteria);

        ArrayList<String[]> fixedRequirements = new ArrayList<>();
        for (String name : advCriteria.keySet()) {
            fixedRequirements.add(new String[]{name});
        }

        String[][] advRequirements = Arrays.stream(fixedRequirements.toArray()).toArray(String[][]::new);
        this.saveCriteriaRequirements(advRequirements);
    }

    @Override
    public IChatBaseComponent getMessage(Player player) {
        String translation = "chat.type.advancement." + this.getDisplay().getFrame().name().toLowerCase();
        IChatBaseComponent title = IChatBaseComponent.ChatSerializer.a(this.getDisplay().getTitle().getJson());
        IChatBaseComponent description = IChatBaseComponent.ChatSerializer.a(this.getDisplay().getDescription().getJson());
        ChatModifier tm = title.getChatModifier();
        AdvancementFrame frame = this.getDisplay().getFrame();
        EnumChatFormat typeColor = frame == AdvancementFrame.CHALLENGE ? EnumChatFormat.DARK_PURPLE : EnumChatFormat.GREEN;
        EnumChatFormat color = tm.getColor() == null ? typeColor : tm.getColor();
        return IChatBaseComponent.ChatSerializer.a(("{\"translate\":\"" + translation + "\"," + "\"with\":" + "[" + "\"" + player.getDisplayName() + "\"," + "{" + "\"text\":\"[" + title.getText() + "]\",\"color\":\"" + color.name().toLowerCase() + "\",\"bold\":" + tm.isBold() + ",\"italic\":" + tm.isItalic() + ", \"strikethrough\":" + tm.isStrikethrough() + ",\"underlined\":" + tm.isUnderlined() + ",\"obfuscated\":" + tm.isRandom() + "," + "\"hoverEvent\":" + "{" + "\"action\":\"show_text\"," + "\"value\":[\"\", {\"text\":\"" + title.getText() + "\",\"color\":\"" + color.name().toLowerCase() + "\",\"bold\":" + tm.isBold() + ",\"italic\":" + tm.isItalic() + ", \"strikethrough\":" + tm.isStrikethrough() + ",\"underlined\":" + tm.isUnderlined() + ",\"obfuscated\":" + tm.isRandom() + "}, {\"text\":\"\\n\"}, {\"text\":\"" + description.getText() + "\"}]" + "}" + "}" + "]" + "}"));
    }

    private AdvancementFrameType getFrameType(AdvancementFrame type) {
        return AdvancementFrameType.a(type.getId());
    }

    private IChatBaseComponent getBaseComponent(JSONMessage json) {
        return IChatBaseComponent.ChatSerializer.a(json.getJson());
    }

    @Override
    public void displayMessageToEverybody(Player player) {
        IChatBaseComponent message = this.getMessage(player);
        PacketPlayOutChat packet = new PacketPlayOutChat(message);
        for (Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public void displayToast(Player player) {
        MinecraftKey notName = new MinecraftKey("eu.endercentral", "notification");
        org.valdi.SuperApiX.bukkit.advancements.AdvancementDisplay display = this.getDisplay();
        AdvancementRewards advRewards = new AdvancementRewards(0, new MinecraftKey[0], new MinecraftKey[0], null);
        ItemStack icon = CraftItemStack.asNMSCopy(display.getIcon());
        MinecraftKey backgroundTexture = null;
        if (display.getBackgroundTexture() != null) {
            backgroundTexture = new MinecraftKey(display.getBackgroundTexture());
        }

        HashMap<String, Criterion> advCriteria = new HashMap<>();
        advCriteria.put("for_free", new Criterion(() -> new MinecraftKey(SuperKey.MINECRAFT, "impossible")));

        ArrayList<String[]> fixedRequirements = new ArrayList<>();
        fixedRequirements.add(new String[]{"for_free"});
        String[][] advRequirements = Arrays.stream(fixedRequirements.toArray()).toArray(String[][]::new);

        net.minecraft.server.v1_13_R2.AdvancementDisplay saveDisplay = new net.minecraft.server.v1_13_R2.AdvancementDisplay(icon, (IChatBaseComponent) display.getTitle(), (IChatBaseComponent) display.getDescription(), backgroundTexture, (AdvancementFrameType) display.getFrame().getNMS(), display.isToastShown(), display.isAnnouncedToChat(), true);
        net.minecraft.server.v1_13_R2.Advancement saveAdv = new net.minecraft.server.v1_13_R2.Advancement(notName, this.getParent() == null ? null : this.getParent().getSavedAdvancement(), saveDisplay, advRewards, advCriteria, advRequirements);

        HashMap<MinecraftKey, AdvancementProgress> prg = new HashMap<>();
        AdvancementProgress advPrg = new AdvancementProgress();
        advPrg.a(advCriteria, advRequirements);
        advPrg.getCriterionProgress("for_free").b();
        prg.put(notName, advPrg);

        PacketPlayOutAdvancements packet = new PacketPlayOutAdvancements(false, Arrays.asList(saveAdv), new HashSet<>(), prg);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);

        HashSet<MinecraftKey> rm = new HashSet<>();
        rm.add(notName);
        prg.clear();

        packet = new PacketPlayOutAdvancements(false, new ArrayList<>(), rm, prg);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void saveAdvancement(net.minecraft.server.v1_13_R2.Advancement save) {
        this.savedAdvancement = save;
    }

    @Override
    public net.minecraft.server.v1_13_R2.Advancement getSavedAdvancement() {
        return this.savedAdvancement;
    }

    @Override
    public AdvancementProgress getProgress(Player player) {
        if (this.progress == null) {
            this.progress = new HashMap<>();
        }
        return this.progress.containsKey(player.getUniqueId().toString()) ? this.progress.get(player.getUniqueId().toString()) : new AdvancementProgress();
    }

    @Override
    public AdvancementProgress getProgress(UUID uuid) {
        if (this.progress == null) {
            this.progress = new HashMap<>();
        }
        return this.progress.containsKey(uuid.toString()) ? this.progress.get(uuid.toString()) : new AdvancementProgress();
    }

    @Override
    public void setProgress(Player player, AdvancementProgress progress) {
        if (this.progress == null) {
            this.progress = new HashMap<>();
        }
        this.progress.put(player.getUniqueId().toString(), progress);
    }

    @Override
    public boolean isDone(Player player) {
        return this.getProgress(player).isDone();
    }

    @Override
    public boolean isDone(UUID uuid) {
        return this.getProgress(uuid).isDone();
    }

    @Override
    public boolean isGranted(Player player) {
        return this.getProgress(player).isDone();
    }

}
