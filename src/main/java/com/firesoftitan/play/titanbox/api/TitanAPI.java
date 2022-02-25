package com.firesoftitan.play.titanbox.api;
import com.firesoftitan.play.titanbox.api.listeners.MainListener;
import com.firesoftitan.play.titanbox.api.managers.AutoUpdateManager;
import com.firesoftitan.play.titanbox.libs.tools.LibsMessageTool;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public final class TitanAPI extends JavaPlugin {

    public static TitanAPI instants;
    public static MainListener mainListener;
    public static Tools tools;
    public static LibsMessageTool messageTool;
    public static boolean update = false;
    @Override
    public void onEnable() {
        instants = this;
        tools = new Tools(this);
        mainListener = new MainListener();
        mainListener.registerEvents();
        messageTool = tools.getMessageTool();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            messageTool.sendMessageSystem("Could not find PlaceholderAPI! This plugin is required.", Level.WARNING);
            Bukkit.getPluginManager().disablePlugin(this);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                new AutoUpdateManager(TitanAPI.this, 100296).getVersion(version -> {
                    if (TitanAPI.this.getDescription().getVersion().equalsIgnoreCase(version)) {
                        messageTool.sendMessageSystem("Plugin is up to date.");
                    } else {
                        TitanAPI.update = true;
                        messageTool.sendMessageSystem("There is a new update available.");
                        messageTool.sendMessageSystem( "https://www.spigotmc.org/resources/titan-teleport-pads.100296");
                    }
                });
            }
        }.runTaskLater(this,20);
    }

    @Override
    public void onDisable() {

    }
}
