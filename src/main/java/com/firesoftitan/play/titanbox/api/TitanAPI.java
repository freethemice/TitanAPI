package com.firesoftitan.play.titanbox.api;

import com.firesoftitan.play.titanbox.api.enums.DayEnum;
import com.firesoftitan.play.titanbox.api.enums.TimeFormatEnum;
import com.firesoftitan.play.titanbox.api.listeners.MainListener;
import com.firesoftitan.play.titanbox.api.listeners.PluginListener;
import com.firesoftitan.play.titanbox.api.managers.ChatMessageManager;
import com.firesoftitan.play.titanbox.api.managers.TaskManager;
import com.firesoftitan.play.titanbox.api.runnables.SaverRunnable;
import com.firesoftitan.play.titanbox.api.runnables.TaskRunnable;
import com.firesoftitan.play.titanbox.api.subitems.Task;
import com.firesoftitan.play.titanbox.libs.tools.LibsMessageTool;
import com.firesoftitan.play.titanbox.libs.tools.Tools;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;


public final class TitanAPI extends JavaPlugin {

    public static TitanAPI instants;
    public static MainListener mainListener;
    public static PluginListener pluginListener;
    public static Tools tools;
    public static LibsMessageTool messageTool;
    public static ChatMessageManager chatMessageManager;
    private TaskRunnable taskRunnable;
    @Override
    public void onEnable() {
        instants = this;
        tools = new Tools(this, new SaverRunnable(this), 100296);
        messageTool = tools.getMessageTool();
        chatMessageManager = new ChatMessageManager();
        mainListener = new MainListener();
        mainListener.registerEvents();
        if (isBungee())
        {
            pluginListener = new PluginListener();
            pluginListener.registerEvents("titanbox:1");
            messageTool.sendMessageSystem("Bungee cord server enabled.");
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            messageTool.sendMessageSystem("Could not find PlaceholderAPI! This plugin is required.", Level.WARNING);
            Bukkit.getPluginManager().disablePlugin(this);
        }

        new TaskManager();
        taskRunnable = new TaskRunnable();
        taskRunnable.runTaskTimer(this, 15*20, 15*20);

    }
    public boolean isBungee()
    {
        ConfigurationSection settings = getServer().spigot().getConfig();
        boolean bungeecord = settings.getBoolean("settings.bungeecord");
        return bungeecord;
    }
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (sender.hasPermission("titanbox.admin"))
        {
            if (args.length > 0) {
                String name = args[0];
                Player player = null;
                if (sender instanceof Player) player =  (Player) sender;
                TaskManager taskManager = TaskManager.instance;

                if (name.equals("remove")) {
                    if (args.length > 1) {
                        String nameKey = args[1];
                        taskManager.remove(nameKey);
                        sendMessageToSender(player, nameKey + " removed!");
                    }
                }
                if (name.equals("list")) {
                    sendMessageToSender(player, ChatColor.GRAY + "");
                    sendMessageToSender(player, ChatColor.GRAY + "");
                    sendMessageToSender(player, ChatColor.GRAY + "");
                    sendMessageToSender(player, ChatColor.GRAY + "---------------------------");
                    for(String key: taskManager.getTasks())
                    {
                        Task task = taskManager.get(key);
                        long next = task.getNextRun();
                        long lastping = next - System.currentTimeMillis();
                        Date dateadded = new Date( task.getAdded() );
                        Date lastran = new Date( task.getLastRan() );
                        Date nextRun = new Date(task.getNextRun());
                        String when = Tools.getFormattingTool(this).formatTime(lastping);
                        if (lastping < 0) when = " running next";
                        sendMessageToSender(player, ChatColor.AQUA + key +  ChatColor.GRAY + " (" + when + ")");
                        sendMessageToSender(player, ChatColor.GRAY + "   Added: " + ChatColor.WHITE + dateadded);
                        sendMessageToSender(player, ChatColor.GRAY + "   Ran: " + ChatColor.WHITE + task.getTimesRan());
                        sendMessageToSender(player, ChatColor.GRAY + "   Next: " + ChatColor.WHITE + nextRun);
                        if (task.getLastRan() > -1) sendMessageToSender(player, ChatColor.GRAY + "   Last ran: " + ChatColor.WHITE + lastran);
                        else sendMessageToSender(player, ChatColor.GRAY + "   Last ran: " + ChatColor.WHITE + "Never ran");
                        sendMessageToSender(player, ChatColor.GRAY + "   Commands: " + ChatColor.WHITE + task.getCommandSize());
                        for(int i = 0; i < task.getCommandSize(); i++) {
                            int addONe = i +1;
                            sendMessageToSender(player, ChatColor.GRAY + "       " + addONe + ": " + ChatColor.WHITE + task.getCommand(i));
                        }
                    }
                    sendMessageToSender(player, ChatColor.GRAY + "---------------------------");
                }
                if (name.equals("command")) {
                    if (args.length > 3) {
                        String subcommands = args[1];
                        String nameKey = args[2];
                        Task task = TaskManager.instance.get(nameKey);
                        if (task != null) {
                            if (subcommands.equalsIgnoreCase("add")) {
                                String commandToAdd = "";
                                for(int i = 3; i < args.length; i++)
                                {
                                    commandToAdd = commandToAdd + args[i] + " ";
                                }
                                task.addCommand(commandToAdd);
                                player.sendMessage(ChatColor.GRAY + "");
                                player.sendMessage(ChatColor.GRAY + "");
                                player.sendMessage(ChatColor.GRAY + "");
                                player.sendMessage(ChatColor.GRAY + "---------------------------");
                                player.sendMessage(ChatColor.GREEN + "Command was added successfully");
                                player.sendMessage(ChatColor.GRAY + commandToAdd);
                            }
                            if (subcommands.equalsIgnoreCase("remove")) {
                                int number = Integer.parseInt(args[3]);
                                number--;
                                String commandToRemove = task.getCommand(number);
                                task.removeCommand(number);
                                player.sendMessage(ChatColor.GRAY + "");
                                player.sendMessage(ChatColor.GRAY + "");
                                player.sendMessage(ChatColor.GRAY + "");
                                player.sendMessage(ChatColor.GRAY + "---------------------------");
                                player.sendMessage(ChatColor.GREEN + "Command was removed successfully");
                                player.sendMessage(ChatColor.GRAY + commandToRemove);

                            }
                            task.save();
                        }
                    }
                    return true;
                }
                if (name.equals("add")) {
                    if (sender instanceof Player) {
                        if (args.length > 1) {
                            String nameKey = args[1];
                            togglePlayerChat(player, false);
                            chatMessageManager.addToSetup(player.getUniqueId(), nameKey);
                            player.sendMessage(ChatColor.GRAY + "");
                            player.sendMessage(ChatColor.GRAY + "");
                            player.sendMessage(ChatColor.GRAY + "");
                            player.sendMessage(ChatColor.GRAY + "---------------------------");
                            player.sendMessage(ChatColor.GREEN + "Will this run on an interval or day of the week?");
                            player.sendMessage(ChatColor.GREEN + "Enter " + ChatColor.WHITE + "i" + ChatColor.GREEN + " for internal");
                            player.sendMessage(ChatColor.GREEN + "Enter " + ChatColor.WHITE + "a" + ChatColor.GREEN + " for advanced");
                            player.sendMessage(ChatColor.GREEN + "Enter the day of the week for once a week");
                            player.sendMessage(ChatColor.WHITE + "Type Exit or Cancel to stop.");
                            return true;

                        }
                    }
                    else
                    {
                        this.sendMessageToSender(null, "Console input coming soon, maybe...");
                    }
                }

                if (name.equals("reload")) {
                    TaskManager.instance.loadSaves();
                    sendMessageToSender(player,  "All task reloaded.");
                }
            }
        }
        return true;
    }

    public void togglePlayerChat(Player player, boolean canChat) {
        if (isBungee()) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("chat:toggle"); // the channel could be whatever you want
            out.writeUTF(player.getUniqueId().toString()); // this data could be whatever you want
            out.writeUTF(canChat + "");
            getServer().sendPluginMessage(this, "titanbox:1", out.toByteArray());
        }
    }

    private void sendMessageToSender(Player player, String message) {
        if (player != null) messageTool.sendMessagePlayer(player, message);
        else messageTool.sendMessageSystem(message);
    }

    @Override
    public void onDisable() {

    }
    public long getNext(long startingDate, DayEnum dayEnum)
    {
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < 8; i++) {
            long increase = i * TimeFormatEnum.DAYS.getConversion();
            long date = startingDate + increase;
            c.setTime(new Date(date));
            int dayofweek = c.get(Calendar.DAY_OF_WEEK);
            if (dayofweek == dayEnum.getValue()) return date;
        }
        return  -1;
    }


}
