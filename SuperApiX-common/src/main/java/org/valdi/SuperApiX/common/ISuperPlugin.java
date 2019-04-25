package org.valdi.SuperApiX.common;

public interface ISuperPlugin extends StoreLoader {

	/**
	 * Get the plugin's bootstrap
	 * @return the bootstrap
	 */
	ISuperBootstrap getBootstrap();

}
