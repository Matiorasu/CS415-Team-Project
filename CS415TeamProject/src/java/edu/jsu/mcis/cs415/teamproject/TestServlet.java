package edu.jsu.mcis.cs415.teamproject;

import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import edu.jsu.mcis.cs415.tas_fa21_v2.*;

public class TestServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        
        response.setContentType("application/json;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            
            /* Create TASDatabase (uses JDBC pool "jdbc/cs415_teamproject_dbpool") */
            
            TASDatabase db = new TASDatabase();
            
            /* Run JSON Unit Test from "Feature5.java" */
            
            Punch p = db.getPunch(3634);
            Badge b = db.getBadge(p.getBadge().getId());
            Shift s = db.getShift(b);

            /* Get Daily Punch List */

            ArrayList<Punch> dailypunchlist = db.getDailyPunchList(b, p.getOriginaltimestamp().toLocalDate(), s);

            /* JSON Conversion */

            String json = TAS.getPunchListAsJSON(dailypunchlist);
            
            /* Send JSON to Client */
            
            out.println(json);
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        
        processRequest(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        
        processRequest(request, response);
        
    }

    @Override
    public String getServletInfo() {
        return "Test Servlet";
    }

}