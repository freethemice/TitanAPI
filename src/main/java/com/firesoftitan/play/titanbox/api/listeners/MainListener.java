package com.firesoftitan.play.titanbox.api.listeners;


import com.firesoftitan.play.titanbox.api.TitanAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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
        if (event.getPlayer().hasPermission("titanbox.api") || event.getPlayer().isOp()) {
            String message = event.getMessage();
            message = PlaceholderAPI.setPlaceholders(event.getPlayer(), message);
            event.setMessage(message);
        }
    }
    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onServerCommandEvent(ServerCommandEvent event)
    {
        String message = event.getCommand();
        message = PlaceholderAPI.setPlaceholders(null, message);
        event.setCommand(message);
    }
    List<RegisteredListener> commandListers = new ArrayList<RegisteredListener>();
    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event){
        try {
            RegisteredListener[] allRL = event.getHandlers().getRegisteredListeners();
            for(int i = 0; i < allRL.length; i++)
            {
                if (allRL[i].getPlugin() != TitanAPI.instants)
                {
                    commandListers.add(allRL[i]);
                    event.getHandlers().unregister(allRL[i]);
                }
            }
            if (event.isCancelled()) return;
            if (event.getPlayer().hasPermission("titanbox.api") || event.getPlayer().isOp()) {
                String message = event.getMessage();
                message = PlaceholderAPI.setPlaceholders(event.getPlayer(), message);
                event.setMessage(message);
            }
            callUnregEvetns(event);
        } catch (IllegalArgumentException e) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.YELLOW + "Server has no commands =(");
        }
    }
    private void callUnregEvetns(PlayerCommandPreprocessEvent event) {
        for (int i = 0; i < commandListers.size(); i++)
        {
            try {
                commandListers.get(i).callEvent(event);
            } catch (EventException e) {
                e.printStackTrace();
            }
        }
    }
}
