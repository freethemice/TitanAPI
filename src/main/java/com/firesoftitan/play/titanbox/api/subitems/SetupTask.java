package com.firesoftitan.play.titanbox.api.subitems;

import com.firesoftitan.play.titanbox.api.enums.DayEnum;
import com.firesoftitan.play.titanbox.api.enums.TimeFormatEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SetupTask {
    private String name;
    private List<String> commands;
    private TimeFormatEnum timeFormatEnum;
    private int intervals;
    private UUID uuid;
    private int step;
    private DayEnum dayEnum;
    private boolean advanced;
    public SetupTask(UUID uuid, String name, int option) {
        this.uuid = uuid;
        this.name = name;
        this.commands = new ArrayList<String>();
        this.step = option;
        this.dayEnum = null;
        this.advanced = false;
    }
    public SetupTask(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.commands = new ArrayList<String>();
        this.step = 0;
        this.dayEnum = null;
        this.advanced = false;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }

    public int getStep() {
        return step;
    }

    public void addCommand(String text)
    {
        this.commands.add(text);
    }
    public TimeFormatEnum getTimeFormat() {
        return timeFormatEnum;
    }

    public DayEnum getDayEnum() {
        return dayEnum;
    }

    public void setDayEnum(DayEnum dayEnum) {
        this.step++;
        this.dayEnum = dayEnum;
    }

    public void setTimeFormat(TimeFormatEnum timeFormatEnum) {
        this.step++;
        this.timeFormatEnum = timeFormatEnum;
    }

    public int getIntervals() {
        return intervals;
    }

    public void setIntervals(int intervals) {
        this.step++;
        this.intervals = intervals;
    }
    public Task getTask()
    {
        if (step < 2) return null;
        Task task = new Task(this.name, this.dayEnum, this.timeFormatEnum, this.intervals, this.commands);
        return task;
    }
}
