package pvt.disney.dti.gateway.connection;

import java.sql.Types;

/**
 * Wrapper for SQL Null values providing a SQL Type.
 * Use of this Object should be through the static instances.
 * 
 * @author fav2
 * Created on May 7, 2004
 */
public class NullParameter
{
    public static final NullParameter BIT = new NullParameter(Types.BIT);
    public static final NullParameter TINYINT = new NullParameter(Types.TINYINT);
    public static final NullParameter SMALLINT = new NullParameter(Types.SMALLINT);
    public static final NullParameter INTEGER = new NullParameter(Types.INTEGER);
    public static final NullParameter BIGINT = new NullParameter(Types.BIGINT);
    public static final NullParameter FLOAT = new NullParameter(Types.FLOAT);
    public static final NullParameter REAL = new NullParameter(Types.REAL);
    public static final NullParameter DOUBLE = new NullParameter(Types.DOUBLE);
    public static final NullParameter NUMERIC = new NullParameter(Types.NUMERIC);
    public static final NullParameter DECIMAL = new NullParameter(Types.DECIMAL);
    public static final NullParameter CHAR = new NullParameter(Types.CHAR);
    public static final NullParameter VARCHAR = new NullParameter(Types.VARCHAR);
    public static final NullParameter LONGVARCHAR = new NullParameter(Types.LONGVARCHAR);
    public static final NullParameter DATE = new NullParameter(Types.DATE);
    public static final NullParameter TIME = new NullParameter(Types.TIME);
    public static final NullParameter TIMESTAMP = new NullParameter(Types.TIMESTAMP);
    public static final NullParameter BINARY = new NullParameter(Types.BINARY);
    public static final NullParameter VARBINARY = new NullParameter(Types.VARBINARY);
    public static final NullParameter LONGVARBINARY = new NullParameter(Types.LONGVARBINARY);
    public static final NullParameter NULL = new NullParameter(Types.NULL);
    public static final NullParameter OTHER = new NullParameter(Types.OTHER);
    public static final NullParameter JAVA_OBJECT = new NullParameter(Types.JAVA_OBJECT);
    public static final NullParameter DISTINCT = new NullParameter(Types.DISTINCT);
    public static final NullParameter STRUCT = new NullParameter(Types.STRUCT);
    public static final NullParameter ARRAY = new NullParameter(Types.ARRAY);
    public static final NullParameter BLOB = new NullParameter(Types.BLOB);
    public static final NullParameter CLOB = new NullParameter(Types.CLOB);
    public static final NullParameter REF = new NullParameter(Types.REF);

    private int sqlType;

    /**
     * Protected Constructor... only to be used by this class and sub-classes.
     * @param sqlType int value defined from the class: java.sql.Types
     */
    protected NullParameter(int sqlTypeParam)
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
        return "NullParameter[" + sqlType + ']';
    }
}
