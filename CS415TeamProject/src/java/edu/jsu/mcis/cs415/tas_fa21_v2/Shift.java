package edu.jsu.mcis.cs415.tas_fa21_v2;

import java.util.*;

public class Shift {
    
    private int id;
    private String description;
    
    private DailySchedule defaultschedule;
    
    private HashMap<Integer, DailySchedule> schedule;
    
    /**
     * Sole constructor.
     * @param p The parameters for the new shift rule set, given as a {@link HashMap}
     * collection.
     */
    public Shift(HashMap<String, Object> p) {

        try {
            
            this.id = Integer.parseInt(p.get("id").toString());
            this.description = p.get("description").toString();

            this.defaultschedule = (DailySchedule)(p.get("defaultschedule"));

            this.schedule = new HashMap<>();

            schedule.put(Calendar.MONDAY, (DailySchedule)defaultschedule.clone());
            schedule.put(Calendar.TUESDAY, (DailySchedule)defaultschedule.clone());
            schedule.put(Calendar.WEDNESDAY, (DailySchedule)defaultschedule.clone());
            schedule.put(Calendar.THURSDAY, (DailySchedule)defaultschedule.clone());
            schedule.put(Calendar.FRIDAY, (DailySchedule)defaultschedule.clone());
            
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
    }
    
    @Override
    public String toString() {
        
        StringBuilder s = new StringBuilder();
        
        s.append(description).append(": ");
        s.append(defaultschedule.toString());
        
        return s.toString();
        
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns the default daily schedule for the current shift rule set.  (This
     * is the default schedule for every regular work day in the shift unless it
     * is replaced with a new daily schedule.)
     * @return The default daily schedule
     * @see DailySchedule
     */
    public DailySchedule getDefaultschedule() {
        return defaultschedule;
    }
    
    /**
     * Returns the current daily schedule for the specified day in the current
     * shift rule set.
     * @param day The day of the week, expressed as a {@link java.util.Calendar}
     * constant
     * @return The current daily schedule
     * @see DailySchedule
     */
    public DailySchedule getDailySchedule(int day) {
        
        return (schedule.get(day));
        
    }
    
    /**
     * Replaces the current daily schedule for the specified day in the current
     * shift rule set with the new {@link DailySchedule}.
     * @param day The day of the week, expressed as a {@link java.util.Calendar}
     * constant, that the new daily schedule should be applied to.
     * @param s The new daily schedule.
     * @see DailySchedule
     */
    public void setDailySchedule(int day, DailySchedule s) {
        
        schedule.put(day, s);
        
    }
    
    /**
     * Returns the total hours scheduled within the current set of shift rules,
     * given in minutes.
     * @return The number of minutes in the current schedule
     */
    public int getTotalScheduledHours() {
        
        int total = 0;
        
        for (HashMap.Entry<Integer, DailySchedule> e : schedule.entrySet()) {
            total += ( e.getValue().getShiftduration() - e.getValue().getLunchduration() );
        }
        
        return total;
        
    }
    
    /**
     * Returns the daily schedule for each day in the current shift rule set,
     * formatted as a string.
     * @return The "pretty-printed" current schedule
     */
    public String printSchedule() {
        
        StringBuilder s = new StringBuilder();
        
        for (HashMap.Entry<Integer, DailySchedule> e : schedule.entrySet()) {
            s.append("Day: ").append(e.getKey()).append(", Schedule: ").append(e.getValue()).append('\n');
        }
        
        return s.toString();
                
    }
    
}