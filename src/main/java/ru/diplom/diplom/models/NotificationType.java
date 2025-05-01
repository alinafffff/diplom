package ru.diplom.diplom.models;

public enum NotificationType {
    система("система"),
    новость("новость");

    private final String name;

    NotificationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static NotificationType fromName(String n) {
        for (NotificationType type : values()) {
            if (type.name.equalsIgnoreCase(n)) {
                return type;
            }
        }
        throw new IllegalArgumentException("хз тип не тот " + n);
    }
}
