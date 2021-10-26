package edu.jsu.mcis.cs415.tas_fa21_v2;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.*;

public class DailySchedule implements Cloneable {
    
    private int interval, graceperiod, dock, lunchdeduct;
    private LocalTime start, stop, lunchstart, lunchstop;
    
    /**
     * Sole constructor.
     * @param p The parameters for the new object, given as a {@link HashMap}
     * collection.
     */
    public DailySchedule(HashMap<String, Object> p) {
        
        this.interval = Integer.parseInt(p.get("interval").toString());
        this.graceperiod = Integer.parseInt(p.get("graceperiod").toString());
        this.dock = Integer.parseInt(p.get("dock").toString());
        this.lunchdeduct = Integer.parseInt(p.get("lunchdeduct").toString());
        
        this.start = LocalTime.parse(p.get("start").toString());
        this.stop = LocalTime.parse(p.get("stop").toString());
        this.lunchstart = LocalTime.parse(p.get("lunchstart").toString());
        this.lunchstop = LocalTime.parse(p.get("lunchstop").toString());
        
    }
    
    @Override
    public String toString() {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        
        StringBuilder s = new StringBuilder();
        
        s.append(dtf.format(start)).append(" - ");
        s.append(dtf.format(stop)).append(" (");
        
        s.append( getShiftduration() ).append(" minutes)").append("; Lunch: ");
        
        s.append(dtf.format(lunchstart)).append(" - ");
        s.append(dtf.format(lunchstop)).append(" (");
        
        s.append( getLunchduration() ).append(" minutes)");
        
        return (s.toString());
        
    }
    
    public int getShiftduration() {
        return ((int)(ChronoUnit.MINUTES.between(start, stop)));
    }
    
    public int getLunchduration() {
        return ((int)(ChronoUnit.MINUTES.between(lunchstart, lunchstop)));
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        
        DailySchedule d = (DailySchedule)super.clone();
        
        d.interval = interval;
        d.graceperiod = graceperiod;
        d.dock = dock;
        d.lunchdeduct = lunchdeduct;
        
        d.lunchstart = lunchstart;
        d.lunchstop = lunchstop;
        d.start = start;
        d.stop = stop;

        return d;
        
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getStop() {
        return stop;
    }

    public LocalTime getLunchstart() {
        return lunchstart;
    }

    public LocalTime getLunchstop() {
        return lunchstop;
    }
    
    public int getStartHour() {
        return (start.getHour());
    }
    
    public int getStartMinute() {
        return (start.getMinute());
    }
    
    public int getStopHour() {
        return (stop.getHour());
    }
    
    public int getStopMinute() {
        return (stop.getMinute());
    }
    
    public int getLunchStartHour() {
        return (lunchstart.getHour());
    }
    
    public int getLunchStartMinute() {
        return (lunchstart.getMinute());
    }
    
    public int getLunchStopHour() {
        return (lunchstop.getHour());
    }
    
    public int getLunchStopMinute() {
        return (lunchstop.getMinute());
    }

    public int getInterval() {
        return interval;
    }

    public int getGraceperiod() {
        return graceperiod;
    }

    public int getDock() {
        return dock;
    }

    public int getLunchdeduct() {
        return lunchdeduct;
    }
    
}