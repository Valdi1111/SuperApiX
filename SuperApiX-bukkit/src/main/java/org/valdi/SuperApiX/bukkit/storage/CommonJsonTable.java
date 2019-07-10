package org.valdi.SuperApiX.bukkit.storage;

import org.valdi.SuperApiX.bukkit.storage.objects.DataObject;
import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.databases.DatabaseException;
import org.valdi.SuperApiX.common.databases.IDataStorage;
import org.valdi.SuperApiX.common.databases.data.DataManipulationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommonJsonTable<T extends DataObject> extends StandardTable<T> {
	protected static final String CREATE = "CREATE TABLE IF NOT EXISTS [table] (UniqueId VARCHAR(36) NOT NULL PRIMARY KEY, Json LONGTEXT)";
	protected static final String LOAD = "SELECT Json FROM [table]";
	protected static final String LOAD_ONE = "SELECT Json FROM [table] WHERE UniqueId = ?";
	protected static final String SAVE = "INSERT INTO [table] (UniqueId, Json) VALUES (?, ?) ON DUPLICATE KEY UPDATE Json = ?";
	protected static final String DELETE = "DELETE FROM [table] WHERE UniqueId = ?";
	protected static final String EXISTS = "SELECT * FROM [table] WHERE UniqueId = ?";
	
	protected static final String TABLE = "[table]";
	
	private Class<T> dataObject;

	public CommonJsonTable(StoreLoader plugin, Class<T> clazz, IDataStorage database) {
		super(plugin, database);

		this.dataObject = clazz;
	}

	protected String getQuery(String query) {
		return query.replace(TABLE, dataObject.getSimpleName());
	}

	@Override
	public void createTable() throws DatabaseException {
		String query = getQuery(CREATE);
		try(Connection conn = getDataStorage().getConnection();
			PreparedStatement statement = conn.prepareStatement(query)) {
			statement.execute();
        } catch(Exception e) {
            throw new DatabaseException(e);
        }
	}

	@Override
    public List<T> loadObjects() {
		String query = getQuery(LOAD);
		try(Connection conn = getDataStorage().getConnection();
			PreparedStatement statement = conn.prepareStatement(query)) {
			ResultSet result = statement.executeQuery();
			List<T> objects = new ArrayList<>();
			while(result.next()) {
				objects.add(this.getGson().fromJson(result.getString("Json"), dataObject));
			}
			return objects;
		} catch (DatabaseException | SQLException e) {
			getStoreLoader().getLogger().severe("Failed load objects from the database...");
			getStoreLoader().getLogger().severe("Error: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
    }

	@Override
	public void loadObjects(Callback<List<T>, Void> task) {
		getDataStorage().executeQuery(conn -> {
			String query = getQuery(LOAD);
			return conn.prepareStatement(query);
		}, "loading objects from database", e -> {
			getStoreLoader().getLogger().severe("Failed to load objects from the database...");
			getStoreLoader().getLogger().severe("Error: " + e.getMessage());
			e.printStackTrace();
		}, result -> {
			try {
				List<T> objects = new ArrayList<>();
				while(result.next()) {
					objects.add(this.getGson().fromJson(result.getString("Json"), dataObject));
				}
				if(task == null) {
					return;
				}
				task.call(objects);
			} catch (SQLException e) {
				throw new DataManipulationException(e);
			}
		}, true);
	}

	@Override
    public T loadObject(String uniqueId) {
		String query = getQuery(LOAD_ONE);
		try(Connection conn = getDataStorage().getConnection();
			PreparedStatement statement = conn.prepareStatement(query)) {
			statement.setString(1, uniqueId);

			ResultSet result = statement.executeQuery();
			if(result.next()) {
				return this.getGson().fromJson(result.getString("Json"), dataObject);
			}
			return null;
		} catch (DatabaseException | SQLException e) {
			getStoreLoader().getLogger().severe("Failed load an object from the database...");
			getStoreLoader().getLogger().severe("Error: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
    }

	@Override
	public void loadObject(String uniqueId, Callback<T, Void> task) {
		getDataStorage().executeQuery(conn -> {
			String query = getQuery(LOAD_ONE);
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, uniqueId);
			return statement;
		}, "loading object from database", e -> {
			getStoreLoader().getLogger().severe("Failed to load an object from the database...");
			getStoreLoader().getLogger().severe("Error: " + e.getMessage());
			e.printStackTrace();
		}, result -> {
			try {
				if(task == null) {
					return;
				}

				if(result.next()) {
					task.call(this.getGson().fromJson(result.getString("Json"), dataObject));
					return;
				}
				task.call(null);
			} catch (SQLException e) {
				throw new DataManipulationException(e);
			}
		}, true);
	}

	@Override
    public boolean saveObject(T instance) {
        String uniqueId = instance.getUniqueId();
        if(uniqueId == null) {
        	return false;
        }

		String query = getQuery(SAVE);
		try(Connection conn = getDataStorage().getConnection();
			PreparedStatement statement = conn.prepareStatement(query)) {
			String toStore = this.getGson().toJson(instance);
			statement.setString(1, uniqueId);
			statement.setString(2, toStore);
			statement.setString(3, toStore);

			statement.execute();
			return true;
		} catch (DatabaseException | SQLException e) {
			getStoreLoader().getLogger().severe("Failed to save an object in the database...");
			getStoreLoader().getLogger().severe("Error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
    }

	@Override
    public void saveObject(T instance, Callback<Boolean, Void> task) {
		getDataStorage().executeStatement(conn -> {
			String toStore = this.getGson().toJson(instance);
			String query = getQuery(SAVE);
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, instance.getUniqueId());
			statement.setString(2, toStore);
			statement.setString(3, toStore);
			return statement;
		}, "saving an object in database... ", e -> {
			getStoreLoader().getLogger().severe("Failed to save an object in the database...");
			getStoreLoader().getLogger().severe("Error: " + e.getMessage());
			e.printStackTrace();
		}, result -> {
			if(task == null) {
				return;
			}
			task.call(result);
		}, true);
    }

	@Override
    public boolean deleteObject(String uniqueId) {
		String query = getQuery(DELETE);
		try(Connection conn = getDataStorage().getConnection();
			PreparedStatement statement = conn.prepareStatement(query)) {
			statement.setString(1, uniqueId);

			ResultSet result = statement.executeQuery();
			return result.next();
		} catch (DatabaseException | SQLException e) {
			getStoreLoader().getLogger().severe("Failed to remove an object from the database...");
			getStoreLoader().getLogger().severe("Error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
    }

	@Override
	public void deleteObject(String uniqueId, Callback<Boolean, Void> task) {
		getDataStorage().executeStatement(conn -> {
			String query = getQuery(DELETE);
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, uniqueId);
			return statement;
		}, "removing object from database... ", e -> {
			getStoreLoader().getLogger().severe("Failed to remove an object from the database...");
			getStoreLoader().getLogger().severe("Error: " + e.getMessage());
			e.printStackTrace();
		}, result -> {
			if(task == null) {
				return;
			}
			task.call(result);
		}, true);
    }

	@Override
    public boolean objectExists(String uniqueId) {
		String query = getQuery(EXISTS);
		try(Connection conn = getDataStorage().getConnection();
			PreparedStatement statement = conn.prepareStatement(query)) {
			statement.setString(1, uniqueId);

			ResultSet result = statement.executeQuery();
			return result.next();
		} catch (DatabaseException | SQLException e) {
			getStoreLoader().getLogger().severe("Failed check if an object exists in the database...");
			getStoreLoader().getLogger().severe("Error: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
    }

	@Override
	public void objectExists(String uniqueId, Callback<Boolean, Void> task) {
		getDataStorage().executeQuery(conn -> {
			String query = getQuery(EXISTS);
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, uniqueId);
			return statement;
		}, "checking if object exists in database", e -> {
			getStoreLoader().getLogger().severe("Failed to check if an Object exists in the database...");
			getStoreLoader().getLogger().severe("Error: " + e.getMessage());
			e.printStackTrace();
		}, result -> {
			try {
				if(task == null) {
					return;
				}
				task.call(result.next());
			} catch (SQLException e) {
				throw new DataManipulationException(e);
			}
		}, true);
	}

}
