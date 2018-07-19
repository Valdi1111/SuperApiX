package org.valdi.SuperApiX.common.databases.types;

import java.sql.Connection;
import java.sql.DriverManager;

import org.valdi.SuperApiX.common.databases.DatabaseException;
import org.valdi.SuperApiX.common.databases.IDataStorage;
import org.valdi.SuperApiX.common.databases.StorageType;

public class MongodbDatabase implements IDataStorage {
    private Connection connection;    
    private final String url;
    private final String username;
    private final String password;
	
	public MongodbDatabase(String address, int port, String database, String options, 
			String username, String password) throws DatabaseException {
		this.url = "jdbc:mongo://" + address + ":" + port + "/" + database + options;
		
		this.username = username;
		this.password = password;
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

            Class.forName("mongodb.jdbc.MongoDriver");
            connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
	}

	@Override
	public StorageType getType() {
		return StorageType.MONGODB;
	}

}
