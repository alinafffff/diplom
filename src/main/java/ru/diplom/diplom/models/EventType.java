package ru.diplom.diplom.models;

public enum EventType {
    хакатон("хакатон"),
    волонтерство("волонтерство"),
    хакатон_от_партнера("хакатон_от_партнера");

    private final String name;

    EventType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static EventType fromName(String n) {
        for (EventType type : values()) {
            if (type.name.equalsIgnoreCase(n)) {
                return type;
            }
        }
        throw new IllegalArgumentException("хз тип не тот " + n);
    }
}
