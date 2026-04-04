package com.smart.enums;

public enum ContactCategory {
    FRIEND("Friend"),
    FAMILY("Family"),
    COLLEAGUE("Colleague"),
    CLIENT("Client"),
    OTHER("Other");

    private String label;

    // Constructor
    ContactCategory(String label) {
        this.label = label;
    }

    // Getter
    public String getLabel() {
        return label;
    }
}
