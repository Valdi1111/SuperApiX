package org.valdi.SuperApiX.bukkit.users.locale;

import org.valdi.SuperApiX.common.plugin.StoreLoader;

import java.util.Objects;

public class LocaleHandler {
    private final StoreLoader loader;
    private final String folder;

    public LocaleHandler(StoreLoader loader, String folder) {
        this.loader = loader;
        this.folder = folder;
    }

    public StoreLoader getLoader() {
        return loader;
    }

    public String getFolder() {
        return folder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocaleHandler)) return false;
        LocaleHandler that = (LocaleHandler) o;
        return Objects.equals(loader, that.loader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loader);
    }
}
