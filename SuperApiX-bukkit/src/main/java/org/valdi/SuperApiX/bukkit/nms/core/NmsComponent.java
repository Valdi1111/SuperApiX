package org.valdi.SuperApiX.bukkit.nms.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Optional;

import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.versions.MinecraftVersion;

public class NmsComponent<T> {
	private final String id;
	private final String packageName;
	private final String className;
	private final Class<T> superClazz;
	
	private T instance;

	public NmsComponent(final String id, final String className, final Class<T> superClazz) {
		this(id, NmsProvider.PACKAGE, className, superClazz);
	}

	public NmsComponent(final String id, final String packageName, final String className, final Class<T> superClazz) {
		this.id = id;
		this.packageName = packageName;
		this.className = className;
		this.superClazz = superClazz;
	}

	public void setupProvider(final SuperApiBukkit plugin, final MinecraftVersion version)
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		this.setupProvider(version, new Class[] { SuperApiBukkit.class }, new Object[] { plugin });
	}

	public void setupProvider(final MinecraftVersion version, Class[] classes, Object[] values)
			throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		String rawClass = packageName;
		rawClass = rawClass.replace(NmsProvider.VERSION, version.getVersion());
		rawClass = rawClass.replace(NmsProvider.CLASSNAME, className);
		this.instance = (T) Class.forName(rawClass).getConstructor(classes).newInstance(values);
	}

	public String getId() {
		return id;
	}

	public String getClassName() {
		return className;
	}

	public Class<T> getSuper() {
		return superClazz;
	}
	
	public String getSuperName() {
		return superClazz.getSimpleName();
	}
	
	public Optional<T> getInstance() {
		return Optional.ofNullable(instance);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof NmsComponent) {
			NmsComponent<?> component = (NmsComponent<?>) o;
			return Objects.equals(id, component.id);
		}
		if (o instanceof String) {
			String compId = (String) o;
			return Objects.equals(id, compId);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
