package org.valdi.SuperApiX.bukkit.nms;

import java.io.File;
import java.util.Optional;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.nms.base.IWorldManager;

public class WorldBuilder {
    private final String name;
    private long seed;
    private World.Environment environment;
    private GameMode gamemode;
    private Difficulty difficulty;
    private ChunkGenerator generator;
    private WorldType type;
    private boolean generateStructures;
    private String generatorSettings;
    private File folder;

    /**
     * Creates an empty WorldCreationOptions for the given world name
     *
     * @param name Name of the world that will be created
     */
    public WorldBuilder(String name) {
        if (name == null) {
            throw new IllegalArgumentException("World name cannot be null");
        }

        this.name = name;
        this.seed = (new Random()).nextLong();
        this.environment = World.Environment.NORMAL;
        this.generator = null;
        this.gamemode = Bukkit.getDefaultGameMode();
        this.difficulty = Difficulty.EASY;
        this.type = WorldType.NORMAL;
        this.generateStructures = true;
        this.generatorSettings = "";
        this.folder = new File(Bukkit.getWorldContainer(), name);
    }

    /**
     * Copies the options from the specified world
     *
     * @param world World to copy options from
     * @return This object, for chaining
     */
    public WorldBuilder copy(World world) {
        if (world == null) {
            throw new IllegalArgumentException("World cannot be null");
        }

        seed = world.getSeed();
        environment = world.getEnvironment();
        generator = world.getGenerator();
        gamemode = Bukkit.getDefaultGameMode();
        difficulty = world.getDifficulty();
        type = world.getWorldType();
        generateStructures = world.canGenerateStructures();

        return this;
    }

    /**
     * Copies the options from the specified {@link WorldCreator}
     *
     * @param creator World creator to copy options from
     * @return This object, for chaining
     */
    public WorldBuilder copy(WorldCreator creator) {
        if (creator == null) {
            throw new IllegalArgumentException("Creator cannot be null");
        }

        seed = creator.seed();
        environment = creator.environment();
        generator = creator.generator();
        gamemode = Bukkit.getDefaultGameMode();
        difficulty = Difficulty.EASY;
        type = creator.type();
        generateStructures = creator.generateStructures();
        generatorSettings = creator.generatorSettings();

        return this;
    }

    /**
     * Gets the name of the world that is to be loaded or created.
     *
     * @return World name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the seed that will be used to create this world
     *
     * @return World seed
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Sets the seed that will be used to create this world
     *
     * @param seed World seed
     * @return This object, for chaining
     */
    public WorldBuilder setSeed(long seed) {
        this.seed = seed;

        return this;
    }

    /**
     * Gets the environment that will be used to create or load the world
     *
     * @return World environment
     */
    public World.Environment getEnvironment() {
        return environment;
    }

    /**
     * Sets the environment that will be used to create or load the world
     *
     * @param env World environment
     * @return This object, for chaining
     */
    public WorldBuilder setEnvironment(World.Environment env) {
        this.environment = env;

        return this;
    }

    /**
     * Gets the type of the world that will be created or loaded
     *
     * @return World type
     */
    public WorldType getType() {
        return type;
    }

    /**
     * Sets the type of the world that will be created or loaded
     *
     * @param type World type
     * @return This object, for chaining
     */
    public WorldBuilder setType(WorldType type) {
        this.type = type;

        return this;
    }

    /**
     * Gets the generator that will be used to create or load the world.
     * <p>
     * This may be null, in which case the "natural" generator for this
     * environment will be used.
     *
     * @return Chunk generator
     */
    public ChunkGenerator getGenerator() {
        return generator;
    }

    /**
     * Sets the generator that will be used to create or load the world.
     * <p>
     * This may be null, in which case the "natural" generator for this
     * environment will be used.
     *
     * @param generator Chunk generator
     * @return This object, for chaining
     */
    public WorldBuilder setGenerator(ChunkGenerator generator) {
        this.generator = generator;

        return this;
    }

    /**
     * Sets the generator that will be used to create or load the world.
     * <p>
     * This may be null, in which case the "natural" generator for this
     * environment will be used.
     * <p>
     * If the generator cannot be found for the given name, the natural
     * environment generator will be used instead and a warning will be
     * printed to the console.
     *
     * @param generator Name of the generator to use, in "plugin:id" notation
     * @return This object, for chaining
     */
    public WorldBuilder setGenerator(String generator) {
        this.generator = getGeneratorForName(name, generator, Bukkit.getConsoleSender());

        return this;
    }

