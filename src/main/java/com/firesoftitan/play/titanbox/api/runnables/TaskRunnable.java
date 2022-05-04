package com.firesoftitan.play.titanbox.api.runnables;

import com.firesoftitan.play.titanbox.api.managers.TaskManager;
import com.firesoftitan.play.titanbox.api.subitems.Task;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class TaskRunnable extends BukkitRunnable {

    @Override
    public void run() {
        TaskManager taskManager = TaskManager.instance;
        Set<String> tasks = taskManager.getTasks();
        for(String task: tasks)
        {
            Task task1 = taskManager.get(task);
            long runNext = task1.getNextRun();
            if (runNext <= System.currentTimeMillis()) task1.run();
        }
    }
}
