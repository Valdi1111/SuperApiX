package org.valdi.SuperApiX.bukkit.nms.v1_13_R2;

import java.io.File;
import java.util.Locale;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.LongIterator;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.AbstractNmsProvider;
import org.valdi.SuperApiX.bukkit.nms.IWorldManager;
import org.valdi.SuperApiX.bukkit.nms.WorldBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.ChunkCoordIntPair;
import net.minecraft.server.v1_13_R2.DimensionManager;
import net.minecraft.server.v1_13_R2.EntityTracker;
import net.minecraft.server.v1_13_R2.EnumDifficulty;
import net.minecraft.server.v1_13_R2.EnumGamemode;
import net.minecraft.server.v1_13_R2.ForcedChunk;
import net.minecraft.server.v1_13_R2.IDataManager;
import net.minecraft.server.v1_13_R2.MinecraftServer;
import net.minecraft.server.v1_13_R2.PersistentCollection;
import net.minecraft.server.v1_13_R2.ServerNBTManager;
import net.minecraft.server.v1_13_R2.WorldData;
import net.minecraft.server.v1_13_R2.WorldServer;
import net.minecraft.server.v1_13_R2.WorldSettings;
import net.minecraft.server.v1_13_R2.WorldType;

public class WorldManager extends AbstractNmsProvider implements IWorldManager {

	public WorldManager(SuperApiBukkit plugin) {
		super(plugin);
	}

    @SuppressWarnings({ "resource", "deprecation" })
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
        do {
            for (WorldServer server : this.getConsole().getWorlds()) {
                used = server.dimension.getDimensionID() == dimension;
                if (used) {
                    dimension++;
                    break;
                }
            }
        } while(used);
        boolean hardcore = false;

        IDataManager sdm = new ServerNBTManager(folder.getParentFile(), folder.getName(), this.getConsole(), this.getConsole().dataConverterManager);
        PersistentCollection persistentcollection = new PersistentCollection(sdm);
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

        DimensionManager internalDimension = new DimensionManager(dimension, name, name, () -> DimensionManager.a(builder.getEnvironment().getId()).e());
        WorldServer internal = (WorldServer) new WorldServer(this.getConsole(), sdm, persistentcollection, worlddata, internalDimension, this.getConsole().methodProfiler, builder.getEnvironment(), generator).i_();

        /*if (!(worlds.containsKey(name.toLowerCase(Locale.ENGLISH)))) {
            return null;
        }*/
        
        if(this.getCraftServer().getWorld(name.toLowerCase(Locale.ENGLISH)) == null) {
        	return null;
        }

        if (worldSettings != null) {
            internal.a(worldSettings);
        }

        internal.tracker = new EntityTracker(internal);
        internal.addIWorldAccess(new net.minecraft.server.v1_13_R2.WorldManager(this.getConsole(), internal));
        internal.worldData.setDifficulty(EnumDifficulty.getById(builder.getDifficulty().getValue()));
        internal.setSpawnFlags(true, true);
        this.getConsole().worldServer.put(internal.dimension, internal);

        Bukkit.getPluginManager().callEvent(new WorldInitEvent(internal.getWorld()));
        System.out.println("Preparing start region for level " + (this.getConsole().worldServer.size() - 1) + " (Seed: " + internal.getSeed() + ")");

        if (internal.getWorld().getKeepSpawnInMemory()) {
            int short1 = 196;
            long i2 = System.currentTimeMillis();
            int j2 = - short1;
            while (j2 <= short1) {
                int k = - short1;
                while (k <= short1) {
                    long l = System.currentTimeMillis();
                    if (l < i2) {
                        i2 = l;
                    }
                    if (l > i2 + 1000L) {
                        int i1 = (short1 * 2 + 1) * (short1 * 2 + 1);
                        int j1 = (j2 + short1) * (short1 * 2 + 1) + k + 1;
                        System.out.println("Preparing spawn area for " + name + ", " + j1 * 100 / i1 + "%");
                        i2 = l;
                    }
                    BlockPosition chunkcoordinates = internal.getSpawn();
                    internal.getChunkProvider().getChunkAt(chunkcoordinates.getX() + j2 >> 4, chunkcoordinates.getZ() + k >> 4, true, true);
                    k += 16;
                }
                j2 += 16;
            }
        }

        DimensionManager dimensionmanager = internalDimension.e().getDimensionManager();
        ForcedChunk forcedchunk = (ForcedChunk) persistentcollection.get(dimensionmanager, ForcedChunk::new, "chunks");

        if (forcedchunk != null) {
            LongIterator longiterator = forcedchunk.a().iterator();
            while (longiterator.hasNext()) {
                System.out.println("Loading forced chunks for dimension " + dimension + ", " + forcedchunk.a().size() * 100 / 625 + "%");
                long k = longiterator.nextLong();
                ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(k);
                internal.getChunkProvider().getChunkAt(chunkcoordintpair.x, chunkcoordintpair.z, true, true);
            }
        }

        Bukkit.getPluginManager().callEvent(new WorldLoadEvent(internal.getWorld()));
        return internal.getWorld();
    }
    
    private MinecraftServer getConsole() {
    	return this.getCraftServer().getServer();
    }
    
    private CraftServer getCraftServer() {
    	return (CraftServer) Bukkit.getServer();
    }

}
