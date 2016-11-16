package pvt.disney.dti.gateway.provider.hkd.data.common;

import java.io.Serializable;

/**
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class HkdOTMediaDataTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String mediaId;
	private String mfrId;
	private String visualId;

	/**
	 * @param mediaId
	 *            the mediaId to set
	 */

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	/**
	 * @return the mediaId
	 */

	public String getMediaId() {
		return this.mediaId;
	}

	/**
	 * @param mfrId
	 *            the mfrId to set
	 */

	public void setMfrId(String mfrId) {
		this.mfrId = mfrId;
	}

	/**
	 * @return the mfrId
	 */

	public String getMfrId() {
		return this.mfrId;
	}

	/**
	 * @param visualId
	 *            the visualId to set
	 */

	public void setVisualId(String visualId) {
		this.visualId = visualId;
	}

	/**
	 * @return the visualId
	 */

	public String getVisualId() {
		return this.visualId;
	}

}
