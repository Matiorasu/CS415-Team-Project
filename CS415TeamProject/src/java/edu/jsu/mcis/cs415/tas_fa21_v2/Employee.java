package edu.jsu.mcis.cs415.tas_fa21_v2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Employee {
    
    private final int id, departmentid, shiftid;
    private final String firstname, middlename, lastname;
    
    private final EmployeeType employeetype;
    private final Badge badge;
    
    private final LocalDate active, inactive;
    
    public Employee(HashMap<String, Object> p) {
        
        this.id = Integer.parseInt(p.get("id").toString());
        this.departmentid = Integer.parseInt(p.get("departmentid").toString());
        this.shiftid = Integer.parseInt(p.get("shiftid").toString());
        
        this.firstname = p.get("firstname").toString();
        this.middlename = p.get("middlename").toString();
        this.lastname = p.get("lastname").toString();
        
        this.employeetype = EmployeeType.values()[Integer.parseInt(p.get("employeetypeid").toString())];
        this.badge = (Badge)(p.get("badge"));
        
        this.active = (p.get("active") != null ? LocalDate.parse(p.get("active").toString()) : null);
        this.inactive = (p.get("inactive") != null ? LocalDate.parse(p.get("inactive").toString()) : null);
        
    }
    
    @Override
    public String toString() {
        
        StringBuilder s = new StringBuilder();
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        
        s.append(lastname).append(", ");
        s.append(firstname).append(' ');
        s.append(middlename).append(" (#");
        s.append(badge.getId()).append("); ");
        
        s.append("Type ").append(employeetype.ordinal());
        s.append(" (").append(employeetype).append("); ");
        
        s.append("Department ").append(departmentid).append("; ");
        s.append("Shift ").append(shiftid).append("; ");
        
        s.append("Active: ").append( (active != null) ? dtf.format(active) : "none" ).append("; ");
        s.append("Inactive: ").append( (inactive != null) ? dtf.format(inactive) : "none" );
        
        return (s.toString());
        
    }
    
}