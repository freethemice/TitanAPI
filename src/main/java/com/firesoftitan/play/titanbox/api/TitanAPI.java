package com.firesoftitan.play.titanbox.api;
import com.firesoftitan.play.titanbox.api.listeners.MainListener;
import com.firesoftitan.play.titanbox.libs.runnables.MySaveRunnable;
import com.firesoftitan.play.titanbox.libs.runnables.TitanSaverRunnable;
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
    @Override
    public void onEnable() {
        instants = this;
        tools = new Tools(this, new TitanSaverRunnable(this), 100296);
        mainListener = new MainListener();
        mainListener.registerEvents();
        messageTool = tools.getMessageTool();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            messageTool.sendMessageSystem("Could not find PlaceholderAPI! This plugin is required.", Level.WARNING);
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {

    }
}
