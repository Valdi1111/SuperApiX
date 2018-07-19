package org.valdi.SuperApiX.common.databases.types;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import org.valdi.SuperApiX.common.databases.DatabaseException;
import org.valdi.SuperApiX.common.databases.IDataStorage;
import org.valdi.SuperApiX.common.databases.StorageType;

public class H2Database implements IDataStorage {
    private Connection connection;
	private final File file;
	
	public H2Database(File file) throws DatabaseException {
		this.file = file;
		
		if(!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				throw new DatabaseException(e);
			}
		}
	}

	@Override
	public void close() throws DatabaseException {
		try {
			if(connection != null)
				connection.close();
		} catch (Exception e) {
            throw new DatabaseException(e);
		}
	}

	@Override
	public Connection getConnection() throws DatabaseException {
        try {
            if(connection != null && !connection.isClosed()){
                return connection;
            }
            
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:" + file.getAbsolutePath());
            return connection;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
	}

	@Override
	public StorageType getType() {
		return StorageType.H2;
	}

}
