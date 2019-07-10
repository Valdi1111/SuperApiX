package org.valdi.SuperApiX.common.plugin;

public interface ISuperPlugin<T extends ISuperBootstrap> extends StoreLoader {

	void load();

	void enable();

	void disable();

	void reload();

	/**
	 * Get the plugin's bootstrap
	 * @return the bootstrap
	 */
	T getBootstrap();

}
