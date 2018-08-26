package org.valdi.SuperApiX.common.sender;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.UUID;

import org.valdi.SuperApiX.common.ISuperPlugin;
import org.valdi.SuperApiX.common.utils.TextUtils;

import com.google.common.base.Splitter;

import net.kyori.text.Component;

/**
 * Simple implementation of {@link Sender} using a {@link SenderFactory}
 *
 * @param <T> the command sender type
 */
public final class AbstractSender<T> implements Sender {
    private static final Splitter NEW_LINE_SPLITTER = Splitter.on("\n");

    private final ISuperPlugin platform;
    private final SenderFactory<T> factory;
    private final WeakReference<T> reference;

    private final UUID uuid;
    private final String name;

    AbstractSender(ISuperPlugin platform, SenderFactory<T> factory, T t) {
        this.platform = platform;
        this.factory = factory;
        this.reference = new WeakReference<>(t);
        this.uuid = factory.getUuid(t);
        this.name = factory.getName(t);
    }

    @Override
    public ISuperPlugin getPlugin() {
        return this.platform;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void sendMessage(String message) {
        final T t = this.reference.get();
        if (t != null) {

            if (!isConsole()) {
                this.factory.sendMessage(t, message);
                return;
            }

            // if it is console, split up the lines and send individually.
            for (String line : NEW_LINE_SPLITTER.split(message)) {
                this.factory.sendMessage(t, line);
            }
        }
    }

    @Override
    public void sendMessage(Component message) {
        if (isConsole()) {
            sendMessage(TextUtils.toLegacy(message));
            return;
        }

        final T t = this.reference.get();
        if (t != null) {
            this.factory.sendMessage(t, message);
        }
    }

    @Override
    public boolean hasPermission(String permission) {
        T t = this.reference.get();
        if (t != null) {
            if (this.factory.hasPermission(t, permission)) {
                return true;
            }
        }

        return isConsole();
    }

    @Override
    public boolean isValid() {
        return this.reference.get() != null;
    }

    @Override
    public Optional<Object> getHandle() {
        return Optional.ofNullable(this.reference.get());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof AbstractSender)) return false;
        final AbstractSender<?> that = (AbstractSender<?>) o;
        return this.getUUID().equals(that.getUUID());
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

}
