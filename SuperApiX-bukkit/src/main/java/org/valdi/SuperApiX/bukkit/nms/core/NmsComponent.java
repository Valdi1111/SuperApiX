package org.valdi.SuperApiX.bukkit.nms.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.core.VersionManager.NmsVersion;

public class NmsComponent<T> {
	private final NmsName<T> type;
	private final String packageName;
	private final String className;
	private final String superName;
	
	private T instance;

	public NmsComponent(final NmsName<T> type, final String className, final String superName) {
		this(type, NmsProvider.PACKAGE, className, superName);
	}
	
	public NmsComponent(final NmsName<T> type, final String packageName, final String className, final String superName) {
		this.type = type;
		this.packageName = packageName;
		this.className = className;
		this.superName = superName;
	}

	public void setupProvider(final SuperApiBukkit plugin, final NmsVersion version) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		this.setupProvider(plugin, version, new Class[] { SuperApiBukkit.class }, new Object[] { plugin });
	}

	public void setupProvider(final SuperApiBukkit plugin, final NmsVersion version, Class[] classes, Object[] values) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		String rawClass = packageName;
		rawClass = rawClass.replace("[version]", version.getRaw());
		rawClass = rawClass.replace("[classname]", className);
		this.instance = (T) Class.forName(rawClass).getConstructor(classes).newInstance(values);
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
