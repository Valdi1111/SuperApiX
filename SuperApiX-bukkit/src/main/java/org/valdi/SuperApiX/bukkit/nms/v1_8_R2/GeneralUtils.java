package org.valdi.SuperApiX.bukkit.nms.v1_8_R2;

import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.MinecraftKey;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R2.CraftServer;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.base.IGeneralUtils;

import java.lang.reflect.Field;

public class GeneralUtils extends AbstractNmsProvider implements IGeneralUtils<MinecraftKey, IChatBaseComponent> {

    public GeneralUtils(SuperApiBukkit plugin) {
        super(plugin);
    }

    @Override
    public SuperKey spaceKeyFromMinecraft(MinecraftKey key) {
        return new BetterMinecraftKey(key).toSuperKey();
    }

    @Override
    public MinecraftKey minecraftKeyFromSpace(SuperKey key) {
        return new BetterMinecraftKey(key.getNamespace(), key.getKey());
    }

    @Override
    public IChatBaseComponent getBaseComponentFromJson(String json) {
        return IChatBaseComponent.ChatSerializer.a(json);
    }

    public static class BetterMinecraftKey extends MinecraftKey {

        public BetterMinecraftKey(MinecraftKey key) {
            this(getNamespace(key), getKey(key));
        }

        private static String getNamespace(MinecraftKey key) {
            try {
                Field a = MinecraftKey.class.getField("a");
                return (String) a.get(key);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private static String getKey(MinecraftKey key) {
            try {
                Field b = MinecraftKey.class.getField("b");
                return (String) b.get(key);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public BetterMinecraftKey(String namespace, String key) {
            super(0, namespace, key);
        }

        public String getKey() {
            return a;
        }

        public String getNamespace() {
            return b;
        }

        public SuperKey toSuperKey() {
            return new SuperKey(a, b);
        }

    }

    @Override
    public int getTicks() {
        return getServer().getServer().currentTick;
    }

    private CraftServer getServer() {
        return (CraftServer) Bukkit.getServer();
    }

}
