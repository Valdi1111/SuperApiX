package org.valdi.SuperApiX.bukkit;

import org.valdi.SuperApiX.bukkit.storage.objects.DataObject;
import org.valdi.SuperApiX.common.config.advanced.ConfigComment;
import org.valdi.SuperApiX.common.config.advanced.ConfigEntry;
import org.valdi.SuperApiX.common.config.advanced.StoreTo;
import org.valdi.SuperApiX.common.config.advanced.StoredAt;
import org.valdi.SuperApiX.common.databases.StorageType;

@StoredAt(filename = "config.yml")
@StoreTo(filename = "config.yml") // Explicitly call out what name this should have.
public class Settings implements DataObject {

    // Database
    @ConfigComment("H2, MYSQL, MARIADB")
    @ConfigEntry(path = "general.database.type", needsReset = true)
    private StorageType databaseType = StorageType.H2;

    @ConfigEntry(path = "general.database.host")
    private String databaseHost = "localhost";

    @ConfigComment("Port 3306 is MySQL's default. Port 27017 is MongoDB's default.")
    @ConfigEntry(path = "general.database.port")
    private int databasePort = 3306;

    @ConfigEntry(path = "general.database.options")
    private String options = "";

    @ConfigEntry(path = "general.database.name")
    private String databaseName = "superapix";

    @ConfigEntry(path = "general.database.username")
    private String databaseUsername = "admin";

    @ConfigEntry(path = "general.database.password")
    private String databasePassword = "password";

    @ConfigEntry(path = "general.database.pool-size")
    private int databasePoolSize = 15;

    @ConfigEntry(path = "general.database.pool-name")
    private String databasePoolName = "SuperApiX-Hikari";

    @ConfigComment("How often the data will be saved to file in mins. Default is 5 minutes.")
    @ConfigComment("This helps prevent issues if the server crashes.")
    @ConfigComment("Data is also saved at important points in the game.")
    @ConfigEntry(path = "general.database.backup-period")
    private int databaseBackupPeriod = 5;

    // ---------------------------------------------

    /*      GENERAL     */
    @ConfigComment("Default language for new players.")
    @ConfigComment("This is the filename in the locale folder without .yml.")
    @ConfigComment("If this does not exist, the default en-US will be used.")
    @ConfigEntry(path = "general.default-language")
    private String defaultLanguage = "en-US";

    @ConfigEntry(path = "debug")
    private boolean debug = false;

    @ConfigComment("These settings should not be edited")
    private String uniqueId = "config";

    public StorageType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(StorageType databaseType) {
        this.databaseType = databaseType;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(int databasePort) {
        this.databasePort = databasePort;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public int getDatabasePoolSize() {
        return databasePoolSize;
    }

    public void setDatabasePoolSize(int databasePoolSize) {
        this.databasePoolSize = databasePoolSize;
    }

    public String getDatabasePoolName() {
        return databasePoolName;
    }

    public void setDatabasePoolName(String databasePoolName) {
        this.databasePoolName = databasePoolName;
    }

    public int getDatabaseBackupPeriod() {
        return databaseBackupPeriod;
    }

    public void setDatabaseBackupPeriod(int databaseBackupPeriod) {
        this.databaseBackupPeriod = databaseBackupPeriod;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * @return the uniqueId
     */
    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * @param value the uniqueId to set
     */
    @Override
    public void setUniqueId(String value) {
        this.uniqueId = value;
    }

}
