package org.valdi.SuperApiX.common.databases.advanced;

import org.valdi.SuperApiX.common.databases.DatabaseException;
import org.valdi.SuperApiX.common.databases.IDataStorage;
import org.valdi.SuperApiX.common.databases.advanced.adapters.ColumnAdapter;
import org.valdi.SuperApiX.common.databases.advanced.column.ColumnLabel;
import org.valdi.SuperApiX.common.databases.advanced.statement.StatementIndex;
import org.valdi.SuperApiX.common.databases.advanced.query.Query;
import org.valdi.SuperApiX.common.databases.advanced.query.QueryType;
import org.valdi.SuperApiX.common.databases.data.DataManipulationException;
import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.utils.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TableLoader<Data> {
    protected final StoreLoader loader;
    protected final Class<Data> data;

    protected final IDataStorage storage;
    protected List<Field> dataFields;

    /**
     * Constructor
     * @param loader a loader instance (plugin)
     * @param data class to store
     */
    public TableLoader(StoreLoader loader, Class<Data> data, IDataStorage storage) {
        this.loader = loader;
        this.data = data;

        this.storage = storage;
        this.dataFields = ReflectionUtils.getAllFields(data);
    }

    private String getQuery(QueryType type) {
        return Stream.of(data.getAnnotationsByType(Query.class))
                .filter(q -> q.type().equals(type))
                .findFirst()
                .map(Query::value)
                .map(s -> s.replace(QueryType.TABLE, data.getSimpleName()))
                .orElse(null);
    }

    private Data process(ResultSet result, QueryType type) {
        Data instance;

        // Create a new instance of the data of type Data (which can be any class)
        try {
            instance = data.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            loader.getLogger().severe("Failed to create a new instance for class " + data.getName(), e);
            return null;
        }

        // Run through all the fields in the object
        for (Field field : dataFields) {
            try {
                // Gets the getter and setters for this field using the JavaBeans system
                PropertyDescriptor property = new PropertyDescriptor(field.getName(), data);
                // Get the write method
                Method method = property.getWriteMethod();

                // Check if there is a ColumnLabel annotation on the field
                Stream.of(field.getAnnotationsByType(ColumnLabel.class))
                        .filter(l -> l.type().equals(type))
                        .findFirst()
                        .ifPresent(l -> {
                    try {
                        Class<? extends ColumnAdapter> adapterClazz = l.adapter();
                        ColumnAdapter adapter = adapterClazz.getDeclaredConstructor().newInstance();
                        method.invoke(instance, adapter.getValue(result, l.value()));
                    } catch (Exception e) {
                        loader.getLogger().severe("Error on database object reading...", e);
                    }
                });
            } catch (Exception e) {
                loader.getLogger().severe("Error on database object reading...", e);
            }
        }
        return instance;
    }

    private void process(PreparedStatement statement, QueryType type, Data instance) {
        // Run through all the fields in the object
        for (Field field : dataFields) {
            try {
                // Gets the getter and setters for this field using the JavaBeans system
                PropertyDescriptor property = new PropertyDescriptor(field.getName(), data);
                // Get the read method
                Method method = property.getReadMethod();
                // Invoke the read method to get the value. We have no idea what type of value it is.
                Object value = method.invoke(instance);

                // Check if there is a ColumnLabel annotation on the field
                Stream.of(field.getAnnotationsByType(StatementIndex.class))
                        .filter(l -> l.type().equals(type))
                        .findFirst()
                        .ifPresent(l -> {
                    try {
                        Class<? extends ColumnAdapter> adapterClazz = l.adapter();
                        ColumnAdapter adapter = adapterClazz.getDeclaredConstructor().newInstance();
                        adapter.setValue(statement, l.value(), value);
                    } catch (Exception e) {
                        loader.getLogger().severe("Error on database object writing...", e);
                    }
                });
            } catch (Exception e) {
                loader.getLogger().severe("Error on database object writing...", e);
            }
        }
    }

    private void process(PreparedStatement statement, QueryType type, Object... values) {
        // Run through all the fields in the object
        for (Field field : dataFields) {
            // Check if there is a ColumnLabel annotation on the field
            Stream.of(field.getAnnotationsByType(StatementIndex.class))
                    .filter(l -> l.type().equals(type))
                    .findFirst()
                    .ifPresent(l -> {
                try {
                    Class<? extends ColumnAdapter> adapterClazz = l.adapter();
                    ColumnAdapter adapter = adapterClazz.getDeclaredConstructor().newInstance();
                    adapter.setValue(statement, l.value(), values[l.value() - 1]);
                } catch (Exception e) {
                    loader.getLogger().severe("Error on database object writing...", e);
                }
            });
        }
    }

    /**
     * Create table.
     */
    public void create() {
        String query = this.getQuery(QueryType.CREATE);
        if (query == null) {
            loader.getLogger().info("Ignoring table creation for " + data.getName() + ": Query annotation not found!");
            return;
        }

        if (query.isEmpty()) {
            loader.getLogger().info("Ignoring table creation for " + data.getName() + ": Query is empty!");
            return;
        }

        try (Connection conn = storage.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.execute();
        } catch (Exception e) {
            loader.getLogger().severe("Could not create database table: " + query, e);
        }
    }

    /**
     * Load all elements.
     * @return all objects
     */
    public List<Data> load() {
        String query = this.getQuery(QueryType.LOAD_ALL);
        if (query == null) {
            loader.getLogger().info("Ignoring data loading for " + data.getName() + ": Query annotation not found!");
            return null;
        }

        if (query.isEmpty()) {
            loader.getLogger().info("Ignoring data loading for " + data.getName() + ": Query is empty!");
            return null;
        }

        try (Connection conn = getDataStorage().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            List<Data> objects = new ArrayList<>();
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                objects.add(this.process(result, QueryType.LOAD_ALL));
            }
            return objects;
        } catch (DatabaseException | SQLException e) {
            loader.getLogger().severe("Failed to load objects from the database...", e);
            return null;
        }
    }

    /**
     * Load all elements async.
     * @param task a task that will be executed when the load is done
     */
    public void load(Callback<List<Data>> task) {
        storage.executeQuery(conn -> {
            String query = this.getQuery(QueryType.LOAD_ALL);
            if (query == null) {
                loader.getLogger().info("Ignoring data loading for " + data.getName() + ": Query annotation not found!");
                return null;
            }

            if (query.isEmpty()) {
                loader.getLogger().info("Ignoring data loading for " + data.getName() + ": Query is empty!");
                return null;
            }

            return conn.prepareStatement(query);
        }, "loading objects from database", e -> {
            loader.getLogger().severe("Failed to load objects from the database...", e);
        }, result -> {
            if (task == null) {
                return;
            }

            try {
                List<Data> objects = new ArrayList<>();
                while (result.next()) {
                    objects.add(this.process(result, QueryType.LOAD_ALL));
                }
                task.call(objects);
            } catch (SQLException e) {
                throw new DataManipulationException(e);
            }
        }, true);
    }

    /**
     * Load an element.
     * @param uniqueId the element's unique id
     * @return an object
     */
    public Data load(Object... uniqueId) {
        String query = this.getQuery(QueryType.LOAD);
        if (query == null) {
            loader.getLogger().info("Ignoring data loading for " + data.getName() + ": Query annotation not found!");
            return null;
        }

        if (query.isEmpty()) {
            loader.getLogger().info("Ignoring data loading for " + data.getName() + ": Query is empty!");
            return null;
        }

        try (Connection conn = getDataStorage().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            this.process(statement, QueryType.LOAD, uniqueId);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return this.process(result, QueryType.LOAD);
            }
            return null;
        } catch (DatabaseException | SQLException e) {
            loader.getLogger().severe("Failed to load an object from the database...", e);
            return null;
        }
    }

    /**
     * Load an element async.
     * @param task a task that will be executed when the load is done
     * @param uniqueId the element's unique id
     */
    public void load(Callback<Data> task, Object... uniqueId) {
        storage.executeQuery(conn -> {
            String query = this.getQuery(QueryType.LOAD);
            if (query == null) {
                loader.getLogger().info("Ignoring data loading for " + data.getName() + ": Query annotation not found!");
                return null;
            }

            if (query.isEmpty()) {
                loader.getLogger().info("Ignoring data loading for " + data.getName() + ": Query is empty!");
                return null;
            }

            PreparedStatement statement = conn.prepareStatement(query);
            this.process(statement, QueryType.LOAD, uniqueId);
            return statement;
        }, "loading object from database", e -> {
            loader.getLogger().severe("Failed to load an object from the database...", e);
        }, result -> {
            if (task == null) {
                return;
            }

            try {
                if (result.next()) {
                    task.call(this.process(result, QueryType.LOAD));
                    return;
                }
                task.call(null);
            } catch (SQLException e) {
                throw new DataManipulationException(e);
            }
        }, true);
    }

    /**
     * Save an object in the database.
     * @param instance the object
     * @return {@code false} if failed, {@code true} otherwise
     */
    public boolean save(Data instance) {
        String query = this.getQuery(QueryType.SAVE);
        if (query == null) {
            loader.getLogger().info("Ignoring data saving for " + data.getName() + ": Query annotation not found!");
            return false;
        }

        if (query.isEmpty()) {
            loader.getLogger().info("Ignoring data saving for " + data.getName() + ": Query is empty!");
            return false;
        }

        try (Connection conn = getDataStorage().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            this.process(statement, QueryType.SAVE, instance);

            statement.execute();
            return true;
        } catch (DatabaseException | SQLException e) {
            loader.getLogger().severe("Failed to save an object in the database...", e);
            return false;
        }
    }

    /**
     * Save an object in the database async.
     * @param task a task that will be executed when the save is done
     * @param instance the object
     */
    public void save(Callback<Boolean> task, Data instance) {
        getDataStorage().executeStatement(conn -> {
            String query = this.getQuery(QueryType.SAVE);
            if (query == null) {
                loader.getLogger().info("Ignoring data saving for " + data.getName() + ": Query annotation not found!");
                return null;
            }

            if (query.isEmpty()) {
                loader.getLogger().info("Ignoring data saving for " + data.getName() + ": Query is empty!");
                return null;
            }

            PreparedStatement statement = conn.prepareStatement(query);
            this.process(statement, QueryType.SAVE, instance);
            return statement;
        }, "saving an object in database... ", e -> {
            loader.getLogger().severe("Failed to save an object in the database...", e);
        }, result -> {
            if (task == null) {
                return;
            }

            task.call(result);
        }, true);
    }

    /**
     * Delete an element from the database.
     * @param uniqueId the element's unique id
     * @return {@code false} if failed, {@code true} otherwise
     */
    public boolean delete(Object... uniqueId) {
        String query = this.getQuery(QueryType.DELETE);
        if (query == null) {
            loader.getLogger().info("Ignoring data deleting for " + data.getName() + ": Query annotation not found!");
            return false;
        }

        if (query.isEmpty()) {
            loader.getLogger().info("Ignoring data deleting for " + data.getName() + ": Query is empty!");
            return false;
        }

        try (Connection conn = getDataStorage().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            this.process(statement, QueryType.SAVE, uniqueId);

            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (DatabaseException | SQLException e) {
            loader.getLogger().severe("Failed to delete an object from the database...", e);
            return false;
        }
    }

    /**
     * Delete an element from the database async.
     * @param task a task that will be executed when the deletion is done
     * @param uniqueId the element's unique id
     */
    public void delete(Callback<Boolean> task, Object... uniqueId) {
        getDataStorage().executeStatement(conn -> {
            String query = this.getQuery(QueryType.DELETE);
            if (query == null) {
                loader.getLogger().info("Ignoring data deleting for " + data.getName() + ": Query annotation not found!");
                return null;
            }

            if (query.isEmpty()) {
                loader.getLogger().info("Ignoring data deleting for " + data.getName() + ": Query is empty!");
                return null;
            }

            PreparedStatement statement = conn.prepareStatement(query);
            this.process(statement, QueryType.SAVE, uniqueId);
            return statement;
        }, "removing object from database... ", e -> {
            loader.getLogger().severe("Failed to delete an object from the database...", e);
        }, result -> {
            if (task == null) {
                return;
            }

            task.call(result);
        }, true);
    }

    /**
     * Check if an element exists in the database.
     * @param uniqueId the element's unique id
     * @return {@code true} if exists, {@code false} otherwise
     */
    public boolean exists(Object... uniqueId) {
        String query = this.getQuery(QueryType.EXISTS);
        if (query == null) {
            loader.getLogger().info("Ignoring data checking for " + data.getName() + ": Query annotation not found!");
            return false;
        }

        if (query.isEmpty()) {
            loader.getLogger().info("Ignoring data checking for " + data.getName() + ": Query is empty!");
            return false;
        }

        try (Connection conn = getDataStorage().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            this.process(statement, QueryType.EXISTS, uniqueId);

            ResultSet result = statement.executeQuery();
            return result.next();
        } catch (DatabaseException | SQLException e) {
            loader.getLogger().severe("Failed check if an object exists in the database...", e);
            return false;
        }
    }

    /**
     * Check if an element exists in the database async.
     * @param task a task that will be executed when the check is performed
     * @param uniqueId the element's unique id
     */
    public void exists(Callback<Boolean> task, Object... uniqueId) {
        getDataStorage().executeQuery(conn -> {
            String query = this.getQuery(QueryType.EXISTS);
            if (query == null) {
                loader.getLogger().info("Ignoring data checking for " + data.getName() + ": Query annotation not found!");
                return null;
            }

            if (query.isEmpty()) {
                loader.getLogger().info("Ignoring data checking for " + data.getName() + ": Query is empty!");
                return null;
            }

            PreparedStatement statement = conn.prepareStatement(query);
            this.process(statement, QueryType.EXISTS, uniqueId);
            return statement;
        }, "checking if object exists in database", e -> {
            loader.getLogger().severe("Failed check if an object exists in the database...", e);
        }, result -> {
            if (task == null) {
                return;
            }

            try {
                task.call(result.next());
            } catch (SQLException e) {
                throw new DataManipulationException(e);
            }
        }, true);
    }

    public StoreLoader getStoreLoader() {
        return loader;
    }

    /**
     * Get the table's database instance.
     * @return the database
     */
    public IDataStorage getDataStorage() {
        return storage;
    }

    public Class<Data> getClazz() {
        return data;
    }
}
