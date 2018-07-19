package org.valdi.SuperApiX.bungee;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.valdi.SuperApiX.AbstractPlugin;
import org.valdi.SuperApiX.ISuperPlugin;
import org.valdi.SuperApiX.common.databases.data.ExceptionHandler;
import org.valdi.SuperApiX.common.logging.PluginLogger;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class SuperApiBungee extends AbstractPlugin implements ISuperPlugin {
	private final BungeeBootstrap bootstrap;
	private SuperApiBungee instance;

    private ScheduledExecutorService executorService;
    
    public SuperApiBungee(BungeeBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }
	
	@Override
	public void load() {
		instance = this;
		
        executorService = Executors.newScheduledThreadPool(100);
	}

	@Override
	public void enable() {
		bootstrap.getProxy().getConsole().sendMessage(new TextComponent(ChatColor.GREEN + bootstrap.getDescription().getName() + " V" + bootstrap.getDescription().getVersion() + " has been enabled... Enjoy :)"));
	}

	@Override
	public void disable() {
		instance = null;

		bootstrap.getProxy().getConsole().sendMessage(new TextComponent(ChatColor.RED + bootstrap.getDescription().getName() + " V" + bootstrap.getDescription().getVersion() + " has been disabled... Goodbye :("));
	}

	@Override
	public void reload(ExceptionHandler handler) {
		// TODO Auto-generated method stub

	}

	public SuperApiBungee getInstance() {
		return instance;
	}

	@Override
	public BungeeBootstrap getBootstrap() {
		return bootstrap;
	}

	@Override
	public PluginLogger getLogger() {
		return bootstrap.getPluginLogger();
	}

	@Override
	public ThreadFactory getThreadFactory() {
        ScheduledExecutorService scheduledExecutorService = this.executorService;
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) scheduledExecutorService;
        ThreadFactory threadFactory = threadPoolExecutor.getThreadFactory();
        return threadFactory;
	}

}
