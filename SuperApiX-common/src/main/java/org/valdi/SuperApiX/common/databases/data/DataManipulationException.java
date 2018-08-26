package org.valdi.SuperApiX.common.databases.data;

public class DataManipulationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public DataManipulationException(Exception e) {
        super(e.getMessage(), e);
    }

    public DataManipulationException(String message) {
        super(message);
    }

}
