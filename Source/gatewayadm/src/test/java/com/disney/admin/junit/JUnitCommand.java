package com.disney.admin.junit;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.admin.SharedAdminCommand;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.Loader;


/**
 * JUnit Admin Command
 *
 * @version 1.0
 * @author fav2
 */
public class JUnitCommand extends SharedAdminCommand
{
    //~ Instance variables -------------------------------------------------------------------------

    private ArrayList tests = null;

    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet
     * @param response Object that encapsulates the response from the servlet
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
    {
        String currentTest = "";
        try
        {
            String action = request.getParameter ("ACTION");
            if (action == null)
            {
                action = "";
            }

            if (tests == null)
            {
                //String junits = Initializer.getInstance().getProps("admin.properties").getProperty("JUNIT_TEST_CLASSES", "");
                // Get listing of junit tests from the classpath (it uses Manifest.mf in Web Application war)
                //String classPath= Initializer.getInstance().getProps("admin.properties").getProperty("JUNIT_CLASS_PATH", 
                //"ATSAdapter.jar,ATSAdapterEJB.jar,CRMAdapter.jar,CRMAdapterEJB.jar,CRSAdapter.jar,CRSAdapterEJB.jar,DAOUtilities.jar,DomainMessagesApp.jar,Foundation.jar,FoundationEJB.jar,FunctionalBroker.jar,LogibroAdapter.jar,LogibroAdapterEJB.jar,OdsAdapter.jar,OdsAdapterEJB.jar");
                //String separator= System.getProperty("path.separator");
                //String jarDirectory= Initializer.getInstance().getProps("admin.properties").getProperty("JUNIT_JAR_DIRECTORY", 
                //	"/opt/apps/WebSphere/AppServer/installedApps/EAIWave3.ear/");
                String separator = ",";
                JUnitTestCollector tc = new JUnitTestCollector();
                Enumeration e = tc.collectTests (JUnitTestCollector.getManifestClassPath (), separator, 
                    JUnitTestCollector.getEARDirectory ());
                String junits = "";

                while (e.hasMoreElements ())
                {
                    junits += ((String)e.nextElement () + ",");
                }
                evl.sendEvent("junits=" + junits, EventType.DEBUG, this);

                if (junits == "")
                {
                    // JUnitTestCollector returned no tests - use admin.properties test list
                    junits = AbstractInitializer.getInitializer().getProps ("admin.properties")
                                .getProperty ("JUNIT_TEST_CLASSES", 
                                              "com.disney.jdbc.ConnectionUnitTest,com.disney.jms.JmsUnitTest,com.disney.logging.EventLoggerTest,com.disney.msgstats.dao.MessageStatsUnitTest,com.disney.util.ElapsedTimeTest");
                }

                tests = createList (junits);
            }

            currentTest = request.getParameter ("TEST_NAME");

            if (currentTest == null)
            {
                currentTest = "";
            }

            if (action.equalsIgnoreCase ("RUN"))
            {
                evl.sendEvent("About to Redirect StdOut and run JUnit test.", EventType.DEBUG, this);

                // Redirecting stdout
                java.io.ByteArrayOutputStream byteOut = new java.io.ByteArrayOutputStream();
                java.io.PrintStream tmpOut = System.out;
                System.setOut (new java.io.PrintStream(byteOut));

                // Redirecting stderr
                java.io.ByteArrayOutputStream byteErr = new java.io.ByteArrayOutputStream();
                java.io.PrintStream tmpErr = System.err;
                System.setErr (new java.io.PrintStream(byteErr));

                try {
	                // Run the JUnit Test
	                Class junitClass = Loader.loadClass(currentTest);
	                junit.textui.TestRunner.run (junitClass);
                }
                catch (Exception ex)
                {
                	System.out.println( ex );
                	ex.printStackTrace( tmpOut );
                }

                // Restoring stdout
                System.setOut (tmpOut);


                // Restoring stderr
                System.setErr (tmpErr);

                evl.sendEvent ("JUnit Test finished, restored StdOut.", EventType.DEBUG, this);

                request.setAttribute ("stdout", insertBR (byteOut.toString ()));
                request.setAttribute ("stderr", insertBR (byteErr.toString ()));
            }
        }
        catch (Throwable th)
        {
            evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this);
            request.setAttribute ("throwable", th);
        }
        finally
        {
            request.setAttribute ("tests", tests);
            request.setAttribute ("currentTest", currentTest);
        }
        // Testing purposes
        tests = null;
        return "JunitMain.jsp";
    }


    /**
     * DOCUMENT ME!
     *
     * @param commaList DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList createList(String commaList)
    {
        ArrayList list = new ArrayList();
        StringTokenizer st = new StringTokenizer(commaList, ",");

        while (st.hasMoreTokens ())
        {
            String next = st.nextToken ().trim ();

            for (int i = 0; i < list.size (); i++)
            {
                if (next.compareToIgnoreCase ((String)list.get (i)) < 0)
                {
                    String temp = (String)list.get (i);
                    list.set (i, next);
                    next = temp;
                }
            }

            list.add (next);
        }

        return list;
    }


    /**
     * DOCUMENT ME!
     *
     * @param input DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws java.io.IOException DOCUMENT ME!
     */
    public String insertBR(String input) throws java.io.IOException
    {
        String output = "";
        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.StringReader(input));
        String eachLine = null;

        while ((eachLine = in.readLine ()) != null)
        {
            output += (eachLine + "<BR>\n");
        }

        in.close ();

        return output;
    }
}