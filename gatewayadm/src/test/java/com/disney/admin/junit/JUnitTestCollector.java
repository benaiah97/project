package com.disney.admin.junit;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import junit.framework.Test;
import junit.runner.BaseTestRunner;
import junit.runner.ClassPathTestCollector;
import junit.runner.TestCaseClassLoader;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;

/**
 * An implementation of a TestCollector that loads all classes on the class path and tests whether
 * it is assignable from Test or provides a static suite method.
 * 
 * @see junit.runner.LoadingTestCollector
 */
public class JUnitTestCollector extends ClassPathTestCollector
{
    //~ Static variables/initializers --------------------------------------------------------------

    static final int SUFFIX_LENGTH = ".class".length ();
    private static EventLogger evl = null;
    private static Class thisClass = JUnitTestCollector.class;
    private static ArrayList testList = new ArrayList();

    //~ Instance variables -------------------------------------------------------------------------

    TestCaseClassLoader fLoader;


    /**
     * DOCUMENT ME!
     */
    public JUnitTestCollector()
    {
        fLoader = new TestCaseClassLoader();
    }


    /**
     * Collect JUnit tests given a classpath and ear directory
     *
     * @param classPath String containing comma separated list of cp elements from manifest, admin.properties
     * @param separator Classpath separator
     * @param jarDirectory Directory containing jar files for EAR. For WSAD, the Code directory.
     *
     * @return Enumeration of Test elements
     */
    public Enumeration collectTests(String classPath, String separator, String jarDirectory)
    {
    	if (evl == null) {
    		evl = EventLogger.getLogger(this.getClass());
    	}
        //String classPath= System.getProperty("java.class.path");
        Hashtable result = new Hashtable();
        //System.out.println("collectTests: " + classPath + " " + separator + " " + jarDirectory);
        collectFilesInRootsWDW (splitClassPath (classPath, separator, jarDirectory), result);

        return result.elements ();
    }


    /**
     * Gets the class object from the class filename
     *
     * @param classFileName String containing the class file name (without directory
     *
     * @return Class object
     *
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    Class classFromFile(String classFileName) throws ClassNotFoundException
    {
        String className = classNameFromFile (classFileName);

        if (!fLoader.isExcluded (className))
        {
            return fLoader.loadClass (className, false);
        }

        return null;
    }


    /**
     * Check class to see if it is a JUnit class
     *
     * @param className String name of class
     *
     * @return true if JUnit class
     */
    boolean checkTestClass(String className)
    {
    	Class testClass = null;
        if ((className.indexOf ('$') < 0) && (className.indexOf ("junit") < 0) 
            && (className.indexOf ("Test") > 0))
	    {
		    //System.out.println("checking TestClass " + className);
	        try {
	        	testClass = Class.forName(className);
	            if ( (hasSuiteMethod(testClass)) || 
	            	( Test.class.isAssignableFrom(testClass)
	                && Modifier.isPublic(testClass.getModifiers ())
	                && hasPublicConstructor (testClass) ))
	            {
			    	//System.out.println("isTestClass " + className);
			    	return true;
	            }
	        } catch (ClassNotFoundException ex) {
		    	//System.out.println("isTestClass CNF " + className);
	            if (isTestClass (className))
	            {
			    	//System.out.println("isTestClass " + className);
	                return true;
	            }
	        }
	    }

        return false;
    }


