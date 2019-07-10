package org.valdi.SuperApiX.bukkit.gson.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;

public class PotionEffectTypeAdapter extends TypeAdapter<PotionEffectType> {

    @Override
    public void write(JsonWriter out, PotionEffectType value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        
        out.value(value.getName());

    }
    
    @Override
    public PotionEffectType read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
        	in.nextNull();
            return null;
        }
        
        return PotionEffectType.getByName(in.nextString());
    }
}