package org.valdi.SuperApiX.bungee;

import org.valdi.SuperApiX.bungee.plugin.AbstractBungeeBootstrap;
import org.valdi.SuperApiX.common.PluginDetails;
import org.valdi.SuperApiX.common.annotation.plugin.Description;
import org.valdi.SuperApiX.common.annotation.plugin.BungeePlugin;
import org.valdi.SuperApiX.common.annotation.plugin.author.Author;
import org.valdi.SuperApiX.common.dependencies.Dependencies;
import org.valdi.SuperApiX.common.dependencies.DependencyManager;

@BungeePlugin(name = PluginDetails.NAME, version = PluginDetails.VERSION)
@Description(PluginDetails.DESCRIPTION)
@Author(PluginDetails.AUTHOR)
public class BungeeBootstrap extends AbstractBungeeBootstrap<SuperApiBungee> {

    private Metrics metrics;

    public BungeeBootstrap() {
        super();

		DependencyManager.init(this);

		// load dependencies
		getDependencyManager().loadStorageDependencies();
		getDependencyManager().loadDependencies(Dependencies.TEXT, Dependencies.CAFFEINE, Dependencies.OKIO, Dependencies.OKHTTP, Dependencies.JSOUP);
        
        this.plugin = new SuperApiBungee(this);
    }

	@Override
	public void onEnable() {
        super.onEnable();

    	this.metrics = new Metrics(this);
		getLogger().info("Metrics loaded.");
	}

	@Override
	public void onDisable() {
		super.onDisable();

		this.metrics = null;
	}
	
	public Metrics getMetrics() {
		return metrics;
	}

}
