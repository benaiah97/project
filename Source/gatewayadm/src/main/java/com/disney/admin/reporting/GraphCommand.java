package com.disney.admin.reporting;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.admin.AdminCommand;
import com.disney.graph.Graph;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;


/**
 * Graphing Admin Command
 * 
 * @version 1.0
 * @author fav2
 */
public class GraphCommand extends AdminCommand
{
    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Constructor for GraphServlet
     */
    public GraphCommand()
    {
        super ();
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet
     * @param response Object that encapsulates the response from the servlet
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
    {
        String graphXml = "";
        try
        {
            graphXml = request.getParameter("GRAPH");
            if (graphXml != null)
            {
                evl.sendEvent ("Attempting to create JPEG..", EventType.DEBUG, this);

                // Get Graph Object
                Graph graph = Graph.parseXML(graphXml);

                javax.servlet.ServletOutputStream out = response.getOutputStream();
                graph.drawImage(out);
                out.flush();
                out.close();
            }
        }
        catch (Throwable th)
        {
            evl.sendException ("Error trying to graph: ", EventType.EXCEPTION, 
                  ErrorCode.APPLICATION_EXCEPTION, th, this, graphXml, null);
        }
        
        return null;
    }
}