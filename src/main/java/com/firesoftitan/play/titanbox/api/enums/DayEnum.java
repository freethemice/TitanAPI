package com.firesoftitan.play.titanbox.api.enums;

public enum DayEnum {

    SUNDAY("Sunday", 1, "u", "sun"),
    MONDAY("Monday", 2, "m", "mon"),
    TUESDAY("Tuesday", 3, "t", "tu", "tue", "tues"),
    WEDNESDAY("Wednesday", 4, "w", "wed", "whensday", "when"),
    THURSDAY("Thursday", 5, "h", "th", "thu", "thur", "thurs"),
    FRIDAY("Friday", 6,"F", "Fri"),
    SATURDAY("Saturday", 7, "s", "sat");


    private final String name;
    private final String[] abbreviation;
    private final int value;

    DayEnum(String name, int value, String... abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String[] getAbbreviation() {
        return abbreviation;
    }
    public static DayEnum getDay(String name)
    {
        for(DayEnum dayEnum : DayEnum.values())
        {
            if (dayEnum.getName().toLowerCase().equalsIgnoreCase(name)) return dayEnum;
            for(String abb: dayEnum.getAbbreviation())
            {
                if (abb.equalsIgnoreCase(name)) return dayEnum;
            }
        }
        return null;
    }
}
