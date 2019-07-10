package org.valdi.SuperApiX.common.databases.data;

import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.databases.DatabaseException;

public abstract class SafeStatementRunnable implements Runnable {
	private final StoreLoader loader;
	private final String explenation;
	private final ExceptionHandler handler;
	
	public SafeStatementRunnable(final StoreLoader loader, final String explenation, ExceptionHandler handler) {
		this.loader = loader;
		this.explenation = explenation;
		this.handler = handler;
	}

	/**
	 * Execute a statement and return the result
	 *
	 * @throws DataManipulationException some unhandled errors
	 * @throws DatabaseException if a database access error occurs
	 * or this method is called on a closed connection
	 */
	public abstract void executeStatement() throws DataManipulationException, DatabaseException;

	@Override
	public void run() {
		try {
			this.executeStatement();
		} catch(DataManipulationException e) {
			loader.getLogger().severe("### Failed to execute statement on the database: " + explenation + " ###");
			loader.getLogger().severe("Error: " + e.getMessage());
			handler.handle(e);
		} catch (DatabaseException e) {
			loader.getLogger().severe("### Unable to get Database connection on: " + explenation + " ###");
			loader.getLogger().severe("Error: " + e.getMessage());
			handler.handle(e);
		}
	}

	/**
	 * Execute the statement
	 * @param async if it has to be executed async
	 */
	public void execute(boolean async) {
		if(async) {
			loader.getScheduler().runTaskAsynchronously(this);
			return;
		}

		this.run();
	}
	
}
