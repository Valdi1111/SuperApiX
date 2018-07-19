package org.valdi.SuperApiX.common.databases;

import java.io.File;

public interface IDatabasesProvider {
	
	public IDataStorage createSqLiteDatabase(File path, String name) throws DatabaseException;
	
	public IDataStorage createSqLiteDatabase(File file) throws DatabaseException;
	
	public IDataStorage createH2Database(File path, String name) throws DatabaseException;
	
	public IDataStorage createH2Database(File file) throws DatabaseException;
	
	public IDataStorage createMySqlDatabase(String address, int port, String database, String options, 
			String username, String password, int poolSize, String poolName) throws DatabaseException;
	
	public IDataStorage createMariadbDatabase(String address, int port, String database, String options, 
			String username, String password, int poolSize, String poolName) throws DatabaseException;
	
	public IDataStorage createPostgreSqlDatabase(String address, int port, String database, String options, 
			String username, String password, int poolSize, String poolName) throws DatabaseException;
	
	public IDataStorage createMongodbDatabase(String address, int port, String database, String options, 
			String username, String password) throws DatabaseException;

}
