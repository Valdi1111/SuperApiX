package org.valdi.SuperApiX.common.command;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.regex.Pattern;

import org.valdi.SuperApiX.ISuperPlugin;
import org.valdi.SuperApiX.common.sender.Sender;

public class CommandManager {
    public static final Pattern COMMAND_SEPARATOR_PATTERN = Pattern.compile(" (?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

    public static final UUID CONSOLE_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final String CONSOLE_NAME = "Console";

    public static final UUID IMPORT_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static final String IMPORT_NAME = "Import";

    public static final char SECTION_CHAR = '\u00A7'; // §
    public static final char AMPERSAND_CHAR = '&';

    private final ISuperPlugin plugin;

    public CommandManager(ISuperPlugin plugin) {
        this.plugin = plugin;
    }

    public ISuperPlugin getPlugin() {
        return this.plugin;
    }

    /**
     * Generic command method to be called from the command executor object of the platform
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
    public boolean onCommand(Sender sender, String label, List<String> args) {
    	return true;
    }

    /**
     * Generic tab complete method to be called from the command executor object of the platform
     *
     * @param sender who is tab completing
     * @param args   the arguments provided so far
     * @return a list of suggestions
     */
    public List<String> onTabComplete(Sender sender, List<String> args) {
        return args;
    }

    private void sendCommandUsage(Sender sender, String label) {
    	
    }

    /**
     * Strips outer quote marks from a list of parsed arguments.
     *
     * @param input the list of arguments to strip quotes from
     * @return an ArrayList containing the contents of input without quotes
     */
    public static List<String> stripQuotes(List<String> input) {
        input = new ArrayList<>(input);
        ListIterator<String> iterator = input.listIterator();
        while (iterator.hasNext()) {
            String value = iterator.next();
            if (value.length() < 3) {
                continue;
            }

            if (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
                iterator.set(value.substring(1, value.length() - 1));
            }
        }
        return input;
    }

}
