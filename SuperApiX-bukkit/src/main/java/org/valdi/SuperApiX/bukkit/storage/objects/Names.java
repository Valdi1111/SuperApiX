package org.valdi.SuperApiX.bukkit.storage.objects;

import com.google.gson.annotations.Expose;

import java.util.UUID;

/**
 * Stores player names and uuid's
 * @author tastybento
 *
 */
public class Names implements DataObject {

    @Expose
    private String uniqueId = ""; // name
    @Expose
    private UUID uuid;

    /**
     * This is required for database storage
     */
    public Names() {}
    
    public Names(String name, UUID uuid) {
        this.uniqueId = name;
        this.uuid = uuid;
    }
    
    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public void setUniqueId(String value) {
        this.uniqueId = value;
    }

    /**
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * @param value the uuid to set
     */
    public void setUuid(UUID value) {
        this.uuid = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || !(o instanceof Names)) {
            return false;
        }

        Names names = (Names) o;
        return uniqueId.equals(names.getUniqueId());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uniqueId == null) ? 0 : uniqueId.hashCode());
        return result;
    }

}
