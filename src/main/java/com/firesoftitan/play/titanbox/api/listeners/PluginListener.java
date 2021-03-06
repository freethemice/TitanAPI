package com.firesoftitan.play.titanbox.api.listeners;

import com.firesoftitan.play.titanbox.api.TitanAPI;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PluginListener implements PluginMessageListener {

    public PluginListener(){

    }
    public void registerEvents(String channel){
        Messenger pm = TitanAPI.instants.getServer().getMessenger();
        pm.registerIncomingPluginChannel( TitanAPI.instants, channel, this );
        pm.registerOutgoingPluginChannel( TitanAPI.instants, channel);
    }
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if ( !channel.equalsIgnoreCase( "titanbox:1" ) ) return;

        ByteArrayDataInput in = ByteStreams.newDataInput( message );
        String subChannel = in.readUTF();
        if ( subChannel.equalsIgnoreCase( "chat" ) )
        {
            UUID uuid  = UUID.fromString(in.readUTF());
            String chatMessage = in.readUTF();
            Player playerTarget = Bukkit.getPlayer(uuid);
            if (TitanAPI.chatMessageManager.hasPlayer(playerTarget))
            {
                TitanAPI.chatMessageManager.chatInput(playerTarget, chatMessage);
                return;
            }
            // do things with the data
        }
    }
}
