package org.valdi.SuperApiX.common.databases.data;

import java.sql.ResultSet;

public interface CallbackQuery {

	/**
	 * All the content is executed ASYNC.
	 * 
     * @param result a <code>ResultSet</code> object that contains the data produced by the
     *         query; never <code>null</code>
	 * @throws DataManipulationException
	 */
    public void onQueryDone(ResultSet result) throws DataManipulationException;

}
