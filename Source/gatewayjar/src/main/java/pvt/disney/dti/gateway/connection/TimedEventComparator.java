/* TimedEventComparator - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package pvt.disney.dti.gateway.connection;
import java.util.Comparator;

public class TimedEventComparator implements Comparator
{
    public int compare(Object thing1, Object thing2) {
	TimedEvent te1 = (TimedEvent) thing1;
	TimedEvent te2 = (TimedEvent) thing2;
	return new Long(te1.getStartTime() - te2.getStartTime()).intValue();
    }
}
