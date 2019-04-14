package org.valdi.SuperApiX.common;

import java.util.concurrent.ThreadFactory;

import org.valdi.SuperApiX.common.config.advanced.StoreLoader;

public interface ISuperPlugin extends StoreLoader {
	
	ISuperBootstrap getBootstrap();

	ThreadFactory getThreadFactory();

}
