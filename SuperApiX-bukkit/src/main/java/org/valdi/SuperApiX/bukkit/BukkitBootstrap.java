package org.valdi.SuperApiX.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.api.TabList;
import org.valdi.SuperApiX.common.annotation.plugin.Description;
import org.valdi.SuperApiX.common.annotation.plugin.LoadOrder;
import org.valdi.SuperApiX.common.annotation.plugin.LoadOrder.Phase;
import org.valdi.SuperApiX.common.annotation.plugin.BukkitPlugin;
import org.valdi.SuperApiX.common.annotation.plugin.author.Author;

@BukkitPlugin(name = PluginDetails.NAME, version = PluginDetails.VERSION)
@Description(PluginDetails.DESCRIPTION)
@Author(PluginDetails.AUTHOR)
@LoadOrder(Phase.STARTUP)
public class BukkitBootstrap extends AbstractBukkitBootstrap<SuperApiBukkit> {
	
    /**
     * The plugin instance
     */
    private SuperApiBukkit plugin;
	
    // Metrics
    private Metrics metrics;

    public BukkitBootstrap() {
        super();
        
        this.plugin = new SuperApiBukkit(this);
    }

    // lifecycle

    @Override
    public void onEnable() {
    	super.onEnable();
    	
        // Metrics
        metrics = new Metrics(this);
        getLogger().info("Metrics loaded."); 	
    }

    @Override
    public void onDisable() {
        super.onDisable();
        
    	metrics = null;
    }
    
    public SuperApiBukkit getPlugin() {
    	return plugin;
    }
    
    public Metrics getMetrics() {
    	return metrics;
    }
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if(label.equalsIgnoreCase("prova") && sender.isOp()) {
	        if(args.length != 2) {
	        	return false;
	        }
	        
        	TabList.builder().header(args[0]).footer(args[1]).send((Player) sender);
	        sender.sendMessage("asd");
    	}
    	return true;
    }

}
