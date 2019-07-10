package org.valdi.SuperApiX.bukkit.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommandUtils {

    public static boolean hasFlag(List<String> args, char flag) {
        Iterator<String> it = args.iterator();
        while (it.hasNext()) {
            if (!it.next().equalsIgnoreCase("-" + flag)) continue;
            return true;
        }
        return false;
    }

    /**
     * Returns all of the items that begin with the given start,
     * ignoring case.  Intended for tabcompletion.
     *
     * @param list - string list
     * @param start - first few chars of a string
     * @return List of items that start with the letters
     */
    public static List<String> tabLimit(final List<String> list, final String start) {
        final List<String> returned = new ArrayList<>();
        for (String s : list) {
            if (s == null) {
                continue;
            }
            if (s.toLowerCase().startsWith(start.toLowerCase())) {
                returned.add(s);
            }
        }

        return returned;
    }

}
