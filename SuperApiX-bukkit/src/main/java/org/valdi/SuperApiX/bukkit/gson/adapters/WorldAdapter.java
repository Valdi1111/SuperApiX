package org.valdi.SuperApiX.bukkit.gson.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;
import java.util.UUID;

public class WorldAdapter extends TypeAdapter<World> {

    @Override
    public void write(JsonWriter out, World value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        
        out.value(value.getUID().toString());
    }
    
    @Override
    public World read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
        	in.nextNull();
            return null;
        }

        UUID world = UUID.fromString(in.nextString());
        return Bukkit.getServer().getWorld(world);
    }
}