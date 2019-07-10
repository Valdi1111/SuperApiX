package org.valdi.SuperApiX.common.databases;

import org.valdi.SuperApiX.common.plugin.StoreLoader;

import java.io.File;

public interface IDatabasesProvider {

	/**
	 * Create SqLite database
	 * @param loader the store loader
	 * @param path the file path
	 * @param name the file name
	 * @param poolSize size for the hikari pool
	 * @param poolName name for the hikari pool
	 * @return a data storage instance for this file database
	 * @throws DatabaseException if any exception is thrown during database creation
	 */
	IDataStorage createSqLiteDatabase(StoreLoader loader, File path, String name, int poolSize, String poolName) throws DatabaseException;

	/**
	 * Create H2 database
	 * @param loader the store loader
	 * @param path the file path
	 * @param name the file name
	 * @param options database options (added to the url after database name)
	 * @param username database username
	 * @param password database password
	 * @param poolSize size for the hikari pool
	 * @param poolName name for the hikari pool
	 * @return a data storage instance for this file database
	 * @throws DatabaseException if any exception is thrown during database creation
	 */
	IDataStorage createH2Database(StoreLoader loader, File path, String name, String options,
								  String username, String password, int poolSize, String poolName) throws DatabaseException;

	/**
	 * Create MySql database
	 * @param loader the store loader
	 * @param address database ip address
	 * @param port database port
	 * @param database database name
	 * @param options database options (added to the url after database name)
	 * @param username database username
	 * @param password database password
	 * @param poolSize size for the hikari pool
	 * @param poolName name for the hikari pool
	 * @return a data storage instance for this database
	 * @throws DatabaseException if any exception is thrown during database creation
	 */
	IDataStorage createMySqlDatabase(StoreLoader loader, String address, int port, String database, String options,
									 String username, String password, int poolSize, String poolName) throws DatabaseException;

	/**
	 * Create MariaDb database
	 * @param loader the store loader
	 * @param address database ip address
	 * @param port database port
	 * @param database database name
	 * @param options database options (added to the url after database name)
	 * @param username database username
	 * @param password database password
	 * @param poolSize size for the hikari pool
	 * @param poolName name for the hikari pool
	 * @return a data storage instance for this database
	 * @throws DatabaseException if any exception is thrown during database creation
	 */
	IDataStorage createMariadbDatabase(StoreLoader loader, String address, int port, String database, String options,
									   String username, String password, int poolSize, String poolName) throws DatabaseException;

	/**
	 * Create PostgreSql database
	 * @param loader the store loader
	 * @param address database ip address
	 * @param port database port
	 * @param database database name
	 * @param options database options (added to the url after database name)
	 * @param username database username
	 * @param password database password
	 * @param poolSize size for the hikari pool
	 * @param poolName name for the hikari pool
	 * @return a data storage instance for this database
	 * @throws DatabaseException if any exception is thrown during database creation
	 */
	IDataStorage createPostgreSqlDatabase(StoreLoader loader, String address, int port, String database, String options,
										  String username, String password, int poolSize, String poolName) throws DatabaseException;

	/**
	 * Create Mongodb database
	 * @param loader the store loader
	 * @param address database ip address
	 * @param port database port
	 * @param database database name
	 * @param options database options (added to the url after database name)
	 * @param username database username
	 * @param password database password
	 * @return a data storage instance for this database
	 * @throws DatabaseException if any exception is thrown during database creation
	 */
	IDataStorage createMongodbDatabase(StoreLoader loader, String address, int port, String database, String options,
									   String username, String password) throws DatabaseException;

}
