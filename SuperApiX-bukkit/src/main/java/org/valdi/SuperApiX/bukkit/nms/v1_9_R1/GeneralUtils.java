package org.valdi.SuperApiX.bukkit.nms.v1_9_R1;

import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.MinecraftKey;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
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
        return new SuperKey(key.b(), key.a());
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
        return getServer().getServer().currentTick;
    }

    private CraftServer getServer() {
        return (CraftServer) Bukkit.getServer();
    }
}
