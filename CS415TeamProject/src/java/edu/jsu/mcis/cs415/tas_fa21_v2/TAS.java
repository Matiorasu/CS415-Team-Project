package edu.jsu.mcis.cs415.tas_fa21_v2;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;

import org.json.simple.*;

public class TAS {
    
    public static void main(String[] args) {
        
        // Test for new "getEmployeesAsSelectList()" TASDatabase method
        
        TASDatabase db = new TASDatabase("localhost", "root", "CS488");
        
        ArrayList<Department> departments = new ArrayList<>();
        
        departments.add(db.getDepartment(7)); // Press
        departments.add(db.getDepartment(9)); // Tool and Die
        
        // selected departments
        
        String html = db.getEmployeesAsSelectList(departments);
        
        System.out.println(html);
        
        // all departments
        
        html = db.getEmployeesAsSelectList(null);
        
        System.out.println(html);
        
    }
    
    /**
     * Returns a JSON string representing the punch data from the specified list
     * of punches.
     * @param punchlist The punch list
     * @return The punch data as a JSON string
     * @see Punch
     */
    @SuppressWarnings("unchecked")
    public static String getPunchListAsJSON(ArrayList<Punch> punchlist) {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        String json = null;
        
        JSONArray jsonPunchList = new JSONArray();
        
        try {
        
            for (Punch nextPunch : punchlist) {

                JSONObject p = new JSONObject();

                p.put("originaltimestamp", nextPunch.getOriginaltimestamp().format(dtf));
                p.put("adjustedtimestamp", nextPunch.getAdjustedtimestamp().format(dtf));

                p.put("terminalid", String.valueOf(nextPunch.getTerminalid()));
                p.put("punchtype", String.valueOf(nextPunch.getPunchtype()));

                p.put("id", String.valueOf(nextPunch.getId()));

                p.put("badgeid", nextPunch.getBadge().getId());
                p.put("adjustmenttype", nextPunch.getAdjustmenttype().toString());

                jsonPunchList.add(p);

            }

            json = JSONValue.toJSONString(jsonPunchList);
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return json;
        
    }
    
    /**
     * Returns a JSON string representing the punch data from an individual
     * punch object.
     * @param punch The punch object
     * @return The single punch object's data as a JSON string
     * @see Punch
     */
    @SuppressWarnings("unchecked")
    public static String getPunchAsJSON(Punch punch) {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        JSONObject jsonObject = new JSONObject(); 
        JSONArray jsonPunch = new JSONArray(); 
        
        String json = null;
        
        try {

                jsonObject.put("originaltimestamp", punch.getOriginaltimestamp().format(dtf));
                jsonObject.put("adjustedtimestamp", punch.getAdjustedtimestamp().format(dtf));

                jsonObject.put("terminalid", String.valueOf(punch.getTerminalid()));
                jsonObject.put("punchtype", String.valueOf(punch.getPunchtype()));

                jsonObject.put("id", String.valueOf(punch.getId()));

                jsonObject.put("badgeid", punch.getBadge().getId());
                jsonObject.put("adjustmenttype", punch.getAdjustmenttype().toString());

                jsonPunch.add(jsonObject); 

            json = JSONValue.toJSONString(jsonPunch);
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return json;
        
    }
    
    
    
    /**
     * Returns a JSON string representing the punch data from the specified list
     * of punches, including total hours accrued (given as minutes) and the
     * employee's absenteeism (computed according to the specified shift rule
     * set).
     * @param punchlist The (already adjusted) punch list
     * @param shift The shift rule set
     * @return The punch data and attendance totals as a JSON string
     * @see Punch
     * @see Shift
     */
    @SuppressWarnings("unchecked")
    public static String getPunchListPlusTotalsAsJSON(ArrayList<Punch> punchlist, Shift shift) {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        JSONObject jsonContainer = new JSONObject();
        JSONArray jsonPunchList = new JSONArray();
        
        String json = null;
        
        try {

            for (Punch punch : punchlist) {

                JSONObject p = new JSONObject();

                p.put("originaltimestamp", punch.getOriginaltimestamp().format(dtf));
                p.put("adjustedtimestamp", punch.getAdjustedtimestamp().format(dtf));

                p.put("terminalid", String.valueOf(punch.getTerminalid()));
                p.put("punchtype", String.valueOf(punch.getPunchtype()));

                p.put("id", String.valueOf(punch.getId()));

                p.put("badgeid", punch.getBadge().getId());
                p.put("adjustmenttype", punch.getAdjustmenttype().toString());

                jsonPunchList.add(p);

            }

            jsonContainer.put("punchlist", jsonPunchList);

            jsonContainer.put("totalminutes", String.valueOf(calculateTotalMinutes(punchlist, shift)));
            jsonContainer.put("absenteeism", String.format("%.2f%%", calculateAbsenteeism(punchlist, shift)));
            
            json = JSONValue.toJSONString(jsonContainer);
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return json;
        
    }

    /**
     * Returns the number of hours accrued by the employee (given as minutes)
     * within the specified list of punches, computed according to the specified
     * shift rule set.
     * @param punchlist The (already adjusted) punch list
     * @param shift The shift rule set
     * @return The number of hours accrued by the employee, given as minutes
     * @see Punch
     * @see Shift
     */
    public static int calculateTotalMinutes(ArrayList<Punch> punchlist, Shift shift) {
        
        TemporalField fieldUS = WeekFields.of(Locale.US).dayOfWeek();

        /* Initialize local variables */

        LocalDateTime beginBlock = null, endBlock;
        
        boolean withinBlock = false, adjustedForLunch = false, outBeforeLunch = false, inAfterLunch = false, ignorebit = false;

        int subtotalMinutes = 0, totalMinutes = 0, minutesInBlock = 0;

        if (punchlist.size() > 0) {

            /* Iterate Through Punch List and Compute Hours */

            for (int i = 0; i < (punchlist.size()); i++) {
                
                Punch punch = (Punch)(punchlist.get(i));
                PunchType punchtype = punch.getPunchtype();

                LocalDateTime ots = punch.getOriginaltimestamp();
                LocalDateTime ats = punch.getAdjustedtimestamp();
                
                int day = ots.get(fieldUS);
                DailySchedule d = shift.getDailySchedule(day);
                
                if (d == null)
                    d = shift.getDefaultschedule();

                /* Create lunch break start/stop "landmarks" */

                LocalDateTime lunchstart = ots.withHour(d.getLunchstart().getHour());
                lunchstart = lunchstart.withMinute(d.getLunchstart().getMinute());
                lunchstart = lunchstart.withSecond(0).withNano(0);

                LocalDateTime lunchstop = ots.withHour(d.getLunchstop().getHour());
                lunchstop = lunchstop.withMinute(d.getLunchstop().getMinute());
                lunchstop = lunchstop.withSecond(0).withNano(0);

                /* Get lunch deduction and threshold values from Shift rules */

                int lunchDeductionThreshold = d.getLunchdeduct();
                int lunchDeductionAmount = d.getLunchduration();

                /* Clock-In Events (use first Clock-In event in the block and ignore others that occur within the block) */

                if ( (punchtype == PunchType.CLOCK_IN) && (withinBlock == false) ) {

                    /* Set beginning of time block */

                    beginBlock = ats;
                    withinBlock = true;

                    /* Clock-In After Lunch Break */

                    if ( beginBlock.isAfter(lunchstop) ) {

                        inAfterLunch = true;

                    }

                    /* Clock-In Before Lunch Break */

                    else if ( beginBlock.isBefore(lunchstart) ) {

                        outBeforeLunch = false;

                    }

                    /* Clock-In During Lunch Break */

                    else if ( punch.getAdjustmenttype() == AdjustmentType.LUNCH_STOP ) {

                        adjustedForLunch = true;

                    }

                }

                /* Clock-Out Events (use first Clock-Out event in the block and ignore others that occur outside the block) */

                else if ( (punchtype == PunchType.CLOCK_OUT) && (withinBlock == true) ) {

                    /* Set end of time block */

                    endBlock = ats;
                    withinBlock = false;

                    /* Calculate Minutes Accrued Within Block */
                    
                    if ( (beginBlock != null) && (endBlock != null) ) {
                    
                        minutesInBlock = (int)(ChronoUnit.MINUTES.between(beginBlock, endBlock));
                    
                    }

                    /* If Clock-Out occured before Clock-In, set accrued minutes to zero */

                    if ( minutesInBlock < 0 ) {

                        minutesInBlock = 0;

                    }

                    /* Add Minutes to Running Total */

                    subtotalMinutes += minutesInBlock;

                    /* Clock-Out Before Lunch Break */

                    if ( (endBlock != null) && (endBlock.isBefore(lunchstart)) ) {

                        outBeforeLunch = true;

                    }

                    /* Clock-Out During Lunch Break */

                    else if ( punch.getAdjustmenttype() == AdjustmentType.LUNCH_START ) {

                        adjustedForLunch = true;

                    }

                }

                /* Time-Out Event (close block but do not add any minutes to the total; this is an error condition that must be corrected by a user) */

                else if ( (punchtype == PunchType.TIME_OUT) && (withinBlock == true) ) {

                    withinBlock = false;

                }

                /* Check to see if person was clocked out before lunch and clocked in after lunch; does not apply on weekends */

                if ( (adjustedForLunch == false) && ( outBeforeLunch && inAfterLunch ) ) {

                    adjustedForLunch = true;

                }

                /* If lunch has not already been deducted or if this is a weekend, and if the person has exceeded their threshold, deduct lunch break minutes */

                if ( (subtotalMinutes > lunchDeductionThreshold) && ( adjustedForLunch == false ) ) {

                    subtotalMinutes -= lunchDeductionAmount;

                }

                /* Add Running Total to Weekly Total */

                totalMinutes += subtotalMinutes;

                subtotalMinutes = 0;
                adjustedForLunch = false;

            }

        }
        
        return totalMinutes;

    }

    /**
     * Returns an employee's absenteeism, based on the specified list of punches
     * and the employee's scheduled hours, expressed as a percentage.
     * @param punchlist The (already adjusted) punch list
     * @param shift The shift rule set
     * @return The employee's absenteeism, expressed as a percentage
     * @see Punch
     * @see Shift
     */
    public static double calculateAbsenteeism(ArrayList<Punch> punchlist, Shift shift) {
        
        int minutesScheduled = shift.getTotalScheduledHours();
        int minutesWorked = calculateTotalMinutes(punchlist, shift);

        double absenteeism = ( 100.0 - (((double)minutesWorked / (double)minutesScheduled) * 100.0) );
        
        return absenteeism;
        
    }

    /**
     * Identifies the date on which the specified (weekly) pay period began.
     * @param date A date within the specified pay period
     * @return The date on which this pay period began
     * @see java.time.LocalDate
     */
    public static LocalDate getStartOfPayPeriod(LocalDate date) {
        
        TemporalField fieldUS = WeekFields.of(Locale.US).dayOfWeek();
        return (date.with(fieldUS, Calendar.SUNDAY));
        
    }
    
    /**
     * Identifies the date on which the specified (weekly) pay period ended.
     * @param date A date within the specified pay period
     * @return The date on which this pay period ended
     * @see java.time.LocalDate
     */
    public static LocalDate getEndOfPayPeriod(LocalDate date) {
        
        TemporalField fieldUS = WeekFields.of(Locale.US).dayOfWeek();
        return (date.with(fieldUS, Calendar.SATURDAY));
        
    }
    
}