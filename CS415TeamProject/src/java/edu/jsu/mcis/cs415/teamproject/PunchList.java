package edu.jsu.mcis.cs415.teamproject;

import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.jsu.mcis.cs415.tas_fa21_v2.*;
import java.time.*;
import java.util.ArrayList;

public class PunchList extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        
        response.setContentType("application/json;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            
            // Get Reference to TASDatabase Bean
            
            TASDatabase db = (TASDatabase)request.getSession().getAttribute("db");
            
            // Get "employeeid" and "payperiod" Parameters
            
            int employeeid = Integer.parseInt(request.getParameter("employeeid"));
            LocalDate payperiod = LocalDate.parse(request.getParameter("payperiod"));
            
            // Get Shift Ruleset
            
            Shift shift = db.getShift(db.getEmployee(employeeid).getShiftid());
            
            // Get Punch List (as JSON)
            
            ArrayList<Punch> punchlist = db.getPayPeriodPunchList(employeeid, payperiod);
            
            // Send JSON to Client
            
            out.println( TAS.getPunchListPlusTotalsAsJSON(punchlist, shift) );
            
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Punch List Servlet";
    }

}
