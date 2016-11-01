package pvt.disney.dti.gateway.connection;

import com.disney.exception.WrappedException;

import java.sql.SQLException;


/**
 * Custom Exception for ConnectionMgr. Creation date: (7/18/00 5:25:42 PM)
 */
public class ConnectionException extends WrappedException
{
    //~ Constructors -------------------------------------------------------------------------------


    /**
     * ConnectionException constructor - call super().
     *
     * @param s java.lang.String Exception Message
     */
    public ConnectionException(String s)
    {
        super(s);
    }


    /**
     * Creates a new ConnectionException object.with wrapped exception
     *
     * @param msg Detailed Exception Message
     * @param t Parent Exception
     */
    public ConnectionException(String msg, Throwable t)
    {
        super(msg, t);
    }

    //~ Methods ------------------------------------------------------------------------------------


    /**
     * Prints the Stack Trace and its wrapped SQL Exception.  If the wrapped exception is a
     * SQLException,  it prints the stack trace of the LinkedException also.
     *
     * @param out PrintStream to write to.
     */
    public void printStackTrace(java.io.PrintStream out)
    {
        super.printStackTrace(out);

        Throwable we = this.getWrappedException();

        if ((we != null) && (we instanceof SQLException))
        {
            SQLException sqle = (SQLException)we;
            Exception le = sqle.getNextException();

            if (le != null)
            {
                out.println("Stack trace of Connection-linked exception: " + le.toString());
                out.println("----");
                le.printStackTrace(out);
                out.println("----");
            }
        }
    }


    /**
     * Prints the Stack Trace and its wrapped SQL Exception.  If the wrapped exception is a
     * SQLException,  it prints the stack trace of the LinkedException also.
     *
     * @param out PrintWriter to write to.
     */
    public void printStackTrace(java.io.PrintWriter out)
    {
        super.printStackTrace(out);

        Throwable we = this.getWrappedException();

        if ((we != null) && (we instanceof SQLException))
        {
            SQLException sqle = (SQLException)we;
            Exception le = sqle.getNextException();

            if (le != null)
            {
                out.println("Stack trace of Connection-linked exception: " + le.toString());
                out.println("----");
                le.printStackTrace(out);
                out.println("----");
            }
        }
    }
}
