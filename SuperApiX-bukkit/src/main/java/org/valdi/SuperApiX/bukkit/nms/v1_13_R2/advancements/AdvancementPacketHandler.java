package org.valdi.SuperApiX.bukkit.nms.v1_13_R2.advancements;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import net.minecraft.server.v1_13_R2.NetworkManager;
import net.minecraft.server.v1_13_R2.Packet;
import net.minecraft.server.v1_13_R2.PacketPlayInAdvancements;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.valdi.SuperApiX.bukkit.SuperApiBukkit;
import org.valdi.SuperApiX.bukkit.SuperKey;
import org.valdi.SuperApiX.bukkit.advancements.AdvancementProvider;
import org.valdi.SuperApiX.bukkit.advancements.managers.IAdvancementPacketHandler;
import org.valdi.SuperApiX.bukkit.events.advancements.AdvancementScreenCloseEvent;
import org.valdi.SuperApiX.bukkit.events.advancements.AdvancementTabChangeEvent;
import org.valdi.SuperApiX.bukkit.nms.AbstractNmsProvider;

public class AdvancementPacketHandler extends AbstractNmsProvider implements IAdvancementPacketHandler {
    private static HashMap<String, ChannelHandler> handlers = new HashMap<>();
    private static Field channelField;

    public AdvancementPacketHandler(SuperApiBukkit plugin) {
        super(plugin);

        for(Field field : NetworkManager.class.getDeclaredFields()) {
            if(field.getType().isAssignableFrom(Channel.class)) {
                channelField = field;
                channelField.setAccessible(true);
                break;
            }
        }
    }

    @Override
    public ChannelHandler listen(final Player player, final InAdvancementsHandler handler) {
        Channel ch = getNettyChannel(player);
        ChannelPipeline pipe = ch.pipeline();

        ChannelHandler handle = new MessageToMessageDecoder<Packet>() {
            @Override
            protected void decode(ChannelHandlerContext channel, Packet packet, List<Object> out) throws Exception {

                if(packet instanceof PacketPlayInAdvancements) {
                    if(!handler.handle(player, (PacketPlayInAdvancements) packet)) {
                        out.add(packet);
                    }
                    return;
                }

                out.add(packet);
            }
        };
        pipe.addAfter("decoder", "superapix_advancements_listener", handle);


        return handle;
    }

    @Override
    public Channel getNettyChannel(Player player) {
        NetworkManager manager = ((CraftPlayer)player).getHandle().playerConnection.networkManager;
        Channel channel = null;
        try {
            channel = (Channel) channelField.get(manager);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return channel;
    }

    @Override
    public boolean close(Player player, ChannelHandler handler) {
        try {
            ChannelPipeline pipe = getNettyChannel(player).pipeline();
            pipe.remove(handler);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public HashMap<String, ChannelHandler> getHandlers() {
        return handlers;
    }

    @Override
    public void initPlayer(Player player) {
        handlers.put(player.getName(), listen(player, (p, packet) -> {
            if(packet.c() == PacketPlayInAdvancements.Status.OPENED_TAB) {
                SuperKey name = SuperKey.fromMinecraftKey(packet.d());
                AdvancementTabChangeEvent event = new AdvancementTabChangeEvent(p, name);
                Bukkit.getPluginManager().callEvent(event);

                if(event.isCancelled()) {
                    AdvancementProvider.clearActiveTab(p);
                    return false;
                } else {
                    if(!event.getTabAdvancement().equals(name)) {
                        AdvancementProvider.setActiveTab(p, event.getTabAdvancement());
                    } else {
                        AdvancementProvider.setActiveTab(p, name, false);
                    }
                }
            } else {
                AdvancementScreenCloseEvent event = new AdvancementScreenCloseEvent(p);
                Bukkit.getPluginManager().callEvent(event);
            }

            return true;
        }));
    }

}

