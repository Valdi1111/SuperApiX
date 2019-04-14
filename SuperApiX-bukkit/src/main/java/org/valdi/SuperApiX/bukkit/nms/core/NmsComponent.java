package org.valdi.SuperApiX.bukkit.nms.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.core.VersionManager.NmsVersion;

public class NmsComponent<T> {	
	public static final String PACKAGE = "org.valdi.SuperApiX.bukkit.nms.";
	
	private final NmsName<T> type;
	private final String className;
	private final String superName;
	
	private T instance;
	
	public NmsComponent(final NmsName<T> type, final String className, final String superName) {
		this.type = type;
		this.className = className;
		this.superName = superName;
	}
	
	public void setupProvider(final SuperApiBukkit plugin, final NmsVersion version) throws InstantiationException, IllegalAccessException, 
						IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		this.instance = (T) Class.forName(PACKAGE + version.getRaw() + "." + className).getConstructor(SuperApiBukkit.class).newInstance(plugin);
	}
	
	public NmsName<T> getType() {
		return type;
	}
	
	public boolean isType(NmsName<?> type) {
		return this.type.equals(type);
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getSuperName() {
		return superName;
	}
	
	public Optional<T> getInstance() {
		return Optional.ofNullable(instance);
	}

}
