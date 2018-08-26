package org.valdi.SuperApiX.common.sender;

import java.util.Objects;
import java.util.UUID;

import org.valdi.SuperApiX.common.ISuperPlugin;

import net.kyori.text.Component;

/**
 * Factory class to make a thread-safe sender instance
 *
 * @param <T> the command sender type
 */
public abstract class SenderFactory<T> {

    private final ISuperPlugin plugin;

    public SenderFactory(ISuperPlugin plugin) {
        this.plugin = plugin;
    }

    protected ISuperPlugin getPlugin() {
        return this.plugin;
    }

    protected abstract UUID getUuid(T t);

    protected abstract String getName(T t);

    protected abstract void sendMessage(T t, String s);

    protected abstract void sendMessage(T t, Component message);

    protected abstract boolean hasPermission(T t, String node);

    public final Sender wrap(T sender) {
        Objects.requireNonNull(sender, "sender");
        return new AbstractSender<>(this.plugin, this, sender);
    }

}
