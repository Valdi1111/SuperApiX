package org.valdi.SuperApiX.bukkit.nms.v1_7_R4;

import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.ChatComponentText;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.nms.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.IGeneralUtils;
import org.valdi.SuperApiX.bukkit.nms.NmsExceptions;
import org.valdi.SuperApiX.bukkit.nms.core.VersionUnsupportedException;

public class GeneralUtils extends AbstractNmsProvider implements IGeneralUtils<Object, IChatBaseComponent> {

    public GeneralUtils(SuperApiBukkit plugin) {
        super(plugin);
    }

    @Override
    public SuperKey spaceKeyFromMinecraft(Object key) throws VersionUnsupportedException {
        throw new VersionUnsupportedException(NmsExceptions.NOT_PRESENT.getExplenation());
    }

    @Override
    public Object minecraftKeyFromSpace(SuperKey key) throws VersionUnsupportedException {
        throw new VersionUnsupportedException(NmsExceptions.NOT_PRESENT.getExplenation());
    }

    @Override
    public IChatBaseComponent getBaseComponentFromJson(String json) {
        return new ChatComponentText(json);
    }
}
