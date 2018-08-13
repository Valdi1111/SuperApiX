package org.valdi.SuperApiX.common.databases;

import java.io.File;

import org.valdi.SuperApiX.common.databases.types.H2Database;
import org.valdi.SuperApiX.common.databases.types.MariadbDatabase;
import org.valdi.SuperApiX.common.databases.types.MongodbDatabase;
import org.valdi.SuperApiX.common.databases.types.MySqlDatabase;
import org.valdi.SuperApiX.common.databases.types.PostgreSqlDatabase;
import org.valdi.SuperApiX.common.databases.types.SqLiteDatabase;

public class DatabasesProvider implements IDatabasesProvider {

	@Override
	public IDataStorage createSqLiteDatabase(File file) throws DatabaseException {
		if(file == null) {
			throw new IllegalArgumentException("Database file cannot be null!");
		}
		
		return new SqLiteDatabase(file);
	}

	@Override
	public IDataStorage createSqLiteDatabase(File path, String name) throws DatabaseException {
		if(path == null || name == null) {
			throw new IllegalArgumentException("Database path & name cannot be null!");
		}
		
		return createSqLiteDatabase(new File(path, name));
	}

	@Override
	public IDataStorage createH2Database(File file) throws DatabaseException {
		if(file == null) {
			throw new IllegalArgumentException("Database file cannot be null!");
		}
		
		return new H2Database(file);
	}

	@Override
	public IDataStorage createH2Database(File path, String name) throws DatabaseException {
		if(path == null || name == null) {
			throw new IllegalArgumentException("Database path & name cannot be null!");
		}
		
		return createH2Database(new File(path, name));
	}

	@Override
	public IDataStorage createMySqlDatabase(String address, int port, String database, String options, 
			String username, String password, int poolSize, String poolName) throws DatabaseException {
		return new MySqlDatabase(fixedAddress(address), port, ifNull(database, "database"), ifNull(options, ""), 
				ifNull(username, "root"), ifNull(password, "password"), poolSize, ifNull(poolName, "SuperApiX"));
	}

	@Override
	public IDataStorage createMariadbDatabase(String address, int port, String database, String options, 
			String username, String password, int poolSize, String poolName) throws DatabaseException {
		return new MariadbDatabase(fixedAddress(address), port, ifNull(database, "database"), ifNull(options, ""), 
				ifNull(username, "root"), ifNull(password, "password"), poolSize, ifNull(poolName, "SuperApiX"));
	}

	@Override
	public IDataStorage createPostgreSqlDatabase(String address, int port, String database, String options, 
			String username, String password, int poolSize, String poolName) throws DatabaseException {
		return new PostgreSqlDatabase(fixedAddress(address), port, ifNull(database, "database"), ifNull(options, ""), 
				ifNull(username, "root"), ifNull(password, "password"), poolSize, ifNull(poolName, "SuperApiX"));
	}

	@Override
	public IDataStorage createMongodbDatabase(String address, int port, String database, String options, 
			String username, String password) throws DatabaseException {
		return new MongodbDatabase(fixedAddress(address), port, ifNull(database, "database"), ifNull(options, ""), 
				ifNull(username, "root"), ifNull(password, "password"));
	}
	
	private static String fixedAddress(String address) {
        if (address.equalsIgnoreCase("localhost") || address.equals("0.0.0.0")) {
        	return "127.0.0.1";
        }
        
        return address;
	}
	
	private static String ifNull(String string, String value) {
		if(string == null) {
			return value;
		}
		
		return string;
	}
	
	private static LocalBuilder localBuilder = new LocalBuilder();
	
	public static LocalBuilder localBuilder() {
		return localBuilder;
	}
	
	private static RemoteBuilder remoteBuilder = new RemoteBuilder();
	
	public static RemoteBuilder remoteBuilder() {
		return remoteBuilder;
	}
	
	public static class LocalBuilder {
		private File file;
		private StorageType type;
		
		public LocalBuilder setFile(File path, String name) {
			this.file = new File(path, name);
			return this;
		}
		
		public LocalBuilder setFile(File file) {
			this.file = file;
			return this;
		}
		
		public LocalBuilder setType(StorageType type) {
			if(type != StorageType.H2 && type != StorageType.SQLITE) {
				throw new IllegalArgumentException("StorageType must be H2 or SQLITE for LocalBuilder!");
			}
			
			this.type = type;
			return this;
		}
		
		public IDataStorage build() throws DatabaseException {
			if(file == null || type == null) {
				throw new IllegalArgumentException("Database file & type cannot be null!");
			}
			
			switch(type) {
				case H2: {
					return new H2Database(file);
				}
				case SQLITE: {
					return new SqLiteDatabase(file);
				}
				default: {
					return null;
				}
			}
		}
	}
	
	public static class RemoteBuilder {
		private String address = "127.0.0.1";
		private int port = 3306;
		private String database = "database";
		private String options = "";
		private String username = "user";
		private String password = "password";
		private StorageType type;
		
		private int poolSize = 150;
		private String poolName = "SuperApiX";
		
		public RemoteBuilder setAddress(String address) {
			this.address = fixedAddress(address);
			return this;
		}
		
		public RemoteBuilder setPort(int port) {
			this.port = port;
			return this;
		}
		
		public RemoteBuilder setDatabase(String database) {
			this.database = ifNull(database, "database");
			return this;
		}
		
		public RemoteBuilder setOptions(String options) {
			this.options = ifNull(options, "");
			return this;
		}
		
		public RemoteBuilder setUsername(String username) {
			this.username = ifNull(username, "root");
			return this;
		}
		
		public RemoteBuilder setPassword(String password) {
			this.password = ifNull(password, "password");
			return this;
		}
		
		public RemoteBuilder setPoolSize(int poolSize) {
			this.poolSize = poolSize;
			return this;
		}
		
		public RemoteBuilder setPoolName(String poolName) {
			this.poolName = ifNull(poolName, "SuperApiX");
			return this;
		}
		
		public RemoteBuilder setType(StorageType type) {
			if(type == StorageType.H2 || type == StorageType.SQLITE) {
				throw new IllegalArgumentException("StorageType must be MYSQL, HIKARI, POSTGRESQL, MONGODB or MARIADB for RemoteBuilder!");
			}
			
			this.type = type;
			return this;
		}
		
		public IDataStorage build() throws DatabaseException {
			if(type == null) {
				throw new IllegalArgumentException("Database type cannot be null!");
			}
			
			switch(type) {
				case MYSQL: {
					return new MySqlDatabase(address, port, database, options, username, password, poolSize, poolName);
				}
				case MARIADB: {
					return new MariadbDatabase(address, port, database, options, username, password, poolSize, poolName);
				}
				case POSTGRESQL: {
					return new PostgreSqlDatabase(address, port, database, options, username, password, poolSize, poolName);
				}
				case MONGODB: {
					return new MongodbDatabase(address, port, database, options, username, password);
				}
				default: {
					return null;
				}
			}
		}
	}

}
