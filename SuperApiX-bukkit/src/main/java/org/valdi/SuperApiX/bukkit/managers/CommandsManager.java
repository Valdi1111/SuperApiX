package org.valdi.SuperApiX.bukkit.managers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.valdi.SuperApiX.bukkit.plugin.BukkitStoreLoader;
import org.valdi.SuperApiX.bukkit.commands.CompositeCommand;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class CommandsManager {
    private final BukkitStoreLoader plugin;

    private Field commandMapField;
    private Field knownCommandsField;

    private Map<String, CompositeCommand> commands = new ConcurrentHashMap<>();

    public CommandsManager(final BukkitStoreLoader plugin) {
        this.plugin = plugin;
        try{
            // Use reflection to obtain the commandMap method in Bukkit's server. It used to be visible, but isn't anymore.
            commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
        } catch(Exception e) {
            plugin.getLogger().severe("Bukkit server commandMap method is not there! This means no commands can be registered!", e);
        }
    }

    public boolean registerCommand(CompositeCommand command) {
        if (plugin.getJavaPlugin().getCommand(command.getLabel()) != null) {
        	return false;
        }
        
        commands.put(command.getLabel(), command);
        try{
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());
            String prefix = plugin.getName().toLowerCase();
            commandMap.register(prefix, command);
            plugin.getLogger().info("Registered command /" + prefix + ":" + command.getLabel());
        	return true;
        } catch(Exception e) {
            plugin.getLogger().severe("Bukkit server commandMap method is not there! This means no commands can be registered!", e);
            return false;
        }
    }

    public void unregisterCommand(CompositeCommand command) {
        commands.remove(command.getLabel());
        unregister(command);
    }

    private void unregister(CompositeCommand command) {
        try{
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());
            Map<String, Command> knownCommands = (Map) knownCommandsField.get(commandMap);

            Iterator<Entry<String, Command>> it = knownCommands.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, Command> entry = it.next();
                if (!(entry.getValue() instanceof CompositeCommand)) {
                    continue;
                }

                CompositeCommand localCommand = (CompositeCommand) entry.getValue();
                if(localCommand == command) {
                    command.unregister(commandMap);
                    it.remove();
                    plugin.getLogger().info("Unregistered command /" + command.getLabel());
                }
            }
        } catch(Exception e) {
            plugin.getLogger().severe("Bukkit server commandMap method is not there! This means no commands can be registered!", e);
        }
    }

    public void unregisterCommands() {
        for(CompositeCommand command : commands.values()) {
            this.unregister(command);
        }
        commands.clear();
    }
    
    /**
     * @return the commands
     */
    public Map<String, CompositeCommand> getCommands() {
    	return commands;
    }

    public CompositeCommand getCommand(String command) {
        return commands.get(command);
    }

    /**
     * List all commands registered so far
     * @return set of commands
     */
    public String listCommands() {
        return commands.keySet().toString();
    }
    
}
