package org.valdi.SuperApiX.bungee;

import org.valdi.SuperApiX.common.PluginDetails;
import org.valdi.SuperApiX.common.annotation.plugin.Description;
import org.valdi.SuperApiX.common.annotation.plugin.BungeePlugin;
import org.valdi.SuperApiX.common.annotation.plugin.author.Author;

@BungeePlugin(name = PluginDetails.NAME, version = PluginDetails.VERSION)
@Description(PluginDetails.DESCRIPTION)
@Author(PluginDetails.AUTHOR)
public class BungeeBootstrap extends AbstractBungeeBootstrap<SuperApiBungee> {

    /**
     * The plugin instance
     */
    private final SuperApiBungee plugin;

    private Metrics metrics;

    public BungeeBootstrap() {
        super();
        
        this.plugin = new SuperApiBungee(this);
    }

	@Override
	public void onEnable() {
        super.onEnable();

    	metrics = new Metrics(this);
        // Optional: Add custom charts
        // metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.md_5.bungee.api.plugin.Plugin#onDisable()
	 */
	@Override
	public void onDisable() {
		super.onDisable();
		
		metrics = null;
	}
	
	public SuperApiBungee getPlugin() {
		return plugin;
	}
	
	public Metrics getMetrics() {
		return metrics;
	}

}
