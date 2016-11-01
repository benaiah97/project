package com.disney.admin.util;

import com.disney.util.Converter;

/**
 * Common Converter for HTML Related Functions.
 * 
 * @author FAV2
 * Created on Sep 5, 2003
 */
public class HtmlConverter
{

	/**
	 * Constructor for HtmlConverter.
	 */
	private HtmlConverter()
	{
		super();
	}

	/**
	 * Double Escapes HTML Form Data.
	 * Converts the following strings in the following order:
	 * <PRE>
	 * &amp; to &amp;amp;
	 * &lt;  to &amp;lt;
	 * &gt;  to &amp;gt;
	 * 
	 * @param s Raw Data
	 * @return String Double Escaped Data
	 */
	public static String doubleEscapeData(String s)
	{
		// Change all &amp; to &amp;amp;
		String s1 = Converter.findAndReplace(s, "&amp;", "&amp;amp;");

		// Change all &lt; to &amp;lt;
		String s2 = Converter.findAndReplace(s1, "&lt;", "&amp;lt;");
		
		// Change all &rt; to &amp;rt;
		String s3 = Converter.findAndReplace(s2, "&gt;", "&amp;gt;");
		
		return s3;
	}

	/**
	 * Separates the input String into substrings of the maximum input size.
	 * 
	 * @param s Input String
	 * @param size Maximum Size
	 * @return String[] Separated String[]
	 */
	public static String[] separate(String s, int size)
	{
		int length = s.length();
		int numPieces = (length / size) + 1;
		String[] result = new String[numPieces];
		for (int i = 0; i < numPieces; i++)
		{
			int start = i * size;
			int stop = start + size;
			stop = (length < stop)?length:stop;
			result[i] = s.substring(start, stop);
		}
		
		return result;
	}

    /**
     * DOCUMENT ME!
     *
     * @param formData DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String fixHtmlFormData(String formData)
    {
        formData = Converter.findAndReplace(formData, "+", "%2B");
        formData = Converter.findAndReplace(formData, "=", "%3D");
        formData = Converter.findAndReplace(formData, "&", "%26");

        return formData;
    }


}
