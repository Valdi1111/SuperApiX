package org.valdi.SuperApiX.common.databases;

import java.io.File;

import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.databases.types.H2Database;
import org.valdi.SuperApiX.common.databases.types.MariadbDatabase;
import org.valdi.SuperApiX.common.databases.types.MongodbDatabase;
import org.valdi.SuperApiX.common.databases.types.MySqlDatabase;
import org.valdi.SuperApiX.common.databases.types.PostgreSqlDatabase;
import org.valdi.SuperApiX.common.databases.types.SqLiteDatabase;

public class DatabasesProvider implements IDatabasesProvider {

	@Override
	public IDataStorage createSqLiteDatabase(StoreLoader loader, File path, String name, int poolSize, String poolName) throws DatabaseException {
		if(path == null || name == null) {
			throw new IllegalArgumentException("Database path & name cannot be null!");
		}
		
		return new SqLiteDatabase(loader, new File(path, name), poolSize, ifNull(poolName, loader.getName() + "-Hikari"));
	}

	@Override
	public IDataStorage createH2Database(StoreLoader loader, File path, String name, String options,
										 String username, String password, int poolSize, String poolName) throws DatabaseException {
		if(path == null || name == null) {
			throw new IllegalArgumentException("Database path & name cannot be null!");
		}
		
		return new H2Database(loader, new File(path, name), ifNull(options, ""), ifNull(username, "root"),
				ifNull(password, "password"), poolSize, ifNull(poolName, loader.getName() + "-Hikari"));
	}

	@Override
	public IDataStorage createMySqlDatabase(StoreLoader loader, String address, int port, String database, String options,
											String username, String password, int poolSize, String poolName) throws DatabaseException {
		return new MySqlDatabase(loader, fixedAddress(address), port, ifNull(database, "database"), ifNull(options, ""),
				ifNull(username, "root"), ifNull(password, "password"), poolSize, ifNull(poolName, loader.getName() + "-Hikari"));
	}

	@Override
	public IDataStorage createMariadbDatabase(StoreLoader loader, String address, int port, String database, String options,
											  String username, String password, int poolSize, String poolName) throws DatabaseException {
		return new MariadbDatabase(loader, fixedAddress(address), port, ifNull(database, "database"), ifNull(options, ""),
				ifNull(username, "root"), ifNull(password, "password"), poolSize, ifNull(poolName, loader.getName() + "-Hikari"));
	}

	@Override
	public IDataStorage createPostgreSqlDatabase(StoreLoader loader, String address, int port, String database, String options,
												 String username, String password, int poolSize, String poolName) throws DatabaseException {
		return new PostgreSqlDatabase(loader, fixedAddress(address), port, ifNull(database, "database"), ifNull(options, ""),
				ifNull(username, "root"), ifNull(password, "password"), poolSize, ifNull(poolName, loader.getName() + "-Hikari"));
	}

	@Override
	public IDataStorage createMongodbDatabase(StoreLoader loader, String address, int port, String database, String options,
											  String username, String password) throws DatabaseException {
		return new MongodbDatabase(loader, fixedAddress(address), port, ifNull(database, "database"), ifNull(options, ""),
				ifNull(username, "root"), ifNull(password, "password"));
	}
	
	private static String fixedAddress(String address) {
        if (address.equalsIgnoreCase("localhost") || address.equals("0.0.0.0")) {
        	return "127.0.0.1";
        }
        
        return address;
	}
	
	private static String ifNull(String string, String value) {
		if(string == null || string.isEmpty()) {
			return value;
		}
		
		return string;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private StoreLoader loader;
		private File file;
		private String address = "127.0.0.1";
		private int port = 3306;
		private String database = "database";
		private String options = "";
		private String username = "user";
		private String password = "password";
		private StorageType type;
		
		private int poolSize = 15;
		private String poolName;

		public Builder setStoreLoader(StoreLoader loader) {
			this.loader = loader;
			return this;
		}

		public Builder setFile(File path, String name) {
			if(name == null) {
				throw new IllegalArgumentException("Filename cannot be null!");
			}
			this.file = new File(path, name);
			return this;
		}

		public Builder setFile(File file) {
			this.file = file;
			return this;
		}
		
		public Builder setAddress(String address) {
			this.address = fixedAddress(address);
			return this;
		}
		
		public Builder setPort(int port) {
			this.port = port;
			return this;
		}
		
		public Builder setDatabase(String database) {
			this.database = ifNull(database, "database");
			return this;
		}
		
		public Builder setOptions(String options) {
			this.options = ifNull(options, "");
			return this;
		}
		
		public Builder setUsername(String username) {
			this.username = ifNull(username, "root");
			return this;
		}
		
		public Builder setPassword(String password) {
			this.password = ifNull(password, "password");
			return this;
		}
		
		public Builder setPoolSize(int poolSize) {
			this.poolSize = poolSize;
			return this;
		}
		
		public Builder setPoolName(String poolName) {
			this.poolName = poolName;
			return this;
		}
		
		public Builder setType(StorageType type) {
			this.type = type;
			return this;
		}
		
		public IDataStorage build() throws DatabaseException {
			if(loader == null || type == null) {
				throw new IllegalArgumentException("Database type cannot be null!");
			}

			this.poolName = ifNull(poolName, loader.getName() + "-Hikari");
			
			switch(type) {
				case SQLITE: {
					if(file == null) {
						throw new IllegalArgumentException("Database file cannot be null!");
					}
					return new SqLiteDatabase(loader, file, poolSize, poolName);
				}
				case H2: {
					if(file == null) {
						throw new IllegalArgumentException("Database file cannot be null!");
					}
					return new H2Database(loader, file, options, username, password, poolSize, poolName);
				}
				case MYSQL: {
					return new MySqlDatabase(loader, address, port, database, options, username, password, poolSize, poolName);
				}
				case MARIADB: {
					return new MariadbDatabase(loader, address, port, database, options, username, password, poolSize, poolName);
				}
				case POSTGRESQL: {
					return new PostgreSqlDatabase(loader, address, port, database, options, username, password, poolSize, poolName);
				}
				case MONGODB: {
					return new MongodbDatabase(loader, address, port, database, options, username, password);
				}
				default: {
					return null;
				}
			}
		}
	}

}