    /**
     * Sets the generator that will be used to create or load the world.
     * <p>
     * This may be null, in which case the "natural" generator for this
     * environment will be used.
     * <p>
     * If the generator cannot be found for the given name, the natural
     * environment generator will be used instead and a warning will be
     * printed to the specified output
     *
     * @param generator Name of the generator to use, in "plugin:id" notation
     * @param output {@link CommandSender} that will receive any error
     *     messages
     * @return This object, for chaining
     */
    public WorldBuilder setGenerator(String generator, CommandSender output) {
        this.generator = getGeneratorForName(name, generator, output);

        return this;
    }
    
    public GameMode getGamemode() {
    	return this.gamemode;
    }
    
    public WorldBuilder setGamemode(GameMode gamemode) {
    	this.gamemode = gamemode;
    	
    	return this;
    }
    
    public Difficulty getDifficulty() {
    	return this.difficulty;
    }
    
    public WorldBuilder setDifficulty(Difficulty difficulty) {
    	this.difficulty = difficulty;
    	
    	return this;
    }

    /**
     * Sets the generator settings of the world that will be created or loaded
     *
     * @param generatorSettings The settings that should be used by the generator
     * @return This object, for chaining
     */
    public WorldBuilder setGeneratorSettings(String generatorSettings) {
        this.generatorSettings = generatorSettings;

        return this;
    }

    /**
     * Gets the generator settings of the world that will be created or loaded
     *
     * @return The settings that should be used by the generator
     */
    public String getGeneratorSettings() {
        return generatorSettings;
    }

    /**
     * Sets whether or not worlds created or loaded with this creator will
     * have structures.
     *
     * @param generate Whether to generate structures
     * @return This object, for chaining
     */
    public WorldBuilder setGenerateStructures(boolean generate) {
        this.generateStructures = generate;

        return this;
    }

    /**
     * Gets whether or not structures will be generated in the world.
     *
     * @return True if structures will be generated
     */
    public boolean getGenerateStructures() {
        return generateStructures;
    }
    
    public File getFolder() {
    	return folder;
    }
    
    public WorldBuilder setFolder(File folder) {
    	this.folder = folder;
    	
    	return this;
    }

    /**
     * Creates a world with the specified options.
     * <p>
     * If the world already exists, it will be loaded from disk and some
     * options may be ignored.
     *
     * @return Newly created or loaded world
     */
    public World createWorld() {
    	Optional<IWorldManager> provider = SuperApiBukkit.getInstance().getNmsManager().getWorldManager();
    	if(provider.isPresent()) {
            return provider.get().createWorld(this);
    	}
    	
    	return null;
    }

    /**
     * Creates a new {@link WorldCreator} for the given world name
     *
     * @param name Name of the world to load or create
     * @return Resulting WorldCreator
     */
    public static WorldBuilder setName(String name) {
        return new WorldBuilder(name);
    }

    /**
     * Attempts to get the {@link ChunkGenerator} with the given name.
     * <p>
     * If the generator is not found, null will be returned and a message will
     * be printed to the specified {@link CommandSender} explaining why.
     * <p>
     * The name must be in the "plugin:id" notation, or optionally just
     * "plugin", where "plugin" is the safe-name of a plugin and "id" is an
     * optional unique identifier for the generator you wish to request from
     * the plugin.
     *
     * @param world Name of the world this will be used for
     * @param name Name of the generator to retrieve
     * @param output Where to output if errors are present
     * @return Resulting generator, or null
     */
    private static ChunkGenerator getGeneratorForName(String world, String name, CommandSender output) {
        ChunkGenerator result = null;

        if (world == null) {
            throw new IllegalArgumentException("World name must be specified");
        }

        if (output == null) {
            output = Bukkit.getConsoleSender();
        }

        if (name != null) {
            String[] split = name.split(":", 2);
            String id = (split.length > 1) ? split[1] : null;
            Plugin plugin = Bukkit.getPluginManager().getPlugin(split[0]);

            if (plugin == null) {
                output.sendMessage("Could not set generator for world '" + world + "': Plugin '" + split[0] + "' does not exist");
            } else if (!plugin.isEnabled()) {
                output.sendMessage("Could not set generator for world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' is not enabled");
            } else {
                result = plugin.getDefaultWorldGenerator(world, id);
            }
        }

        return result;
    }

}
