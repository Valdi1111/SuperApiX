package org.valdi.SuperApiX.bukkit.gson.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.IOException;
import java.util.UUID;

public class LocationAdapter extends TypeAdapter<Location> {

    @Override
    public void write(JsonWriter out, Location location) throws IOException {
        if (location == null || location.getWorld() == null) {
            out.nullValue();
            return;
        }
        
        out.beginArray();
        out.value(location.getWorld().getUID().toString());
        out.value(location.getX());
        out.value(location.getY());
        out.value(location.getZ());
        out.value(location.getYaw());
        out.value(location.getPitch());
        out.endArray();
   }
    
    @Override
    public Location read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        
        in.beginArray();
        UUID world = UUID.fromString(in.nextString());
        double x = in.nextDouble();
        double y = in.nextDouble();
        double z = in.nextDouble();
        float yaw = (float)in.nextDouble();
        float pitch = (float)in.nextDouble();
        in.endArray();
        return new Location(Bukkit.getServer().getWorld(world), x, y, z, yaw, pitch);
    }
    
}