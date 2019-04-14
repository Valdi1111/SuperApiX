package org.valdi.SuperApiX.sponge;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.AsynchronousExecutor;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.scheduler.SynchronousExecutor;
import org.valdi.SuperApiX.common.PluginDetails;

import com.google.inject.Inject;

@Plugin(id = PluginDetails.ID, name = PluginDetails.NAME, version = PluginDetails.VERSION, description = PluginDetails.DESCRIPTION, authors = PluginDetails.AUTHOR)
public class SpongeBootstrap extends AbstractSpongeBootstrap<SuperApiSponge> {
    /**
     * The plugin instance
     */
    private final SuperApiSponge plugin;

    @Inject
    private Metrics metrics;

    @Inject
    public SpongeBootstrap(Logger logger, Game game, PluginContainer plugin, @ConfigDir(sharedRoot = false) Path dir, @SynchronousExecutor SpongeExecutorService syncExecutor, @AsynchronousExecutor SpongeExecutorService asyncExecutor) {
        super(logger, game, plugin, dir, syncExecutor, asyncExecutor);
        
        this.plugin = new SuperApiSponge(this);
    }

    // lifecycle

    @Listener(order = Order.FIRST)
    public void onLoad(GamePreInitializationEvent event) {
        super.onLoad(event);
    }

    @Listener(order = Order.LATE)
    public void onEnable(GamePreInitializationEvent event) {
        super.onEnable(event);
    }
    
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        // Optional: Add custom charts
        // metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));    	
    }

    @Listener
    public void onDisable(GameStoppingServerEvent event) {
        super.onDisable(event);
    }
	
	@Listener
	public void onReload(GameReloadEvent event) {
	    super.onReload(event);
	}
	
	@Override
	public SuperApiSponge getPlugin() {
		return plugin;
	}
	
	public Metrics getMetrics() {
		return metrics;
	}

}
