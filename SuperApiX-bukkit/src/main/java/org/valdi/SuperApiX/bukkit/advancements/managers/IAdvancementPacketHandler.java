package org.valdi.SuperApiX.bukkit.advancements.managers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public interface IAdvancementPacketHandler<T> {

    ChannelHandler listen(final Player player, final InAdvancementsHandler<T> handler);

    Channel getNettyChannel(Player player);

    boolean close(Player player);

    HashMap<UUID, ChannelHandler> getHandlers();

    void initPlayer(Player player);

    public interface InAdvancementsHandler<T> {

        boolean handle(Player player, T packet);

    }

}
