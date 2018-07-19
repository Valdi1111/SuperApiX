package org.valdi.SuperApiX.common.databases.data;

public interface CallbackUpdate {

	/**
	 * All the content is executed ASYNC.
	 * 
	 * @param result
	 * @throws DataManipulationException
	 */
    public void onUpdateDone(int result) throws DataManipulationException;

}
