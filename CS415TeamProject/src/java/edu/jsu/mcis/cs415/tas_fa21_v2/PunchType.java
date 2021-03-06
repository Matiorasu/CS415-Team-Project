package edu.jsu.mcis.cs415.tas_fa21_v2;

public enum PunchType {
    
    CLOCK_OUT("CLOCK OUT"),
    CLOCK_IN("CLOCK IN"),
    TIME_OUT("TIME OUT");

    private final String description;

    private PunchType(String d) {
        description = d;
    }

    @Override
    public String toString() {
        return description;
    }
    
}