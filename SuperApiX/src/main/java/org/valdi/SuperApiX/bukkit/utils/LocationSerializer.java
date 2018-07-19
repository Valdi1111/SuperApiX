package org.valdi.SuperApiX.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class LocationSerializer {

    /**
     * Serializes the location of the block specified.
     * @param b The block whose location is to be saved.
     * @return The serialized data.
     */
    public String serializeLocation(Block b) {
        if (b == null)
            return "";
        return serializeLocation(b.getLocation());
    }

    /**
     * Serializes the location specified.
     * @param location The location that is to be saved.
     * @return The serialized data.
     */
    public String serializeLocation(Location location) {
        if (location == null) {
            return "";
        }
        
        String w = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        String str = w + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch;
        return str.replace(".0", "").replace("/", "");
    }

    /**
     * Deserializes a location from the string.
     * @param str The string to parse.
     * @return The location that was serialized in the string.
     */
    public Location unserializeLocation(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        
        str = str.replace("w:", "").replace("x:", ":").replace("y:", ":").replace("z:", ":").replace("yaw:", ":").replace("pitch:", ":").replace("/", ".");
        String[] args = str.split("\\s*:\\s*");

        World world = Bukkit.getWorld(args[0]);
        double x = Double.parseDouble(args[1]);
		double y = Double.parseDouble(args[2]);
		double z = Double.parseDouble(args[3]);
		if(args.length > 4) {
			float yaw = Float.parseFloat(args[4]);
			float pitch = Float.parseFloat(args[5]);
			return new Location(world, x, y, z, yaw, pitch);
		}
		
		return new Location(world, x, y, z);
    }

}
