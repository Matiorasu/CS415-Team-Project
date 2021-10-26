package edu.jsu.mcis.cs415.tas_fa21_v2;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;

public class Absenteeism {
    
    private final Badge badge;
    private final LocalDate payperiod;
    private double percentage;

    public Absenteeism(Badge badge, LocalDate payperiod, double percentage) {
        
        this.badge = badge;
        this.percentage = percentage;
        
        /* Roll Pay Period Date Back to Sunday */
        
        TemporalField fieldUS = WeekFields.of(Locale.US).dayOfWeek();
        this.payperiod = (payperiod.with(fieldUS, Calendar.SUNDAY));
        
    }
    
    @Override
    public String toString() {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        
        StringBuilder s = new StringBuilder();
        
        s.append("#");
        s.append(badge.getId());
        s.append(" (Pay Period Starting ");
        
        s.append(dtf.format(payperiod));
        
        s.append("): ");
        s.append(String.format("%.2f%%", percentage));
        
        return (s.toString());
        
    }

    public Badge getBadge() {
        return badge;
    }

    public LocalDate getPayperiod() {
        return payperiod;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    
}