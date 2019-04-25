package org.valdi.SuperApiX.common.databases;

public class DatabaseException extends Exception {
	private static final long serialVersionUID = 1L;

    public DatabaseException(Exception e) {
        super(e.getMessage(), e);
    }

    public DatabaseException(String message) {
        super(message);
    }

}
