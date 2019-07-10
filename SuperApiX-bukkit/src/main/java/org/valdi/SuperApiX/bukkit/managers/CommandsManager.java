package org.valdi.SuperApiX.bukkit.managers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.valdi.SuperApiX.bukkit.plugin.ISuperBukkitPlugin;
import org.valdi.SuperApiX.bukkit.commands.CompositeCommand;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CommandsManager {
    private final ISuperBukkitPlugin plugin;

    private Field commandMapField;
    private Field knownCommandsField;

    private Map<String, CompositeCommand> commands = new HashMap<>();

    public CommandsManager(final ISuperBukkitPlugin plugin) {
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
        if (((JavaPlugin)plugin.getBootstrap()).getCommand(command.getLabel()) != null) {
        	return false;
        }
        
        commands.put(command.getLabel(), command);
        try{
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());
            String prefix = plugin.getBootstrap().getName().toLowerCase();
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
