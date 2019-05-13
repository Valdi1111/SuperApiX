package org.valdi.SuperApiX.bukkit.advancements;

import org.bukkit.Bukkit;
import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.advancements.managers.AbstractAdvancement;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancement;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancementManager;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancementPacketHandler;
import org.valdi.SuperApiX.bukkit.nms.core.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AdvancementProvider implements Listener {
    private final HashMap<String, IAdvancementManager> accessible = new HashMap<>();
    private ArrayList<IAdvancementManager> managers = new ArrayList<>();

    private boolean announceAdvancementMessages = true;

    public HashMap<String, IAdvancementManager> getManagers() {
        return accessible;
    }

    /**
     * Gets an accessible AbstractAdvancement Manager by it's Name
     *
     * @param name
     * @return
     */
    public IAdvancementManager getAccessibleManager(String name) {
        name = name.toLowerCase();
        return accessible.getOrDefault(name, null);
    }

    public Collection<IAdvancementManager> getAccessibleManagers() {
        return accessible.values();
    }

    private final SuperApiBukkit plugin;
    private final NmsComponent<IAdvancementManager> mngComponent;
    private final NmsComponent<IAdvancement> advComponent;

    private final IAdvancementPacketHandler packetHandler;
    private final List<Player> initiatedPlayers = new ArrayList<>();
    private final Map<String, SuperKey> openedTabs = new HashMap<>();

    public AdvancementProvider(SuperApiBukkit plugin) throws VersionUnsupportedException {
        this.plugin = plugin;
        VersionManager.NmsVersion version = plugin.getVersionManager().getNmsVersion();
        if(version == VersionManager.NmsVersion.INCOMPATIBLE) {
            plugin.getLogger().severe("Cannot provide support for this nms version... Using bukkit handler, some methods won't work.");
            throw new VersionUnsupportedException();
        }

        this.mngComponent = new NmsComponent<>(NmsName.ADVANCEMENT_MANAGER, NmsProvider.ADVANCEMENTS_PACKAGE, "AdvancementManager", "IAdvancementManager");
        this.advComponent = new NmsComponent<>(NmsName.ADVANCEMENT, NmsProvider.ADVANCEMENTS_PACKAGE, "Advancement", "IAdvancement");

        NmsComponent<IAdvancementPacketHandler> packetComponent = new NmsComponent<>(NmsName.ADVANCEMENT_PACKET_HANDLER, NmsProvider.ADVANCEMENTS_PACKAGE, "AdvancementPacketHandler", "IAdvancementPacketHandler");
        try {
            packetComponent.setupProvider(plugin, version);
        } catch(Exception e) {
            e.printStackTrace();
            plugin.getLogger().severe("Error initializing " + packetComponent.getSuperName() + " provider class...");
            plugin.getLogger().severe("Try updating SuperApiX!");
            throw new VersionUnsupportedException();
        }

        if(!packetComponent.getInstance().isPresent()) {
            plugin.getLogger().severe("Error initializing " + packetComponent.getSuperName() + " provider class...");
            plugin.getLogger().severe("Try updating SuperApiX!");
            throw new VersionUnsupportedException();
        }
        this.packetHandler = packetComponent.getInstance().get();
    }

    /**
     * Creates a new instance of an advancement manager
     *
     * @param players All players that should be in the new manager from the start, can be changed at any time
     * @return the generated advancement manager
     */
    public IAdvancementManager createAdvancementManager(Player... players) {
        VersionManager.NmsVersion version = plugin.getVersionManager().getNmsVersion();
        if(version == VersionManager.NmsVersion.INCOMPATIBLE) {
            plugin.getLogger().severe("Cannot provide support for this nms version... AdvancementManager won't work.");
            return null;
        }

        try {
            mngComponent.setupProvider(plugin, version);
        } catch(Exception e) {
            e.printStackTrace();
            plugin.getLogger().severe("Error initializing " + mngComponent.getSuperName() + " provider class...");
            plugin.getLogger().severe("Try updating SuperApiX!");
            return null;
        }

        if(!mngComponent.getInstance().isPresent()) {
            plugin.getLogger().severe("Error initializing " + mngComponent.getSuperName() + " provider class...");
            plugin.getLogger().severe("Try updating SuperApiX!");
            return null;
        }

        IAdvancementManager manager = mngComponent.getInstance().get();
        for(Player player : players) {
            manager.addPlayer(player);
        }
        return manager;
    }

    public IAdvancement createAdvancement(@Nullable AbstractAdvancement parent, SuperKey name, AdvancementDisplay display) {
        VersionManager.NmsVersion version = plugin.getVersionManager().getNmsVersion();
        if(version == VersionManager.NmsVersion.INCOMPATIBLE) {
            plugin.getLogger().severe("Cannot provide support for this nms version... Advancement won't work.");
            return null;
        }

        try {
            advComponent.setupProvider(plugin, version, new Class[] { AbstractAdvancement.class, SuperKey.class, AdvancementDisplay.class }, new Object[] { parent, name, display });
        } catch(Exception e) {
            e.printStackTrace();
            plugin.getLogger().severe("Error initializing " + advComponent.getSuperName() + " provider class...");
            plugin.getLogger().severe("Try updating SuperApiX!");
            return null;
        }

        if(!advComponent.getInstance().isPresent()) {
            plugin.getLogger().severe("Error initializing " + advComponent.getSuperName() + " provider class...");
            plugin.getLogger().severe("Try updating SuperApiX!");
            return null;
        }

        return advComponent.getInstance().get();
    }

    public void onEnable() {
        plugin.getScheduler().syncLater(() -> {
            Bukkit.getOnlinePlayers().forEach(p -> {
                //fileAdvancementManager.addPlayer(player);
                packetHandler.initPlayer(p);
                initiatedPlayers.add(p);
            });
        }, 250L, TimeUnit.MILLISECONDS);

        plugin.getServer().getPluginManager().registerEvents(this, plugin.getBootstrap());
    }

    public void onDisable() {
        managers.forEach(IAdvancementManager::removeAdvancements);
        Bukkit.getOnlinePlayers().forEach(p -> {
            plugin.getNmsProvider().getAdvancementUtils().ifPresent(u -> {
                try {
                    u.resetAdvancements(p);
                } catch (VersionUnsupportedException e) {
                    e.printStackTrace();
                }
            });
            packetHandler.close(p);
        });

        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        plugin.getScheduler().syncLater(() -> {
            //fileAdvancementManager.addPlayer(e.getPlayer());
            initiatedPlayers.add(e.getPlayer());
        }, 250L, TimeUnit.MILLISECONDS);
        packetHandler.initPlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        packetHandler.close(e.getPlayer());
        initiatedPlayers.remove(e.getPlayer());
    }

    public void clearActiveTab(Player player) {
        this.setActiveTab(player, null, true);
    }

    public void setActiveTab(Player player, String rootAdvancement) {
        this.setActiveTab(player, SuperKey.minecraft(rootAdvancement));
    }

    public void setActiveTab(Player player, SuperKey rootAdvancement) {
        this.setActiveTab(player, rootAdvancement, true);
    }

    public void setActiveTab(Player player, SuperKey rootAdvancement, boolean update) {
        if (update) {
            plugin.getNmsProvider().getAdvancementUtils().ifPresent(u -> {
                try {
                    u.changeAdvancementTab(player, rootAdvancement);
                } catch (VersionUnsupportedException e) {
                    e.printStackTrace();
                }
            });
        }
        openedTabs.put(player.getName(), rootAdvancement);
    }

    public SuperKey getActiveTab(Player player) {
        return openedTabs.get(player.getName());
    }

    @Warning(reason="Unsafe")
    public List<Player> getInitiatedPlayers() {
        return initiatedPlayers;
    }

    public boolean isAnnounceAdvancementMessages() {
        return announceAdvancementMessages;
    }

    public void setAnnounceAdvancementMessages(boolean value) {
        this.announceAdvancementMessages = value;
    }

}
