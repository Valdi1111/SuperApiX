package org.valdi.SuperApiX.common.databases.types;

import java.sql.Connection;
import java.sql.SQLException;

import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.databases.DatabaseException;
import org.valdi.SuperApiX.common.databases.HikariDataSource;
import org.valdi.SuperApiX.common.databases.IDataStorage;
import org.valdi.SuperApiX.common.databases.StorageType;

import com.zaxxer.hikari.HikariConfig;

public class MariadbDatabase implements IDataStorage {
	private final HikariDataSource hikari;
	private final StoreLoader loader;
	
	public MariadbDatabase(StoreLoader loader, String address, int port, String database, String options,
						   String username, String password, int poolSize, String poolName) throws DatabaseException {
		this.loader = loader;

        String url = "jdbc:mariadb://" + address + ":" + port + "/" + database + options;
        
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("cachePrepStmts", "true");
        
        config.setConnectionTimeout(1000);
        config.setMaximumPoolSize(poolSize);
        config.setThreadFactory(loader.getThreadFactory());
        config.setPoolName(poolName);
        config.setValidationTimeout(1000);
        
        try {
			this.hikari = new HikariDataSource(loader.getLogger(), config);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
	}

	@Override
	public void close() throws DatabaseException {
		try {
			if(hikari != null) {
				hikari.close();
			}
		} catch (Exception e) {
            throw new DatabaseException(e);
		}
	}

	@Override
	public Connection getConnection() throws DatabaseException {
		try {
			return this.hikari.getConnection();
		} catch (SQLException e) {
            throw new DatabaseException(e);
		}
	}

	@Override
	public StorageType getType() {
		return StorageType.MARIADB;
	}

	@Override
	public StoreLoader getStoreLoader() {
		return loader;
	}

}
