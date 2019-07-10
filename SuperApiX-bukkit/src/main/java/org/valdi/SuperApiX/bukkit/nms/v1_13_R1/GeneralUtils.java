package org.valdi.SuperApiX.bukkit.nms.v1_13_R1;

import net.minecraft.server.v1_13_R1.IChatBaseComponent;
import net.minecraft.server.v1_13_R1.MinecraftKey;
import net.minecraft.server.v1_13_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R1.CraftServer;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.base.IGeneralUtils;

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

    @Override
    public int getTicks() {
        return getServer().getServer().aj();
    }

    private CraftServer getServer() {
        return (CraftServer) Bukkit.getServer();
    }
}
