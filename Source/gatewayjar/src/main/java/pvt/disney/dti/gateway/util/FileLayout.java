package pvt.disney.dti.gateway.util;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Provides FileLayout services needed by Log4J.
 * 
 * @author ctl2
 * @author %derived_by: ctl2 %
 * @version %version: 1 %
 */
public final class FileLayout extends Layout {
	private FileRenderer etr = null;

	/**
	 * Default (and only) contructor.
	 */
	public FileLayout() {
		super();
		etr = new FileRenderer();
	}

	/**
	 * @see org.apache.log4j.spi.OptionHandler#activateOptions()
	 */
	public void activateOptions() {
	}

	/**
	 * @see org.apache.log4j.Layout#format(LoggingEvent)
	 */
	public String format(LoggingEvent le) {
		return etr.doRender(le.getMessage());
	}

	/**
	 * @see org.apache.log4j.Layout#ignoresThrowable()
	 */
	public boolean ignoresThrowable() {
		return false;
	}

}
