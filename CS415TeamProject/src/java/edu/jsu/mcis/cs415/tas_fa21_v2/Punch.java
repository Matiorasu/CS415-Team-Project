package edu.jsu.mcis.cs415.tas_fa21_v2;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;

public class Punch {
    
    private final int id, terminalid;
    private final PunchType punchtype;
    private final Badge badge;
    private final LocalDateTime originaltimestamp;
    
    private LocalDateTime adjustedtimestamp;
    private AdjustmentType adjustmenttype;
    
    /**
     * Constructor for new punch records (not yet existing in the database)
     * @param terminalid The ID of the terminal at which the punch was logged
     * @param badge The employee badge that was swiped to create the punch
     * @param punchtypeid The punch type
     * @see Badge
     */
    public Punch(int terminalid, Badge badge, int punchtypeid) {
        
        this.id = 0;
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchtype = PunchType.values()[punchtypeid];
        this.originaltimestamp = LocalDateTime.now().withNano(0);
        
    }
    
    /**
     * Constructor for existing punch records (already existing in the database)
     * @param id The ID of the punch, assigned by the database
     * @param terminalid The ID of the terminal at which the punch was logged
     * @param badge The employee badge that was swiped to create the punch
     * @param originaltimestamp The date and time when the punch was logged
     * @param punchtypeid The punch type
     * @see Badge
     */
    public Punch(int id, int terminalid, Badge badge, LocalDateTime originaltimestamp, int punchtypeid) {
        
        this.id = id;
        this.terminalid = terminalid;
        this.badge = badge;
        this.punchtype = PunchType.values()[punchtypeid];
        this.originaltimestamp = originaltimestamp.withNano(0);
        
    }
    
    /**
     * Returns the punch information, including the original timestamp, as a
     * formatted string.
     * @return The "pretty-printed" punch data
     */
    public String printOriginal() {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss");
        
        StringBuilder s = new StringBuilder();
        
        s.append("#").append(badge.getId()).append(" ");
        s.append(punchtype);
        s.append(": ").append(originaltimestamp.format(dtf));

        return s.toString().toUpperCase();
        
    }
    
    /**
     * Returns the punch information, including the adjusted timestamp, as a
     * formatted string.
     * @return The "pretty-printed" punch data
     */
    public String printAdjusted() {
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE MM/dd/yyyy HH:mm:ss");
        
        StringBuilder s = new StringBuilder();
        
        s.append("#").append(badge.getId()).append(" ");
        s.append(punchtype);
        s.append(": ").append(adjustedtimestamp.format(dtf).toUpperCase());
        
        s.append(" (").append(adjustmenttype).append(")");

        return s.toString();
        
    }
    
    /**
     * Returns the punch information, including the original timestamp, as a
     * formatted string.
     * @return The "pretty-printed" punch data
     */
    @Override
    public String toString() {
        return this.printOriginal();
    }

    public int getId() {
        return id;
    }

    public int getTerminalid() {
        return terminalid;
    }

    public PunchType getPunchtype() {
        return punchtype;
    }

    public Badge getBadge() {
        return badge;
    }

    public AdjustmentType getAdjustmenttype() {
        return adjustmenttype;
    }

    public LocalDateTime getOriginaltimestamp() {
        return originaltimestamp;
    }

    public LocalDateTime getAdjustedtimestamp() {
        return adjustedtimestamp;
    }
    
