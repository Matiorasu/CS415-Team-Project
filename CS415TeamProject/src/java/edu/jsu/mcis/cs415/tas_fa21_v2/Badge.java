package edu.jsu.mcis.cs415.tas_fa21_v2;

public class Badge {
    
    private final String id;
    private final String description;

    public Badge(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        
        StringBuilder s = new StringBuilder();
        
        s.append('#').append(id).append(' ');
        s.append('(').append(description).append(')');
        
        return s.toString();
        
    }
    
}