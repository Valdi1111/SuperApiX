package org.valdi.SuperApiX.common.databases.data;

public interface CallbackStatement {

	/**
	 * All the content is executed ASYNC.
	 * 
     * @param result <code>true</code> if the first result is a <code>ResultSet</code>
     *         object; <code>false</code> if the first result is an update
     *         count or there is no result
	 * @throws DataManipulationException
	 */
    public void onStatementDone(boolean result) throws DataManipulationException;

}
