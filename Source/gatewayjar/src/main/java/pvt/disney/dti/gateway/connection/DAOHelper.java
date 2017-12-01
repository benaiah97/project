package pvt.disney.dti.gateway.connection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.disney.exception.WrappedException;

import pvt.disney.dti.gateway.connection.ConnectionException;
import pvt.disney.dti.gateway.connection.ConnectionManager;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.Loader;
import com.disney.util.PropertyHelper;

/**
 * This class moves all the common logic for running a PreparedStatement to a central place.  It
 * has methods for executing queries, updates, inserts, and deletes.  There are three methods for
 * each, which correspond to varying access into the system.  The first method allows you to set
 * the input values for the prepared statement only.  The second method allows you to pass in the
 * input values as well as parameters for a dynamic SQL builder interface which is specified in a
 * property file.  The third method allows you to specify the actual dynamic SQL builder interface
 * which will be called.
 * 
 * <p>
 * This class uses the dataaccess.properties file for configuring the data.  It is based around a
 * <b>data-access name</b> which is passed in the getInstance method.  The format of this file
 * looks like this:
 * </p>
 * 
 * <p>
 * <code><b>[data-access name]</b>_DATA_SOURCE=[jdbc.properties dataSourceName]</code> The datasource
 * name for the SQL operation <i>REQUIRED</i>
 * </p>
 * 
 * <p>
 * <code><b>[data-access name]</b>_ARGUMENTS=[int number]</code> The number of PreparedStatement arguments
 * for the SQL operation <i>REQUIRED</i>
 * </p>
 * 
 * <p>
 * <code><b>[data-access name]</b>_QUERY=[SQL Statement String]</code> The SQL to be executed by the DAOHelper.
 * If using a QueryBuilder, the base SQL to be modified<i>REQUIRED</i>
 * </p>
 * 
 * <p>
 * <code><b>[data-access name]</b>_RESULT_SET_PROCESSOR_CLASS=[java package.classname]</code> The Java class
 * which implements ResultSetProcessor, to map ResultSet values to ValueObject attributes.<i>REQUIRED</i>
 * </p>
 * 
 * <p>
 * <code><b>[data-access name]</b>_[QUERY | DELETE | UPDATE | INSERT]_BUILDER_CLASS=[java package.classname]</code>
 * The Java class which implements QueryBuilder | DeleteBuilder | UpdateBuilder | InsertBuilder, allows a SQL 
 * statement to be dynamically built based on run-time parameters.<i>OPTIONAL - default = pvt.disney.dti.gateway.connection.DefaultSqlBuilder</i>
 * </p>
 * 
 * <p>
 * <code><b>[data-access name]</b>_EXCEPTION=[true | false]</code> Should SQL reaise an exception if
 * a result set is expected but none returned?<i>OPTIONAL - default = false</i>
 * </p>
 * 
 * <p>
 * <code><b>[data-access name]</b>_AUDIT_ACCESS=[true | false]</code> Should SQL operation be audited in
 * the application logs?<i>OPTIONAL - default = false</i>
 * </p>
 * 
 * <p>
 * <code><b>[data-access name]</b>_AUTOCOMMIT=[true | false]</code> Set Autocommit=true before performing
 * the SQL operation? Requires passing in a connection reference and setting it to false at a later point
 * <i>OPTIONAL - default = false</i>
 * </p>
 * 
 * <p>
 * <code><b>[data-access name]</b>_IS_STORED_PROC=[true | false]</code> Is the SQL operation a stored 
 * procedure (CallableStatement)? <i>OPTIONAL - default = false</i>
 * </p>
 * 
 * <p>
 * <code><b>[data-access name]</b>_IS_CACHED=[true | false]</code> whether or not this data-access
 * should be cached - <i>OPTIONAL - default = false</i>
 * </p>
 * 
 * <p>
 * <code><b>[data-access name]</b>_REFRESH_PRIMARY_CACHE=[true | false]</code>- whether or not the
 * primary cache should ever be refreshed (a refresh of this dumps all cached objects)-
 * <i>OPTIONAL - default = false</i>
 * </p>
 * 
 * <p>
 * <code><b>[data-access name]</b>_REFRESH_PRIMARY_CACHE_INTERVAL = ?</code>-time in milliseconds
 * between cache refreshing - <i>OPTIONAL - default = 1 hour</i>
 * </p>
 * 
 * <p>
 * <code><b>[data-access name]</b>_REFRESH_CACHED_OBJECT= [true | false]</code>0 whether or not the
 * cached object should be refreshed - <i>OPTIONAL - default = false</i>
 * </p>
 * 
 * <p>
 * <code><b>[data-access name]</b>_REFRESH_CACHED_OBJECT_INTERVAL = ?</code>-time in milliseconds
 * between cached object refreshing - <i>OPTIONAL - default = 1/2 hour</i>
 * </p>
 * 
 * @see ResultSetProcessor
 * @see QueryBuilder
 * @see InsertBuilder
 * @see UpdateBuilder
 * @see DeleteBuilder
 */
@SuppressWarnings("rawtypes")
public class DAOHelper
{
    //~ Static variables/initializers --------------------------------------------------------------

    private static final String DATA_ACCESS_PROPERTIES = "dataaccess.properties";
    private static final String DATA_SOURCE = "DATA_SOURCE";
    private static final String EXCEPTION = "EXCEPTION";
    private static final String AUDIT_ACCESS = "AUDIT_ACCESS";
    private static final String AUTOCOMMIT = "AUTOCOMMIT";
    private static final String IS_STORED_PROC = "IS_STORED_PROC";
    private static final String QUERY_BUILDER_CLASS = "QUERY_BUILDER_CLASS";
    private static final String DELETE_BUILDER_CLASS = "DELETE_BUILDER_CLASS";
    private static final String UPDATE_BUILDER_CLASS = "UPDATE_BUILDER_CLASS";
    private static final String INSERT_BUILDER_CLASS = "INSERT_BUILDER_CLASS";
    private static final String RESULT_SET_PROCESSOR_CLASS = "RESULT_SET_PROCESSOR_CLASS";
    private static final String DEFAULT_QUERY_BUILDER_CLASS =
        "pvt.disney.dti.gateway.connection.DefaultSqlBuilder";
    private static final String DEFAULT_DELETE_BUILDER_CLASS =
        "pvt.disney.dti.gateway.connection.DefaultSqlBuilder";
    private static final String DEFAULT_UPDATE_BUILDER_CLASS =
        "pvt.disney.dti.gateway.connection.DefaultSqlBuilder";
    private static final String DEFAULT_INSERT_BUILDER_CLASS =
        "pvt.disney.dti.gateway.connection.DefaultSqlBuilder";
    private static final String IS_CACHED = "IS_CACHED";
    private static final String REFRESH_PRIMARY_CACHE = "REFRESH_PRIMARY_CACHE";
    private static final String REFRESH_PRIMARY_CACHE_INTERVAL = "REFRESH_PRIMARY_CACHE_INTERVAL";
    private static final String REFRESH_CACHED_OBJECT = "REFRESH_CACHED_OBJECT";
    private static final String REFRESH_CACHED_OBJECT_INTERVAL = "REFRESH_CACHED_OBJECT_INTERVAL";
    // 1 hour
    private static final long REFRESH_PRIMARY_CACHE_INTERVAL_DEFAULT = 3600000;
    // 1/2 hour
    private static final long REFRESH_CACHED_OBJECT_INTERVAL_DEFAULT = 1800000;
    private static final String NULL_CACHE_MAP = "NULL";
    private static Map theMap = new HashMap();

