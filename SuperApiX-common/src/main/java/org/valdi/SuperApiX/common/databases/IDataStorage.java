package org.valdi.SuperApiX.common.databases;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.valdi.SuperApiX.common.ISuperPlugin;
import org.valdi.SuperApiX.common.databases.data.CallbackQuery;
import org.valdi.SuperApiX.common.databases.data.CallbackStatement;
import org.valdi.SuperApiX.common.databases.data.CallbackUpdate;
import org.valdi.SuperApiX.common.databases.data.DataManipulationException;
import org.valdi.SuperApiX.common.databases.data.ExceptionHandler;
import org.valdi.SuperApiX.common.databases.data.SafeStatementRunnable;

public interface IDataStorage {
	
	public Connection getConnection() throws DatabaseException;

	public void close() throws DatabaseException;

    /**
     * Creates a <code>PreparedStatement</code> object for sending
     * parameterized SQL statements to the database.
     * <P>
     * A SQL statement with or without IN parameters can be
     * pre-compiled and stored in a <code>PreparedStatement</code> object. This
     * object can then be used to efficiently execute this statement
     * multiple times.
     *
     * <P><B>Note:</B> This method is optimized for handling
     * parametric SQL statements that benefit from precompilation. If
     * the driver supports precompilation,
     * the method <code>prepareStatement</code> will send
     * the statement to the database for precompilation. Some drivers
     * may not support precompilation. In this case, the statement may
     * not be sent to the database until the <code>PreparedStatement</code>
     * object is executed.  This has no direct effect on users; however, it does
     * affect which methods throw certain <code>SQLException</code> objects.
     * <P>
     * Result sets created using the returned <code>PreparedStatement</code>
     * object will by default be type <code>TYPE_FORWARD_ONLY</code>
     * and have a concurrency level of <code>CONCUR_READ_ONLY</code>.
     * The holdability of the created result sets can be determined by
     * calling {@link #getHoldability}.
     *
     * @param sql an SQL statement that may contain one or more '?' IN
     * parameter placeholders
     * @return a new default <code>PreparedStatement</code> object containing the
     * pre-compiled SQL statementì
	 * @throws DataManipulationException if a database access error occurs
     * or this method is called on a closed connection
	 * @throws DatabaseException if the connection is inaccessible
     */
	public default CallableStatement prepareCallable(String sql) throws DataManipulationException, DatabaseException {
		try {
			return getConnection().prepareCall(sql);
		} catch (SQLException e) {
			throw new DataManipulationException(e);
		}
	}

    /**
     * Creates a <code>CallableStatement</code> object for calling
     * database stored procedures.
     * The <code>CallableStatement</code> object provides
     * methods for setting up its IN and OUT parameters, and
     * methods for executing the call to a stored procedure.
     *
     * <P><B>Note:</B> This method is optimized for handling stored
     * procedure call statements. Some drivers may send the call
     * statement to the database when the method <code>prepareCall</code>
     * is done; others
     * may wait until the <code>CallableStatement</code> object
     * is executed. This has no
     * direct effect on users; however, it does affect which method
     * throws certain SQLExceptions.
     * <P>
     * Result sets created using the returned <code>CallableStatement</code>
     * object will by default be type <code>TYPE_FORWARD_ONLY</code>
     * and have a concurrency level of <code>CONCUR_READ_ONLY</code>.
     * The holdability of the created result sets can be determined by
     * calling {@link #getHoldability}.
     *
     * @param sql an SQL statement that may contain one or more '?'
     * parameter placeholders. Typically this statement is specified using JDBC
     * call escape syntax.
     * @return a new default <code>CallableStatement</code> object containing the
     * pre-compiled SQL statement
	 * @throws DataManipulationException if a database access error occurs
     * or this method is called on a closed connection
	 * @throws DatabaseException if the connection is inaccessible
     */
	public default PreparedStatement prepareStatement(String sql) throws DataManipulationException, DatabaseException {
		try {
			return getConnection().prepareStatement(sql);
		} catch (SQLException e) {
			throw new DataManipulationException(e);
		}
	}
	
	/**
     * Executes the SQL statement in this <code>PreparedStatement</code> object,
     * which may be any kind of SQL statement.
     * Some prepared statements return multiple results; the <code>execute</code>
     * method handles these complex statements as well as the simpler
     * form of statements handled by the methods <code>executeQuery</code>
     * and <code>executeUpdate</code>.
     * <P>
     * The <code>execute</code> method returns a <code>boolean</code> to
     * indicate the form of the first result.  You must call either the method
     * <code>getResultSet</code> or <code>getUpdateCount</code>
     * to retrieve the result; you must call <code>getMoreResults</code> to
     * move to any subsequent result(s).
	 * 
	 * @param statement The statement to execute
	 * @param explenation
	 * @param handler
	 * @param callback
	 */
	public default void executeStatement(ISuperPlugin plugin, PreparedStatement statement, String explenation, ExceptionHandler handler, CallbackStatement callback, boolean async) {
		SafeStatementRunnable safeStatement = new SafeStatementRunnable(plugin, explenation, handler) {

			@Override
			public void executeStatement() throws DataManipulationException, DatabaseException {
				try {
					if(callback != null)
						callback.onStatementDone(statement.execute());
					else
						statement.execute();
					
					statement.close();
				} catch (SQLException e) {
					throw new DataManipulationException(e);
				}
			}
			
		};

		if(async) {
			plugin.getScheduler().executeAsync(safeStatement);
		} else {
			safeStatement.run();
		}
	}
	
	/**
     * Executes the SQL query in this <code>PreparedStatement</code> object
     * and returns the <code>ResultSet</code> object generated by the query.
	 * 
	 * @param statement The statement to execute
	 * @param explenation
	 * @param handler
	 * @param callback
	 */
	public default void executeQuery(ISuperPlugin plugin, PreparedStatement statement, String explenation, ExceptionHandler handler, CallbackQuery callback, boolean async) {
		SafeStatementRunnable safeStatement = new SafeStatementRunnable(plugin, explenation, handler) {

			@Override
			public void executeStatement() throws DataManipulationException, DatabaseException {
				try {
					if(callback != null)
						callback.onQueryDone(statement.executeQuery());
					else
						statement.executeQuery();
					
					statement.close();
				} catch (SQLException e) {
					throw new DataManipulationException(e);
				}
			}
			
		};

		if(async) {
			plugin.getScheduler().executeAsync(safeStatement);
		} else {
			safeStatement.run();
		}
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
	 */
	public default void executeUpdate(ISuperPlugin plugin, PreparedStatement statement, String explenation, ExceptionHandler handler, CallbackUpdate callback, boolean async) {
		SafeStatementRunnable safeStatement = new SafeStatementRunnable(plugin, explenation, handler) {

			@Override
			public void executeStatement() throws DataManipulationException, DatabaseException {
				try {
					if(callback != null)
						callback.onUpdateDone(statement.executeUpdate());
					else
						statement.executeUpdate();
					
					statement.close();
				} catch (SQLException e) {
					throw new DataManipulationException(e);
				}
			}
			
		};

		if(async) {
			plugin.getScheduler().executeAsync(safeStatement);
		} else {
			safeStatement.run();
		}
	}

	public StorageType getType();

}
