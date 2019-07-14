package org.valdi.SuperApiX.common.plugin;

public interface ISuperPlugin<T extends ISuperBootstrap> extends StoreLoader {

	/**
	 * Get the plugin's bootstrap.
	 * @return the bootstrap
	 */
	T getBootstrap();

	void load();

	void enable();

	void disable();

	void reload();

}
