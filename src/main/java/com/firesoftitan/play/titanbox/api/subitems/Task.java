package com.firesoftitan.play.titanbox.api.subitems;

import com.firesoftitan.play.titanbox.api.TitanAPI;
import com.firesoftitan.play.titanbox.api.enums.DayEnum;
import com.firesoftitan.play.titanbox.api.enums.TimeFormatEnum;
import com.firesoftitan.play.titanbox.libs.managers.SaveManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private long added;
    private long firstRan;
    private long lastRan;
    private int timesRan;
    private String name;
    private List<String> commands;
    private TimeFormatEnum timeFormatEnum;
    private int intervals;
    private DayEnum dayofweek;

    public Task(String name, DayEnum dayofweek, TimeFormatEnum timeFormatEnum, int intervals, List<String> commands) {
        this.name = name;
        this.commands = commands;
        this.timeFormatEnum = timeFormatEnum;
        this.intervals = intervals;
        this.added= System.currentTimeMillis();
        this.timesRan = 0;
        this.firstRan = -1L;
        this.lastRan = -1L;
        this.dayofweek = dayofweek;
    }
    public Task(String name, TimeFormatEnum timeFormatEnum, int intervals) {
        this.name = name;
        this.commands = new ArrayList<String>();
        this.timeFormatEnum = timeFormatEnum;
        this.intervals = intervals;
        this.added= System.currentTimeMillis();
        this.timesRan = 0;
        this.firstRan = -1L;
        this.lastRan = -1L;
        this.dayofweek = null;
    }
    public Task(String name)
    {
        SaveManager saveManager = new SaveManager("TitanAPI", "tasks", name );
        this.name = name;
        this.commands  = saveManager.getStringListFromText("commands");
        String time = saveManager.getString("time");
        if (time != null) this.timeFormatEnum = TimeFormatEnum.getTime(time);
        this.intervals = saveManager.getInt("intervals");
        String dayofweek = saveManager.getString("dayofweek");
        if (dayofweek != null) {
            this.dayofweek = DayEnum.getDay(dayofweek);
            if (time == null || this.intervals == 0)
            {
                this.timeFormatEnum = TimeFormatEnum.DAYS;
                this.intervals = 7;
            }
        }



        SaveManager saveDataManager = new SaveManager("TitanAPI", "data", this.name );
        if (saveDataManager.contains("data")) {
            this.added = saveDataManager.getLong("data.added");
            this.timesRan = saveDataManager.getInt("data.timesRan");
            this.firstRan = saveDataManager.getLong("data.firstRan");
            this.lastRan = saveDataManager.getLong("data.lastRan");
        }
        else
        {
            this.added= System.currentTimeMillis();
            this.timesRan = 0;
            this.firstRan = -1L;
            this.lastRan = -1L;
        }

    }

    public void save()
    {
        SaveManager saveManager = new SaveManager("TitanAPI", "tasks", this.name );
        saveManager.set("time", this.timeFormatEnum.getName());
        saveManager.set("intervals", this.intervals);
        if (this.dayofweek != null) saveManager.set("dayofweek", this.dayofweek.getName());
        saveManager.set("commands", commands, true);
        saveManager.save();

        SaveManager saveDataManager = new SaveManager("TitanAPI", "data", this.name );
        saveDataManager.set("data.added", this.added);
        saveDataManager.set("data.timesRan", this.timesRan);
        saveDataManager.set("data.firstRan", this.firstRan);
        saveDataManager.set("data.lastRan", this.lastRan);
        saveDataManager.save();

    }

    public DayEnum getDayofWeek() {
        return dayofweek;
    }

    public long getNextRun()
    {
        long next = this.added;
        if (this.lastRan > -1L) next = this.lastRan;
        else
        {
            if (this.dayofweek != null)
            {
                next = TitanAPI.instants.getNext(this.added, this.dayofweek);
                return next;
            }
        }
        next += this.intervals * this.timeFormatEnum.getConversion();
        return next;
    }
    public void run()
    {
        this.timesRan++;
        this.lastRan = System.currentTimeMillis();
        if (this.firstRan  < 0) this.firstRan = System.currentTimeMillis();
        for(String command: commands)
        {
            command = PlaceholderAPI.setPlaceholders(null, command);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
    public void addCommand(String command)
    {
        commands.add(command);
    }
    public void removeCommand(int index)
    {
        commands.remove(index);
    }
    public String getCommand(int index)
    {
        return commands.get(index);
    }
    public int getCommandSize()
    {
        return commands.size();
    }
    public long getAdded() {
        return added;
    }

    public long getLastRan() {
        return lastRan;
    }

    public long getFirstRan() {
        return firstRan;
    }

    public int getTimesRan() {
        return timesRan;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TimeFormatEnum getTimeFormat() {
        return timeFormatEnum;
    }

    public int getIntervals() {
        return intervals;
    }
}
