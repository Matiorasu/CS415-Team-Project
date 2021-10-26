package edu.jsu.mcis.cs415.tas_fa21_v2;

public enum AdjustmentType {
    
    NONE("None"),
    SHIFT_START("Shift Start"),
    SHIFT_STOP("Shift Stop"),
    SHIFT_DOCK("Shift Dock"),
    LUNCH_START("Lunch Start"),
    LUNCH_STOP("Lunch Stop"),
    INTERVAL_ROUND("Interval Round");
    
    private final String description;

    private AdjustmentType(String d) {
        description = d;
    }

    @Override
    public String toString() {
        return description;
    }
    
}