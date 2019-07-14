package org.valdi.SuperApiX.bukkit.nms.v1_14_R1;

import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_14_R1.TileEntitySign;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.base.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.base.ISignEditor;

public class SignEditor extends AbstractNmsProvider implements ISignEditor {

	public SignEditor(SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
	public void openSignEditor(Player player, Sign sign) {
		Location loc = sign.getLocation();
		TileEntitySign tileSign = (TileEntitySign) ((CraftWorld) sign.getWorld()).getHandle().getTileEntity(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		tileSign.isEditable = true;
		
		EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
		tileSign.a(nmsPlayer);
		nmsPlayer.playerConnection.sendPacket(new PacketPlayOutOpenSignEditor(new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())));
	}

}
