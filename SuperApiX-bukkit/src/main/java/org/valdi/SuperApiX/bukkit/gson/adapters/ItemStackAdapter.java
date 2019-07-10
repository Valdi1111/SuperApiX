package org.valdi.SuperApiX.bukkit.gson.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.valdi.SuperApiX.bukkit.nms.base.IItemUtils;

import java.io.IOException;

public class ItemStackAdapter extends TypeAdapter<ItemStack> {

    @Override
    public void write(JsonWriter out, ItemStack item) throws IOException {
        if (item == null) {
            out.nullValue();
            return;
        }

        RegisteredServiceProvider<IItemUtils> provider = Bukkit.getServicesManager().getRegistration(IItemUtils.class);
        if(provider == null) {
            throw new RuntimeException("ItemUtils provided isn't loaded! Cannot serialize item.");
        }

        out.value(provider.getProvider().toBase64(item));
   }
    
    @Override
    public ItemStack read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        RegisteredServiceProvider<IItemUtils> provider = Bukkit.getServicesManager().getRegistration(IItemUtils.class);
        if(provider == null) {
            throw new RuntimeException("ItemUtils provided isn't loaded! Cannot deserialize item.");
        }
        
        return provider.getProvider().fromBase64(in.nextString());
    }

    private void asd(JsonWriter writer, String[] strings, Object[] objects) {

    }

}
