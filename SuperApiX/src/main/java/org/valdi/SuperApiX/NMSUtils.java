package org.valdi.SuperApiX;

import org.bukkit.Bukkit;

public abstract class NMSUtils {

    protected Class<?> getNMSClass(String name) {
		try {
			return Class.forName("net.minecraft.server." + getNmsVer() + '.' + name);
		} catch (ClassNotFoundException e) {
			SuperApi.getInstance().getLogger().severe("Error on Title/SubTitle sending (getting nms class error) > " + e.getMessage());
			return null;
		}
	}

	protected Class<?> getCraftClass(String name) {
		try {
			return Class.forName("org.bukkit.craftbukkit." + getNmsVer() + '.' + name);
		} catch (ClassNotFoundException e) {
			SuperApi.getInstance().getLogger().severe("Error on Title/SubTitle sending (getting nms class error) > " + e.getMessage());
			return null;
		}
	}
	
	public static String getNmsVer() {
        String nmsver = Bukkit.getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf('.') + 1);
        return nmsver;
	}

}
