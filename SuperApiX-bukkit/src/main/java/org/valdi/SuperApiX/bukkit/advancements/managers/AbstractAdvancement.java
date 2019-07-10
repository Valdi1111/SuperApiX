package org.valdi.SuperApiX.bukkit.advancements.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.*;
import javax.annotation.Nullable;

import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.advancements.AdvancementDisplay;
import org.valdi.SuperApiX.bukkit.advancements.AdvancementReward;
import org.valdi.SuperApiX.bukkit.advancements.AdvancementVisibility;

public abstract class AbstractAdvancement<K, V, P> implements IAdvancement<K, V, P> {
    protected static HashMap<String, AbstractAdvancement> advancementMap = new HashMap<>();

    protected transient SuperKey name;
    @SerializedName(value="name")
    protected String nameKey;
    protected AdvancementDisplay display;
    protected transient AbstractAdvancement<K, V, P> parent;
    @SerializedName(value="parent")
    protected String parentKey;
    protected transient HashSet<AbstractAdvancement<K, V, P>> children = new HashSet<>();
    @SerializedName(value="criteriaAmount")
    protected int criteria = 1;
    protected transient AdvancementReward reward;
    protected transient Map<String, HashSet<String>> awardedCriteria = new HashMap<>();
    protected transient Map<String, V> progress = new HashMap<>();
    protected transient Map<String, P> savedCriteria = null;
    @SerializedName(value="criteria")
    protected Set<String> savedCriterionNames = null;
    @SerializedName(value="criteriaRequirements")
    protected String[][] savedCriteriaRequirements = null;
    protected transient HashMap<String, Boolean> savedHiddenStatus;
    protected transient K savedAdvancement = null;

    protected SuperApiBukkit plugin;

    public static AbstractAdvancement fromJSON(String json) {
        Gson gson = new GsonBuilder().setLenient().create();
        AbstractAdvancement created = gson.fromJson(json, AbstractAdvancement.class);
        created.loadAfterGSON();
        return created;
    }

    public static AbstractAdvancement fromJSON(JsonElement json) {
        Gson gson = new GsonBuilder().setLenient().create();
        AbstractAdvancement created = gson.fromJson(json, AbstractAdvancement.class);
        created.loadAfterGSON();
        return created;
    }

    private void loadAfterGSON() {
        this.plugin = SuperApiBukkit.getInstance();
        this.children = new HashSet<>();
        this.name = SuperKey.minecraft(this.nameKey);
        advancementMap.put(this.nameKey, this);
        this.parent = advancementMap.get(this.parentKey);
        if (this.parent != null) {
            this.parent.addChildren(this);
        }
        this.display.setVisibility(AdvancementVisibility.byName(this.display.visibilityIdentifier));
    }

    private void addChildren(AbstractAdvancement<K, V, P> adv) {
        this.children.add(adv);
    }

    @Override
    public String getAdvancementJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public AbstractAdvancement(SuperApiBukkit plugin, @Nullable AbstractAdvancement parent, SuperKey name, AdvancementDisplay display) {
        this.plugin = plugin;
        this.parent = parent;
        if (this.parent != null) {
            this.parent.addChildren(this);
        }
        this.parentKey = parent == null ? null : parent.getName().toString();
        this.name = name;
        this.nameKey = name.toString();
        this.display = display;
    }

    @Override
    public AbstractAdvancement<K, V, P> getParent() {
        return this.parent;
    }

    public abstract void setCriteria(int criteria);

    @Override
    public int getCriteria() {
        return this.criteria;
    }

    @Override
    public SuperKey getName() {
        return this.name;
    }

    @Override
    public AdvancementDisplay getDisplay() {
        return this.display;
    }

    @Override
    public void setReward(@Nullable AdvancementReward reward) {
        this.reward = reward;
    }

    @Override
    public AdvancementReward getReward() {
        return this.reward;
    }

    public abstract Object getMessage(Player player);

    public abstract void displayMessageToEverybody(Player player);

    public abstract void displayToast(Player player);

    @Override
    public HashSet<AbstractAdvancement<K, V, P>> getChildren() {
        return new HashSet<>(this.children);
    }

    @Override
    public AbstractAdvancement<K, V, P> getRootAdvancement() {
        if (this.parent == null) {
            return this;
        }
        return this.parent.getRootAdvancement();
    }

    @Override
    public SuperKey getTab() {
        return this.getRootAdvancement().getName();
    }

