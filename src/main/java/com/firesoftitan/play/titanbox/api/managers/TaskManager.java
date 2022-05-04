package com.firesoftitan.play.titanbox.api.managers;

import com.firesoftitan.play.titanbox.api.subitems.Task;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class TaskManager {
    public static TaskManager instance;
    private HashMap<String, Task> allTask = new HashMap<String, Task>();
    private File TitanBoxDIRF_tasks;
    private File TitanBoxDIRF_data;
    public TaskManager() {
        TaskManager.instance = this;
        File dataStorageDIR = new File("plugins");
        if (!dataStorageDIR.exists())
        {
            dataStorageDIR.mkdir();
        }
        File TitanBoxDIR = new File("plugins" + File.separator + "TitanAPI" );
        if (!TitanBoxDIR.exists())
        {
            TitanBoxDIR.mkdir();
        }
        TitanBoxDIRF_tasks = new File("plugins" + File.separator + "TitanAPI" + File.separator + "tasks" );
        if (!TitanBoxDIRF_tasks.exists())
        {
            TitanBoxDIRF_tasks.mkdir();
        }
        TitanBoxDIRF_data = new File("plugins" + File.separator + "TitanAPI" + File.separator + "data" );
        if (!TitanBoxDIRF_data.exists())
        {
            TitanBoxDIRF_data.mkdir();
        }
        loadSaves();

    }

    public void loadSaves() {
        allTask.clear();
        for(String s: TitanBoxDIRF_tasks.list())
        {
            if (s.toLowerCase().endsWith(".yml"))
            {
                Task task = new Task(s.substring(0, s.length() - 4));
                add(task.getName(), task);
            }
        }
    }

    public void saveALL()
    {
        for(String key: getTasks())
        {
            Task task = get(key);
            task.save();
        }
    }
    public void add(String name, Task task)
    {
        allTask.put(name, task);
        task.save();
    }
    public void remove(String name)
    {
        allTask.remove(name);
        try {
            File file = new File(TitanBoxDIRF_tasks, name + ".yml");
            file.delete();
            file = new File(TitanBoxDIRF_data, name + ".yml");
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Task get(String name)
    {
        return allTask.get(name);
    }
    public Set<String> getTasks()
    {
        return allTask.keySet();
    }

}
