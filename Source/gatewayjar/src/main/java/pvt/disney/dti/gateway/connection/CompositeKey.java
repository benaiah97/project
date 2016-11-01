package pvt.disney.dti.gateway.connection;


/**
 * DOCUMENT ME!
 * 
 * @version $Revision$
 * @author $author$
 */
public class CompositeKey
{
    //~ Static variables/initializers --------------------------------------------------------------

    private static final int DEFAULT_KEY_LENGTH = 20;

    //~ Instance variables -------------------------------------------------------------------------

    Object[] compositeKeys;
    private static final Object[] NULL_OBJECT = {""};

    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Creates a new CompositeKey object.
     * 
     * @param compositeKeysSet
     */
    public CompositeKey(Object[] compositeKeysSet)
    {
        if (compositeKeysSet == null)
        {
            this.compositeKeys = NULL_OBJECT;
        }
        else
        {
            this.compositeKeys = compositeKeysSet;
        }
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * True if each element in the composite key is equal
     * 
     * @param obj DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj)
    {
        if ((obj == null) || !(obj instanceof CompositeKey) || (compositeKeys == null))
        {
            return false;
        }

        CompositeKey ck = (CompositeKey)obj;

        if (ck.compositeKeys == null)
        {
            return false;
        }

        for (int i = 0; i < compositeKeys.length; i++)
        {
            if (!compositeKeys[i].equals (ck.compositeKeys[i]))
            {
                return false;
            }
        }

        return compositeKeys.length == ck.compositeKeys.length;
    }


    /**
     * Returns hashcode of all composite keys
     * 
     * @return int hashcode
     */
    public int hashCode()
    {
        int hashcode = 0;

        for (int i = 0; i < compositeKeys.length; i++)
        {
            hashcode += compositeKeys[i].hashCode ();
        }

        return hashcode;
    }


    /**
     * Returns key=<compositeKey String> for each CK
     * 
     * @return String
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer(compositeKeys.length * DEFAULT_KEY_LENGTH);

        for (int i = 0; i < compositeKeys.length; i++)
        {
            sb.append ("key" + i + '=' + compositeKeys[i].toString () + '|');
        }

        return sb.toString ();
    }
}