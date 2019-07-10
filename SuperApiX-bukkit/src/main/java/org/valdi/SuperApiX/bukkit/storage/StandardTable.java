package org.valdi.SuperApiX.bukkit.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.valdi.SuperApiX.bukkit.gson.adapters.*;
import org.valdi.SuperApiX.bukkit.storage.objects.DataObject;
import org.valdi.SuperApiX.common.plugin.StoreLoader;
import org.valdi.SuperApiX.common.databases.IDataStorage;
import org.valdi.SuperApiX.common.gson.adapters.UUIDAdapter;

import java.lang.reflect.Type;
import java.util.UUID;

public abstract class StandardTable<T extends DataObject> implements IDatabaseTable<T> {
	private final StoreLoader loader;
	private final IDataStorage database;

	private GsonBuilder builder;
	
	protected StandardTable(StoreLoader loader, IDataStorage database) {
		this.loader = loader;
		this.database = database;

		initBuilder();
	}
	
	public StoreLoader getStoreLoader() {
		return this.loader;
	}

    public IDataStorage getDataStorage() {
        return database;
    }

    protected void initBuilder() {
        this.builder = new GsonBuilder();
        // Every field to be stored should use @Expose
        builder.excludeFieldsWithoutExposeAnnotation();
        // Forces GSON to use TypeAdapters even for Map keys
        builder.enableComplexMapKeySerialization();
        // Register default adapters
        registerTypeAdapter(UUID.class, new UUIDAdapter());
        registerTypeAdapter(World.class, new WorldAdapter());
        registerTypeAdapter(Vector.class, new VectorAdapter()) ;
        registerTypeAdapter(Location.class, new LocationAdapter()) ;
        registerTypeAdapter(PotionEffectType.class, new PotionEffectTypeAdapter());
        registerTypeAdapter(ItemStack.class, new ItemStackAdapter());
        // Keep null in the database
        builder.serializeNulls();
        // Allow characters like < or > without escaping them
        builder.disableHtmlEscaping();
    }

    protected void registerTypeAdapter(Type type, Object typeAdapter) {
        builder.registerTypeAdapter(type, typeAdapter);
    }
    
    protected GsonBuilder getBuilder() {
        return builder;
    }

    // Gets the GSON builder
    protected Gson getGson() {
        return builder.create();
    }

}
