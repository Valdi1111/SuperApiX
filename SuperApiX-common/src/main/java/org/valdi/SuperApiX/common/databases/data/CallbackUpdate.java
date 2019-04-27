package org.valdi.SuperApiX.common.databases.data;

public interface CallbackUpdate {

	/**
	 * All the content may be executed ASYNC.
	 *
	 * @param result either (1) the row count for SQL Data Manipulation Language (DML) statements
	 *         or (2) 0 for SQL statements that return nothing
	 * @throws DataManipulationException if any exception in thrown
	 */
    void onUpdateDone(int result) throws DataManipulationException;

}
