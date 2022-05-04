package com.firesoftitan.play.titanbox.api.enums;

public enum TimeFormatEnum {
    MINUTES("Minutes", 60000L, "m", "min"),
    HOURS("Hours",3600000L, "h", "hr"),
    DAYS("Days", 86400000L, "d", "dy", "dys");
    private String name;
    private String[] abbreviation;
    private long conversion;

    TimeFormatEnum(String name, long conversion, String... abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.conversion = conversion;
    }


    public String getName() {
        return name;
    }

    public String[] getAbbreviation() {
        return abbreviation;
    }

    public long getConversion() {
        return conversion;
    }
    public static TimeFormatEnum getTime(String name)
    {
        for(TimeFormatEnum timeFormatEnum : TimeFormatEnum.values())
        {
            if (timeFormatEnum.getName().toLowerCase().equalsIgnoreCase(name)) return timeFormatEnum;
            for(String abb: timeFormatEnum.getAbbreviation())
            {
                if (abb.equalsIgnoreCase(name)) return timeFormatEnum;
            }
        }
        return null;
    }

}
