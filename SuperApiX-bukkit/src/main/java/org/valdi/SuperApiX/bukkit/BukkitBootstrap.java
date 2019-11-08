package org.valdi.SuperApiX.bukkit;

import org.valdi.SuperApiX.bukkit.plugin.AbstractBukkitBootstrap;
import org.valdi.SuperApiX.common.PluginDetails;
import org.valdi.SuperApiX.common.annotation.dependency.SoftDependency;
import org.valdi.SuperApiX.common.annotation.plugin.Description;
import org.valdi.SuperApiX.common.annotation.plugin.LoadOrder;
import org.valdi.SuperApiX.common.annotation.plugin.LoadOrder.Phase;
import org.valdi.SuperApiX.common.annotation.plugin.BukkitPlugin;
import org.valdi.SuperApiX.common.annotation.plugin.author.Author;
import org.valdi.SuperApiX.common.dependencies.Dependencies;
import org.valdi.SuperApiX.common.dependencies.DependencyManager;

@BukkitPlugin(name = PluginDetails.NAME, version = PluginDetails.VERSION)
@Description(PluginDetails.DESCRIPTION)
@Author(PluginDetails.AUTHOR)
@LoadOrder(Phase.STARTUP)
@SoftDependency("Vault")
public class BukkitBootstrap extends AbstractBukkitBootstrap<SuperApiBukkit> {
	
    // Metrics
    private Metrics metrics;

    public BukkitBootstrap() {
        super();

        DependencyManager.init(this);

        // load dependencies
        getDependencyManager().loadStorageDependencies();
        getDependencyManager().loadDependencies(Dependencies.TEXT, Dependencies.CAFFEINE, Dependencies.OKIO, Dependencies.OKHTTP, Dependencies.JSOUP);

        this.plugin = new SuperApiBukkit(this);
    }

    // lifecycle

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
    	super.onEnable();

        if(!isCompatible()) {
            return;
        }
    	
        // Metrics
        this.metrics = new Metrics(this);
        getLogger().info("Metrics loaded."); 	
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if(!isCompatible()) {
            return;
        }

        this.metrics = null;
    }
    
    public Metrics getMetrics() {
    	return metrics;
    }

}
