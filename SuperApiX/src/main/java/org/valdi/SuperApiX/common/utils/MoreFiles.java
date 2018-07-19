package org.valdi.SuperApiX.common.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MoreFiles {

    public static Path createFileIfNotExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        return path;
    }

    public static Path createDirectoryIfNotExists(Path path) throws IOException {
        if (Files.exists(path) && (Files.isDirectory(path) || Files.isSymbolicLink(path))) {
            return path;
        }

        Files.createDirectory(path);
        return path;
    }

    public static Path createDirectoriesIfNotExists(Path path) throws IOException {
        if (Files.exists(path) && (Files.isDirectory(path) || Files.isSymbolicLink(path))) {
            return path;
        }

        Files.createDirectories(path);
        return path;
    }

    private MoreFiles() {}

}
