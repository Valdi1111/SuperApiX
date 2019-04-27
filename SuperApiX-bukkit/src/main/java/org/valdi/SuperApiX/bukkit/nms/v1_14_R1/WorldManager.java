package org.valdi.SuperApiX.bukkit.nms.v1_14_R1;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.server.v1_14_R1.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.IWorldManager;
import org.valdi.SuperApiX.bukkit.nms.WorldBuilder;

import java.io.File;
import java.util.Locale;

public class WorldManager extends AbstractNmsProvider implements IWorldManager {

	public WorldManager(SuperApiBukkit plugin) {
		super(plugin);
	}

	@Override
    public World createWorld(WorldBuilder builder) {
        Validate.notNull(builder, "Builder may not be null");

        String name = builder.getName();
    	File folder = builder.getFolder();
        ChunkGenerator generator = builder.getGenerator();
        World world = Bukkit.getWorld(name);
        WorldType type = WorldType.getType(builder.getType().getName());
        boolean generateStructures = builder.getGenerateStructures();

        if (world != null) {
            return world;
        }

        if (folder.exists() && !folder.isDirectory()) {
            throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
        }

        if (generator == null) {
            generator = this.getCraftServer().getGenerator(name);
        }

        this.getConsole().convertWorld(name);

        int dimension = CraftWorld.CUSTOM_DIMENSION_OFFSET + this.getConsole().worldServer.size();
        boolean used = false;
        block0: do {
            for (WorldServer server : this.getConsole().getWorlds()) {
                used = server.dimension.getDimensionID() == dimension;
                if (!used) {
                    continue;
                }

                ++dimension;
                continue block0;
            }
        } while(used);
        boolean hardcore = false;

        WorldNBTStorage sdm = new WorldNBTStorage(folder.getParentFile(), folder.getName(), this.getConsole(), this.getConsole().dataConverterManager);
        WorldData worlddata = sdm.getWorldData();
        WorldSettings worldSettings = null;
        if (worlddata == null) {
            worldSettings = new WorldSettings(builder.getSeed(), EnumGamemode.getById(builder.getGamemode().getValue()), generateStructures, hardcore, type);
            JsonElement parsedSettings = new JsonParser().parse(builder.getGeneratorSettings());
            if (parsedSettings.isJsonObject()) {
                worldSettings.setGeneratorSettings(parsedSettings.getAsJsonObject());
            }
            worlddata = new WorldData(worldSettings, name);
        }
        worlddata.checkName(name); // CraftBukkit - Migration did not rewrite the level.dat; This forces 1.8 to take the last loaded world as respawn (in this case the end)

        DimensionManager actualDimension = DimensionManager.a(builder.getEnvironment().getId());
        DimensionManager internalDimension = new DimensionManager(dimension, name, name, (w, manager) -> actualDimension.getWorldProvider(w), actualDimension.hasSkyLight());
        WorldServer internal = new WorldServer(this.getConsole(), this.getConsole().executorService, sdm, worlddata, internalDimension, this.getConsole().getMethodProfiler(), this.getConsole().worldLoadListenerFactory.create(11), builder.getEnvironment(), generator);

        /*if (!(worlds.containsKey(name.toLowerCase(Locale.ENGLISH)))) {
            return null;
        }*/
        
        if(this.getCraftServer().getWorld(name.toLowerCase(Locale.ENGLISH)) == null) {
        	return null;
        }

        this.getConsole().initWorld(internal, worlddata, worldSettings);
        internal.worldData.setDifficulty(EnumDifficulty.EASY);
        internal.setSpawnFlags(true, true);

        this.getConsole().worldServer.put(internal.dimension, internal);
        Bukkit.getPluginManager().callEvent(new WorldInitEvent(internal.getWorld()));

        this.getConsole().loadSpawn(internal.getChunkProvider().playerChunkMap.worldLoadListener, internal);
        Bukkit.getPluginManager().callEvent(new WorldLoadEvent(internal.getWorld()));

        return internal.getWorld();
    }
    
    private DedicatedServer getConsole() {
    	return this.getCraftServer().getServer();
    }
    
    private CraftServer getCraftServer() {
    	return (CraftServer) Bukkit.getServer();
    }

}
