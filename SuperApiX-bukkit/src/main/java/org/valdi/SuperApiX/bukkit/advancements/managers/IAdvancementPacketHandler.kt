package org.valdi.SuperApiX.bukkit.advancements.managers

import io.netty.channel.Channel
import io.netty.channel.ChannelHandler
import org.bukkit.entity.Player
import java.util.UUID;

interface IAdvancementPacketHandler<T> {

    fun listen(player: Player, handler: InAdvancementsHandler<T>): ChannelHandler

    fun getNettyChannel(player: Player): Channel

    fun close(player: Player): Boolean

    fun getHandlers(): Map<UUID, ChannelHandler>

    fun initPlayer(player: Player)

    interface InAdvancementsHandler<T> {

        fun handle(player: Player, packet: T): Boolean

    }

}