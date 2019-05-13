package org.valdi.SuperApiX.bukkit.nms.v1_14_R1.advancements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.advancements.AdvancementProvider;
import org.valdi.SuperApiX.bukkit.advancements.managers.AbstractAdvancementManager;
import org.valdi.SuperApiX.bukkit.events.advancements.AdvancementGrantEvent;
import org.valdi.SuperApiX.bukkit.events.advancements.AdvancementRevokeEvent;
import org.valdi.SuperApiX.bukkit.events.advancements.CriteriaGrantEvent;
import org.valdi.SuperApiX.bukkit.events.advancements.offline.OfflineAdvancementRevokeEvent;
import org.valdi.SuperApiX.bukkit.events.advancements.offline.OfflineCriteriaGrantEvent;

public class AdvancementManager extends AbstractAdvancementManager<Advancement> {

    public AdvancementManager(SuperApiBukkit plugin) {
        super(plugin);
    }

    @Override
    protected void addPlayer(Player player, SuperKey tab) {
        if(!players.contains(player)) {
            players.add(player);
        }

        Collection<net.minecraft.server.v1_14_R1.Advancement> advs = new ArrayList<>();
        Set<MinecraftKey> remove = new HashSet<>();
        Map<MinecraftKey, AdvancementProgress> prgs = new HashMap<>();

        for(Advancement advancement : advancements) {
            boolean isTab = tab != null && advancement.getTab().equals(tab);
            if(isTab) {
                remove.add((MinecraftKey) advancement.getName().toMinecraftKey());
            }

            if(tab == null || isTab) {
                //Criteria
                checkAwarded(player, advancement);

                org.valdi.SuperApiX.bukkit.advancements.AdvancementDisplay display = advancement.getDisplay();

                boolean showToast = display.isToastShown() && getCriteriaProgress(player, advancement) < advancement.getSavedCriteria().size();

                ItemStack icon = CraftItemStack.asNMSCopy(display.getIcon());

                MinecraftKey backgroundTexture = null;
                boolean hasBackgroundTexture = display.getBackgroundTexture() != null;

                if(hasBackgroundTexture) {
                    backgroundTexture = new MinecraftKey(display.getBackgroundTexture());
                }

                boolean hidden = !display.isVisible(player, advancement);
                advancement.saveHiddenStatus(player, hidden);

                if(!hidden || hiddenBoolean) {
                    AdvancementDisplay advDisplay = new AdvancementDisplay(icon, (IChatBaseComponent) display.getTitle().getBaseComponent(), (IChatBaseComponent) display.getDescription().getBaseComponent(), backgroundTexture, (AdvancementFrameType) display.getFrame().getNMS(), showToast, display.isAnnouncedToChat(), hidden && hiddenBoolean);
                    advDisplay.a(display.generateX() - getSmallestX(advancement.getTab()), display.generateY() - getSmallestY(advancement.getTab()));

                    AdvancementRewards advRewards = new AdvancementRewards(0, new MinecraftKey[0], new MinecraftKey[0], null);

                    Map<String, Criterion> advCriteria = new HashMap<>();
                    String[][] advRequirements;

                    if(advancement.getSavedCriteria() == null) {
                        for(int i = 0; i < advancement.getCriteria(); i++) {
                            advCriteria.put("criterion." + i, new Criterion(() -> new MinecraftKey(SuperKey.MINECRAFT, "impossible")));
                        }
                        advancement.saveCriteria(advCriteria);
                    } else {
                        advCriteria = advancement.getSavedCriteria();
                    }

                    if(advancement.getSavedCriteriaRequirements() == null) {
                        ArrayList<String[]> fixedRequirements = new ArrayList<>();
                        for(String name : advCriteria.keySet()) {
                            fixedRequirements.add(new String[] {name});
                        }
                        advRequirements = Arrays.stream(fixedRequirements.toArray()).toArray(String[][]::new);
                        advancement.saveCriteriaRequirements(advRequirements);
                    } else {
                        advRequirements = advancement.getSavedCriteriaRequirements();
                    }

                    net.minecraft.server.v1_14_R1.Advancement adv = new net.minecraft.server.v1_14_R1.Advancement((MinecraftKey) advancement.getName().toMinecraftKey(), advancement.getParent() == null ? null : advancement.getParent().getSavedAdvancement(), advDisplay, advRewards, advCriteria, advRequirements);

                    advs.add(adv);

                    AdvancementProgress advPrg = advancement.getProgress(player);
                    advPrg.a(advancement.getSavedCriteria(), advancement.getSavedCriteriaRequirements());

                    for(String criterion : advancement.getAwardedCriteria().get(player.getUniqueId().toString())) {
                        CriterionProgress critPrg = advPrg.getCriterionProgress(criterion);
                        critPrg.b();
                    }

                    advancement.setProgress(player, advPrg);

                    prgs.put((MinecraftKey) advancement.getName().toMinecraftKey(), advPrg);
                }
            }

        }

        //Packet
        PacketPlayOutAdvancements packet = new PacketPlayOutAdvancements(false, advs, remove, prgs);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void removePlayer(Player player) {
        players.remove(player);

        Collection<net.minecraft.server.v1_14_R1.Advancement> advs = new ArrayList<>();
        Set<MinecraftKey> remove = new HashSet<>();
        Map<MinecraftKey, AdvancementProgress> prgs = new HashMap<>();

        for(Advancement advancement : advancements) {
            remove.add((MinecraftKey) advancement.getName().toMinecraftKey());
        }

        //Packet
        PacketPlayOutAdvancements packet = new PacketPlayOutAdvancements(false, advs, remove, prgs);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void addAdvancement(Advancement... advancementsAdded) {
        HashMap<Player, Collection<net.minecraft.server.v1_14_R1.Advancement>> advancementsList = new HashMap<>();
        Set<MinecraftKey> remove = new HashSet<>();
        HashMap<Player, Map<MinecraftKey, AdvancementProgress>> progressList = new HashMap<>();

        HashSet<SuperKey> updatedTabs = new HashSet<>();

        for(Advancement adv : advancementsAdded) {
            float smallestY = getSmallestY(adv.getTab());
            float y = adv.getDisplay().generateY();
            if(y < smallestY) {
                smallestY = y;
                updatedTabs.add(adv.getTab());
                AdvancementManager.smallestY.put(adv.getTab(), smallestY);
            }

            float smallestX = getSmallestX(adv.getTab());
            float x = adv.getDisplay().generateY();
            if(x < smallestX) {
                smallestX = x;
                updatedTabs.add(adv.getTab());
                AdvancementManager.smallestX.put(adv.getTab(), smallestX);
            }
        }

        for(SuperKey key : updatedTabs) {
            for(Player player : players) {
                update(player, key);
            }
        }

        for(Advancement advancement : advancementsAdded) {
            if(advancements.contains(advancement)) {
                remove.add((MinecraftKey) advancement.getName().toMinecraftKey());
            } else {
                advancements.add(advancement);
            }
            org.valdi.SuperApiX.bukkit.advancements.AdvancementDisplay display = advancement.getDisplay();

            AdvancementRewards advRewards = new AdvancementRewards(0, new MinecraftKey[0], new MinecraftKey[0], null);

            ItemStack icon = CraftItemStack.asNMSCopy(display.getIcon());

            MinecraftKey backgroundTexture = null;
            boolean hasBackgroundTexture = display.getBackgroundTexture() != null;

            if(hasBackgroundTexture) {
                backgroundTexture = new MinecraftKey(display.getBackgroundTexture());
            }

            Map<String, Criterion> advCriteria = new HashMap<>();
            String[][] advRequirements;

            if(advancement.getSavedCriteria() == null) {
                for(int i = 0; i < advancement.getCriteria(); i++) {
                    advCriteria.put("criterion." + i, new Criterion(() -> new MinecraftKey(SuperKey.MINECRAFT, "impossible")));
                }
                advancement.saveCriteria(advCriteria);
            } else {
                advCriteria = advancement.getSavedCriteria();
            }

            if(advancement.getSavedCriteriaRequirements() == null) {
                ArrayList<String[]> fixedRequirements = new ArrayList<>();
                for(String name : advCriteria.keySet()) {
                    fixedRequirements.add(new String[] {name});
                }
                advRequirements = Arrays.stream(fixedRequirements.toArray()).toArray(String[][]::new);
                advancement.saveCriteriaRequirements(advRequirements);
            } else {
                advRequirements = advancement.getSavedCriteriaRequirements();
            }

            AdvancementDisplay saveDisplay = new AdvancementDisplay(icon, (IChatBaseComponent) display.getTitle().getBaseComponent(), (IChatBaseComponent) display.getDescription().getBaseComponent(), backgroundTexture, (AdvancementFrameType) display.getFrame().getNMS(), display.isToastShown(), display.isAnnouncedToChat(), true);
            saveDisplay.a(display.generateX() - getSmallestY(advancement.getTab()), display.generateY() - getSmallestX(advancement.getTab()));

            net.minecraft.server.v1_14_R1.Advancement saveAdv = new net.minecraft.server.v1_14_R1.Advancement((MinecraftKey) advancement.getName().toMinecraftKey(), advancement.getParent() == null ? null : advancement.getParent().getSavedAdvancement(), saveDisplay, advRewards, advCriteria, advRequirements);

            advancement.saveAdvancement(saveAdv);

            for(Player player : getPlayers()) {
                Map<MinecraftKey, AdvancementProgress> prgs = progressList.containsKey(player) ? progressList.get(player) : new HashMap<>();
                checkAwarded(player, advancement);

                boolean showToast = display.isToastShown() && getCriteriaProgress(player, advancement) < advancement.getSavedCriteria().size();

                Collection<net.minecraft.server.v1_14_R1.Advancement> advs = advancementsList.containsKey(player) ? advancementsList.get(player) : new ArrayList<>();

                boolean hidden = !display.isVisible(player, advancement);
                advancement.saveHiddenStatus(player, hidden);

                if(!hidden || hiddenBoolean) {
                    AdvancementDisplay advDisplay = new AdvancementDisplay(icon, (IChatBaseComponent) display.getTitle().getBaseComponent(), (IChatBaseComponent) display.getDescription().getBaseComponent(), backgroundTexture, (AdvancementFrameType) display.getFrame().getNMS(), showToast, display.isAnnouncedToChat(), hidden && hiddenBoolean);
                    advDisplay.a(display.generateX() - getSmallestX(advancement.getTab()), display.generateY() - getSmallestY(advancement.getTab()));

                    net.minecraft.server.v1_14_R1.Advancement adv = new net.minecraft.server.v1_14_R1.Advancement((MinecraftKey) advancement.getName().toMinecraftKey(), advancement.getParent() == null ? null : advancement.getParent().getSavedAdvancement(), advDisplay, advRewards, advCriteria, advRequirements);

                    advs.add(adv);

                    advancementsList.put(player, advs);

                    AdvancementProgress advPrg = advancement.getProgress(player);
                    advPrg.a(advCriteria, advRequirements);

                    for(String criterion : advancement.getAwardedCriteria().get(player.getUniqueId().toString())) {
                        CriterionProgress critPrg = advPrg.getCriterionProgress(criterion);
                        critPrg.b();
                    }

                    advancement.setProgress(player, advPrg);

                    prgs.put((MinecraftKey) advancement.getName().toMinecraftKey(), advPrg);

                    progressList.put(player, prgs);
                }
            }
        }

        for(Player player : getPlayers()) {
            //Packet
            PacketPlayOutAdvancements packet = new PacketPlayOutAdvancements(false, advancementsList.get(player), remove, progressList.get(player));
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public void removeAdvancement(Advancement... advancementsRemoved) {
        Collection<net.minecraft.server.v1_14_R1.Advancement> advs = new ArrayList<>();
        Set<MinecraftKey> remove = new HashSet<>();
        Map<MinecraftKey, AdvancementProgress> prgs = new HashMap<>();

        for(Advancement advancement : advancementsRemoved) {
            if(advancements.contains(advancement)) {
                advancements.remove(advancement);

                remove.add((MinecraftKey) advancement.getName().toMinecraftKey());
            }
        }

        for(Player player : getPlayers()) {
            //Packet
            PacketPlayOutAdvancements packet = new PacketPlayOutAdvancements(false, advs, remove, prgs);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    protected void updateProgress(Player player, boolean alreadyGranted, boolean fireEvent, Advancement... advancementsUpdated) {
        if(players.contains(player)) {
            Collection<net.minecraft.server.v1_14_R1.Advancement> advs = new ArrayList<>();
            Set<MinecraftKey> remove = new HashSet<>();
            Map<MinecraftKey, AdvancementProgress> prgs = new HashMap<>();

            for(Advancement advancement : advancementsUpdated) {
                if(advancements.contains(advancement)) {
                    checkAwarded(player, advancement);

                    AdvancementProgress advPrg = advancement.getProgress(player);
                    boolean hidden = advancement.getHiddenStatus(player);


                    advPrg.a(advancement.getSavedCriteria(), advancement.getSavedCriteriaRequirements());

                    Map<String, HashSet<String>> awardedCriteria = advancement.getAwardedCriteria();
                    HashSet<String> awarded = awardedCriteria.get(player.getUniqueId().toString());

                    for(String criterion : advancement.getSavedCriteria().keySet()) {
                        if(awarded.contains(criterion)) {
                            CriterionProgress critPrg = advPrg.getCriterionProgress(criterion);
                            critPrg.b();
                        } else {
                            CriterionProgress critPrg = advPrg.getCriterionProgress(criterion);
                            critPrg.c();
                        }
                    }

                    advancement.setProgress(player, advPrg);
                    prgs.put((MinecraftKey) advancement.getName().toMinecraftKey(), advPrg);

                    if(hidden && advPrg.isDone()) {
                        org.valdi.SuperApiX.bukkit.advancements.AdvancementDisplay display = advancement.getDisplay();

                        AdvancementRewards advRewards = new AdvancementRewards(0, new MinecraftKey[0], new MinecraftKey[0], null);

                        ItemStack icon = CraftItemStack.asNMSCopy(display.getIcon());

                        MinecraftKey backgroundTexture = null;
                        boolean hasBackgroundTexture = display.getBackgroundTexture() != null;

                        if(hasBackgroundTexture) {
                            backgroundTexture = new MinecraftKey(display.getBackgroundTexture());
                        }

                        Map<String, Criterion> advCriteria = new HashMap<>();
                        String[][] advRequirements;

                        if(advancement.getSavedCriteria() == null) {
                            for(int i = 0; i < advancement.getCriteria(); i++) {
                                advCriteria.put("criterion." + i, new Criterion(() -> new MinecraftKey(SuperKey.MINECRAFT, "impossible")));
                            }
                            advancement.saveCriteria(advCriteria);
                        } else {
                            advCriteria = advancement.getSavedCriteria();
                        }

                        if(advancement.getSavedCriteriaRequirements() == null) {
                            ArrayList<String[]> fixedRequirements = new ArrayList<>();
                            for(String name : advCriteria.keySet()) {
                                fixedRequirements.add(new String[] {name});
                            }
                            advRequirements = Arrays.stream(fixedRequirements.toArray()).toArray(String[][]::new);
                            advancement.saveCriteriaRequirements(advRequirements);
                        } else {
                            advRequirements = advancement.getSavedCriteriaRequirements();
                        }

                        AdvancementDisplay advDisplay = new AdvancementDisplay(icon, (IChatBaseComponent) display.getTitle().getBaseComponent(), (IChatBaseComponent) display.getDescription().getBaseComponent(), backgroundTexture, (AdvancementFrameType) display.getFrame().getNMS(), display.isToastShown(), display.isAnnouncedToChat(), hiddenBoolean);
                        advDisplay.a(display.generateX() - getSmallestX(advancement.getTab()), display.generateY() - getSmallestY(advancement.getTab()));

                        net.minecraft.server.v1_14_R1.Advancement adv = new net.minecraft.server.v1_14_R1.Advancement((MinecraftKey) advancement.getName().toMinecraftKey(), advancement.getParent() == null ? null : advancement.getParent().getSavedAdvancement(), advDisplay, advRewards, advCriteria, advRequirements);

                        advs.add(adv);
                    }

                    if(!alreadyGranted) {
                        if(advPrg.isDone()) {
                            grantAdvancement(player, advancement, true, false, fireEvent);
                        }
                    }
                }
            }

            PacketPlayOutAdvancements packet = new PacketPlayOutAdvancements(false, advs, remove, prgs);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public void updateVisibility(Player player, Advancement advancement) {
        if(players.contains(player)) {
            Collection<net.minecraft.server.v1_14_R1.Advancement> advs = new ArrayList<>();
            Set<MinecraftKey> remove = new HashSet<>();
            Map<MinecraftKey, AdvancementProgress> prgs = new HashMap<>();

            if(advancements.contains(advancement)) {
                checkAwarded(player, advancement);

                org.valdi.SuperApiX.bukkit.advancements.AdvancementDisplay display = advancement.getDisplay();
                boolean hidden = !display.isVisible(player, advancement);

                if(hidden == advancement.getHiddenStatus(player)) {
                    return;
                }

                advancement.saveHiddenStatus(player, hidden);

                if(!hidden || hiddenBoolean) {
                    remove.add((MinecraftKey) advancement.getName().toMinecraftKey());

                    AdvancementRewards advRewards = new AdvancementRewards(0, new MinecraftKey[0], new MinecraftKey[0], null);

                    ItemStack icon = CraftItemStack.asNMSCopy(display.getIcon());

                    MinecraftKey backgroundTexture = null;
                    boolean hasBackgroundTexture = display.getBackgroundTexture() != null;

                    if(hasBackgroundTexture) {
                        backgroundTexture = new MinecraftKey(display.getBackgroundTexture());
                    }

                    Map<String, Criterion> advCriteria = new HashMap<>();
                    String[][] advRequirements;

                    if(advancement.getSavedCriteria() == null) {
                        for(int i = 0; i < advancement.getCriteria(); i++) {
                            advCriteria.put("criterion." + i, new Criterion(() -> new MinecraftKey(SuperKey.MINECRAFT, "impossible")));
                        }
                        advancement.saveCriteria(advCriteria);
                    } else {
                        advCriteria = advancement.getSavedCriteria();
                    }

                    if(advancement.getSavedCriteriaRequirements() == null) {
                        ArrayList<String[]> fixedRequirements = new ArrayList<>();
                        for(String name : advCriteria.keySet()) {
                            fixedRequirements.add(new String[] {name});
                        }
                        advRequirements = Arrays.stream(fixedRequirements.toArray()).toArray(String[][]::new);
                        advancement.saveCriteriaRequirements(advRequirements);
                    } else {
                        advRequirements = advancement.getSavedCriteriaRequirements();
                    }

                    boolean showToast = display.isToastShown();

                    AdvancementDisplay advDisplay = new AdvancementDisplay(icon, (IChatBaseComponent) display.getTitle().getBaseComponent(), (IChatBaseComponent) display.getDescription().getBaseComponent(), backgroundTexture, (AdvancementFrameType) display.getFrame().getNMS(), showToast, display.isAnnouncedToChat(), hidden && hiddenBoolean);
                    advDisplay.a(display.generateX() - getSmallestX(advancement.getTab()), display.generateY() - getSmallestY(advancement.getTab()));

                    net.minecraft.server.v1_14_R1.Advancement adv = new net.minecraft.server.v1_14_R1.Advancement((MinecraftKey) advancement.getName().toMinecraftKey(), advancement.getParent() == null ? null : advancement.getParent().getSavedAdvancement(), advDisplay, advRewards, advCriteria, advRequirements);

                    advs.add(adv);

                    AdvancementProgress advPrg = advancement.getProgress(player);
                    advPrg.a(advCriteria, advRequirements);

                    for(String criterion : advancement.getAwardedCriteria().get(player.getUniqueId().toString())) {
                        CriterionProgress critPrg = advPrg.getCriterionProgress(criterion);
                        critPrg.b();
                    }

                    advancement.setProgress(player, advPrg);

                    prgs.put((MinecraftKey) advancement.getName().toMinecraftKey(), advPrg);
                }
            }

            // Packet
            PacketPlayOutAdvancements packet = new PacketPlayOutAdvancements(false, advs, remove, prgs);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public void displayMessage(Player player, Advancement advancement) {
        IChatBaseComponent message = advancement.getMessage(player);

        PacketPlayOutChat packet = new PacketPlayOutChat(message);
        for(Player receivers : getPlayers()) {
            ((CraftPlayer) receivers).getHandle().playerConnection.sendPacket(packet);
        }
    }

    /*
     * Awards section
     *
     */

    @Override
    protected void checkAwarded(Player player, Advancement advancement) {
        Map<String, Criterion> advCriteria = new HashMap<>();
        String[][] advRequirements;

        if(advancement.getSavedCriteria() == null) {
            for(int i = 0; i < advancement.getCriteria(); i++) {
                advCriteria.put("criterion." + i, new Criterion(() -> new MinecraftKey(SuperKey.MINECRAFT, "impossible")));
            }
            advancement.saveCriteria(advCriteria);
        } else {
            advCriteria = advancement.getSavedCriteria();
        }

        if(advancement.getSavedCriteriaRequirements() == null) {
            ArrayList<String[]> fixedRequirements = new ArrayList<>();
            for(String name : advCriteria.keySet()) {
                fixedRequirements.add(new String[] {name});
            }
            advRequirements = Arrays.stream(fixedRequirements.toArray()).toArray(String[][]::new);
            advancement.saveCriteriaRequirements(advRequirements);
        } else {
            advRequirements = advancement.getSavedCriteriaRequirements();
        }

        Map<String, HashSet<String>> awardedCriteria = advancement.getAwardedCriteria();
        if(!awardedCriteria.containsKey(player.getUniqueId().toString())) {
            awardedCriteria.put(player.getUniqueId().toString(), new HashSet<>());
        }
    }

    @Override
    protected void checkAwarded(UUID uuid, Advancement advancement) {
        Map<String, Criterion> advCriteria = new HashMap<>();
        String[][] advRequirements;

        if(advancement.getSavedCriteria() == null) {
            for(int i = 0; i < advancement.getCriteria(); i++) {
                advCriteria.put("criterion." + i, new Criterion(() -> new MinecraftKey(SuperKey.MINECRAFT, "impossible")));
            }
            advancement.saveCriteria(advCriteria);
        } else {
            advCriteria = advancement.getSavedCriteria();
        }

        if(advancement.getSavedCriteriaRequirements() == null) {
            ArrayList<String[]> fixedRequirements = new ArrayList<>();
            for(String name : advCriteria.keySet()) {
                fixedRequirements.add(new String[] {name});
            }
            advRequirements = Arrays.stream(fixedRequirements.toArray()).toArray(String[][]::new);
            advancement.saveCriteriaRequirements(advRequirements);
        } else {
            advRequirements = advancement.getSavedCriteriaRequirements();
        }

        Map<String, HashSet<String>> awardedCriteria = advancement.getAwardedCriteria();
        if(!awardedCriteria.containsKey(uuid.toString())) {
            awardedCriteria.put(uuid.toString(), new HashSet<>());
        }
    }

    @Override
    protected void grantAdvancement(Player player, Advancement advancement, boolean alreadyGranted, boolean updateProgress, boolean fireEvent) {
        checkAwarded(player, advancement);
        Map<String, HashSet<String>> awardedCriteria = advancement.getAwardedCriteria();

        HashSet<String> awarded = awardedCriteria.get(player.getUniqueId().toString());
        for(String criterion : advancement.getSavedCriteria().keySet()) {
            awarded.add(criterion);
        }
        awardedCriteria.put(player.getUniqueId().toString(), awarded);
        advancement.setAwardedCriteria(awardedCriteria);

        if(fireEvent) {
            boolean announceChat = advancement.getDisplay().isAnnouncedToChat() && plugin.getAdvProvider().getInitiatedPlayers().contains(player) && plugin.getAdvProvider().isAnnounceAdvancementMessages() && isAnnounceAdvancementMessages();

            AdvancementGrantEvent event = new AdvancementGrantEvent(this, advancement, player, announceChat);
            Bukkit.getPluginManager().callEvent(event);
            if(advancement.getReward() != null) advancement.getReward().onGrant(player);
            if(event.isDisplayMessage()) {
                displayMessage(player, advancement);
            }
        }

        if(updateProgress) {
            updateProgress(player, alreadyGranted, false, advancement);
            updateAllPossiblyAffectedVisibilities(player, advancement);
        }
    }

    @Override
    public void revokeCriteria(Player player, Advancement advancement, String... criteria) {
        checkAwarded(player, advancement);
        Map<String, HashSet<String>> awardedCriteria = advancement.getAwardedCriteria();

        AdvancementProgress advPrg = advancement.getProgress(player);
        if(advPrg.isDone()) {
            AdvancementRevokeEvent event = new AdvancementRevokeEvent(this, advancement, player);
            Bukkit.getPluginManager().callEvent(event);
        }

        HashSet<String> awarded = awardedCriteria.get(player.getUniqueId().toString());
        for(String criterion : criteria) {
            awarded.remove(criterion);
        }
        awardedCriteria.put(player.getUniqueId().toString(), awarded);
        advancement.setAwardedCriteria(awardedCriteria);

        updateProgress(player, advancement);
        updateAllPossiblyAffectedVisibilities(player, advancement);

        CriteriaGrantEvent event = new CriteriaGrantEvent(this, advancement, criteria, player);
        Bukkit.getPluginManager().callEvent(event);
    }

    @Override
    public void revokeCriteria(UUID uuid, Advancement advancement, String... criteria) {
        if(isOnline(uuid)) {
            revokeCriteria(Bukkit.getPlayer(uuid), advancement, criteria);
        } else {
            checkAwarded(uuid, advancement);
            Map<String, HashSet<String>> awardedCriteria = advancement.getAwardedCriteria();

            AdvancementProgress advPrg = advancement.getProgress(uuid);
            if(advPrg.isDone()) {
                OfflineAdvancementRevokeEvent event = new OfflineAdvancementRevokeEvent(this, advancement, uuid);
                Bukkit.getPluginManager().callEvent(event);
            }

            HashSet<String> awarded = awardedCriteria.get(uuid.toString());
            for(String criterion : criteria) {
                awarded.remove(criterion);
            }
            awardedCriteria.put(uuid.toString(), awarded);
            advancement.setAwardedCriteria(awardedCriteria);

            OfflineCriteriaGrantEvent event = new OfflineCriteriaGrantEvent(this, advancement, criteria, uuid);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    /*
     * Online Save/Load
     *
     */

    @Override
    public String getProgressJSON(Player player) {
        HashMap<String, List<String>> prg = new HashMap<>();

        for(Advancement advancement : getAdvancements()) {
            String nameKey = advancement.getName().toString();

            List<String> progress = prg.containsKey(nameKey) ? prg.get(nameKey) : new ArrayList<>();
            AdvancementProgress advPrg = advancement.getProgress(player);
            for(String criterion : advancement.getSavedCriteria().keySet()) {
                CriterionProgress critPrg = advPrg.getCriterionProgress(criterion);
                if(critPrg != null && critPrg.a()) {
                    progress.add(criterion);
                }
            }
            prg.put(nameKey, progress);
        }

        check();

        return gson.toJson(prg);
    }

    @Override
    public String getProgressJSON(Player player, String namespace) {
        HashMap<String, List<String>> prg = new HashMap<>();

        for(Advancement advancement : getAdvancements()) {
            String anotherNamespace = advancement.getName().getNamespace();

            if(namespace.equalsIgnoreCase(anotherNamespace)) {
                String nameKey = advancement.getName().toString();

                List<String> progress = prg.containsKey(nameKey) ? prg.get(nameKey) : new ArrayList<>();
                AdvancementProgress advPrg = advancement.getProgress(player);
                for(String criterion : advancement.getSavedCriteria().keySet()) {
                    CriterionProgress critPrg = advPrg.getCriterionProgress(criterion);
                    if(critPrg != null && critPrg.a()) {
                        progress.add(criterion);
                    }
                }
                prg.put(nameKey, progress);
            }
        }

        check();

        return gson.toJson(prg);
    }

    /*
     * Offline Save/Load
     *
     */

    @Override
    public String getProgressJSON(UUID uuid) {
        HashMap<String, List<String>> prg = new HashMap<>();

        for(Advancement advancement : getAdvancements()) {
            String nameKey = advancement.getName().toString();

            List<String> progress = prg.containsKey(nameKey) ? prg.get(nameKey) : new ArrayList<>();
            AdvancementProgress advPrg = advancement.getProgress(uuid);
            for(String criterion : advancement.getSavedCriteria().keySet()) {
                CriterionProgress critPrg = advPrg.getCriterionProgress(criterion);
                if(critPrg != null && critPrg.a()) {
                    progress.add(criterion);
                }
            }
            prg.put(nameKey, progress);
        }

        check();

        return gson.toJson(prg);
    }

    @Override
    public String getProgressJSON(UUID uuid, String namespace) {
        HashMap<String, List<String>> prg = new HashMap<>();

        for(Advancement advancement : getAdvancements()) {
            String anotherNamespace = advancement.getName().getNamespace();

            if(namespace.equalsIgnoreCase(anotherNamespace)) {
                String nameKey = advancement.getName().toString();

                List<String> progress = prg.containsKey(nameKey) ? prg.get(nameKey) : new ArrayList<>();
                AdvancementProgress advPrg = advancement.getProgress(uuid);
                for(String criterion : advancement.getSavedCriteria().keySet()) {
                    CriterionProgress critPrg = advPrg.getCriterionProgress(criterion);
                    if(critPrg != null && critPrg.a()) {
                        progress.add(criterion);
                    }
                }
                prg.put(nameKey, progress);
            }
        }

        check();

        return gson.toJson(prg);
    }

}

