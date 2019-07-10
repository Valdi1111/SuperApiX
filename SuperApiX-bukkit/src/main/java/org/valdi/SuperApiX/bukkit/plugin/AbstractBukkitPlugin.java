package org.valdi.SuperApiX.bukkit.plugin;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.managers.CommandsManager;
import org.valdi.SuperApiX.bukkit.managers.LocalesManager;
import org.valdi.SuperApiX.bukkit.managers.PlayersManager;
import org.valdi.SuperApiX.bukkit.users.Notifier;
import org.valdi.SuperApiX.common.plugin.AbstractPlugin;
import org.valdi.SuperApiX.common.config.IFilesProvider;
import org.valdi.SuperApiX.common.databases.IDatabasesProvider;

public abstract class AbstractBukkitPlugin<T extends ISuperBukkitBootstrap> extends AbstractPlugin<T> implements ISuperBukkitPlugin<T> {

	// Managers
	protected LocalesManager localesManager;
	protected CommandsManager commandsManager;

	// Notifier
	protected Notifier notifier;

	public AbstractBukkitPlugin(final T bootstrap) {
		super(bootstrap);

		this.localesManager = new LocalesManager(this);
		this.commandsManager = new CommandsManager(this);

		// Load notifier
		notifier = new Notifier();
	}
	
	@Override
	public Optional<IDatabasesProvider> getDatabasesProvider() {
        RegisteredServiceProvider<IDatabasesProvider> provider = Bukkit.getServicesManager().getRegistration(IDatabasesProvider.class);
        if(provider == null) {
        	this.getLogger().debug("Cannot get file provider...");
        	return Optional.empty();
        }
        
        return Optional.ofNullable(provider.getProvider());
	}

	@Override
	public Optional<IFilesProvider> getFilesProvider() {
        RegisteredServiceProvider<IFilesProvider> provider = Bukkit.getServicesManager().getRegistration(IFilesProvider.class);
        if(provider == null) {
        	this.getLogger().debug("Cannot get file provider...");
        	return Optional.empty();
        }
        
        return Optional.ofNullable(provider.getProvider());
	}

	public Server getServer() {
		return this.getBootstrap().getServer();
	}

	@Override
	public LocalesManager getLocalesManager() {
		return localesManager;
	}

	@Override
	public CommandsManager getCommandsManager() {
		return commandsManager;
	}

	@Override
	public PlayersManager getPlayersManager() {
		return SuperApiBukkit.getInstance().getPlayersManager();
	}

	@Override
	public Notifier getNotifier() {
		return notifier;
	}

}
