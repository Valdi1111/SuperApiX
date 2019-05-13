package org.valdi.SuperApiX.bukkit.nms.v1_13_R2;

import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.MinecraftKey;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.nms.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.IGeneralUtils;

public class GeneralUtils extends AbstractNmsProvider implements IGeneralUtils<MinecraftKey, IChatBaseComponent> {

    public GeneralUtils(SuperApiBukkit plugin) {
        super(plugin);
    }

    @Override
    public SuperKey spaceKeyFromMinecraft(MinecraftKey key) {
        return new SuperKey(key.b(), key.getKey());
    }

    @Override
    public MinecraftKey minecraftKeyFromSpace(SuperKey key) {
        return new MinecraftKey(key.getNamespace(), key.getKey());
    }

    @Override
    public IChatBaseComponent getBaseComponentFromJson(String json) {
        return IChatBaseComponent.ChatSerializer.a(json);
    }
}
