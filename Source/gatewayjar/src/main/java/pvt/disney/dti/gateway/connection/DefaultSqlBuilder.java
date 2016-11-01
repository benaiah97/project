package pvt.disney.dti.gateway.connection;

import com.disney.exception.WrappedException;

/**
 * This is the default class that is used to build the different SQL statements. The behavior is
 * only to return the passed in statement from the properties file.
 */
public class DefaultSqlBuilder implements DeleteBuilder, QueryBuilder, UpdateBuilder, InsertBuilder
{
    //~ Methods ------------------------------------------------------------------------------------

    /**
     * @param propertiesFileQuery String SQL Query string from datasource.properties file
     * @param queryParameters Object[] Array with first element (String) to be appended to default query
     * @return String SQL Query result
     * Return default SQL query if null queryParameters.
     * Return default SQL + String queryParameters[0] if parameter provided
     * @see QueryBuilder#getQuery(String, Object[])
     */
    public String getQuery(String propertiesFileQuery, Object[] queryParameters)
        throws WrappedException
    {
        if (queryParameters == null)
        {
            return propertiesFileQuery;
        }
        else
        {
            // Assume the values should be appended to the sql string dynamically
            try
            {
                String append = (String)queryParameters[0];
                return propertiesFileQuery + append;
            }
            catch (ClassCastException ex)
            {
                // object value was not a string
                return propertiesFileQuery;
            }
        }
    }


    /**
     * @see UpdateBuilder#getUpdate(String, Object[])
     */
    public String getUpdate(String propertiesFileUpdate, Object[] updateParameters)
        throws WrappedException
    {
        return propertiesFileUpdate;
    }


    /**
     * @see DeleteBuilder#getDelete(String, Object[])
     */
    public String getDelete(String propertiesFileDelete, Object[] deleteParameters)
        throws WrappedException
    {
        return propertiesFileDelete;
    }


    /**
     * @see InsertBuilder#getInsert(String, Object[])
     */
    public String getInsert(String propertiesFileInsert, Object[] insertParameters)
        throws WrappedException
    {
        return propertiesFileInsert;
    }
}