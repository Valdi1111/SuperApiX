package org.valdi.SuperApiX.bukkit.advancements.managers;

import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.advancements.AdvancementDisplay;
import org.valdi.SuperApiX.bukkit.advancements.AdvancementReward;

import javax.annotation.Nullable;
import java.util.*;

public interface IAdvancement<K, V, P> {

    String getAdvancementJSON();

    @Nullable
    AbstractAdvancement<K, V, P> getParent();

    int getCriteria();

    SuperKey getName();

    AdvancementDisplay getDisplay();

    void setReward(@Nullable AdvancementReward reward);

    AdvancementReward getReward();

    HashSet<AbstractAdvancement<K, V, P>> getChildren();

    AbstractAdvancement<K, V, P> getRootAdvancement();

    SuperKey getTab();

    List<AbstractAdvancement<K, V, P>> getRow();

    List<AbstractAdvancement<K, V, P>> getRowUntil();

    List<AbstractAdvancement<K, V, P>> getRowAfter();

    boolean isAnythingGrantedUntil(Player player);

    boolean isAnythingGrantedAfter(Player player);

    @Warning(reason="Only use if you know what you are doing!")
    void saveHiddenStatus(Player player, boolean hidden);

    boolean getHiddenStatus(Player player);

    @Warning(reason="Only use if you know what you are doing!")
    void saveCriteria(Map<String, P> save);

    Map<String, P> getSavedCriteria();

    @Warning(reason="Only use if you know what you are doing!")
    void saveCriteriaRequirements(String[][] save);

    String[][] getSavedCriteriaRequirements();

    Map<String, HashSet<String>> getAwardedCriteria();

    @Warning(reason="Unsafe")
    void setAwardedCriteria(Map<String, HashSet<String>> awardedCriteria);

    @Warning(reason="Only use if you know what you are doing!")
    void unsetProgress(UUID uuid);

    boolean hasName(SuperKey key);

    String toString();
}
