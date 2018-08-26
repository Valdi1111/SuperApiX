package org.valdi.SuperApiX.bukkit.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class VectorSerializer {

    /**
     * Serializes the vector of the block specified.
     * @param b The block whose vector is to be saved.
     * @return The serialized data.
     */
    public String serializeLocation(Block b) {
        if (b == null)
            return "";
        return serializeLocation(b.getLocation());
    }

    /**
     * Serializes the vector of the location specified.
     * @param b The block whose vector is to be saved.
     * @return The serialized data.
     */
    public String serializeLocation(Location location) {
        if (location == null)
            return "";
        return serializeLocation(location.toVector());
    }

    /**
     * Serializes the vector specified.
     * @param vector The vector that is to be saved.
     * @return The serialized data.
     */
    public String serializeLocation(Vector vector) {
        if (vector == null) {
            return "";
        }
        
        double x = vector.getX();
        double y = vector.getY();
        double z = vector.getZ();
        String str = x + ":" + y + ":" + z;
        return str.replace(".0", "").replace("/", "");
    }

    /**
     * Deserializes a vector from the string.
     * @param str The string to parse.
     * @return The vector that was serialized in the string.
     */
    public Vector unserializeLocation(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        
        str = str.replace("x:", ":").replace("y:", ":").replace("z:", ":").replace("/", ".");
        String[] args = str.split("\\s*:\\s*");

        double x = Double.parseDouble(args[0]);
		double y = Double.parseDouble(args[1]);
		double z = Double.parseDouble(args[2]);
		
		return new Vector(x, y, z);
    }

}
