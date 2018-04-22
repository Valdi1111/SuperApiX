package org.valdi.SuperApiX.chat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.valdi.SuperApiX.NMSUtils;
import org.valdi.SuperApiX.SuperApi;
import org.valdi.SuperApiX.events.ActionBarMessageEvent;

public class ActionBarAPI extends NMSUtils {
	
    public ActionBarAPI(Player player, String message) {
    	
        if (!player.isOnline()) {
            return; // Player may have logged out
        }
        // Call the event, if cancelled don't send Action Bar
        ActionBarMessageEvent event = new ActionBarMessageEvent(player, message);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

        if (getNmsVer().startsWith("v1_12_")) {
            sendActionBarPost112(player, event.getMessage());
        } else {
            sendActionBarPre112(player, event.getMessage());
        }
    }

    public ActionBarAPI(final Player player, final String message, int duration) {
        new ActionBarAPI(player, message);

        if (duration >= 0) {
            // Sends empty message at the end of the duration. Allows messages shorter than 3 seconds, ensures precision.
            new BukkitRunnable() {
                public void run() {
                	new ActionBarAPI(player, "");
                }
            }.runTaskLater(SuperApi.getInstance(), duration + 1);
        }

        // Re-sends the messages every 3 seconds so it doesn't go away from the player's screen.
        while (duration > 40) {
            duration -= 40;
            new BukkitRunnable() {
                public void run() {
                	new ActionBarAPI(player, message);
                }
            }.runTaskLater(SuperApi.getInstance(), (long) duration);
        }
    }

    private void sendActionBarPost112(Player player, String message) {
        if (!player.isOnline()) {
            return; // Player may have logged out
        }

        try {
            Class<?> craftPlayerClass = getCraftClass("entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object ppoc;
            Class<?> c4 = getNMSClass("PacketPlayOutChat");
            Class<?> c5 = getNMSClass("Packet");
            Class<?> c2 = getNMSClass("ChatComponentText");
            Class<?> c3 = getNMSClass("IChatBaseComponent");
            Class<?> chatMessageTypeClass = getNMSClass("ChatMessageType");
            Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
            Object chatMessageType = null;
            for (Object obj : chatMessageTypes) {
                if (obj.toString().equals("GAME_INFO")) {
                    chatMessageType = obj;
                }
            }
            Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
            ppoc = c4.getConstructor(new Class<?>[]{c3, chatMessageTypeClass}).newInstance(o, chatMessageType);
            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
            Object h = m1.invoke(craftPlayer);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception e) {
        	SuperApi.getInstance().getLogger().severe("Error on ActionBar sending > " + e.getMessage());
        }
    }

    private void sendActionBarPre112(Player player, String message) {
        if (!player.isOnline()) {
            return; // Player may have logged out
        }

        try {
            Class<?> craftPlayerClass = getCraftClass("entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Object ppoc;
            Class<?> c4 = getNMSClass("PacketPlayOutChat");
            Class<?> c5 = getNMSClass("Packet");
            if (getNmsVer().equalsIgnoreCase("v1_8_R1") || getNmsVer().startsWith("v1_7_")) {
                Class<?> c2 = getNMSClass("ChatSerializer");
                Class<?> c3 = getNMSClass("IChatBaseComponent");
                Method m3 = c2.getDeclaredMethod("a", String.class);
                Object cbc = c3.cast(m3.invoke(c2, "{text:"  + message + "}"));
                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(cbc, (byte) 2);
            } else {
                Class<?> c2 = getNMSClass("ChatComponentText");
                Class<?> c3 = getNMSClass("IChatBaseComponent");
                Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
            }
            Method m1 = craftPlayerClass.getDeclaredMethod("getHandle");
            Object h = m1.invoke(craftPlayer);
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            Object pc = f1.get(h);
            Method m5 = pc.getClass().getDeclaredMethod("sendPacket", c5);
            m5.invoke(pc, ppoc);
        } catch (Exception e) {
        	SuperApi.getInstance().getLogger().severe("Error on ActionBar sending > " + e.getMessage());
        }
    }
	  
}