    /**
     * Main method which prints out the EARDirectory and Tests given the Manifest classpath
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args)
    {
    	if (evl == null) {
    		evl = EventLogger.getLogger(thisClass);
    	}
    	System.out.println("EARDirectory=" + getEARDirectory ());
    	System.out.println("Manifest Tests=" + getManifestTests ());
        //System.out.println ("ManifestClasspath=" + getManifestClassPath ());
        //System.out.println ("EARDirectory=" + getEARDirectory ());
        //System.out.println ("Manifest Tests=" + getManifestTests ());
        //System.out.println ("Classpath Tests=" + getClasspathTests ());
    }


    /**
     * Get the JUnit tests from the manifest file contained within this application
     *
     * @return String comma separated list of JUnit tests found in the classpath.
     */
    public static String getManifestTests()
    {
    	if (evl == null) {
    		evl = EventLogger.getLogger(thisClass);
    	}
        String classPath = getManifestClassPath ();
        //System.out.println("classPath=" + classPath);
        String jarDirectory = getEARDirectory ();
        return getClasspathTests( classPath, jarDirectory );
        
        /* String separator = ",";
        ArrayList order = new ArrayList();
        Enumeration e = new JUnitTestCollector().collectTests (classPath, separator, jarDirectory);

        while (e.hasMoreElements ())
        {
            String next = (String)e.nextElement ();
            //System.out.println("MF Element=" + next);
	    	//System.out.println("MF Element=" + next);

            for (int i = 0; i < order.size (); i++)
            {
                if (next.compareToIgnoreCase ((String)order.get (i)) < 0)
                {
                    String temp = (String)order.get (i);
                    order.set (i, next);
                    next = temp;
                }
            }

            order.add (next);
        }

        Iterator it = order.iterator ();

        while (it.hasNext ())
        {
            result += ((String)it.next () + ", \\");
        }
        //System.out.println("result=" + result);

        if (result == "")
        {
            // JUnitTestCollector returned no tests - use eaihub.properties test list
            result = AbstractInitializer.getInitializer().getProps ("admin.properties")
                                .getProperty ("JUNIT_TEST_CLASSES", 
                                              "com.disney.jdbc.ConnectionUnitTest,com.disney.jms.JmsUnitTest,com.disney.logging.EventLoggerTest,com.disney.msgstats.dao.MessageStatsUnitTest,com.disney.util.ElapsedTimeTest");
        }

        return result;
        */
    }


    /**
     * Get the JUnit Tests from a ClassPath and jar Directory
     *
     * @return String comma separated list of JUnit tests
     */
    public static String getClasspathTests(String classPath, String jarDirectory)
    {
    	if (evl == null) {
    		evl = EventLogger.getLogger(thisClass);
    	}
        String result = "";
        ArrayList order = new ArrayList();
        /* String classPath = getManifestClassPath ();
        System.out.println("classPath=" + classPath);
        String jarDirectory = getEARDirectory ();
        */
        String separator = ",";
        Enumeration e = new JUnitTestCollector().collectTests (classPath, separator, jarDirectory);

        while (e.hasMoreElements ())
        {
            String next = (String)e.nextElement ();
            //System.out.println("CP Element=" + next);

            for (int i = 0; i < order.size (); i++)
            {
                if (next.compareToIgnoreCase ((String)order.get (i)) < 0)
                {
                    String temp = (String)order.get (i);
                    order.set (i, next);
                    next = temp;
                }
            }

            order.add (next);
        }

        Iterator it = order.iterator ();

        while (it.hasNext ())
        {
            result += ((String)it.next () + ", \\");
        }

        return result;
    }


    /**
     * Get the classloader ear installation directory
     * 
     * @return String EARDirectory - Directory under which the enterprise application is installed
     */
    public static String getEARDirectory()
    {
    	if (evl == null) {
    		evl = EventLogger.getLogger(thisClass);
    	}
        String earDirectory = null;

        try
        {
            Class theClass = (new JUnitTestCollector()).getClass ();
            String className = theClass.getName ().replace ('.', '/');
            String classResource = theClass.getResource ("/" + className + ".class").toString ();

            if (classResource.indexOf(".ear")>-1)
            {
            	classResource = classResource.substring(0, classResource.indexOf(".ear") + 5);
            } else if (classResource.indexOf(".war")>-1)
            {
            	classResource = classResource.substring(0, classResource.indexOf(".war"));
            	classResource = classResource.substring(0, classResource.lastIndexOf("/"));
            }
            if (classResource.startsWith ("file:"))
            {
                // This class is not loaded from a jar file, just from the file system directly. The file location will look like:
                // file:/C:/Wprog/DD/RJF3_EAIHub3/EAI_Hub/Code/FoundationWeb/webApplication/WEB-INF/classes/com/wdw/eai/admin/junit/JUnitTestCollector.class
                if (classResource.startsWith ("file:/C:"))
                {
                    // Windows appends an extra /C: slash in front of directory
                    earDirectory = classResource.substring (6, classResource.length());
                }
                else
                {
                    earDirectory = classResource.substring (5, classResource.length());
                }
                // classResource represents directory spaces as %20. Stupid Microsoft.
                while (earDirectory.indexOf("%20")>-1) {
	                earDirectory = earDirectory.substring(0, earDirectory.indexOf("%20")) + " " + earDirectory.substring(earDirectory.indexOf("%20")+3, earDirectory.length());
                }
                //earDirectory=earDirectory.substring(0, earDirectory.indexOf("/com"));
            }
            else if (classResource.startsWith ("jar:"))
            {
                InputStream is = theClass.getResourceAsStream ("META-INF/MANIFEST.MF");
            }

            if (earDirectory == null)
            {
                earDirectory = AbstractInitializer.getInitializer().getProps ("admin.properties")
                                          .getProperty ("JUNIT_JAR_DIRECTORY", 
                                                        "/opt/apps/WebSphere/AppServer/installedApps/EAIWave3.ear/");
            }
        }
        catch (Throwable t)
        {
            earDirectory = AbstractInitializer.getInitializer().getProps ("admin.properties")
                                      .getProperty ("JUNIT_JAR_DIRECTORY", 
                                                    "/opt/apps/WebSphere/AppServer/installedApps/EAIWave3.ear/");
        }
    	//System.out.println("earDirectory="+earDirectory);

        return earDirectory;
    }