    //~ Instance variables -------------------------------------------------------------------------

    private String dataAccessName;
    private QueryBuilder queryBuilder = null;
    private DeleteBuilder deleteBuilder = null;
    private UpdateBuilder updateBuilder = null;
    private InsertBuilder insertBuilder = null;
    private EventLogger evl = null;
    private Map primaryCacheMap = new HashMap();
    private boolean isCached = false;
    private long primaryCacheLastRefreshed;
    private boolean refreshPrimaryCache;
    private long refreshPrimaryCacheInterval = REFRESH_PRIMARY_CACHE_INTERVAL_DEFAULT;
    private boolean refreshCachedObject;
    private long refreshCachedObjectInterval = REFRESH_CACHED_OBJECT_INTERVAL_DEFAULT;
    private String dataSourceName = null;
    
    /** The standard core logging mechanism. */
	private EventLogger eventLogger = EventLogger.getLogger(this.getClass());

    //~ Constructors -------------------------------------------------------------------------------

    /**
     * The private constructor.  Call getInstance(String dataAccessNameParam) to get a DAOHelper
     * instance. Logs can be enabled for each individual SQL through log4j.xml by appending
     * the dataAccessName (DAOHelper instance name) to the DAOHelper logger class:
     * <logger additivity="true" name="pvt.disney.dti.gateway.connection.DAOHelper.DAOInstance"><level value="DEBUG"/></logger>
     * 
     * @param dataAccessNameParam dataAccessName Sting
     * @param dataSource SQL datasource, so the same SQL can be used in multiple databases
     */
    private DAOHelper(String dataAccessNameParam, String dataSource)
    {
        this.dataAccessName = dataAccessNameParam;

        primaryCacheLastRefreshed = System.currentTimeMillis();

        // Log at a level below the classname - logger for each SQL dataAccessName
        evl = EventLogger.getLogger(this.getClass().getName() + '.' + dataAccessNameParam);

        // Set datasource info at construction so it can be changed dynamically
        String dsPropName = dataAccessNameParam + '_' + DATA_SOURCE;
        if (dataSource == null)
        {
            dataSourceName = PropertyHelper.readPropsValue(dsPropName, 
                DATA_ACCESS_PROPERTIES, false, null);
            try
            {
                assertNotNull(DATA_ACCESS_PROPERTIES, dsPropName, dataSourceName);
            }
            catch (Throwable t)
            {
                evl.sendException(
                    "The property specified in the dataaccess.properties as "
                        + dataAccessNameParam
                        + '_'
                        + DATA_SOURCE
                        + " cannot be null.",
                    EventType.EXCEPTION,
                    ErrorCode.APPLICATION_EXCEPTION,
                    t,
                    this);
            }
        }
        else
        {
            dataSourceName = dataSource;
        }

        isCached = Boolean.valueOf(
                        PropertyHelper.readPropsValue(dataAccessNameParam + '_' + IS_CACHED,
                        DATA_ACCESS_PROPERTIES, null, 
                        "false"))
                .booleanValue();

        if (isCached)
        {
            refreshPrimaryCache = Boolean.valueOf(
                            PropertyHelper.readPropsValue(
                            dataAccessNameParam + '_' + REFRESH_PRIMARY_CACHE,
                            DATA_ACCESS_PROPERTIES, null, 
                            "false"))
                    .booleanValue();

            refreshCachedObject = Boolean.valueOf(
                            PropertyHelper.readPropsValue(
                            dataAccessNameParam + '_' + REFRESH_CACHED_OBJECT,
                            DATA_ACCESS_PROPERTIES, null, 
                            "false"))
                    .booleanValue();

            try
            {
                if (refreshPrimaryCache)
                {
                    refreshPrimaryCacheInterval =
                        Long.parseLong(
                                PropertyHelper.readPropsValue(
                                dataAccessNameParam + '_' + REFRESH_PRIMARY_CACHE_INTERVAL,
                                DATA_ACCESS_PROPERTIES, null, "0"));
                }
            }
            catch (Throwable t)
            {
                evl.sendException(
                    "The property specified in the dataaccess.properties as "
                        + dataAccessNameParam
                        + '_'
                        + REFRESH_PRIMARY_CACHE_INTERVAL
                        + " with a value of "
                        + PropertyHelper.readPropsValue(
                            dataAccessNameParam + '_' + REFRESH_PRIMARY_CACHE_INTERVAL, 
                             DATA_ACCESS_PROPERTIES, null, "0")
                        + " was not a valid number... setting refreshPrimaryCacheInterval to default of "
                        + REFRESH_PRIMARY_CACHE_INTERVAL_DEFAULT
                        + " milliseconds and continuing.",
                    EventType.EXCEPTION,
                    ErrorCode.APPLICATION_EXCEPTION,
                    t,
                    this);

                refreshPrimaryCacheInterval = REFRESH_PRIMARY_CACHE_INTERVAL_DEFAULT;
            }

            try
            {
                if (refreshCachedObject)
                {
                    refreshCachedObjectInterval =
                        Long.parseLong(
                                PropertyHelper.readPropsValue(
                                dataAccessNameParam + '_' + REFRESH_CACHED_OBJECT_INTERVAL, 
                                DATA_ACCESS_PROPERTIES, null, "0"));
                }
            }
            catch (Throwable t)
            {
                evl.sendException(
                    "The property specified in the dataaccess.properties as "
                        + dataAccessNameParam
                        + '_'
                        + REFRESH_CACHED_OBJECT_INTERVAL
                        + " with a value of "
                        + PropertyHelper.readPropsValue(
                            dataAccessNameParam + '_' + REFRESH_CACHED_OBJECT_INTERVAL, 
                            DATA_ACCESS_PROPERTIES, null, "0")
                        + " was not a valid number... setting refreshCachedObjectInterval to the default of "
                        + REFRESH_CACHED_OBJECT_INTERVAL_DEFAULT
                        + " milliseconds and continuing.",
                    EventType.EXCEPTION,
                    ErrorCode.APPLICATION_EXCEPTION,
                    t,
                    this);

                refreshCachedObjectInterval = REFRESH_CACHED_OBJECT_INTERVAL_DEFAULT;
            }
        }
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * Get a DAOHelper from the hashmap, or instantiate a new one if there is none.
     * Logs can be enabled for each individual SQL through log4j.xml by appending
     * the dataAccessName (DAOHelper instance name) to the DAOHelper logger class:
     * <logger additivity="true" name="pvt.disney.dti.gateway.connection.DAOHelper.DAOInstance">
     * <level value="DEBUG"/></logger>
     *  
     * @param dataAccessName the name of the data-access data to load from dataaccess.properties.
     * 
     * @return a shared DAOHelper object.
     */
    public static DAOHelper getInstance(String dataAccessNameParam)
    {
        // Set datasource info at construction so it can be changed dynamically
        String dsPropName = dataAccessNameParam + '_' + DATA_SOURCE;
        String dataSourceNameInstance = PropertyHelper.readPropsValue(dsPropName, 
            DATA_ACCESS_PROPERTIES, null, "");
        synchronized (theMap)
        {
            if (!(theMap.containsKey(dataAccessNameParam)))
            {
                theMap.put(
                    dataAccessNameParam,
                    new DAOHelper(dataAccessNameParam, dataSourceNameInstance));
            }
            
            // If the DataSourceName for the DAO has been changed in dataaccess.properties
            // and reloaded by AbstractInitializer, Update the cached hashmap value 
            // for the DAOHelper instance.
            DAOHelper helper = (DAOHelper) theMap.get(dataAccessNameParam);
            if (!dataSourceNameInstance.equals(helper.getDataSourceName()))
            {
                helper.setDataSourceName(dataSourceNameInstance);
                // Don't need to update object since referenced object has been updated
                // theMap.put(dataAccessNameParam, helper);
            }

            return helper;
        }
    }

    /**
     * Get a DAOHelper from the hashmap, or instantiate a new one if there is none.
     * 
     * @param dataAccessName the name of the data-access data to load from dataaccess.properties.
     * @param dataSource Allows setting an alternate dataSource for the query
     * @return a shared DAOHelper object.
     */
    public static DAOHelper getInstance(String dataAccessNameParam, String dataSource)
    {
        synchronized (theMap)
        {
            String accessName = dataAccessNameParam + '|' + dataSource;
            if (!(theMap.containsKey(accessName)))
            {
                theMap.put(accessName, new DAOHelper(dataAccessNameParam, dataSource));
            }

            return (DAOHelper) theMap.get(accessName);
        }
    }

    /**
     * Get a list of all DAOHelpers cached.
     * 
     * @return Collection a list of names
     */
    @SuppressWarnings("rawtypes")
	public static Collection getDataAccessNames()
    {
        List list = new ArrayList();
        synchronized (theMap)
        {
            Iterator keys = theMap.keySet().iterator();

            while (keys.hasNext())
            {
                list.add(keys.next());
            }
        }

        return list;
    }

    /**
     * Process a query.  This is the same as calling processQuery(inputValues, null).
     * 
     * @param inputValues the input values, in order, to be inserted in the prepared statement SQL
     * 
     * @return the built object from the ResultSetProcessor class.
     * 
     * @throws WrappedException if an WrappedException occurs
     * 
     * @see ResultSetProcessor
     */
    public Object processQuery(Object[] inputValues) throws WrappedException
    {
        return processQuery(inputValues, null);
    }

    /**
     * Process a query with dynamic parameters passed into the QueryBuilder object.  The
     * QueryBuilder object is cached - if it is null, a new one will be dynamically instantiated
     * with the value &lt;data-access name&gt;_QUERY_BUILDER = full-qualified class name from the
     * dataaccess.properties file.  This object will then be stored for future look-ups. The
     * specified class must implement the QueryBuilder interface.  If no class is specified, the
     * default implementation will be used, which merely returns the SQL from the properties file.
     * 
     * @param inputValues the input values, in order, to be inserted in the prepared statement SQL
     * @param queryParameters the parameters for the QueryBuilder
     * 
     * @return the built object from the ResultSetProcessor class.
     * 
     * @throws WrappedException if an error occurs while dynamically instantiating the QueryBuilder, or
     *         any other error occurs
     * @throws WrappedException DOCUMENT ME!
     * 
     * @see QueryBuilder
     * @see ResultSetProcessor
     */
    public Object processQuery(Object[] inputValues, Object[] queryParameters)
        throws WrappedException
    {
        if (queryBuilder == null)
        {
            synchronized (this)
            {
                if (queryBuilder == null)
                {
                    String queryBuilderClass = PropertyHelper.readPropsValue(
                        dataAccessName + '_' + QUERY_BUILDER_CLASS, 
                        DATA_ACCESS_PROPERTIES, null, (String) null);
 
                    if (queryBuilderClass == null)
                    {
                        queryBuilderClass = DEFAULT_QUERY_BUILDER_CLASS;
                    }

                    try
                    {
                        Object obj = Loader.loadClass(queryBuilderClass).newInstance();

                        queryBuilder = (QueryBuilder) obj;
                    }
                    catch (Exception ex)
                    {
                        throw new WrappedException(
                            "While dynamically instantiating a QueryBuilder object of type "
                                + queryBuilderClass
                                + " an exception occured.",
                            ex);
                    }
                }
            }
        }

        return processQuery(inputValues, queryParameters, queryBuilder);
    }

    /**
     * Dump the primary cache.  Any subsequent calls to get the cached objects will 
     * be forced to get the object from the DB again and recache it.
     */
    public void dumpPrimaryCache()
    {
        synchronized (primaryCacheMap)
        {
            // dump the cache
            primaryCacheMap.clear();
        }

        primaryCacheLastRefreshed = System.currentTimeMillis();
    }

    /**
     * Return the primary cache Map.  Frankly, I am not happy with this method.  Someone could  use
     * this method to get the Map out and screw with the cache, causing all sorts of problems,
     * especially without synchronizing around it.  This means that only the  admin console is
     * allowed to get this out, and it better do the synchronization around this  map correctly or
     * we will see some strange problems.
     * 
     * @return DOCUMENT ME!
     * 
     * @deprecated DOCUMENT ME!
     */
    public Map getPrimaryCacheMap()
    {
        return primaryCacheMap;
    }

    /**
     * Implementation of the caching method, which relies on two hashmaps. The first map (the
     * primary map) is a map of key=SQL statements,     value=secondary Map.  This is done because
     * a QueryBuilder could return two SQL statements, which could have identical input values.
     * The secondary Map has a key of the composite key of all the input values, and the  value is
     * the cached object.
     * 
     * @param sql DOCUMENT ME!
     * @param ck DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    private Object getCachedObject(String sql, CompositeKey compositeKey)
    {
        // see if the primary cache has expired, if caching
        if (refreshPrimaryCache
            && ((primaryCacheLastRefreshed + refreshPrimaryCacheInterval)
                <= System.currentTimeMillis()))
        {
            dumpPrimaryCache();

            evl.sendEvent(
                "Dumping primary cache for data-access object " + dataAccessName,
                EventType.INFO,
                this);
        }

        Map secondaryCacheMap = null;
        synchronized (primaryCacheMap)
        {
            if (!primaryCacheMap.containsKey(sql))
            {
                primaryCacheMap.put(sql, new HashMap());
            }

            secondaryCacheMap = (Map) primaryCacheMap.get(sql);
        }
        synchronized (secondaryCacheMap)
        {
            // No input values - lookup table query
            if (compositeKey.compositeKeys == null)
            {
                CachedObject cachedObject = (CachedObject) secondaryCacheMap.get(NULL_CACHE_MAP);

                if (!refreshCachedObject)
                {
                    return cachedObject.getCachedObject();
                }
                else if (
                    (cachedObject.getLastRefreshed() + refreshCachedObjectInterval)
                        <= System.currentTimeMillis())
                {
                    evl.sendEvent(
                        "Refreshing cached object " + NULL_CACHE_MAP,
                        EventType.DEBUG,
                        this);
                    cachedObject.setCachedObject(null);

                    return null;
                }
                else // caching expires, but this cached object hasn't expired yet
                    {
                    return cachedObject.getCachedObject();
                }
            }
            else if (secondaryCacheMap.containsKey(compositeKey))
            {
                CachedObject co = (CachedObject) secondaryCacheMap.get(compositeKey);

                if (!refreshCachedObject)
                {
                    return co.getCachedObject();
                }
                else if (
                    (co.getLastRefreshed() + refreshCachedObjectInterval)
                        <= System.currentTimeMillis())
                {
                    evl.sendEvent(
                        "Refreshing cached object " + compositeKey,
                        EventType.DEBUG,
                        this);
                    co.setCachedObject(null);

                    return null;
                }
                else // caching expires, but this cached object hasn't expired yet
                    {
                    return co.getCachedObject();
                }
            }
            else // key does not exist => go get it!
                {
                return null;
            }
        }
    }

    /**
     * Put the object into the map structure as described in the above method.
     * 
     * @param sql DOCUMENT ME!
     * @param ck DOCUMENT ME!
     * @param toPut DOCUMENT ME!
     */
    private void putCachedObject(String sql, CompositeKey ck, Object toPut)
    {
        Map secondaryCacheMap = null;
        synchronized (primaryCacheMap)
        {
            secondaryCacheMap = (Map) primaryCacheMap.get(sql);
        }
        synchronized (secondaryCacheMap)
        {
            if (ck.compositeKeys == null)
            {
                // Put cached object into cache with default key
                secondaryCacheMap.put(NULL_CACHE_MAP, new CachedObject(toPut));
            }
            else if (secondaryCacheMap.containsKey(ck))
            {
                CachedObject co = (CachedObject) secondaryCacheMap.get(ck);

                co.setCachedObject(toPut);
            }
            else
            {
                secondaryCacheMap.put(ck, new CachedObject(toPut));
            }
        }
    }

    /**
     * Process a query with a given QueryBuilder.  This will call the QueryBuilder method getQuery,
     * passing in the query value from the dataaccess.properties file (which could be null), and
     * the queryParameters array (which could also be null).
     * 
     * @param inputValues the input values, in order, to be inserted in the prepared statement SQL
     * @param queryParameters the parameters for the QueryBuilder
     * @param theQueryBuilder the QueryBuilder that will build the SQL query string
     * 
     * @return the built object from the ResultSetProcessor class.
     * 
     * @throws WrappedException DOCUMENT ME!
     * @throws WrappedException DOCUMENT ME!
     * 
     * @see QueryBuilder
     * @see ResultSetProcessor
     */
    public Object processQuery(
        Object[] inputValues,
        Object[] queryParameters,
        QueryBuilder theQueryBuilder)
        throws WrappedException
    {
        Properties dataAccessProperties =
            AbstractInitializer.getInitializer().getProps(DATA_ACCESS_PROPERTIES);
        if (dataAccessProperties == null)
        {
            throw new WrappedException("Property file not found: " + DATA_ACCESS_PROPERTIES);
        }

        String propName = dataAccessName + '_' + RESULT_SET_PROCESSOR_CLASS;
        String resultSetProcessorClass = PropertyHelper.readPropsValue(
            propName, DATA_ACCESS_PROPERTIES, null, (String) null);

        assertNotNull(DATA_ACCESS_PROPERTIES, propName, resultSetProcessorClass);

        CompositeKey compositeKey = null;

        Object returnValue = null;

        String sqlPropName = dataAccessName + '_' + QueryBuilder.QUERY;
        String sql =
            theQueryBuilder.getQuery(
                PropertyHelper.readPropsValue(sqlPropName, 
                    DATA_ACCESS_PROPERTIES, null, (String) null),
                queryParameters);
        if (isCached)
        {
            compositeKey = new CompositeKey(inputValues);

            returnValue = getCachedObject(sql, compositeKey);

            if (returnValue != null)
            {
                return returnValue;
            }
        }

        Object resultSetProcessor = null;

        try
        {
            resultSetProcessor = Loader.loadClass(resultSetProcessorClass).newInstance();
        }
        catch (Exception ex)
        {
            throw new WrappedException(
                "While dynamically instantiating a ResultSetProcessor object of type "
                    + resultSetProcessorClass
                    + " an exception occurred.",
                ex);
        }

        if (!(resultSetProcessor instanceof ResultSetProcessor))
        {
            evl.sendEvent(
                "The class specified by "
                    + resultSetProcessorClass
                    + " was not of type ResultSetProcessor!",
                EventType.EXCEPTION,
                this);
            throw new WrappedException(
                "The class specified by "
                    + resultSetProcessorClass
                    + " was not of type ResultSetProcessor!");
        }

        ResultSetProcessor theProcessor = (ResultSetProcessor) resultSetProcessor;

        assertNotNull(DATA_ACCESS_PROPERTIES, sqlPropName, sql);

        prepareAndExecuteSql(inputValues, sql, true, theProcessor);

        returnValue = theProcessor.getProcessedObject();

        if (isCached && (returnValue != null))
        {
            putCachedObject(sql, compositeKey, returnValue);
        }

        return returnValue;
    }

    /**
     * Process a delete.  This is the same as calling processDelete(inputValues, null).
     * 
     * @param inputValues the input values, in order, to be inserted in the prepared statement SQL
     * 
     * @return the return code from the SQL call
     * 
     * @throws WrappedException if an error occurs while dynamically instantiating the DeleteBuilder,
     *         or any other error occurs
     */
    public int processDelete(Object[] inputValues) throws WrappedException
    {
        return processDelete(inputValues, null);
    }

    /**
     * Process a delete with dynamic parameters passed into the DeleteBuilder object.  The
     * DeleteBuilder object is cached - if it is null, a new one will be dynamically instantiated
     * with the value &lt;data-access name&gt;_DELETE_BUILDER = full-qualified class name from the
     * dataaccess.properties file.  This object will then be stored for future look-ups.  The
     * specified class must implement the DeleteBuilder interface.  If no class is specified, the
     * default implementation will be used, which merely returns the SQL from the properties file.
     * 
     * @param inputValues the input values, in order, to be inserted in the prepared statement SQL
     * @param deleteParameters the parameters for the DeleteBuilder
     * 
     * @return the return code from execution of the SQL
     * 
     * @throws WrappedException if an error occurs while dynamically instantiating the DeleteBuilder,
     *         or any other error occurs
     * 
     * @see DeleteBuilder
     */
    public int processDelete(Object[] inputValues, Object[] deleteParameters)
        throws WrappedException
    {
        if (deleteBuilder == null)
        {
            synchronized (this)
            {
                if (deleteBuilder == null)
                {
                    String deleteBuilderClass =
                        PropertyHelper.readPropsValue(
                            dataAccessName + '_' + DELETE_BUILDER_CLASS, 
                            DATA_ACCESS_PROPERTIES, null, (String) null);

                    if (deleteBuilderClass == null)
                    {
                        deleteBuilderClass = DEFAULT_DELETE_BUILDER_CLASS;
                    }

                    try
                    {
                        Object obj = Loader.loadClass(deleteBuilderClass).newInstance();

                        if (!(obj instanceof DeleteBuilder))
                        {
                            WrappedException ee =
                                new WrappedException(
                                    "The class specified by "
                                        + deleteBuilderClass
                                        + " was not of type DeleteBuilder!",
                                    ErrorCode.REFLECTION_EXCEPTION);
                            evl.sendException(
                                EventType.EXCEPTION,
                                ErrorCode.REFLECTION_EXCEPTION,
                                ee,
                                this);
                            throw ee;
                        }

                        deleteBuilder = (DeleteBuilder) obj;
                    }
                    catch (Exception ex)
                    {
                        throw new WrappedException(
                            "While dynamically instantiating a DeleteBuilder object of type "
                                + deleteBuilderClass
                                + " an exception occurred.",
                            ex,
                            ErrorCode.REFLECTION_EXCEPTION);
                    }
                }
            }
        }

        return processDelete(inputValues, deleteParameters, deleteBuilder);
    }

    /**
     * Process a delete with a given DeleteBuilder.  This will call the DeleteBuilder method
     * getDelete, passing in the delete value from the dataaccess.properties file (which could be
     * null), and the deleteParameters array (which could also be null).
     * 
     * @param inputValues the input values, in order, to be inserted in the prepared statement SQL
     * @param deleteParameters the parameters for the DeleteBuilder
     * @param theDeleteBuilder the DeleteBuilder that will build the SQL delete string
     * 
     * @return the return code from the execution of the SQL
     * 
     * @throws WrappedException if an error occurs while dynamically instantiating the DeleteBuilder,
     *         or any other error occurs
     * 
     * @see DeleteBuilder
     */
    public int processDelete(
        Object[] inputValues,
        Object[] deleteParameters,
        DeleteBuilder theDeleteBuilder)
        throws WrappedException
    {
        String deletePropName = dataAccessName + '_' + DeleteBuilder.DELETE;
        String sql = theDeleteBuilder.getDelete(
            PropertyHelper.readPropsValue(deletePropName, 
                DATA_ACCESS_PROPERTIES, null, (String) null),
            deleteParameters);

        assertNotNull(DATA_ACCESS_PROPERTIES, deletePropName, sql);

        return prepareAndExecuteSql(inputValues, sql, false, null);
    }

    /**
     * Process an update.  This is the same as calling processUpdate(inputValues, null).
     * 
     * @param inputValues the input values, in order, to be inserted in the prepared statement SQL
     * 
     * @return the return code from the SQL call
     * 
     * @throws WrappedException if an error occurs while dynamically instantiating the UpdateBuilder,
     *         or any other error occurs
     */
    public int processUpdate(Object[] inputValues) throws WrappedException
    {
        return processUpdate(inputValues, null);
    }

    /**
     * Process an update with dynamic parameters passed into the UpdateBuilder object.  The
     * UpdateBuilder object is cached - if it is null, a new one will be dynamically instantiated
     * with the value &lt;data-access name&gt;_UPDATE_BUILDER = full-qualified class name from the
     * dataaccess.properties file.  This object will then be stored for future look-ups.  The
     * specified class must implement the UpdateBuilder interface.  If no class is specified, the
     * default implementation will be used, which merely returns the SQL from the properties file.
     * 
     * @param inputValues the input values, in order, to be inserted in the prepared statement SQL
     * @param updateParameters the parameters for the UpdateBuilder
     * 
     * @return the return code from execution of the SQL
     * 
     * @throws WrappedException if an error occurs while dynamically instantiating the UpdateBuilder,
     *         or any other error occurs
     * 
     * @see UpdateBuilder
     */
    public int processUpdate(Object[] inputValues, Object[] updateParameters)
        throws WrappedException
    {
        if (updateBuilder == null)
        {
            synchronized (this)
            {
                if (updateBuilder == null)
                {
                    String updateBuilderClass =
                        PropertyHelper.readPropsValue(
                            dataAccessName + '_' + UPDATE_BUILDER_CLASS, 
                            DATA_ACCESS_PROPERTIES, null, (String) null);

                    if (updateBuilderClass == null)
                    {
                        updateBuilderClass = DEFAULT_UPDATE_BUILDER_CLASS;
                    }

                    try
                    {
                        Object obj = Loader.loadClass(updateBuilderClass).newInstance();

                        if (!(obj instanceof UpdateBuilder))
                        {
                            WrappedException ee =
                                new WrappedException(
                                    "The class specified by "
                                        + updateBuilderClass
                                        + " was not of type UpdateBuilder!",
                                    ErrorCode.REFLECTION_EXCEPTION);
                            evl.sendException(
                                EventType.EXCEPTION,
                                ErrorCode.REFLECTION_EXCEPTION,
                                ee,
                                this);
                            throw ee;
                        }

                        updateBuilder = (UpdateBuilder) obj;
                    }
                    catch (Exception ex)
                    {
                        throw new WrappedException(
                            "While dynamically instantiating an UpdateBuilder object of type "
                                + updateBuilderClass
                                + " an exception occurred.",
                            ex,
                            ErrorCode.REFLECTION_EXCEPTION);
                    }
                }
            }
        }

        return processUpdate(inputValues, updateParameters, updateBuilder);
    }

    /**
     * Process an update with a given UpdateBuilder.  This will call the UpdateBuilder method
     * getUpdate, passing in the update value from the dataaccess.properties file (which could be
     * null), and the updateParameters array (which could also be null).
     * 
     * @param inputValues the input values, in order, to be inserted in the prepared statement SQL
     * @param updateParameters the parameters for the UpdateBuilder
     * @param theUpdateBuilder the UpdateBuilder that will build the SQL update string
     * 
     * @return the return code from the execution of the SQL
     * 
     * @throws WrappedException if an error occurs while dynamically instantiating the UpdateBuilder,
     *         or any other error occurs
     * 
     * @see UpdateBuilder
     */
    public int processUpdate(
        Object[] inputValues,
        Object[] updateParameters,
        UpdateBuilder theUpdateBuilder)
        throws WrappedException
    {
        String updatePropName = dataAccessName + '_' + UpdateBuilder.UPDATE;
        String sql =
            theUpdateBuilder.getUpdate(
                PropertyHelper.readPropsValue(updatePropName, 
                DATA_ACCESS_PROPERTIES, null, (String) null),
                updateParameters);

        assertNotNull(DATA_ACCESS_PROPERTIES, updatePropName, sql);

        return prepareAndExecuteSql(inputValues, sql, false, null);
    }

    /**
     * Process an insert.  This is the same as calling processInsert(inputValues, null).
     * 
     * @param inputValues the input values, in order, to be inserted in the prepared statement SQL
     * 
     * @return the return code from the SQL call
     * 
     * @throws WrappedException if an error occurs while dynamically instantiating the InsertBuilder,
     *         or any other error occurs
     */
    public int processInsert(Object[] inputValues) throws WrappedException
    {
        return processInsert(inputValues, null);
    }

    /**
     * Process an insert with dynamic parameters passed into the InsertBuilder object.  The
     * InsertBuilder object is cached - if it is null, a new one will be dynamically instantiated
     * with the value &lt;data-access name&gt;_INSERT_BUILDER = full-qualified class name from the
     * dataaccess.properties file.  This object will then be stored for future look-ups.  The
     * specified class must implement the InsertBuilder interface.  If no class is specified, the
     * default implementation will be used, which merely returns the SQL from the properties file.
     * 
     * @param inputValues the input values, in order, to be inserted in the prepared statement SQL
     * @param insertParameters the parameters for the InsertBuilder
     * 
     * @return the return code from execution of the SQL
     * 
     * @throws WrappedException if an error occurs while dynamically instantiating the InsertBuilder,
     *         or any other error occurs
     * 
     * @see InsertBuilder
     */
    public int processInsert(Object[] inputValues, Object[] insertParameters)
        throws WrappedException
    {
        if (insertBuilder == null)
        {
            synchronized (this)
            {
                if (insertBuilder == null)
                {
                    String insertBuilderClass =
                        PropertyHelper.readPropsValue(
                            dataAccessName + '_' + INSERT_BUILDER_CLASS, 
                            DATA_ACCESS_PROPERTIES, null, (String) null);

                    if (insertBuilderClass == null)
                    {
                        insertBuilderClass = DEFAULT_INSERT_BUILDER_CLASS;
                    }

                    try
                    {
                        Object obj = Loader.loadClass(insertBuilderClass).newInstance();

                        if (!(InsertBuilder.class.isInstance(obj)))
                        {
                            WrappedException ee =
                                new WrappedException(
                                    "The class specified by "
                                        + insertBuilderClass
                                        + " was not of type InsertBuilder!",
                                    ErrorCode.REFLECTION_EXCEPTION);
                            evl.sendException(
                                EventType.EXCEPTION,
                                ErrorCode.REFLECTION_EXCEPTION,
                                ee,
                                this);
                            throw ee;
                        }

                        insertBuilder = (InsertBuilder) obj;
                    }
                    catch (Exception ex)
                    {
                        throw new WrappedException(
                            "While dynamically instantiating a InsertBuilder object of type "
                                + insertBuilderClass
                                + " an exception occurred.",
                            ex,
                            ErrorCode.REFLECTION_EXCEPTION);
                    }
                }
            }
        }

        return processInsert(inputValues, insertParameters, insertBuilder);
    }

    /**
     * Process an insert with a given InsertBuilder.  This will call the InsertBuilder method
     * getInsert, passing in the insert value from the dataaccess.properties file (which could be
     * null), and the insertParameters array (which could also be null).
     * 
     * @param inputValues the input values, in order, to be inserted in the prepared statement SQL
     * @param insertParameters the parameters for the InsertBuilder
     * @param theInsertBuilder the InsertBuilder that will build the SQL insert string
     * 
     * @return the return code from the execution of the SQL
     * 
     * @throws WrappedException if an error occurs while dynamically instantiating the InsertBuilder,
     *         or any other error occurs
     * 
     * @see InsertBuilder
     */
    public int processInsert(
        Object[] inputValues,
        Object[] insertParameters,
        InsertBuilder theInsertBuilder)
        throws WrappedException
    {
        String insertPropName = dataAccessName + '_' + InsertBuilder.INSERT;
        String sql =
            theInsertBuilder.getInsert(
                PropertyHelper.readPropsValue(insertPropName, 
                DATA_ACCESS_PROPERTIES, null, (String) null),
                insertParameters);

        assertNotNull(DATA_ACCESS_PROPERTIES, insertPropName, sql);

        return prepareAndExecuteSql(inputValues, sql, false, null);
    }

    /**
     * Helper method to assert that nothing is null or else throw an WrappedException.
     * 
     * @param propertyFile Property File in which to check for missing value
     * @param name Property file value to check for null value
     * @param value Value to be checked for null
     * 
     * @throws WrappedException Wrapped created exception (instead of NullPointerException)
     */
    protected void assertNotNull(String propertyFile, String name, String value)
        throws WrappedException
    {
        if (value == null)
        {
            throw new WrappedException(
                "The value of the property named "
                    + name
                    + " in property file "
                    + propertyFile
                    + " was null.",
                ErrorCode.APPLICATION_EXCEPTION);
        }
    }

    /**
     * Execute the SQL.  Use the sql string passed in for a prepared statement,  lookup the
     * connection with the dataSourceName, and use theProcessor to build the return object if it
     * is a query.  return the return code from processing.
     * 
     * 
     * @param inputValues Object[] Array of PreparedStatement parameters
     * @param sql String to prepare/execute
     * @param query boolean true=executeQuery, false=executeUpdate
     * @param theProcessor ResultSetProcessor Java class to output value object from ResultSet
     * 
     * @return int query returncode or update record count
     * 
     * @throws WrappedException Wraps SQLException if an error occurs while processing the query
     * @see ResultSetProcessor
     */
    protected int prepareAndExecuteSql(
        Object[] inputValues,
        String sql,
        boolean query,
        ResultSetProcessor theProcessor)
        throws WrappedException
    {
        boolean retry = true;
        int retryCount = 3;
        int sleepInterval = 2;

        /*
        // dataSourceName is already set at construction time
        String dsPropName = dataAccessName + '_' + DATA_SOURCE;
        String dataSourceName = dataAccessProperties.getProperty(dsPropName);
        assertNotNull(DATA_ACCESS_PROPERTIES, dsPropName, dataSourceName);
        */

        // If auditSql is set in properties file, the last inputValues[] element is the userID for audit
        boolean auditSql =
            Boolean
                .valueOf(
                    PropertyHelper.readPropsValue(dataAccessName + '_' + AUDIT_ACCESS, 
                    DATA_ACCESS_PROPERTIES, null, "false"))
                .booleanValue();

        // Indicates an exception condition is thrown if no result set is returned
        boolean noResultSetException =
            Boolean
                .valueOf(
                    PropertyHelper.readPropsValue(dataAccessName + '_' + EXCEPTION, 
                    DATA_ACCESS_PROPERTIES, null, "false"))
                .booleanValue();

        boolean autocommitFlag =
            Boolean
                .valueOf(
                    PropertyHelper.readPropsValue(dataAccessName + '_' + AUTOCOMMIT, 
                    DATA_ACCESS_PROPERTIES, null, "true"))
                .booleanValue();
        //autocommitFlag = Boolean.valueOf(autocommitString.equals("true")).booleanValue ();

        String ispPropName = dataAccessName + '_' + IS_STORED_PROC;
        boolean isStoredProc =
            Boolean.valueOf(PropertyHelper.readPropsValue(ispPropName, 
                            DATA_ACCESS_PROPERTIES, null, "false"))
            .booleanValue();

        //evl.sendEvent("Processing query " + sql + " on " + dataSourceName, EventType.DEBUG, this);
        long startTime = System.currentTimeMillis();

        Connection con = null;
        CallableStatement cs = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List exceptionList = new LinkedList();
        boolean throwException = false;
        boolean result = false;
        // Indicates no result from the query - throw exception
        int returnCode = 0;
        String parameters = ""; // Debug parameter list
        String auditID = null; // UserID passed into the DAOHelper

        long connectionManagerLength = -1;
        long executionLength = -1;
        int valuesLength = 0;

        while (retry && retryCount > 0) 
        {
            retry = false;
            try
            {
                if (inputValues != null)
                {
                    valuesLength = inputValues.length;
                    // The last parameter is the audit ID if auditing
                    if (auditSql)
                    {
                        valuesLength -= 1;
                        auditID = (String) inputValues[valuesLength];
                    }
                }

                ConnectionManager conMan = ConnectionManager.getInstance();
                if (auditSql)
                {
                    con = conMan.getConnection(dataSourceName, auditID);
                }
                else
                {
                    con = conMan.getConnection(dataSourceName);
                }

                connectionManagerLength = System.currentTimeMillis() - startTime;

                try
                {
                    try // Separate try for StaleConnectionException because SQLException must be handled
                        {
                        if (!autocommitFlag)
                        {
                            con.setAutoCommit(false);
                        }
                        if (isStoredProc)
                        {
                            // CallableStatement is only used if out parameter(s) are part of the SProc
                            cs = con.prepareCall(sql);
                            ps = cs;
                        }
                        else
                        {
                            ps = con.prepareStatement(sql);
                        }
                    }
                    catch (SQLException s)
                    {
                        // Make this code portable between Websphere 3, 4, 5.
                        if (s.toString().indexOf("StaleConnection") > 0)
                        {
                            // Stale Connection is when the pooled connection has been closed externally.
                            // The entire connection pool is dumped when we see this.
                            // Closing and reopening the connection will re-establish the pool if the database is up
                            try
                            {
                                con.close();
                            }
                            catch (Exception ex)
                            {
                            }

                            // Log exception after Stale connection has been fixed.
                            WrappedException ee =
                                new WrappedException(
                                    "StaleConnectionException from " + dataSourceName + ": '" + sql,
                                    ErrorCode.DATABASE_SQL_EXCEPTION);
							evl.sendException(
								EventType.EXCEPTION,
								ErrorCode.DATABASE_SQL_EXCEPTION,
								ee,
								this);

                            try
                            {
                                Thread.sleep(sleepInterval * 1000);
                            }
                            catch (InterruptedException sleepException)
                            {
                                // who waked me up?
                                System.out.println(
                                    "StaleConnection InterruptedException: "
                                        + sleepException.toString());
                                throw s;
                            }

                            retry = true;
                            retryCount--;
                            // Go back to the top to get a new connection and PS etc.
                            continue;
                        }
                        else
                        {
                            WrappedException ee =
                                new WrappedException(
                                    "SQLException from " + dataSourceName + ": '" + sql + ": " + s,
                                    ErrorCode.DATABASE_SQL_EXCEPTION);
                            evl.sendException(
                                EventType.EXCEPTION,
                                ErrorCode.DATABASE_SQL_EXCEPTION,
                                ee,
                                this);
                            throw s;
                        }
                    }

                    for (int i = 0; i < valuesLength; i++)
                    {
                        if (inputValues[i] != null)
                        {
                            if (inputValues[i] instanceof java.util.Date)
                            {
                                if ((!(inputValues[i] instanceof java.sql.Date))
                                    && (!(inputValues[i] instanceof java.sql.Time))
                                    && (!(inputValues[i] instanceof java.sql.Timestamp)))
                                {
                                    java.util.Date convert = (java.util.Date) inputValues[i];
                                    inputValues[i] = new java.sql.Date(convert.getTime());
                                }
                            }

                            if (isStoredProc)
                            {
                                if (inputValues[i] instanceof NullParameter)
                                {
                                    NullParameter nullParam = (NullParameter) inputValues[i];
                                    cs.setNull(i + 1, nullParam.getSqlType());
                                }
                                else if (inputValues[i] instanceof OutParameter)
                                {
                                    OutParameter outParam = (OutParameter) inputValues[i];
                                    cs.registerOutParameter(i + 1, outParam.getSqlType());
                                    if (outParam.getSqlInParameter() != null)
                                    {
                                        cs.setObject(i + 1, outParam.getSqlInParameter());
                                    }
                                }
                                else
                                {
                                    cs.setObject(i + 1, inputValues[i]);
                                }
                            }
                            else
                            {
                                if (inputValues[i] instanceof NullParameter)
                                {
                                    NullParameter nullParam = (NullParameter) inputValues[i];
                                    ps.setNull(i + 1, nullParam.getSqlType());
                                }
                                else
                                {
                                    ps.setObject(i + 1, inputValues[i]);
                                }
                            }
                            parameters += (' ' + inputValues[i].toString());
                        }
                    }
                }
                catch (SQLException se)
                {	
                    parameters = "";
                    // reset debug string and redo to get correct values

                    for (int i = 0; i < inputValues.length; i++)
                    {
                        parameters += (' ' + inputValues[i].toString());
                    }

                    WrappedException ee =
                        new WrappedException(
                            "An exception occured while preparing the statement to "
                                + dataSourceName
                                + " with parameters ["
                                + parameters
                                + "] : '"
                                + sql
                                + '\'',
                            se,
                            ErrorCode.DATABASE_SQL_EXCEPTION);
                    evl.sendException(
                        EventType.EXCEPTION,
                        ErrorCode.DATABASE_SQL_EXCEPTION,
                        ee,
                        this);

                    throw ee;
                }

                long settingStatementTime = System.currentTimeMillis();

                if (isStoredProc)
                {
                    boolean resultSetReturned = cs.execute();
                    if (resultSetReturned)
                    {
                        rs = cs.getResultSet();
                    }
                    else
                    {
                        returnCode = cs.getUpdateCount();
                    }

                    // Set the values in the original array to the output parameter value(s).
                    // Object is by reference so updated values are returned from fn call.
                    for (int i = 0; i < valuesLength; i++)
                    {
                        if (inputValues[i] instanceof OutParameter)
                        {
                            inputValues[i] = cs.getObject(i + 1);
                            // Oracle can't return resultset values from a stored procedure.
                            // Allow any of the output parameters to be a resultset.
                            // The parameter must be declared as type REF CURSOR in the procedure and the inputValues[]
                            // must be of type oracle.jdbc.OracleTypes.CURSOR
                            if (inputValues[i] instanceof ResultSet && rs == null)
                            {
                                rs = (ResultSet) inputValues[i];
                            }
                        }
                    }
                }
                else
                {
                    if (query)
                    {
                        rs = ps.executeQuery();
                    }
                    else
                    {
                        returnCode = ps.executeUpdate();
                    }
                }

                executionLength = System.currentTimeMillis() - settingStatementTime;

                if (rs != null)
                {
                    while (rs.next())
                    {
                        // Throw exception if no result set from the query...
                        result = true;
                        theProcessor.processNextResultSet(rs);
                    }
                }
                else
                {
                    result = true;
                }

                if (isStoredProc)
                {
                    cs.close();
                }
                else
                {
                    ps.close();
                }
            }
            catch (ConnectionException ce)
            {
                evl.sendException(
                    EventType.EXCEPTION,
                    ErrorCode.DATABASE_CONNECTION_EXCEPTION,
                    ce,
                    this);

                throwException = true;
                exceptionList.add(
                    new WrappedException(
                        "An exception occurred while opening the connection",
                        ce,
                        ErrorCode.DATABASE_CONNECTION_EXCEPTION));
            }
            catch (SQLException sqle)
            {
               /* WrappedException ex =
                    new WrappedException(
                        "An exception occurred while performing the SQL: "
                            + sql
                            + " ["
                            + parameters
                            + ']',
                        sqle,
                        ErrorCode.DATABASE_SQL_EXCEPTION);
	                    evl.sendException(EventType.EXCEPTION, ErrorCode.DATABASE_SQL_EXCEPTION, sqle, this);*/
            	eventLogger.sendEvent("SQLException: " + sqle.toString(), EventType.EXCEPTION, this);


                throwException = true;
                exceptionList.add(sqle);
            }
            catch (WrappedException ee)
            {
                // don't log, already logged at source
                throwException = true;
                exceptionList.add(ee);
            }
            catch (Exception th)
            {
                WrappedException ex =
                    new WrappedException(
                        "A throwable occurred while performing the SQL: "
                            + sql
                            + " ["
                            + parameters
                            + ']',
                        th,
                        ErrorCode.DATABASE_SQL_EXCEPTION);
                evl.sendException(EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, ex, this);

                throwException = true;
                exceptionList.add(
                    new WrappedException(
                        "Throwable occurred while performing the SQL: "
                            + sql
                            + " ["
                            + parameters
                            + ']',
                        th,
                        ErrorCode.APPLICATION_EXCEPTION));
            }
            finally
            {
                try
                {
                    if (rs != null)
                    {
                        rs.close();
                    }
                }
                catch (Exception th)
                {
                    throwException = true;
                    exceptionList.add(
                        new WrappedException(
                            "An error occurred while closing the result set!",
                            th,
                            ErrorCode.DATABASE_CONNECTION_EXCEPTION));
                }

                try
                {
                    if (isStoredProc)
                    {
                        if (cs != null)
                        {
                            cs.close();
                        }
                    }
                    else
                    {
                        if (ps != null)
                        {
                            ps.close();
                        }
                    }
                }
                catch (Exception th)
                {
                    throwException = true;
                    exceptionList.add(
                        new WrappedException(
                            "An error occurred while closing the prepared statement!",
                            th,
                            ErrorCode.DATABASE_CONNECTION_EXCEPTION));
                }

                try
                {
                    if (con != null)
                    {
                        if (!autocommitFlag)
                        {
                            // Close would rollback the current transaction unless it is committed first
                            con.commit();
                            con.setAutoCommit(true);
                        }

                        con.close();
                    }
                }
                catch (Exception th)
                {
                    throwException = true;
                    exceptionList.add(
                        new WrappedException(
                            "An error occurred while closing the connection!",
                            th,
                            ErrorCode.DATABASE_CONNECTION_EXCEPTION));
                }
            }
        } // End while retry

        long elapsedLength = System.currentTimeMillis() - startTime;

        evl.sendEvent(
            "Opening the ConnectionManager took "
                + connectionManagerLength
                + " ms., "
                + "Executing the statement took "
                + executionLength
                + " ms., "
                + "and Processing took "
                + elapsedLength
                + " ms. on "
                + sql
                + " ["
                + parameters
                + ']',
            EventType.DEBUG,
            this);

        if (throwException)
        {
            WrappedException lastException = null;

            for (Iterator iterator = exceptionList.iterator(); iterator.hasNext();)
            {
                Throwable next = (Throwable) iterator.next();

                if (lastException != null)
                {
                    lastException =
                        new WrappedException(
                            next.toString(),
                            lastException,
                            ErrorCode.DATABASE_SQL_EXCEPTION);
                }
                else if (next instanceof WrappedException)
                {
                    lastException = (WrappedException) next;
                }
                else
                {
                    lastException =
                        new WrappedException(
                            "Wrapped exception of type " + next.getClass(),
                            next,
                            ErrorCode.DATABASE_SQL_EXCEPTION);
                }
            }

            throw lastException;
        }

        if (!result && noResultSetException)
        {
            // Query returned no values - throw exception
            WrappedException ex =
                new WrappedException(
                    "DAOHelper query returned no result: " + sql + " [" + parameters + ']',
                    ErrorCode.BUSINESS_RULE_EXCEPTION);
            evl.sendException(EventType.EXCEPTION, ErrorCode.BUSINESS_RULE_EXCEPTION, ex, this);
            throw ex;
        }

        return returnCode;
    }

    /**
     * Gets the isCached
     * 
     * @return Returns a boolean
     */
    public boolean getIsCached()
    {
        return isCached;
    }

    /**
     * Returns the dataSourceName.
     * @return String
     */
    public String getDataSourceName()
    {
        return dataSourceName;
    }

    /**
     * Sets the dataSourceName.
     * @param dataSourceName The dataSourceName to set
     */
    public void setDataSourceName(String dataSourceNameParam)
    {
        this.dataSourceName = dataSourceNameParam;
    }

    //~ Classes ------------------------------------------------------------------------------------

    /**
     * This class is a wrapper for the cached data.  It contains information about the last
     * previous update and the actual cached object.
     */
    private class CachedObject
    {
        //~ Instance variables ---------------------------------------------------------------------

        private Object toCache;
        private long lastRefreshed;

        //~ Constructors ---------------------------------------------------------------------------

        /**
         * Creates a new CachedObject object.
         * 
         * @param toCache DOCUMENT ME!
         */
        private CachedObject(Object toCache)
        {
            setCachedObject(toCache);
        }

        //~ Methods --------------------------------------------------------------------------------

        /**
         * DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        public synchronized Object getCachedObject()
        {
            return toCache;
        }

        /**
         * DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        public synchronized long getLastRefreshed()
        {
            return lastRefreshed;
        }

        /**
         * DOCUMENT ME!
         * 
         * @param toCache DOCUMENT ME!
         */
        public synchronized void setCachedObject(Object toCache)
        {
            this.toCache = toCache;
            lastRefreshed = System.currentTimeMillis();
        }
    }

}
