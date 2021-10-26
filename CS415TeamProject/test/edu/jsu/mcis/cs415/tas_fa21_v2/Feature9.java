package edu.jsu.mcis.cs415.tas_fa21_v2;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import org.junit.*;
import static org.junit.Assert.*;

public class Feature9 {
    
    private TASDatabase db;
    
    @Before
    public void setup() {
        
        db = new TASDatabase("localhost", "root", "CS488");
        
    }
    
    @Test
    public void testEmployee() {
        
        /* Create Employee Objects */
        
        Employee e1 = db.getEmployee(db.getBadge("9D527CFB"));
        Employee e2 = db.getEmployee(db.getBadge("29C3C7D4"));
        Employee e3 = db.getEmployee(db.getBadge("2A5620A0"));
        Employee e4 = db.getEmployee(db.getBadge("12565C60"));
        Employee e5 = db.getEmployee(db.getBadge("6C0D1549"));
        Employee e6 = db.getEmployee(db.getBadge("8709982E"));
        
        /* Test Employee Objects */
        
        assertEquals("Rodriquez, Jarvis B (#9D527CFB); Type 0 (Temporary Employee); Department 8; Shift 1; Active: 09/22/2015; Inactive: none", e1.toString());
        assertEquals("Gomez, Rose M (#29C3C7D4); Type 0 (Temporary Employee); Department 1; Shift 1; Active: 11/02/2015; Inactive: none", e2.toString());
        assertEquals("Eaton, Curtis M (#2A5620A0); Type 0 (Temporary Employee); Department 2; Shift 1; Active: 10/16/2015; Inactive: none", e3.toString());
        assertEquals("Chapman, Joshua E (#12565C60); Type 0 (Temporary Employee); Department 5; Shift 1; Active: 09/11/2015; Inactive: none", e4.toString());
        assertEquals("Franklin, Ronald W (#6C0D1549); Type 0 (Temporary Employee); Department 1; Shift 1; Active: 09/22/2015; Inactive: none", e5.toString());
        assertEquals("Dent, Judy E (#8709982E); Type 1 (Full-Time Employee); Department 1; Shift 1; Active: 06/27/2016; Inactive: none", e6.toString());

    }
    
    @Test
    public void testDepartment() {
        
        /* Create Department Objects */
        
        Department d1 = db.getDepartment(1);
        Department d2 = db.getDepartment(2);
        Department d3 = db.getDepartment(3);
        Department d4 = db.getDepartment(6);
        Department d5 = db.getDepartment(10);
        
        /* Test Department Objects */
        
        assertEquals("Department #1 (Assembly); Terminal #103", d1.toString());
        assertEquals("Department #2 (Cleaning); Terminal #107", d2.toString());
        assertEquals("Department #3 (Warehouse); Terminal #106", d3.toString());
        assertEquals("Department #6 (Office); Terminal #102", d4.toString());
        assertEquals("Department #10 (Maintenance); Terminal #104", d5.toString());
                        
    }
    
    @Test
    public void testPayPeriodMethods() {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE MM/dd/yyyy");
        
        /* Create Timestamp Objects */
        
        LocalDate v1 = LocalDate.of(2018, Month.AUGUST, 20); // Original Timestamp: Mon 08/20/2018 17:38:35
        LocalDate v2 = LocalDate.of(2018, Month.AUGUST, 1); // Original Timestamp: Wed 08/01/2018 15:31:51
        LocalDate v3 = LocalDate.of(2018, Month.AUGUST, 20); // Original Timestamp: Mon 08/20/2018 16:31:56
        LocalDate v4 = LocalDate.of(2018, Month.SEPTEMBER, 12); // Original Timestamp: Wed 09/12/2018 06:47:32
        LocalDate v5 = LocalDate.of(2018, Month.SEPTEMBER, 14); // Original Timestamp: Fri 09/14/2018 15:43:09
        
        LocalDate d1 = TAS.getStartOfPayPeriod(v1);
        LocalDate d2 = TAS.getEndOfPayPeriod(v1);
        LocalDate d3 = TAS.getStartOfPayPeriod(v2);
        LocalDate d4 = TAS.getEndOfPayPeriod(v2);
        LocalDate d5 = TAS.getStartOfPayPeriod(v3);
        LocalDate d6 = TAS.getEndOfPayPeriod(v3);
        LocalDate d7 = TAS.getStartOfPayPeriod(v4);
        LocalDate d8 = TAS.getEndOfPayPeriod(v4);
        LocalDate d9 = TAS.getStartOfPayPeriod(v5);
        LocalDate d10 = TAS.getEndOfPayPeriod(v5);
        
        assertEquals("Sun 08/19/2018", dtf.format(d1));
        assertEquals("Sat 08/25/2018", dtf.format(d2));
        assertEquals("Sun 07/29/2018", dtf.format(d3));
        assertEquals("Sat 08/04/2018", dtf.format(d4));
        assertEquals("Sun 08/19/2018", dtf.format(d5));
        assertEquals("Sat 08/25/2018", dtf.format(d6));
        assertEquals("Sun 09/09/2018", dtf.format(d7));
        assertEquals("Sat 09/15/2018", dtf.format(d8));
        assertEquals("Sun 09/09/2018", dtf.format(d9));
        assertEquals("Sat 09/15/2018", dtf.format(d10));
                        
    }
    
}