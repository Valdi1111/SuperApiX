package org.valdi.SuperApiX.bukkit.advancements.managers;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.advancements.exceptions.UnloadProgressFailedException;
import org.valdi.SuperApiX.bukkit.events.advancements.AdvancementRevokeEvent;
import org.valdi.SuperApiX.bukkit.events.advancements.CriteriaGrantEvent;
import org.valdi.SuperApiX.bukkit.events.advancements.CriteriaProgressChangeEvent;
import org.valdi.SuperApiX.bukkit.events.advancements.offline.OfflineAdvancementGrantEvent;
import org.valdi.SuperApiX.bukkit.events.advancements.offline.OfflineAdvancementRevokeEvent;
import org.valdi.SuperApiX.bukkit.events.advancements.offline.OfflineCriteriaGrantEvent;
import org.valdi.SuperApiX.bukkit.events.advancements.offline.OfflineCriteriaProgressChangeEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class AbstractAdvancementManager<T extends AbstractAdvancement> implements IAdvancementManager<T> {

    protected static HashMap<SuperKey, Float> smallestY = new HashMap<>();
    protected static HashMap<SuperKey, Float> smallestX = new HashMap<>();

    protected static float getSmallestY(SuperKey key) {
        return smallestY.containsKey(key) ? smallestY.get(key) : 0;
    }

    protected static float getSmallestX(SuperKey key) {
        return smallestX.containsKey(key) ? smallestX.get(key) : 0;
    }

    protected boolean hiddenBoolean = false;

    protected boolean announceAdvancementMessages = true;
    protected ArrayList<Player> players;
    protected ArrayList<T> advancements = new ArrayList<>();

    protected SuperApiBukkit plugin;

    protected AbstractAdvancementManager(SuperApiBukkit plugin) {
        this.plugin = plugin;
        this.players = new ArrayList<>();
    }

    @Override
    public ArrayList<Player> getPlayers() {
        Iterator<Player> it = players.iterator();
        while(it.hasNext()) {
            Player p = it.next();
            if(p == null || !p.isOnline()) {
                it.remove();
            }
        }
        return players;
    }

    @Override
    public void addPlayer(Player player) {
        Validate.notNull(player);
        addPlayer(player, null);
    }

    protected abstract void addPlayer(Player player, SuperKey tab);

    @Override
    public void update(Player player) {
        if(players.contains(player)) {
            SuperKey rootAdvancement = plugin.getAdvProvider().getActiveTab(player);
            plugin.getAdvProvider().clearActiveTab(player);
            addPlayer(player);
            SuperApiBukkit.getInstance().getScheduler().runTaskLater(() -> plugin.getAdvProvider().setActiveTab(player, rootAdvancement), 250L, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void update(Player player, SuperKey tab) {
        if(players.contains(player)) {
            SuperKey rootAdvancement = plugin.getAdvProvider().getActiveTab(player);
            plugin.getAdvProvider().clearActiveTab(player);
            addPlayer(player, tab);
            SuperApiBukkit.getInstance().getScheduler().runTaskLater(() -> plugin.getAdvProvider().setActiveTab(player, rootAdvancement), 250L, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void updateProgress(Player player, T... advancementsUpdated) {
        updateProgress(player, false, true, advancementsUpdated);
    }

    protected abstract void updateProgress(Player player, boolean alreadyGranted, boolean fireEvent, T... advancementsUpdated);

    @Override
    public void updateAllPossiblyAffectedVisibilities(Player player, T from) {
        List<T> updated = from.getRow();
        for(T adv : updated) {
            updateVisibility(player, adv);
        }
    }

    @Override
    public  ArrayList<T> getAdvancements() {
        return new ArrayList<>(advancements);
    }

    @Override
    public  ArrayList<T> getAdvancements(String namespace) {
        ArrayList<T> advs = getAdvancements();
        Iterator<T> it = advs.iterator();
        while(it.hasNext()) {
            T adv = it.next();
            if(!adv.getName().getNamespace().equalsIgnoreCase(namespace)) {
                it.remove();
            }
        }
        return advs;
    }

    @Override
    public  T getAdvancement(SuperKey name) {
        for(T advancement : advancements) {
            if(advancement.hasName(name)) {
                return advancement;
            }
        }
        return null;
    }

    @Override
    public boolean isAnnounceAdvancementMessages() {
        return announceAdvancementMessages;
    }

    @Override
    public void setAnnounceAdvancementMessages(boolean value) {
        this.announceAdvancementMessages = value;
    }

    @Override
    public void makeAccessible(String name) {
        name = name.toLowerCase();
        if(name.equals("file")) {
            throw new RuntimeException("There is already an AdvancementManager with Name '" + name + "'!");
        }
        if(plugin.getAdvProvider().getManagers().containsKey(name)) {
            throw new RuntimeException("There is already an AdvancementManager with Name '" + name + "'!");
        } else if(plugin.getAdvProvider().getManagers().containsValue(this)) {
            throw new RuntimeException("AdvancementManager is already accessible with a different Name!");
        }
        plugin.getAdvProvider().getManagers().put(name, this);
    }

    @Override
    public void resetAccessible() {
        Iterator<String> it = plugin.getAdvProvider().getManagers().keySet().iterator();
        while(it.hasNext()) {
            String name = it.next();
            if(plugin.getAdvProvider().getManagers().get(name).equals(this)) {
                it.remove();
                break;
            }
        }
    }

    @Override
    public String getName() {
        for(String name : plugin.getAdvProvider().getManagers().keySet()) {
            if(plugin.getAdvProvider().getManagers().get(name).equals(this)) return name;
        }
        return null;
    }

    protected  abstract void checkAwarded(Player player, T advancement);

    protected  abstract void checkAwarded(UUID uuid, T advancement);

    protected boolean isOnline(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        return player.isOnline();
    }

    @Override
    public void grantAdvancement(Player player, T advancement) {
        grantAdvancement(player, advancement, false, true, true);
    }

    protected abstract void grantAdvancement(Player player, T advancement, boolean alreadyGranted, boolean updateProgress, boolean fireEvent);

    @Override
    public void grantAdvancement(UUID uuid, T advancement) {
        if(isOnline(uuid)) {
            grantAdvancement(Bukkit.getPlayer(uuid), advancement);
        } else {
            checkAwarded(uuid, advancement);

            Map<String, HashSet<String>> awardedCriteria = advancement.getAwardedCriteria();

            HashSet<String> awarded = awardedCriteria.get(uuid.toString());
            for(String criterion : (Set<String>) advancement.getSavedCriteria().keySet()) {
                awarded.add(criterion);
            }
            awardedCriteria.put(uuid.toString(), awarded);
            advancement.setAwardedCriteria(awardedCriteria);

            OfflineAdvancementGrantEvent event = new OfflineAdvancementGrantEvent(this, advancement, uuid);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @Override
    public void revokeAdvancement(Player player, T advancement) {
        checkAwarded(player, advancement);

        advancement.setAwardedCriteria(new HashMap<>());

        updateProgress(player, advancement);
        updateAllPossiblyAffectedVisibilities(player, advancement);

        AdvancementRevokeEvent event = new AdvancementRevokeEvent(this, advancement, player);
        Bukkit.getPluginManager().callEvent(event);
    }

    @Override
    public void revokeAdvancement(UUID uuid, T advancement) {
        if(isOnline(uuid)) {
            revokeAdvancement(Bukkit.getPlayer(uuid), advancement);
        } else {
            checkAwarded(uuid, advancement);

            advancement.setAwardedCriteria(new HashMap<>());

            OfflineAdvancementRevokeEvent event = new OfflineAdvancementRevokeEvent(this, advancement, uuid);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @Override
    public void grantCriteria(Player player, T advancement, String... criteria) {
        checkAwarded(player, advancement);

        Map<String, HashSet<String>> awardedCriteria = advancement.getAwardedCriteria();

        HashSet<String> awarded = awardedCriteria.get(player.getUniqueId().toString());
        for(String criterion : criteria) {
            awarded.add(criterion);
        }
        awardedCriteria.put(player.getUniqueId().toString(), awarded);
        advancement.setAwardedCriteria(awardedCriteria);

        updateProgress(player, false, true, advancement);
        updateAllPossiblyAffectedVisibilities(player, advancement);

        CriteriaGrantEvent event = new CriteriaGrantEvent(this, advancement, criteria, player);
        Bukkit.getPluginManager().callEvent(event);
    }

    @Override
    public void grantCriteria(UUID uuid, T advancement, String... criteria) {
        if(isOnline(uuid)) {
            grantCriteria(Bukkit.getPlayer(uuid), advancement, criteria);
        } else {
            checkAwarded(uuid, advancement);

            Map<String, HashSet<String>> awardedCriteria = advancement.getAwardedCriteria();

            HashSet<String> awarded = awardedCriteria.get(uuid.toString());
            for(String criterion : criteria) {
                awarded.add(criterion);
            }
            awardedCriteria.put(uuid.toString(), awarded);
            advancement.setAwardedCriteria(awardedCriteria);

            OfflineCriteriaGrantEvent event = new OfflineCriteriaGrantEvent(this, advancement, criteria, uuid);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @Override
    public void setCriteriaProgress(Player player, T advancement, int progress) {
        checkAwarded(player, advancement);
        Map<String, HashSet<String>> awardedCriteria = advancement.getAwardedCriteria();

        HashSet<String> awarded = awardedCriteria.get(player.getUniqueId().toString());

        CriteriaProgressChangeEvent event = new CriteriaProgressChangeEvent(this, advancement, player, awarded.size(), progress);
        Bukkit.getPluginManager().callEvent(event);
        progress = event.getProgress();

        int difference = Math.abs(awarded.size() - progress);

        if(awarded.size() > progress) {
            //Count down
            int i = 0;
            for(String criterion : (Set<String>) advancement.getSavedCriteria().keySet()) {
                if(i >= difference) break;
                if(awarded.contains(criterion)) {
                    awarded.remove(criterion);
                    i++;
                }
            }
        } else if(awarded.size() < progress) {
            //Count up
            int i = 0;
            for(String criterion : (Set<String>) advancement.getSavedCriteria().keySet()) {
                if(i >= difference) break;
                if(!awarded.contains(criterion)) {
                    awarded.add(criterion);
                    i++;
                }
            }
        }

        awardedCriteria.put(player.getUniqueId().toString(), awarded);
        advancement.setAwardedCriteria(awardedCriteria);

        updateProgress(player, false, true, advancement);
        updateAllPossiblyAffectedVisibilities(player, advancement);
    }

    @Override
    public void setCriteriaProgress(UUID uuid, T advancement, int progress) {
        if(isOnline(uuid)) {
            setCriteriaProgress(Bukkit.getPlayer(uuid), advancement, progress);
        } else {
            checkAwarded(uuid, advancement);
            Map<String, HashSet<String>> awardedCriteria = advancement.getAwardedCriteria();

            HashSet<String> awarded = awardedCriteria.get(uuid.toString());

            OfflineCriteriaProgressChangeEvent event = new OfflineCriteriaProgressChangeEvent(this, advancement, uuid, awarded.size(), progress);
            Bukkit.getPluginManager().callEvent(event);
            progress = event.getProgress();

            int difference = Math.abs(awarded.size() - progress);

            if(awarded.size() > progress) {
                //Count down
                int i = 0;
                for(String criterion : (Set<String>) advancement.getSavedCriteria().keySet()) {
                    if(i >= difference) break;
                    if(awarded.contains(criterion)) {
                        awarded.remove(criterion);
                        i++;
                    }
                }
            } else if(awarded.size() < progress) {
                //Count up
                int i = 0;
                for(String criterion : (Set<String>) advancement.getSavedCriteria().keySet()) {
                    if(i >= difference) break;
                    if(!awarded.contains(criterion)) {
                        awarded.add(criterion);
                        i++;
                    }
                }
            }

            awardedCriteria.put(uuid.toString(), awarded);
            advancement.setAwardedCriteria(awardedCriteria);
        }
    }

    @Override
    public int getCriteriaProgress(Player player, T advancement) {
        checkAwarded(player, advancement);
        Map<String, HashSet<String>> awardedCriteria = advancement.getAwardedCriteria();

        return awardedCriteria.get(player.getUniqueId().toString()).size();
    }

    @Override
    public int getCriteriaProgress(UUID uuid, T advancement) {
        checkAwarded(uuid, advancement);

        Map<String, HashSet<String>> awardedCriteria = advancement.getAwardedCriteria();

        return awardedCriteria.get(uuid.toString()).size();
    }

    protected String getSavePath(Player player, String namespace) {
        return getSaveDirectory(namespace) + player.getUniqueId() + ".json";
    }

    protected String getSaveDirectory(String namespace) {
        return SuperApiBukkit.getInstance().getBootstrap().getDataFolder().getAbsolutePath() + File.separator + "saved_data" + File.separator + namespace + File.separator;
    }

    protected File getSaveFile(Player player, String namespace) {
        File file = new File(getSaveDirectory(namespace));
        file.mkdirs();
        return new File(getSavePath(player, namespace));
    }


    protected String getSavePath(UUID uuid, String namespace) {
        return getSaveDirectory(namespace) + uuid + ".json";
    }

    protected File getSaveFile(UUID uuid, String namespace) {
        File file = new File(getSaveDirectory(namespace));
        file.mkdirs();
        return new File(getSavePath(uuid, namespace));
    }

    @Override
    public void saveProgress(Player player, String namespace) {
        File saveFile = getSaveFile(player, namespace);

        String json = getProgressJSON(player, namespace);

        try {
            if(!saveFile.exists()) {
                saveFile.createNewFile();
            }
            FileWriter w = new FileWriter(saveFile);
            w.write(json);
            w.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load Progress

    @Override
    public void loadProgress(Player player, String namespace) {
        File saveFile = getSaveFile(player, namespace);

        if(saveFile.exists() && saveFile.isFile()) {
            HashMap<String, List<String>> prg = getProgress(player, namespace);

            for(T advancement : advancements) {
                if(advancement.getName().getNamespace().equalsIgnoreCase(namespace)) {
                    checkAwarded(player, advancement);

                    String nameKey = advancement.getName().toString();

                    if(prg.containsKey(nameKey)) {
                        List<String> loaded = prg.get(nameKey);

                        grantCriteria(player, advancement, loaded.toArray(new String[loaded.size()]));

                    }
                }
            }
        }
    }

    @Override
    public void loadProgress(Player player, T... advancementsLoaded) {
        if(advancementsLoaded.length == 0) return;
        List<T> advancements = Arrays.asList(advancementsLoaded);

        String namespace = advancements.get(0).getName().getNamespace();

        File saveFile = getSaveFile(player, namespace);

        if(saveFile.exists() && saveFile.isFile()) {
            HashMap<String, List<String>> prg = getProgress(player, namespace);

            for(T advancement : advancements) {
                if(advancement.getName().getNamespace().equalsIgnoreCase(namespace)) {
                    checkAwarded(player, advancement);

                    String nameKey = advancement.getName().toString();

                    if(prg.containsKey(nameKey)) {
                        List<String> loaded = prg.get(nameKey);

                        grantCriteria(player, advancement, loaded.toArray(new String[loaded.size()]));

                    }
                }
            }
        }
    }

    @Override
    public void loadCustomProgress(Player player, String json, T... advancementsLoaded) {
        if(advancementsLoaded.length == 0) return;
        List<T> advancements = Arrays.asList(advancementsLoaded);

        HashMap<String, List<String>> prg = getCustomProgress(json);

        for(T advancement : advancements) {
            checkAwarded(player, advancement);

            String nameKey = advancement.getName().toString();

            if(prg.containsKey(nameKey)) {
                List<String> loaded = prg.get(nameKey);

                grantCriteria(player, advancement, loaded.toArray(new String[loaded.size()]));

            }
        }
    }

    @Override
    public void loadCustomProgress(Player player, String json) {
        HashMap<String, List<String>> prg = getCustomProgress(json);

        for(T advancement : advancements) {
            checkAwarded(player, advancement);

            String nameKey = advancement.getName().toString();

            if(prg.containsKey(nameKey)) {
                List<String> loaded = prg.get(nameKey);

                grantCriteria(player, advancement, loaded.toArray(new String[loaded.size()]));

            }
        }
    }

    @Override
    public void loadCustomProgress(Player player, String json, String namespace) {
        HashMap<String, List<String>> prg = getCustomProgress(json);

        for(T advancement : advancements) {
            if(advancement.getName().getNamespace().equalsIgnoreCase(namespace)) {
                checkAwarded(player, advancement);

                String nameKey = advancement.getName().toString();

                if(prg.containsKey(nameKey)) {
                    List<String> loaded = prg.get(nameKey);

                    grantCriteria(player, advancement, loaded.toArray(new String[loaded.size()]));

                }
            }
        }
    }

    protected HashMap<String, List<String>> getProgress(Player player, String namespace) {
        File saveFile = getSaveFile(player, namespace);

        try {
            FileReader os = new FileReader(saveFile);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(os);
            os.close();

            check();

            return gson.fromJson(element, progressListType);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HashMap<>();
        }
    }

    protected HashMap<String, List<String>> getCustomProgress(String json) {
        check();

        return gson.fromJson(json, progressListType);
    }

    @Override
    public void saveProgress(UUID uuid, String namespace) {
        File saveFile = getSaveFile(uuid, namespace);

        String json = getProgressJSON(uuid, namespace);

        try {
            if(!saveFile.exists()) {
                saveFile.createNewFile();
            }
            FileWriter w = new FileWriter(saveFile);
            w.write(json);
            w.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Load Progress

    @Override
    public void loadProgress(UUID uuid, String namespace) {
        File saveFile = getSaveFile(uuid, namespace);

        if(saveFile.exists() && saveFile.isFile()) {
            HashMap<String, List<String>> prg = getProgress(uuid, namespace);

            for(T advancement : advancements) {
                if(advancement.getName().getNamespace().equalsIgnoreCase(namespace)) {
                    checkAwarded(uuid, advancement);

                    String nameKey = advancement.getName().toString();

                    if(prg.containsKey(nameKey)) {
                        List<String> loaded = prg.get(nameKey);

                        grantCriteria(uuid, advancement, loaded.toArray(new String[loaded.size()]));

                    }
                }
            }
        }
    }

    @Override
    public void loadProgress(UUID uuid, T... advancementsLoaded) {
        if(advancementsLoaded.length == 0) return;
        List<T> advancements = Arrays.asList(advancementsLoaded);

        String namespace = advancements.get(0).getName().getNamespace();

        File saveFile = getSaveFile(uuid, namespace);

        if(saveFile.exists() && saveFile.isFile()) {
            HashMap<String, List<String>> prg = getProgress(uuid, namespace);

            for(T advancement : advancements) {
                if(advancement.getName().getNamespace().equalsIgnoreCase(namespace)) {
                    checkAwarded(uuid, advancement);

                    String nameKey = advancement.getName().toString();

                    if(prg.containsKey(nameKey)) {
                        List<String> loaded = prg.get(nameKey);

                        grantCriteria(uuid, advancement, loaded.toArray(new String[loaded.size()]));

                    }
                }
            }
        }
    }

    @Override
    public void loadCustomProgress(UUID uuid, String json, T... advancementsLoaded) {
        if(advancementsLoaded.length == 0) return;
        List<T> advancements = Arrays.asList(advancementsLoaded);

        HashMap<String, List<String>> prg = getCustomProgress(json);

        for(T advancement : advancements) {
            checkAwarded(uuid, advancement);

            String nameKey = advancement.getName().toString();

            if(prg.containsKey(nameKey)) {
                List<String> loaded = prg.get(nameKey);

                grantCriteria(uuid, advancement, loaded.toArray(new String[loaded.size()]));

            }
        }
    }

    @Override
    public void loadCustomProgress(UUID uuid, String json) {
        HashMap<String, List<String>> prg = getCustomProgress(json);

        for(T advancement : advancements) {
            checkAwarded(uuid, advancement);

            String nameKey = advancement.getName().toString();

            if(prg.containsKey(nameKey)) {
                List<String> loaded = prg.get(nameKey);

                grantCriteria(uuid, advancement, loaded.toArray(new String[loaded.size()]));

            }
        }
    }

    @Override
    public void loadCustomProgress(UUID uuid, String json, String namespace) {
        HashMap<String, List<String>> prg = getCustomProgress(json);

        for(T advancement : advancements) {
            if(advancement.getName().getNamespace().equalsIgnoreCase(namespace)) {
                checkAwarded(uuid, advancement);

                String nameKey = advancement.getName().toString();

                if(prg.containsKey(nameKey)) {
                    List<String> loaded = prg.get(nameKey);

                    grantCriteria(uuid, advancement, loaded.toArray(new String[loaded.size()]));

                }
            }
        }
    }

    //Unload Progress

    @Override
    public void unloadProgress(UUID uuid) {
        if(isOnline(uuid)) {
            throw new UnloadProgressFailedException(uuid);
        } else {
            for(T advancement : getAdvancements()) {
                advancement.unsetProgress(uuid);
            }
        }
    }

    @Override
    public void unloadProgress(UUID uuid, String namespace) {
        if(isOnline(uuid)) {
            throw new UnloadProgressFailedException(uuid);
        } else {
            for(T advancement : getAdvancements(namespace)) {
                advancement.unsetProgress(uuid);
            }
        }
    }

    @Override
    public void unloadProgress(UUID uuid, T... advancements) {
        if(isOnline(uuid)) {
            throw new UnloadProgressFailedException(uuid);
        } else {
            for(T advancement : advancements) {
                advancement.unsetProgress(uuid);
            }
        }
    }

    protected HashMap<String, List<String>> getProgress(UUID uuid, String namespace) {
        File saveFile = getSaveFile(uuid, namespace);

        try {
            FileReader os = new FileReader(saveFile);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(os);
            os.close();

            check();

            return gson.fromJson(element, progressListType);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new HashMap<>();
        }
    }

    protected static Gson gson;
    protected static Type progressListType;

    protected static void check() {
        if(gson == null) {
            gson = new Gson();
        }
        if(progressListType == null) {
            progressListType = new TypeToken<HashMap<String, List<String>>>() {private static final long serialVersionUID = 5832697137241815078L;}.getType();
        }
    }

}
