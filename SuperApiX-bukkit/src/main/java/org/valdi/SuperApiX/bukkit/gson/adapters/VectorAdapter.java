package org.valdi.SuperApiX.bukkit.gson.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.util.Vector;

import java.io.IOException;

public class VectorAdapter extends TypeAdapter<Vector> {

    @Override
    public void write(JsonWriter out, Vector position) throws IOException {
        if (position == null) {
            out.nullValue();
            return;
        }
        
        out.beginArray();
        out.value(position.getX());
        out.value(position.getY());
        out.value(position.getZ());
        out.endArray();
   }
    
    @Override
    public Vector read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        
        in.beginArray();
        double x = in.nextDouble();
        double y = in.nextDouble();
        double z = in.nextDouble();
        in.endArray();
        return new Vector(x, y, z);
    }
    
}