    /**
     * Get the classpath jar list from the current classloader manifest file
     * 
     * @return String ClassPath - Comma delimited classpath list from war MANIFEST.MF file
     */
    public static String getManifestClassPath()
    {
    	if (evl == null) {
    		evl = EventLogger.getLogger(thisClass);
    	}
        InputStream is = null;
        String cp = "";

        try
        {
            Class theClass = (new JUnitTestCollector()).getClass ();
            String className = theClass.getName ().replace ('.', '/');
            String classResource = theClass.getResource ("/" + className + ".class").toString ();
            //System.out.println("classResource=" + classResource);
	    	//System.out.println("classResource=" + classResource);
            String warDirectory = null;

            if (classResource.startsWith ("file:"))
            {
                // This class is not loaded from a jar file, just from the file system directly. 
                // The file location will look like:
                // file:/C:/Wprog/DD/RJF3_EAIHub3/EAI_Hub/Code/FoundationWeb/webApplication/WEB-INF/classes/com/wdw/eai/admin/junit/JUnitTestCollector.class
                if (classResource.startsWith ("file:/C:"))
                {
                    // Windows appends an extra /C: slash in front of directory
                    warDirectory = classResource.substring (6, classResource.indexOf ("WEB-INF"));
                }
                else
                {
                    warDirectory = classResource.substring (5, classResource.indexOf ("WEB-INF"));
                }
                // classResource represents directory spaces as %20. Stupid Microsoft.
                while (warDirectory.indexOf("%20")>-1) {
	                warDirectory = warDirectory.substring(0, warDirectory.indexOf("%20")) + " " + warDirectory.substring(warDirectory.indexOf("%20")+3, warDirectory.length());
                }

				try {
	                is = (InputStream)new FileInputStream(warDirectory + "META-INF/MANIFEST.MF");
				} catch (Throwable t) {
					// Invalid file - ignore
				}
				// Make sure the classes directory for this war file is searched for TestCases.
				cp = warDirectory + "WEB-INF/classes/,";
            }
            else if (classResource.startsWith ("jar:"))
            {
            	try {
	                is = theClass.getResourceAsStream ("/META-INF/MANIFEST.MF");
            	} catch (Throwable t) {
            		// Invalid manifest file - ignore
            	}
            }

			if (is != null) {
	            Manifest mf = new Manifest(is);
	            Attributes map = mf.getMainAttributes ();
	            cp = cp + map.getValue ("Class-Path").replace (' ', ',');
	            try
	            {
	                is.close ();
	            }
	            catch (Exception ex) {}
			}

	    	//System.out.println("cp="+cp);

        	// Application has not been loaded from deployed environment, loaded from WSAD
            // Find the code directory, assume jar file name == Project name
            Vector result = findOtherProjects(cp, warDirectory, null);
            // Now we can look through the other project references at runtime. Since there's no
            // easy way to retrieve the runtime classpath, we just look up the info in the
            // admin.prioperties file
            String projects = AbstractInitializer.getInitializer().getProps ("admin.properties")
                        .getProperty ("JUNIT_PROJECTS", "SharedCore.jar,SharedJdbc.jar,SharedJms.jar");
            result = findOtherProjects(projects, warDirectory, result);
            
            if (result.size() > 0) {
            	// Put result entries into cp
            	Enumeration e = result.elements();
            	while (e.hasMoreElements()) {
            		String cpEntry = (String) e.nextElement();
            		if (cp.indexOf(cpEntry)<0) {
            			cp +=  "," + cpEntry;
            		}
            	}
            }

            if (cp != null)
            {
                return cp;
            }
            else
            {
                cp = AbstractInitializer.getInitializer().getProps ("admin.properties")
                                .getProperty ("JUNIT_CLASS_PATH", 
                                              "SharedCore.jar,SharedJdbc.jar,SharedJms.jar");
                return cp;
            }
        }
        catch (Throwable t)
        {
        	if (cp==null) {
	            // no manifest file or Version entry
	            return System.getProperty ("java.class.path");
        	} else {
        		return cp;
        	}
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close ();
                }
                catch (Throwable t) {}
            }
        }
    }

    /**
     * Add entries to a Vector from a comma delimited project list from WSAD.
     *
     * @param String cp Classpath to be searched for classes
     * @param Vector result Existing or new Vector of valid class directories
     *
     * @return Vector of directories containing classes
     */
    static Vector findOtherProjects(String cp, String warDirectory, Vector result)
    {
    	if (result == null) {
	        result = new Vector();
    	}
        StringTokenizer tokenizer = new StringTokenizer(cp, ",");
    	// WSAD returns forward slash, not backslash, for directory name
    	if (warDirectory.endsWith("/")) {
    		warDirectory = warDirectory.substring(0, warDirectory.length()-1);
    	}
    	if (warDirectory.indexOf(".ear")>0) {
    		warDirectory = warDirectory.substring(0, warDirectory.indexOf(".ear") + 5);
    	} else if (warDirectory.indexOf(".jar")<0 && warDirectory.indexOf(".war")<0)
        {
        	// Class has been loaded from WSAD or dev environment. Assume project name
        	// is same as jar/war name. Remove those extensions. warDirectory = code directory
        	// WSAD adds an extra directory level (Web Content) below project name

	        // Remove the last two directories from current project WebContent to find WSAD projects directory.
	        warDirectory = warDirectory.substring(0, warDirectory.lastIndexOf("/"));
	        warDirectory = warDirectory.substring(0, warDirectory.lastIndexOf("/")) + "/";
        }
        //System.out.println("findOtherProjects warDirectory " + warDirectory);
        try {
	        while (tokenizer.hasMoreTokens())
	        {
	            String classDir = tokenizer.nextToken ();
	            // Assume Project directory is the same as the jar file name
	            if (classDir.endsWith(".jar")) {
	            	classDir = classDir.substring(0, classDir.indexOf(".jar"));
	            }
	        	// Remove the .jar, .war extension if in WSAD. This allows other war files
	        	// to be referenced without breaking the manifest file. Assumes that the war/jar
	        	// file name is the same as the WSAD project name.
		        if (warDirectory.indexOf(".jar")<0 && warDirectory.indexOf(".ear")<0 
		           && classDir.endsWith(".war")) {
	            	classDir = classDir.substring(0, classDir.indexOf(".war"));
		        }
		        //System.out.println("findOtherProjects " + warDirectory + classDir);
	            File tokenDir = new File(warDirectory + classDir);
	            if (tokenDir.isDirectory())
	            {
	            	tokenDir = new File(warDirectory + classDir + "/bin");
		            if (tokenDir.isDirectory())
		            {
		            	// Java project with src and bin directories
			            result.addElement (warDirectory + classDir + "/bin");
		            } else {
		            	tokenDir = new File(warDirectory + classDir + "/com");
			            if (tokenDir.isDirectory())
			            {
			            	// Java project with src and bin directories = top level directory
				            result.addElement (warDirectory + classDir);
			            } else {
			            	tokenDir = new File(warDirectory + classDir + "/webApplication/WEB-INF/classes");
				            if (tokenDir.isDirectory())
				            {
				            	// Web project with webApplication directories
					            result.addElement (warDirectory + classDir + "/webApplication/WEB-INF/classes");
				            } else {
				            	tokenDir = new File(warDirectory + classDir + "/Web Content/WEB-INF/classes");
					            if (tokenDir.isDirectory())
					            {
					            	// Web project with webApplication directories
						            result.addElement (warDirectory + classDir + "/Web Content/WEB-INF/classes");
					            } else {
					            	tokenDir = new File(warDirectory + classDir + "/WEB-INF/classes");
						            if (tokenDir.isDirectory())
						            {
						            	// Web project with webApplication directories
							            result.addElement (warDirectory + classDir + "/WEB-INF/classes");
						            } else {
						            	// No corresponding WSAD project found for manifest entry
						            	System.out.println("No project found for " + classDir);
						            }
					            }
				            }
			            }
		            }
	            }
	        } 
        } catch (Exception ex) {
        	// Stop processing after errors, but return what we already have.
        	evl.sendException("findWSADProjects", EventType.EXCEPTION, ErrorCode.GENERAL_EXCEPTION, ex, thisClass);
        }
        return result;
    }
    
    /**
     * Determine if a class is a JUnit class - if it has a Suite() method
     *
     * @param testClass Class to be checked
     *
     * @return boolean true = JUnit suite test class
     */
    boolean hasSuiteMethod(Class testClass)
    {
        try
        {
            testClass.getMethod (BaseTestRunner.SUITE_METHODNAME, new Class[0]);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }


    /**
     * See if the class has a public contsructor
     *
     * @param testClass Class to be checked
     *
     * @return true if class has a public constructor
     */
    boolean hasPublicConstructor(Class testClass)
    {
        Class[] args = 
        {
            String.class
        };

        try
        {
            testClass.getConstructor (args);
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }


    /**
     * Collect files for the entire classpath which extend JUnit Test Case
     *
     * @param roots Individual classpath elements
     * @param result Hashtable of all results
     */
    void collectFilesInRootsWDW(Vector roots, Hashtable result)
    {
        Enumeration e = roots.elements ();

        while (e.hasMoreElements ())
        {
            String fileName = (String)e.nextElement ();
            if (fileName.endsWith(".war"))
            {
            	// Though war files can't be referenced in classpath, allow them there
            	// anyway so that JUnit runner can find all the tests.
            	fileName += "/WEB-INF/classes";
            }
	    	//System.out.println("collectFilesInRoots " + fileName);
            gatherFilesWDW (new File(fileName), "", result);
        }
    }


    /**
     * Find classes which are test cases
     *
     * @param classRoot Classpath root
     * @param classFileName Class package and filename
     * @param result Hashtable of classes which are test cases
     */
    void gatherFilesWDW(File classRoot, String classFileName, Hashtable result)
    {
    	if (evl == null) {
    		evl = EventLogger.getLogger(thisClass);
    	}
        File thisRoot = null;
    	if (classFileName==null || classFileName.length()<1) {
    		thisRoot = classRoot;
    	} else {
	        thisRoot = new File(classRoot, classFileName);
    	}
    	if (thisRoot.isDirectory())
    	{
	        //System.out.println("gatherFilesWDW "+classRoot.getName() + " " + classFileName);
	    	//System.out.println("gatherFilesWDW Dir: "+thisRoot.getAbsolutePath());
            // Entry is a directory, or else file doesn't exist.
            String[] contents = thisRoot.list ();

            if (contents != null)
            {
                for (int i = 0; i < contents.length; i++)
                {
                    gatherFilesWDW (classRoot, classFileName + File.separatorChar + contents[i], 
                                 result);
                }
            }
    	}
    	else
    	{
	        //System.out.println("gatherFilesWDW "+classRoot.getName() + " " + classFileName);
	    	//System.out.println("gatherFilesWDW " + classFileName);
	
	        if (thisRoot.isFile ())
	        {
	            if (thisRoot.getName ().indexOf (".jar") > 0)
	            {
	                // File is a jar file - get contents
	                JarFile jarFile = null;
	
	                try
	                {
	                    jarFile = new JarFile(thisRoot);
	                }
	                catch (Throwable t)
	                {
	                    // Jar file doesn't exist - punt
	                    return;
	                }
	
	                Enumeration e = jarFile.entries ();
	
	                while (e.hasMoreElements ())
	                {
	                    String jarEntry = ((JarEntry)e.nextElement ()).getName ();
	
                        String className = classNameFromFile (jarEntry).replace ('/', '.');
	                    if (checkTestClass (className))
	                    {
	                        result.put (className, className);
	                    }
	                }
	            }
	            else
	            {
                    String className = classNameFromFile (classFileName);
	                if (checkTestClass (className))
	                {
	                    result.put (className, className);
	                }
	            }
	
	            return;
	        }
        }
    }

    /**
     * Returns a vector of jars/directories given a classpath and a jar directory
     *
     * @param classPath Comma separated list of classpath elements
     * @param separator Classpath element separator
     * @param jarDirectory Directory containing jar files in EAR application path (EnterpriseApp.ear)
     *
     * @return Vector of jars/directories in classpath
     */
    Vector splitClassPath(String classPath, String separator, String jarDirectory)
    {
        Vector result = new Vector();
        StringTokenizer tokenizer = new StringTokenizer(classPath, separator);

        while (tokenizer.hasMoreTokens ())
        {
            String token = tokenizer.nextToken ();
            if (token.indexOf(".jar") > -1) {
            	token = jarDirectory + token;
            }
            result.addElement (token);
        }

        return result;
    }
}