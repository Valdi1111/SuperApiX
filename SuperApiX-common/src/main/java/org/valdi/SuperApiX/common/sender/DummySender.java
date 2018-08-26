package org.valdi.SuperApiX.common.sender;

import java.util.UUID;

import org.valdi.SuperApiX.common.ISuperPlugin;
import org.valdi.SuperApiX.common.command.CommandManager;
import org.valdi.SuperApiX.common.utils.TextUtils;

import net.kyori.text.Component;

public abstract class DummySender implements Sender {
    private final ISuperPlugin platform;

    private final UUID uuid;
    private final String name;

    public DummySender(ISuperPlugin plugin, UUID uuid, String name) {
        this.platform = plugin;
        this.uuid = uuid;
        this.name = name;
    }

    public DummySender(ISuperPlugin plugin) {
        this(plugin, CommandManager.IMPORT_UUID, CommandManager.IMPORT_NAME);
    }

    protected abstract void consumeMessage(String s);

    @Override
    public void sendMessage(String message) {
        consumeMessage(message);
    }

    @Override
    public void sendMessage(Component message) {
        consumeMessage(TextUtils.toLegacy(message));
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
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

}
