package org.valdi.SuperApiX.bungee;

import net.md_5.bungee.api.chat.ComponentBuilder;
import org.valdi.SuperApiX.bungee.plugin.AbstractBungeePlugin;

import net.md_5.bungee.api.ChatColor;

public class SuperApiBungee extends AbstractBungeePlugin<BungeeBootstrap> {
	private SuperApiBungee instance;
    
    public SuperApiBungee(BungeeBootstrap bootstrap) {
        super(bootstrap);

		instance = this;
    }
	
	@Override
	public void load() {
		super.load();
	}

	@Override
	public void enable() {
		super.enable();
		
		bootstrap.getProxy().getConsole().sendMessage(
				new ComponentBuilder(bootstrap.getDescription().getName())
						.append(" V")
						.append(bootstrap.getDescription().getVersion())
						.append(" has been enabled... Enjoy :)")
						.color(ChatColor.GREEN)
						.create());
	}

	@Override
	public void disable() {
		super.disable();

		instance = null;

		bootstrap.getProxy().getConsole().sendMessage(
				new ComponentBuilder(bootstrap.getDescription().getName())
						.append(" V")
						.append(bootstrap.getDescription().getVersion())
						.append(" has been disabled... Goodbye :(")
						.color(ChatColor.RED)
						.create());
	}

	public SuperApiBungee getInstance() {
		return instance;
	}

}
