
package edu.jsu.mcis.cs415.teamproject;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.jsu.mcis.cs415.tas_fa21_v2.*;

public class IndividualPunch extends HttpServlet {
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            
            // Get Reference to TASDatabase Bean
            TASDatabase db = (TASDatabase)request.getSession().getAttribute("db");
            
            // Get "punchid" parameter
            int punchid = Integer.parseInt(request.getParameter("punchid"));

            // Get individual Punch 
            Punch punch = db.getPunch(punchid);
            
            // Set punch adjustment 
            punch.adjust(db.getShift(punch.getBadge()));

            // Send punch object as JSON to Client
            String jsonString = TAS.getPunchAsJSON(punch);
            
            // Send to client as JSON string
            out.println(jsonString);
            
        }
        catch (Exception e) { e.printStackTrace(); }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
    }
    
     @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       doPost(request,response);
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    response.setContentType("application/json;charset=UTF-8");

    try(PrintWriter out = response.getWriter()){
     
            // acknowledge the TASDatabase bean
            TASDatabase db = (TASDatabase)request.getSession().getAttribute("db");
            
            // get "punchid" parameter
            int punchid = Integer.parseInt(request.getParameter("punchid"));

            // pass the punchid to the deletePunch method and store return value (a boolean) into the boolean variable called dp (if no records were affected, returns false; if one record was affected, returns true)
            boolean dp = db.deletePunch(punchid);
            
            // confirm if the delete was successful and store in JSON object
            String jsonString = "[{\"success\": " + dp + "}]";

          //return the JSON object to the client
          out.println(jsonString);
     
    } //end of try block
     
      catch (Exception e) { e.printStackTrace();}
     
    } //end of doDelete() method
    

    @Override
    public String getServletInfo() {
        return "Handles Individual Punches. Accepts GET, POST, PUT, DELETE requests.";
    }

}
