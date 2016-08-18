package com.disney.admin.jdbc;

import java.io.IOException;
import java.io.Reader;
import java.sql.*;

import java.util.*;


/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class ResultBean
{
    //~ Instance variables -------------------------------------------------------------------------

    private long connectionTime = 0;
    private long queryTime = 0;
    private List columnNameList = null;
    private List rowList = null;
    private long rowCount = 0;

    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Constructor for QueryResultBean
     */
    public ResultBean()
    {
        super ();
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * Gets the connectionTime
     * 
     * @return Returns a long
     */
    public long getConnectionTime()
    {
        return connectionTime;
    }


    /**
     * Sets the connectionTime
     * 
     * @param connectionTime The connectionTime to set
     */
    public void setConnectionTime(long connectionTime)
    {
        this.connectionTime = connectionTime;
    }


    /**
     * Gets the queryTime
     * 
     * @return Returns a long
     */
    public long getQueryTime()
    {
        return queryTime;
    }


    /**
     * Sets the queryTime
     * 
     * @param queryTime The queryTime to set
     */
    public void setQueryTime(long queryTime)
    {
        this.queryTime = queryTime;
    }


    /**
     * Sets the resultSet
     * 
     * @param resultSet The resultSet to set
     * @throws SQLException DOCUMENT ME!
     */
    public void setResultSet(ResultSet resultSet)
        throws SQLException, IOException
    {
        this.columnNameList = new ArrayList();

        ResultSetMetaData queryMeta = resultSet.getMetaData ();

        for (int colNum = 1; colNum <= queryMeta.getColumnCount (); colNum++)
        {
            this.columnNameList.add (queryMeta.getColumnName (colNum));
        }

        this.rowCount = 0;
        this.rowList = new ArrayList();

        while (resultSet.next ())
        {
            ArrayList thisRow = new ArrayList();

            for (int thisColNum = 1; thisColNum <= queryMeta.getColumnCount (); thisColNum++)
            {
            	Object obj = resultSet.getObject(thisColNum);
            	if (obj instanceof Clob)
            	{
					StringBuffer buff = new StringBuffer();
					Clob clob = (Clob)obj;
					Reader reader = clob.getCharacterStream();
	
					int num = 0;
					char[] ch = new char[1000];
					while ((num = reader.read(ch)) > 0)
					{
						buff.append(ch, 0, num);
					}
					
					thisRow.add(buff.toString());
            	}
            	else if (obj == null)
            	{
	                thisRow.add("null");
            	}
            	else
            	{
	                thisRow.add(obj.toString());
            	}
            }

            this.rowList.add (thisRow);
            this.rowCount++;
        }
    }


    /**
     * Gets the rowList
     * 
     * @return Returns a List
     */
    public List getRowList()
    {
        return rowList;
    }


    /**
     * Gets the columnNameList
     * 
     * @return Returns a List
     */
    public List getColumnNameList()
    {
        return columnNameList;
    }


    /**
     * Gets the rowCount
     * 
     * @return Returns a long
     */
    public long getRowCount()
    {
        return rowCount;
    }
}