    /**
     * Computes the adjusted timestamp of the punch, adjusting it according to
     * the indicated shift rule set.
     * @param shift The shift rule set containing the parameters which should be used
     * to adjust the punch
     * @see Shift
     */
    public void adjust(Shift shift) {
        
        TemporalField fieldUS = WeekFields.of(Locale.US).dayOfWeek();
        
        boolean adjusted = false;
        
        DayOfWeek dayofweek = originaltimestamp.getDayOfWeek();
        
        DailySchedule d = shift.getDailySchedule(originaltimestamp.get(fieldUS));
        
        if (d == null)
            d = shift.getDefaultschedule();
        
        /* INITIALIZE ADJUSTED TIMESTAMP TO ORIGINAL TIMESTAMP */
        
        adjustedtimestamp = originaltimestamp;
        
        /* CREATE TIMESTAMP OBJECTS */

        /* Shift Start */
        
        LocalDateTime shiftstart = originaltimestamp.withHour(d.getStart().getHour());
        shiftstart = shiftstart.withMinute(d.getStart().getMinute());
        shiftstart = shiftstart.withSecond(0).withNano(0);
        
        /* Shift Stop */
        
        LocalDateTime shiftstop = originaltimestamp.withHour(d.getStop().getHour());
        shiftstop = shiftstop.withMinute(d.getStop().getMinute());
        shiftstop = shiftstop.withSecond(0).withNano(0);
        
        /* Lunch Start */
        
        LocalDateTime lunchstart = originaltimestamp.withHour(d.getLunchstart().getHour());
        lunchstart = lunchstart.withMinute(d.getLunchstart().getMinute());
        lunchstart = lunchstart.withSecond(0).withNano(0);
        
        /* Lunch Stop */
        
        LocalDateTime lunchstop = originaltimestamp.withHour(d.getLunchstop().getHour());
        lunchstop = lunchstop.withMinute(d.getLunchstop().getMinute());
        lunchstop = lunchstop.withSecond(0).withNano(0);
        
        /* Interval Start */
        
        LocalDateTime intervalstart = shiftstart.minusMinutes(d.getInterval());
        
        /* Grace Start */
        
        LocalDateTime gracestart = shiftstart.plusMinutes(d.getGraceperiod());
        
        /* Dock Start */
        
        LocalDateTime dockstart = shiftstart.plusMinutes(d.getDock());
        
        /* Interval Stop */
        
        LocalDateTime intervalstop = shiftstop.plusMinutes(d.getInterval());
        
        /* Grace Stop */
        
        LocalDateTime gracestop = shiftstop.minusMinutes(d.getGraceperiod());
        
        /* Dock Stop */
        
        LocalDateTime dockstop = shiftstop.minusMinutes(d.getDock());
        
        /* PERFORM CLOCK-IN ADJUSTMENTS */
        
        if ( (dayofweek != DayOfWeek.SATURDAY) && (dayofweek != DayOfWeek.SUNDAY) ) {
            
            /* Clock In Punches */
        
            if (punchtype == PunchType.CLOCK_IN) {

                /* Interval Start */

                if ( ((originaltimestamp.isAfter(intervalstart)) || (originaltimestamp.isEqual(intervalstart)) ) &&
                        ( (originaltimestamp.isBefore(shiftstart)) || (originaltimestamp.isEqual(shiftstart)) ) ) {
                    adjustedtimestamp = shiftstart;
                    adjustmenttype = AdjustmentType.SHIFT_START;
                    adjusted = true;
                }

                /* Grace Start */

                else if ( ((originaltimestamp.isAfter(shiftstart)) ) &&
                        ( (originaltimestamp.isBefore(gracestart)) || (originaltimestamp.isEqual(gracestart)) ) ) {
                    adjustedtimestamp = shiftstart;
                    adjustmenttype = AdjustmentType.SHIFT_START;
                    adjusted = true;
                }

                /* Dock Start */

                else if ( ((originaltimestamp.isAfter(gracestart)) ) &&
                        ( (originaltimestamp.isBefore(dockstart)) || (originaltimestamp.isEqual(dockstart)) ) ) {
                    adjustedtimestamp = dockstart;
                    adjustmenttype = AdjustmentType.SHIFT_DOCK;
                    adjusted = true;
                }

                /* Lunch Stop */

                else if ( ((originaltimestamp.isAfter(lunchstart)) || (originaltimestamp.isEqual(lunchstart)) ) &&
                        ( (originaltimestamp.isBefore(lunchstop)) || (originaltimestamp.isEqual(lunchstop)) ) ) {
                    adjustedtimestamp = lunchstop;
                    adjustmenttype = AdjustmentType.LUNCH_STOP;
                    adjusted = true;
                }
                
            }
            
            /* Clock Out Punches */

            else if (punchtype == PunchType.CLOCK_OUT) {

                /* Lunch Start */

                if ( ((originaltimestamp.isAfter(lunchstart)) || (originaltimestamp.isEqual(lunchstart)) ) &&
                        ( (originaltimestamp.isBefore(lunchstop)) || (originaltimestamp.isEqual(lunchstop)) ) ) {
                    adjustedtimestamp = lunchstart;
                    adjustmenttype = AdjustmentType.LUNCH_START;
                    adjusted = true;
                }

                /* Dock Stop */

                else if ( ((originaltimestamp.isAfter(dockstop)) || (originaltimestamp.isEqual(dockstop)) ) &&
                        ( originaltimestamp.isBefore(gracestop)) ) {
                    adjustedtimestamp = dockstop;
                    adjustmenttype = AdjustmentType.SHIFT_DOCK;
                    adjusted = true;
                }

                /* Grace Stop */

                else if ( ((originaltimestamp.isAfter(gracestop)) || (originaltimestamp.isEqual(gracestop)) ) &&
                        ( originaltimestamp.isBefore(shiftstop)) ) {
                    adjustedtimestamp = shiftstop;
                    adjustmenttype = AdjustmentType.SHIFT_STOP;
                    adjusted = true;
                }

                /* Interval Stop */

                else if ( ((originaltimestamp.isAfter(shiftstop)) || (originaltimestamp.isEqual(shiftstop)) ) &&
                        ( (originaltimestamp.isBefore(intervalstop)) || (originaltimestamp.isEqual(intervalstop)) ) ) {
                    adjustedtimestamp = shiftstop;
                    adjustmenttype = AdjustmentType.SHIFT_STOP;
                    adjusted = true;
                }
                
            }
            
        }
        
        /* If none of the foregoing rules apply ... */
        
        if (!adjusted) {
            
            /* Get Minutes */
            
            int minute = originaltimestamp.getMinute();
            int interval = d.getInterval();
            
            /* ... perform an interval round if needed ... */

            if ( minute % interval != 0 ) {
                
                int adjustedminute;

                if ((minute % interval) < (interval / 2)) {
                    adjustedminute = (Math.round(minute / interval) * interval);
                }

                else {
                    adjustedminute = (Math.round(minute / interval) * interval) + interval;
                }
                
                adjustedtimestamp = adjustedtimestamp.plusMinutes(adjustedminute - minute);                
                adjustedtimestamp = adjustedtimestamp.withSecond(0).withNano(0);
                
                adjustmenttype = AdjustmentType.INTERVAL_ROUND;

            }
            
            /* ... or else, leave the punch alone */

            else {
                adjustedtimestamp = adjustedtimestamp.withSecond(0).withNano(0);
                adjustmenttype = AdjustmentType.NONE;
            }

        }
        
    }
    
}