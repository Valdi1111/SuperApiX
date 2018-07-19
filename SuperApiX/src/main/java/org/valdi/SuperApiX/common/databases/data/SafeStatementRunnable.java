package org.valdi.SuperApiX.common.databases.data;

import org.valdi.SuperApiX.ISuperPlugin;
import org.valdi.SuperApiX.common.databases.DatabaseException;

public abstract class SafeStatementRunnable implements Runnable {
	
	private final ISuperPlugin plugin;
	private final String explenation;
	private final ExceptionHandler handler;
	
	public SafeStatementRunnable(final ISuperPlugin plugin, final String explenation, ExceptionHandler handler) {
		this.plugin = plugin;
		this.explenation = explenation;
		this.handler = handler;
	}

	/**
	 * Execute a statement async and return the result
	 * 
	 * @throws DataManipulationException
	 * @throws DatabaseException
	 */
	public abstract void executeStatement() throws DataManipulationException, DatabaseException;

	@Override
	public void run() {
		try {
			this.executeStatement();
		} catch(DataManipulationException e) {
            plugin.getLogger().severe("### Failed to execute statement on the database: " + explenation + " ###");
            plugin.getLogger().severe("Error: " + e.getMessage());
			handler.handle(e);
		} catch (DatabaseException e) {
			plugin.getLogger().severe("### Unable to get Database connection on: " + explenation + " ###");
			plugin.getLogger().severe("Error: " + e.getMessage());
			handler.handle(e);
		}
	}
	
}
