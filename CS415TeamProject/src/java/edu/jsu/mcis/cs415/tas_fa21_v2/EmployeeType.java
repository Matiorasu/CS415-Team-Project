package edu.jsu.mcis.cs415.tas_fa21_v2;

public enum EmployeeType {
    
    TEMPORARY_EMPLOYEE("Temporary Employee"),
    FULL_TIME_EMPLOYEE("Full-Time Employee");
    
    private final String description;

    private EmployeeType(String d) {
        description = d;
    }

    @Override
    public String toString() {
        return description;
    }
    
}