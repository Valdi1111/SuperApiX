package org.valdi.SuperApiX;

import org.valdi.SuperApiX.common.databases.data.ExceptionHandler;

public abstract class AbstractPlugin implements ISuperPlugin {
	
	public abstract void load(); 
	
	public abstract void enable();
	
	public abstract void disable();
	
	public abstract void reload(ExceptionHandler handler);

}
