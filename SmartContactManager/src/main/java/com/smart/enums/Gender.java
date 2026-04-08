package com.smart.enums;

public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    public final String label;
     Gender(String label){
         this.label = label;
     }

    public String getLabel() {
        return label;
    }

}
