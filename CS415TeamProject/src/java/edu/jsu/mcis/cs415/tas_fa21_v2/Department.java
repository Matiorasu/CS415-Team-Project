package edu.jsu.mcis.cs415.tas_fa21_v2;

public class Department {
    
    private final int id, terminalid;
    private final String description;

    public Department(int id, String description, int terminalid) {
        this.id = id;
        this.terminalid = terminalid;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getTerminalid() {
        return terminalid;
    }

    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        
        StringBuilder s = new StringBuilder();
        
        s.append("Department #").append(id);
        s.append(" (").append(description).append("); Terminal #").append(terminalid);
        
        return (s.toString());

    }
    
}