package com.disney.admin.jms;

import com.disney.jms.*;

/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class JmsDetailsBean
{
    //~ Instance variables -------------------------------------------------------------------------

    private String jmsDefName = null;
    private JmsDefinition jmsDef = null;
    private JmsBrowser jmsBrowser = null;

    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Constructor for JmsDetailsBean
     */
    public JmsDetailsBean() {}


    /**
     * Constructor for JmsDetailsBean
     * @param jmsDefName DOCUMENT ME!
     * @throws JmsException DOCUMENT ME!
     */
    public JmsDetailsBean(String jmsDefName) throws JmsException
    {
        this.jmsDefName = jmsDefName;
        this.jmsDef = JmsDefinition.getInstance (jmsDefName);
        this.jmsBrowser = new JmsBrowser(jmsDefName);
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * Gets the jmsDef
     * 
     * @return Returns a JmsDefinition
     */
    public JmsDefinition getJmsDef()
    {
        return jmsDef;
    }


    /**
     * Gets the jmsDefName
     * 
     * @return Returns a String
     */
    public String getJmsDefName()
    {
        return jmsDefName;
    }


    /**
     * Gets the jmsBrowser
     * 
     * @return Returns a JmsBrowser
     */
    public JmsBrowser getJmsBrowser()
    {
        return jmsBrowser;
    }
}