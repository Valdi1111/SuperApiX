package org.valdi.SuperApiX.common.logging;

public abstract class AbstractPluginLogger implements PluginLogger {
	private boolean debug = false;

	@Override
	public void setDebugStatus(boolean active) {
		this.debug = active;
	}
	
	@Override
	public boolean isDebugEnabled() {
		return debug;
	}

}
