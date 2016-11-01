package pvt.disney.dti.gateway.connection;

// import java.sql.Types;

/**
 * Wrapper for SQL out Parameter values providing a SQL Type.
 * A new instance must be created for each invocaton since it could also
 * be an in parameter. Example usage in DAOHelperTest.
 * 
 * @author rjf3
 * Created on June 17, 2004
 */
public class OutParameter
{
    /* public static final OutParameter BIT = new OutParameter(Types.BIT);
    public static final OutParameter TINYINT = new OutParameter(Types.TINYINT);
    public static final OutParameter SMALLINT = new OutParameter(Types.SMALLINT);
    public static final OutParameter INTEGER = new OutParameter(Types.INTEGER);
    public static final OutParameter BIGINT = new OutParameter(Types.BIGINT);
    public static final OutParameter FLOAT = new OutParameter(Types.FLOAT);
    public static final OutParameter REAL = new OutParameter(Types.REAL);
    public static final OutParameter DOUBLE = new OutParameter(Types.DOUBLE);
    public static final OutParameter NUMERIC = new OutParameter(Types.NUMERIC);
    public static final OutParameter DECIMAL = new OutParameter(Types.DECIMAL);
    public static final OutParameter CHAR = new OutParameter(Types.CHAR);
    public static final OutParameter VARCHAR = new OutParameter(Types.VARCHAR);
    public static final OutParameter LONGVARCHAR = new OutParameter(Types.LONGVARCHAR);
    public static final OutParameter DATE = new OutParameter(Types.DATE);
    public static final OutParameter TIME = new OutParameter(Types.TIME);
    public static final OutParameter TIMESTAMP = new OutParameter(Types.TIMESTAMP);
    public static final OutParameter BINARY = new OutParameter(Types.BINARY);
    public static final OutParameter VARBINARY = new OutParameter(Types.VARBINARY);
    public static final OutParameter LONGVARBINARY = new OutParameter(Types.LONGVARBINARY);
    public static final OutParameter NULL = new OutParameter(Types.NULL);
    public static final OutParameter OTHER = new OutParameter(Types.OTHER);
    public static final OutParameter JAVA_OBJECT = new OutParameter(Types.JAVA_OBJECT);
    public static final OutParameter DISTINCT = new OutParameter(Types.DISTINCT);
    public static final OutParameter STRUCT = new OutParameter(Types.STRUCT);
    public static final OutParameter ARRAY = new OutParameter(Types.ARRAY);
    public static final OutParameter BLOB = new OutParameter(Types.BLOB);
    public static final OutParameter CLOB = new OutParameter(Types.CLOB);
    public static final OutParameter REF = new OutParameter(Types.REF);
    */

    private int sqlType;
    // Used only for an object that is both in and out - set the Object value within the out parameter
    private Object sqlInParameter = null;

    /**
     * Constructor must be public since OutParameter may also be an InParameter
     * @param sqlType int value defined from the class: java.sql.Types
     */
    public OutParameter(int sqlTypeParam)
    {
        this.sqlType = sqlTypeParam;
    }

    /**
     * Get the associated SQL Type value.
     * @return sqlType
     */
    public int getSqlType()
    {
        return sqlType;
    }

    /**
     * Returns a String including the wrapped SQL Type value.
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        String rtn = null;
        if (sqlInParameter != null)
        {
            rtn = "InOutParameter[" + sqlInParameter + ']';
        }
        else
        {
            rtn = "OutParameter[" + sqlType + ']';
        }
        return rtn;
    }
    /**
     * Returns the sqlInParameter.
     * @return Object
     */
    public Object getSqlInParameter()
    {
        return sqlInParameter;
    }

    /**
     * Sets the sqlInParameter.
     * @param sqlInParameter The sqlInParameter to set
     */
    public void setSqlInParameter(Object sqlInParameterParam)
    {
        this.sqlInParameter = sqlInParameterParam;
    }

}
