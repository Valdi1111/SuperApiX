package org.valdi.SuperApiX.common.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.databases.data.CallbackQuery;
import org.valdi.SuperApiX.common.databases.data.CallbackStatement;
import org.valdi.SuperApiX.common.databases.data.CallbackUpdate;
import org.valdi.SuperApiX.common.databases.data.DataManipulationException;
import org.valdi.SuperApiX.common.databases.data.ExceptionHandler;
import org.valdi.SuperApiX.common.databases.data.SafeStatementRunnable;

public interface IDataStorage {

	/**
	 * Get the database connection
	 * @return the database connection
	 * @throws DatabaseException if a database access error occurs
	 */
	Connection getConnection() throws DatabaseException;

	/**
	 * Close the database connection
	 * @throws DatabaseException if the connection throws an Exception
	 */
	void close() throws DatabaseException;

	public interface StatementCreator {
		PreparedStatement create(Connection connection) throws SQLException;
	}

	default void executeStatement(StatementCreator creator, String explenation, ExceptionHandler handler, CallbackStatement callback, boolean async) {
		new SafeStatementRunnable(getStoreLoader(), explenation, handler) {

			@Override
			public void executeStatement() throws DataManipulationException, DatabaseException {
				try(Connection conn = getConnection();
					PreparedStatement statement = creator.create(conn)) {
					if(callback != null)
						callback.onStatementDone(statement.execute());
					else
						statement.execute();
				} catch (SQLException e) {
					throw new DatabaseException(e);
				} catch (Exception e) {
					throw new DataManipulationException(e);
				}
			}

		}.execute(async);
	}
	
	/**
     * Executes the SQL query in this <code>PreparedStatement</code> object
     * and returns the <code>ResultSet</code> object generated by the query.
	 * 
	 * @param statement The statement to execute
	 * @param explenation
	 * @param handler
	 * @param callback
	 * @param async
	 */
	/*default void executeQuery(PreparedStatement statement, String explenation, ExceptionHandler handler, CallbackQuery callback, boolean async) {
		new SafeStatementRunnable(getStoreLoader(), explenation, handler) {

			@Override
			public void executeStatement() throws DataManipulationException, DatabaseException {
				try {
					if(callback != null)
						callback.onQueryDone(statement.executeQuery());
					else
						statement.executeQuery();
					
					statement.close();
				} catch (SQLException e) {
					throw new DatabaseException(e);
				} catch (Exception e) {
					throw new DataManipulationException(e);
				}
			}
			
		}.execute(async);
	}*/

	default void executeQuery(StatementCreator creator, String explenation, ExceptionHandler handler, CallbackQuery callback, boolean async) {
		new SafeStatementRunnable(getStoreLoader(), explenation, handler) {

			@Override
			public void executeStatement() throws DataManipulationException, DatabaseException {
				try(Connection conn = getConnection();
					PreparedStatement statement = creator.create(conn)) {
					if(callback != null)
						callback.onQueryDone(statement.executeQuery());
					else
						statement.executeQuery();
				} catch (SQLException e) {
					throw new DatabaseException(e);
				} catch (Exception e) {
					throw new DataManipulationException(e);
				}
			}

		}.execute(async);
	}
	
	/**
     * Executes the SQL statement in this <code>PreparedStatement</code> object,
     * which must be an SQL Data Manipulation Language (DML) statement, such as <code>INSERT</code>, <code>UPDATE</code> or
     * <code>DELETE</code>; or an SQL statement that returns nothing,
     * such as a DDL statement.
	 * 
	 * @param statement The statement to execute
	 * @param explenation
	 * @param handler
	 * @param callback
	 * @param async
	 */
	/*default void executeUpdate(PreparedStatement statement, String explenation, ExceptionHandler handler, CallbackUpdate callback, boolean async) {
		new SafeStatementRunnable(getStoreLoader(), explenation, handler) {

			@Override
			public void executeStatement() throws DataManipulationException, DatabaseException {
				try {
					if(callback != null)
						callback.onUpdateDone(statement.executeUpdate());
					else
						statement.executeUpdate();
					
					statement.close();
				} catch (SQLException e) {
					throw new DatabaseException(e);
				} catch (Exception e) {
					throw new DataManipulationException(e);
				}
			}
			
		}.execute(async);
	}*/

	default void executeUpdate(StatementCreator creator, String explenation, ExceptionHandler handler, CallbackUpdate callback, boolean async) {
		new SafeStatementRunnable(getStoreLoader(), explenation, handler) {

			@Override
			public void executeStatement() throws DataManipulationException, DatabaseException {
				try(Connection conn = getConnection();
					PreparedStatement statement = creator.create(conn)) {
					if(callback != null)
						callback.onUpdateDone(statement.executeUpdate());
					else
						statement.executeUpdate();
				} catch (SQLException e) {
					throw new DatabaseException(e);
				} catch (Exception e) {
					throw new DataManipulationException(e);
				}
			}

		}.execute(async);
	}

	/**
	 * Get the StorageType for this database
	 * @return the storage type
	 */
	StorageType getType();

	/**
	 * Get the store loader associated with this database
	 * @return the storage loader
	 */
	StoreLoader getStoreLoader();

}
