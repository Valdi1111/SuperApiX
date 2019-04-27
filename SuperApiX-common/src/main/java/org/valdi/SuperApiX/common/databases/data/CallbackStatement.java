package org.valdi.SuperApiX.common.databases.data;

public interface CallbackStatement {

	/**
	 * All the content may be executed ASYNC.
	 * 
     * @param result <code>true</code> if the first result is a <code>ResultSet</code>
     *         object; <code>false</code> if the first result is an update
     *         count or there is no result
	 * @throws DataManipulationException if any exception in thrown
	 */
    void onStatementDone(boolean result) throws DataManipulationException;

}