    @Override
    public List<AbstractAdvancement<K, V, P>> getRow() {
        ArrayList<AbstractAdvancement<K, V, P>> row = new ArrayList<>();
        row.add(this);
        if (this.getParent() != null) {
            for (AbstractAdvancement<K, V, P> untilRow : this.getParent().getRowUntil()) {
                if (row.contains(untilRow)) continue;
                row.add(untilRow);
            }
            Collections.reverse(row);
        }
        for (AbstractAdvancement<K, V, P> child : this.getChildren()) {
            for (AbstractAdvancement<K, V, P> afterRow : child.getRowAfter()) {
                if (row.contains(afterRow)) continue;
                row.add(afterRow);
            }
        }
        return row;
    }

    @Override
    public List<AbstractAdvancement<K, V, P>> getRowUntil() {
        ArrayList<AbstractAdvancement<K, V, P>> row = new ArrayList<>();
        row.add(this);
        if (this.getParent() != null) {
            for (AbstractAdvancement<K, V, P> untilRow : this.getParent().getRowUntil()) {
                if (row.contains(untilRow)) continue;
                row.add(untilRow);
            }
        }
        return row;
    }

    @Override
    public List<AbstractAdvancement<K, V, P>> getRowAfter() {
        ArrayList<AbstractAdvancement<K, V, P>> row = new ArrayList<>();
        row.add(this);
        for (AbstractAdvancement<K, V, P> child : this.getChildren()) {
            for (AbstractAdvancement<K, V, P> afterRow : child.getRowAfter()) {
                if (row.contains(afterRow)) continue;
                row.add(afterRow);
            }
        }
        return row;
    }

    @Override
    public boolean isAnythingGrantedUntil(Player player) {
        for (AbstractAdvancement<K, V, P> until : this.getRowUntil()) {
            if (!until.isGranted(player)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isAnythingGrantedAfter(Player player) {
        for (AbstractAdvancement<K, V, P> after : this.getRowAfter()) {
            if (!after.isGranted(player)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void saveHiddenStatus(Player player, boolean hidden) {
        if (this.savedHiddenStatus == null) {
            this.savedHiddenStatus = new HashMap<>();
        }
        this.savedHiddenStatus.put(player.getUniqueId().toString(), hidden);
    }

    @Override
    public boolean getHiddenStatus(Player player) {
        if (this.savedHiddenStatus == null) {
            this.savedHiddenStatus = new HashMap<>();
        }
        if (!this.savedHiddenStatus.containsKey(player.getUniqueId().toString())) {
            this.savedHiddenStatus.put(player.getUniqueId().toString(), this.getDisplay().isVisible(player, this));
        }
        return this.savedHiddenStatus.get(player.getUniqueId().toString());
    }

    @Override
    public void saveCriteria(Map<String, P> save) {
        this.savedCriteria = save;
        this.savedCriterionNames = save.keySet();
    }

    @Override
    public Map<String, P> getSavedCriteria() {
        return this.savedCriteria;
    }

    @Override
    public void saveCriteriaRequirements(String[][] save) {
        this.savedCriteriaRequirements = save;
    }

    @Override
    public String[][] getSavedCriteriaRequirements() {
        return this.savedCriteriaRequirements;
    }

    @Warning(reason="Unsafe")
    public abstract void saveAdvancement(K save);

    public abstract K getSavedAdvancement();

    @Override
    public Map<String, HashSet<String>> getAwardedCriteria() {
        if (this.awardedCriteria == null) {
            this.awardedCriteria = new HashMap<>();
        }
        return this.awardedCriteria;
    }

    @Override
    public void setAwardedCriteria(Map<String, HashSet<String>> awardedCriteria) {
        this.awardedCriteria = awardedCriteria;
    }

    public abstract V getProgress(Player player);

    public abstract V getProgress(UUID uuid);

    @Warning(reason="Only use if you know what you are doing!")
    public abstract void setProgress(Player player, V progress);

    @Override
    public void unsetProgress(UUID uuid) {
        if (this.progress == null) {
            this.progress = new HashMap<>();
        }

        this.progress.remove(uuid.toString());
    }

    public abstract boolean isDone(Player player);

    public abstract boolean isDone(UUID uuid);

    public abstract boolean isGranted(Player player);

    @Override
    public boolean hasName(SuperKey key) {
        if (key.getNamespace().equalsIgnoreCase(this.name.getNamespace()) && key.getKey().equalsIgnoreCase(this.name.getKey())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Advancement " + this.getAdvancementJSON();
    }

}

