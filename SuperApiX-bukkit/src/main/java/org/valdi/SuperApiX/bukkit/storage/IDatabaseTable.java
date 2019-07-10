package org.valdi.SuperApiX.bukkit.storage;

import org.valdi.SuperApiX.bukkit.storage.objects.DataObject;
import org.valdi.SuperApiX.common.databases.DatabaseException;

import java.util.List;

public interface IDatabaseTable<T extends DataObject> {

	void createTable() throws DatabaseException;

	/**
	 * Load objects sync
	 * @return
	 */
	List<T> loadObjects();

	/**
	 * Load objects async
	 * @param task
	 */
	void loadObjects(Callback<List<T>, Void> task);

	/**
	 * Load object sync
	 * @param uniqueId
	 * @return
	 */
	T loadObject(String uniqueId);

	/**
	 * Load object async
	 * @param uniqueId
	 * @param task
	 */
	void loadObject(String uniqueId, Callback<T, Void> task);

	/**
	 * Delete an object sync
	 * @param instance
	 */
	boolean saveObject(T instance);

	void saveObject(T instance, Callback<Boolean, Void> task);

	/**
	 * Delete an object sync
	 * @param instance
	 */
	default boolean deleteObject(T instance) {
		return deleteObject(instance.getUniqueId());
	}

	/**
	 * Delete an object sync
	 * @param uniqueId
	 */
	boolean deleteObject(String uniqueId);

	/**
	 * Delete an object async
	 * @param instance
	 * @param task
	 */
	default void deleteObject(T instance, Callback<Boolean, Void> task) {
		deleteObject(instance.getUniqueId(), task);
	}

	/**
	 * Delete an object async
	 * @param uniqueId
	 * @param task
	 */
	void deleteObject(String uniqueId, Callback<Boolean, Void> task);

	boolean objectExists(String uniqueId);

	void objectExists(String uniqueId, Callback<Boolean, Void> task);

}
