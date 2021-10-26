package edu.jsu.mcis.cs415.tas_fa21_v2;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

import org.json.simple.*;

public class Feature5 {
    
    private TASDatabase db;
    
    @Before
    public void setup() {
        
        db = new TASDatabase("localhost", "root", "CS488");
        
    }
    
    @Test
    public void testJSONShift1Weekday() {
        
        /* Expected JSON Data */
        
        String expectedJSON = "[{\"originaltimestamp\":\"2018-09-07 06:50:35\",\"badgeid\":\"28DC3FB8\",\"adjustedtimestamp\":\"2018-09-07 07:00:00\",\"adjustmenttype\":\"Shift Start\",\"terminalid\":\"104\",\"id\":\"3634\",\"punchtype\":\"CLOCK IN\"},{\"originaltimestamp\":\"2018-09-07 12:03:54\",\"badgeid\":\"28DC3FB8\",\"adjustedtimestamp\":\"2018-09-07 12:00:00\",\"adjustmenttype\":\"Lunch Start\",\"terminalid\":\"104\",\"id\":\"3687\",\"punchtype\":\"CLOCK OUT\"},{\"originaltimestamp\":\"2018-09-07 12:23:41\",\"badgeid\":\"28DC3FB8\",\"adjustedtimestamp\":\"2018-09-07 12:30:00\",\"adjustmenttype\":\"Lunch Stop\",\"terminalid\":\"104\",\"id\":\"3688\",\"punchtype\":\"CLOCK IN\"},{\"originaltimestamp\":\"2018-09-07 15:34:13\",\"badgeid\":\"28DC3FB8\",\"adjustedtimestamp\":\"2018-09-07 15:30:00\",\"adjustmenttype\":\"Shift Stop\",\"terminalid\":\"104\",\"id\":\"3716\",\"punchtype\":\"CLOCK OUT\"}]";
        
        JSONArray expected = (JSONArray)JSONValue.parse(expectedJSON);
		
        /* Get Punch */
        
        Punch p = db.getPunch(3634);
        Badge b = db.getBadge(p.getBadge().getId());
        Shift s = db.getShift(b);
		
        /* Get Daily Punch List */
        
        ArrayList<Punch> dailypunchlist = db.getDailyPunchList(b, p.getOriginaltimestamp().toLocalDate(), s);
        
        /* JSON Conversion */
        
        String actualJSON = TAS.getPunchListAsJSON(dailypunchlist);
        
        JSONArray actual = (JSONArray)JSONValue.parse(actualJSON);
		
        /* Compare to Expected JSON */
        
        assertEquals(expected, actual);
        
    }
    
    @Test
    public void testJSONShift1Weekend() {
        
        /* Expected JSON Data */
        
        String expectedJSON = "[{\"originaltimestamp\":\"2018-08-11 05:54:58\",\"badgeid\":\"F1EE0555\",\"adjustedtimestamp\":\"2018-08-11 06:00:00\",\"adjustmenttype\":\"Interval Round\",\"terminalid\":\"105\",\"id\":\"1087\",\"punchtype\":\"CLOCK IN\"},{\"originaltimestamp\":\"2018-08-11 12:04:02\",\"badgeid\":\"F1EE0555\",\"adjustedtimestamp\":\"2018-08-11 12:00:00\",\"adjustmenttype\":\"Interval Round\",\"terminalid\":\"105\",\"id\":\"1162\",\"punchtype\":\"CLOCK OUT\"}]";
        
        JSONArray expected = (JSONArray)JSONValue.parse(expectedJSON);
		
        /* Get Punch */
        
        Punch p = db.getPunch(1087);
        Badge b = db.getBadge(p.getBadge().getId());
        Shift s = db.getShift(b);
        
        /* Get Daily Punch List */
        
        ArrayList<Punch> dailypunchlist = db.getDailyPunchList(b, p.getOriginaltimestamp().toLocalDate(), s);
        
        /* JSON Conversion */
        
        String actualJSON = TAS.getPunchListAsJSON(dailypunchlist);
        
        JSONArray actual = (JSONArray)JSONValue.parse(actualJSON);
		
        /* Compare to Expected JSON */
        
        assertEquals(expected, actual);
        
    }
    
    @Test
    public void testJSONShift2Weekday() {
        
        /* Expected JSON Data */
        
        String expectedJSON = "[{\"originaltimestamp\":\"2018-09-18 11:59:33\",\"badgeid\":\"08D01475\",\"adjustedtimestamp\":\"2018-09-18 12:00:00\",\"adjustmenttype\":\"Shift Start\",\"terminalid\":\"104\",\"id\":\"4943\",\"punchtype\":\"CLOCK IN\"},{\"originaltimestamp\":\"2018-09-18 21:30:27\",\"badgeid\":\"08D01475\",\"adjustedtimestamp\":\"2018-09-18 21:30:00\",\"adjustmenttype\":\"None\",\"terminalid\":\"104\",\"id\":\"5004\",\"punchtype\":\"CLOCK OUT\"}]";
        
        JSONArray expected = (JSONArray)JSONValue.parse(expectedJSON);
		
        /* Get Punch */
        
        Punch p = db.getPunch(4943);
        Badge b = db.getBadge(p.getBadge().getId());
        Shift s = db.getShift(b);
        
        /* Get Daily Punch List */
        
        ArrayList<Punch> dailypunchlist = db.getDailyPunchList(b, p.getOriginaltimestamp().toLocalDate(), s);
        
        /* JSON Conversion */
        
        String actualJSON = TAS.getPunchListAsJSON(dailypunchlist);
        
        JSONArray actual = (JSONArray)JSONValue.parse(actualJSON);
		
        /* Compare to Expected JSON */
        
        assertEquals(expected, actual);
        
    }
    
}