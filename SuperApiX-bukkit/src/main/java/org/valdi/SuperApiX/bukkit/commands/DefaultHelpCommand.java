package org.valdi.SuperApiX.bukkit.commands;

import org.apache.commons.lang.math.NumberUtils;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.users.User;
import org.valdi.SuperApiX.bukkit.users.locale.TextVariables;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Adds a default help to every command that will show the usage of the command
 * and the usage of any subcommands that the command has.
 * @author tastybento
 *
 */
public class DefaultHelpCommand extends CompositeCommand {
    private final SuperApiBukkit superApi;

    private static final int MAX_DEPTH = 2;
    private static final String USAGE_PLACEHOLDER = "[usage]";
    private static final String PARAMS_PLACEHOLDER = "[parameters]";
    private static final String DESC_PLACEHOLDER = "[description]";
    private static final String HELP_SYNTAX_REF = "commands.help.syntax";
    private static final String HELP_SYNTAX_NO_PARAMETERS_REF = "commands.help.syntax-no-parameters";
    private static final String HELP = "help";

    public DefaultHelpCommand(CompositeCommand parent) {
        super(parent, HELP);

        this.superApi = SuperApiBukkit.getInstance();
    }

    @Override
    public void setup() {
        // Set the usage to what the parent's command is
    	setParametersHelp(parent.getParameters());
        setDescription(parent.getDescription());
        inheritPermission();
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        int depth = 0;
        if (args.size() == 1) {
            if (NumberUtils.isDigits(args.get(0))) {
                // Converts first argument into an int, or returns -1 if it cannot. Avoids exceptions.
                depth = Optional.ofNullable(args.get(0)).map(NumberUtils::toInt).orElse(-1);
            } else {
                String usage = parent.getUsage();
                String params = user.getTranslation(superApi, "commands.help.parameters");
                String desc = user.getTranslation(superApi, "commands.help.description");
                user.sendMessage(superApi, HELP_SYNTAX_REF, USAGE_PLACEHOLDER, usage, PARAMS_PLACEHOLDER, params, DESC_PLACEHOLDER, desc);
                return true;
            }
        }
        if (depth == 0) {
            // Get the name of the world for the help header, or console if there is no world association
            String labelText = plugin.getBootstrap().getName(); //PluginDetails.NAME;
            user.sendMessage(superApi, "commands.help.header", TextVariables.LABEL, labelText);
        }
        if (depth < MAX_DEPTH) {
            if (!parent.getLabel().equals(HELP)) {
                // Get elements
                String usage = parent.getUsage();
                String params = user.getTranslationOrNothing(superApi, getParameters());
                String desc = user.getTranslation(superApi, getDescription());

                if (showPrettyHelp(user, usage, params, desc)) {
                    // No more to show
                    return true;
                }
            }
            // Increment the depth and run through any subcommands and get their help too
            runSubCommandHelp(user, depth + 1);
        }
        if (depth == 0) {
            user.sendMessage(superApi, "commands.help.end");
        }
        return true;
    }

    private void runSubCommandHelp(User user, int newDepth) {
        for (CompositeCommand subCommand : parent.getSubCommands().values()) {
            // Ignore the help command
            if (!subCommand.getLabel().equals(HELP)) {
                // Every command should have help because every command has a default help
                Optional<CompositeCommand> sub = subCommand.getSubCommand(HELP);
                sub.ifPresent(compositeCommand -> compositeCommand.execute(user, HELP, Collections.singletonList(String.valueOf(newDepth))));
            }
        }
    }

    private boolean showPrettyHelp(User user, String usage, String params, String desc) {
        // Show the help
        if (user.isPlayer()) {
            // Player. Check perms
            if (user.hasPermission(parent.getPermission())) {
                if (params == null || params.isEmpty()) {
                    user.sendMessage(superApi, HELP_SYNTAX_NO_PARAMETERS_REF, USAGE_PLACEHOLDER, usage, DESC_PLACEHOLDER, desc);
                } else {
                    user.sendMessage(superApi, HELP_SYNTAX_REF, USAGE_PLACEHOLDER, usage, PARAMS_PLACEHOLDER, params, DESC_PLACEHOLDER, desc);
                }
            } else {
                // No permission, nothing to see here. If you don't have permission, you cannot see any sub commands
                return true;
            }
        } else if (!parent.isOnlyPlayer()) {
            // Console. Only show if it is a console command
            if (params == null || params.isEmpty()) {
                user.sendMessage(superApi, HELP_SYNTAX_NO_PARAMETERS_REF, USAGE_PLACEHOLDER, usage, DESC_PLACEHOLDER, desc);
            } else {
                user.sendMessage(superApi, HELP_SYNTAX_REF, USAGE_PLACEHOLDER, usage, PARAMS_PLACEHOLDER, params, DESC_PLACEHOLDER, desc);
            }
        }
        return false;
    }

}
