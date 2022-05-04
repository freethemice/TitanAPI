package com.firesoftitan.play.titanbox.api.listeners;


import com.firesoftitan.play.titanbox.api.TitanAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainListener implements Listener {

    private static HashMap<UUID, Location> changeNames = new HashMap<UUID, Location>();
    private static HashMap<UUID, Location> changeIcons = new HashMap<UUID, Location>();
    public MainListener(){

    }
    public void registerEvents(){
        PluginManager pm = TitanAPI.instants.getServer().getPluginManager();
        pm.registerEvents(this, TitanAPI.instants);
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        if (!TitanAPI.instants.isBungee())
        {
            if (TitanAPI.chatMessageManager.hasPlayer(player))
            {
                event.setCancelled(true);
                TitanAPI.chatMessageManager.chatInput(player, event.getMessage());
                return;
            }
        }
        else if (player.hasPermission("titanbox.api") || player.isOp()) {
            String message = event.getMessage();
            message = PlaceholderAPI.setPlaceholders(player, message);
            event.setMessage(message);
        }
    }
    List<RegisteredListener> serverCommandListener = new ArrayList<RegisteredListener>();
    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onServerCommandEvent(ServerCommandEvent event)
    {
        try {
        RegisteredListener[] allRL = event.getHandlers().getRegisteredListeners();
        for(int i = 0; i < allRL.length; i++)
        {
            if (allRL[i].getPlugin() != TitanAPI.instants)
            {
                serverCommandListener.add(allRL[i]);
                event.getHandlers().unregister(allRL[i]);
            }
        }
        if (event.isCancelled()) return;
        String message = event.getCommand();
        message = PlaceholderAPI.setPlaceholders(null, message);
        event.setCommand(message);
        callUnregisterEvents(event, commandListener);
        } catch (IllegalArgumentException e) {
            event.setCancelled(true);
            TitanAPI.messageTool.sendMessageSystem(ChatColor.YELLOW + "Server has no commands =(");
        }
    }
    List<RegisteredListener> commandListener = new ArrayList<RegisteredListener>();
    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event){
        try {
            RegisteredListener[] allRL = event.getHandlers().getRegisteredListeners();
            for(int i = 0; i < allRL.length; i++)
            {
                if (allRL[i].getPlugin() != TitanAPI.instants)
                {
                    commandListener.add(allRL[i]);
                    event.getHandlers().unregister(allRL[i]);
                }
            }
            if (event.isCancelled()) return;
            if (event.getPlayer().hasPermission("titanbox.api") || event.getPlayer().isOp()) {
                String message = event.getMessage();
                message = PlaceholderAPI.setPlaceholders(event.getPlayer(), message);
                event.setMessage(message);
            }
            callUnregisterEvents(event, commandListener);
        } catch (IllegalArgumentException e) {
            event.setCancelled(true);
            TitanAPI.messageTool.sendMessagePlayer(event.getPlayer(),ChatColor.YELLOW + "Server has no commands =(");
        }
    }
    private void callUnregisterEvents(Event event, List<RegisteredListener> listers) {
        for (int i = 0; i < listers.size(); i++)
        {
            try {
                listers.get(i).callEvent(event);
            } catch (EventException e) {
                e.printStackTrace();
            }
        }
    }
}
