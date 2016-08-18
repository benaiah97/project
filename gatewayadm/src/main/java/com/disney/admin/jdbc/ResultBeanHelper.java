package com.disney.admin.jdbc;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import com.disney.util.Converter;

/**
 * @author FAV2
 * Created on May 22, 2003
 */
public class ResultBeanHelper
{
	private final static String QUOTE = "\"";
	private final static String COMMA = ",";
	private final static String TAB = "\t";
	private final static String NEW_LINE = "\n";
	private final static String SPACE = " ";
	
	/**
	 * Constructor for ResultBeanHelper.
	 */
	private ResultBeanHelper()
	{
		super();
	}

	/**
	 * Method outputToCsv.
	 * @param out
	 * @param resultBean
	 */
	public static void outputToCsv(OutputStream out, ResultBean resultBean)
	{
		outputToDelim(out, resultBean, COMMA);
	}
	
	/**
	 * Method outputToText.
	 * @param out
	 * @param resultBean
	 */
	public static void outputToText(OutputStream out, ResultBean resultBean)
	{
		outputToDelim(out, resultBean, TAB);
	}
	
	/**
	 * Method outputToDelim.
	 * @param out
	 * @param resultBean
	 * @param delim
	 */
	public static void outputToDelim(OutputStream out, ResultBean resultBean, String delim)
	{
		PrintWriter writer = new PrintWriter(out);
		List cnList = resultBean.getColumnNameList();
		for (int i = 0; i < cnList.size(); i++)
		{
			writer.print(QUOTE);
			writer.print(Converter.findAndReplace((String)cnList.get(i), QUOTE, QUOTE + QUOTE));
			writer.print(QUOTE);
			writer.print(delim);
		}
		writer.println();
		writer.flush();
		
		List rowList = resultBean.getRowList();
		for (int r = 0; r < rowList.size(); r++)
		{
			List row = (List)rowList.get(r);
			for (int col = 0; col < row.size(); col++)
			{
				writer.print(QUOTE);
				String scrubbedData = Converter.findAndReplace((String)row.get(col), QUOTE, QUOTE + QUOTE);
				scrubbedData = Converter.findAndReplace(scrubbedData, NEW_LINE, SPACE);
				writer.print(scrubbedData);
				writer.print(QUOTE);
				writer.print(delim);
			}
			writer.println();
			writer.flush();
		}
	}

}
