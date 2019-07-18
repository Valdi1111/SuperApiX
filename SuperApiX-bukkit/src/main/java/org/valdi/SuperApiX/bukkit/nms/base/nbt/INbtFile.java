package org.valdi.SuperApiX.bukkit.nms.base.nbt;

import java.io.File;
import java.io.IOException;

/**
 * Creates a NBTFile that uses a file to store it's data. If this file
 * exists, the data will be loaded.
 */
public interface INbtFile extends INbtCompound {

    /**
     * Saves the data to the file
     *
     * @throws IOException
     */
    void save() throws IOException;

    /**
     * @return The File used to store the data
     */
    File getFile();

}
