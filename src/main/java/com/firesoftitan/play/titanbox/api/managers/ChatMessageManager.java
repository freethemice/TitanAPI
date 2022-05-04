package com.firesoftitan.play.titanbox.api.managers;

import com.firesoftitan.play.titanbox.api.TitanAPI;
import com.firesoftitan.play.titanbox.api.enums.DayEnum;
import com.firesoftitan.play.titanbox.api.enums.TimeFormatEnum;
import com.firesoftitan.play.titanbox.api.subitems.SetupTask;
import com.firesoftitan.play.titanbox.api.subitems.Task;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ChatMessageManager {


    HashMap<UUID, SetupTask> setupList = new HashMap<UUID, SetupTask>();
    public ChatMessageManager() {
        
    }

    public void addToSetup(Player player, String name)
    {
        addToSetup(player.getUniqueId(), name);
    }
    public void addToSetup(UUID uuid, String name)
    {
        setupList.put(uuid, new SetupTask(uuid, name));
    }
    public void addToSetup(UUID uuid, String name, int option)
    {
        setupList.put(uuid, new SetupTask(uuid, name));
    }

    public boolean hasPlayer(Player player)
    {
        return hasPlayer(player.getUniqueId());
    }
    public boolean hasPlayer(UUID uuid)
    {
        return setupList.containsKey(uuid);
    }
    
    public void chatInput(Player player, String message)
    {
        if (message.toLowerCase().equalsIgnoreCase("exit") || message.toLowerCase().equalsIgnoreCase("cancel"))
        {
            TitanAPI.instants.togglePlayerChat(player, true);
            setupList.remove(player.getUniqueId());
            player.sendMessage(ChatColor.RED + "Task setup canceled!");
            return;
        }
        SetupTask setupTask = setupList.get(player.getUniqueId());
        switch (setupTask.getStep())
        {
            case 0:
                if (message.equalsIgnoreCase("i") || message.equalsIgnoreCase("interval"))
                {
                    setupTask.setDayEnum(null);
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "---------------------------");
                    player.sendMessage(ChatColor.GREEN + "Please enter what time format to use, Examples: M, H, D, minutes, hours, days, min, hrs, dy");
                    player.sendMessage(ChatColor.WHITE + "Type Exit or Cancel to stop.");
                }
                else if (message.equalsIgnoreCase("a") || message.equalsIgnoreCase("advanced"))
                {
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "---------------------------");
                    player.sendMessage(ChatColor.GREEN + "Please enter the day of the week when it will start.");
                    player.sendMessage(ChatColor.GREEN + "Type Exit or Cancel to stop.");
                    setupTask.setAdvanced(true);
                }
                else {
                    DayEnum dayEnum = DayEnum.getDay(message);
                    if (dayEnum != null) {
                        setupTask.setDayEnum(dayEnum);
                        if (!setupTask.isAdvanced()) {
                            setupTask.setTimeFormat(TimeFormatEnum.DAYS);
                            setupTask.setIntervals(7);
                            player.sendMessage(ChatColor.GRAY + "");
                            player.sendMessage(ChatColor.GRAY + "");
                            player.sendMessage(ChatColor.GRAY + "");
                            player.sendMessage(ChatColor.GRAY + "---------------------------");
                            player.sendMessage(ChatColor.AQUA + "https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Placeholders");
                            player.sendMessage(ChatColor.GREEN + "Please enter a command to be added, use placeholder api. Type done when all commands have been added.");
                            player.sendMessage(ChatColor.WHITE + "Type Exit or Cancel to stop.");
                        }
                        else
                        {
                            player.sendMessage(ChatColor.GRAY + "");
                            player.sendMessage(ChatColor.GRAY + "");
                            player.sendMessage(ChatColor.GRAY + "");
                            player.sendMessage(ChatColor.GRAY + "---------------------------");
                            player.sendMessage(ChatColor.GREEN + "Please enter what time format to use, Examples: M, H, D, minutes, hours, days, min, hrs, dy");
                            player.sendMessage(ChatColor.WHITE + "Type Exit or Cancel to stop.");
                        }
                    } else {
                        player.sendMessage(ChatColor.GRAY + "");
                        player.sendMessage(ChatColor.GRAY + "");
                        player.sendMessage(ChatColor.GRAY + "");
                        player.sendMessage(ChatColor.GRAY + "---------------------------");
                        player.sendMessage(ChatColor.GREEN + "Will this run on an interval or day of the week?");
                        player.sendMessage(ChatColor.GREEN + "Please enter " + ChatColor.WHITE + "i" + ChatColor.GREEN + " for internal or the day of the week on which it will run");
                        player.sendMessage(ChatColor.WHITE + "Type Exit or Cancel to stop.");
                    }
                }
                break;
            case 1:
                TimeFormatEnum timeFormatEnum = TimeFormatEnum.getTime(message);
                if (timeFormatEnum != null)
                {
                    setupTask.setTimeFormat(timeFormatEnum);
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "---------------------------");
                    player.sendMessage(ChatColor.GREEN + "Please enter the intervals of how often this will run in " + timeFormatEnum.getName());
                    player.sendMessage(ChatColor.WHITE + "Type Exit or Cancel to stop.");
                }
                else
                {
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "---------------------------");
                    player.sendMessage(ChatColor.GREEN + "Please enter what time format to use, Examples: M, H, D, minutes, hours, days, min, hrs, dy");
                    player.sendMessage(ChatColor.GREEN + "Type Exit or Cancel to stop.");
                }
                break;
            case 2:
                try {
                    int interval = Integer.parseInt(message);
                    setupTask.setIntervals(interval);
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "---------------------------");
                    player.sendMessage(ChatColor.AQUA + "https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Placeholders");
                    player.sendMessage(ChatColor.GREEN + "Please enter a command to be added, use placeholder api. Type done when all commands have been added.");
                    player.sendMessage(ChatColor.WHITE + "Type Exit or Cancel to stop.");

                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "---------------------------");
                    player.sendMessage(ChatColor.GREEN + "Please enter the intervals of how often this will run in " + setupTask.getTimeFormat().getName());
                    player.sendMessage(ChatColor.WHITE + "Type Exit or Cancel to stop.");
                }
                break;
            case 3:
                String command = message;
                if (command.equalsIgnoreCase("done"))
                {
                    Task task = setupTask.getTask();
                    TaskManager.instance.add(task.getName(), task);
                    setupList.remove(player.getUniqueId());
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "---------------------------");
                    player.sendMessage(ChatColor.GREEN + "Task has been added!");
                    TitanAPI.instants.togglePlayerChat(player, true);
                }
                else
                {
                    setupTask.addCommand(command);
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "");
                    player.sendMessage(ChatColor.GRAY + "---------------------------");
                    player.sendMessage(ChatColor.GREEN + "Command has been added, please enter another command or");
                    player.sendMessage(ChatColor.GREEN + "type " + ChatColor.WHITE + "Done" + ChatColor.GREEN + " to finish");
                    player.sendMessage(ChatColor.WHITE + "Type Exit or Cancel to stop.");
                }
                break;
        }
    }
}
