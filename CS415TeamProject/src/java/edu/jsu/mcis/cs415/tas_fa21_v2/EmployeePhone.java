package edu.jsu.mcis.cs415.tas_fa21_v2;

import java.util.HashMap;

/**
 *
 * @author Tiffany
 */
public class EmployeePhone {
    
    private final int id, employeeid, employeephonetype;
    private final String name, number, description;
    
    public EmployeePhone(HashMap<String, String> p) {
        
        this.id = Integer.parseInt(p.get("id"));
        this.employeeid = Integer.parseInt(p.get("employeeid"));
        this.employeephonetype = Integer.parseInt(p.get("employeephonetypeid"));
        
        this.name = p.get("name");
        this.number = p.get("number");
        this.description = p.get("description");
    }

    public int getId() {
        return id;
    }

    public int getEmployeeid() {
        return employeeid;
    }

    public int getEmployeephonetype() {
        return employeephonetype;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }


   

    
    
    
    
}
