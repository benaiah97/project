package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import org.junit.Test;

/**
 * This class tests the date time business rules. 
 * 
 * @author lewit019
 * 
 */
public class DateTimeRulesTestCase {

  /**
   * Tests the get GMT date now method.
   *
   */
  @Test
  public final void testGetGMTDateNow() {

    GregorianCalendar gmtDate = DateTimeRules.getGMTDateNow();

    if (gmtDate == null) {
      fail("Unable to get GMT date in Gregorian Calendar format.");
    }

    // GMT Date should be either 4 or 5 hours more than present time
    Date gmt = gmtDate.getTime();
    Date now = new Date();

    long difference = Math.abs((gmt.getTime() - now.getTime()));

    long minusMillis = Math.round((double) difference / 1000);

    long hourVariance = minusMillis / 3600;

    if ((hourVariance < 4) || (hourVariance > 5)) {
      fail("Delta off of GMT does not match EST standard. (4 or 5, but is :" + hourVariance + ").");
    }

    return;
  }

  /**
   * Tests the method that determines if the current time is within a set of GMT times.
   *
   */
  @Test
  public final void testIsNowWithinGMTTime() {

    Long oneHour = 3600000L;

    GregorianCalendar gmtDate = DateTimeRules.getGMTDateNow();

    if (gmtDate == null) {
      fail("Unable to get GMT date in Gregorian Calendar format.");
    }

    // GMT Date should be either 4 or 5 hours more than present time
    Date gmt = gmtDate.getTime();

    // 1 hour from now
    Date tooEarlyStartDate = new Date(gmt.getTime() + (1 * oneHour));
    // 3 hours from now
    Date tooEarlyEndDate = new Date(gmt.getTime() + (3 * oneHour));

    // 3 hours ago
    Date tooLateStartDate = new Date(gmt.getTime() - (3 * oneHour));
    // 1 hour ago
    Date tooLateEndDate = new Date(gmt.getTime() - (1 * oneHour));

    // 1 hour ago
    Date justRightStartDate = new Date(gmt.getTime() - (1 * oneHour));
    // 1 hour from now
    Date justRightEndDate = new Date(gmt.getTime() + (1 * oneHour));

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    // Test 1: Too early (window not yet here)
    String startDate = sdf.format(tooEarlyStartDate);
    String endDate = sdf.format(tooEarlyEndDate);

    if (DateTimeRules.isNowWithinDate(startDate, endDate)) {
      fail("Method improperly approved a time before a window began.");
    }

    // Test 2: Too late (window has passed by)
    startDate = sdf.format(tooLateStartDate);
    endDate = sdf.format(tooLateEndDate);

    if (DateTimeRules.isNowWithinGMTTime(startDate, endDate)) {
      fail("Method improperly approved a time after a window ended.");
    }

    // Test 3: Just right within window)
    startDate = sdf.format(justRightStartDate);
    endDate = sdf.format(justRightEndDate);

    if (!DateTimeRules.isNowWithinGMTTime(startDate, endDate)) {
      fail("Method improperly failed a time in a valid window.");
    }

    return;
  }

  /**
   * Tests the method that determines if the present date is within a range of dates.
   *
   */
  @Test
  public final void testIsNowWithinDate() {

    Long oneDay = 86400000L;

    Date rightNow = new Date();

    // 1 day from now
    Date tooEarlyStartDate = new Date(rightNow.getTime() + (1 * oneDay));
    // 2 days from now
    Date tooEarlyEndDate = new Date(rightNow.getTime() + (2 * oneDay));

    // 2 days ago
    Date tooLateStartDate = new Date(rightNow.getTime() - (2 * oneDay));
    ;
    // 1 day ago
    Date tooLateEndDate = new Date(rightNow.getTime() - (1 * oneDay));
    ;

    // 1 day ago
    Date justRightStartDate = new Date(rightNow.getTime() - (1 * oneDay));
    // 1 day from now
    Date justRightEndDate = new Date(rightNow.getTime() + (1 * oneDay));

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Test 1: Too early (window not yet here)
    String startDate = sdf.format(tooEarlyStartDate);
    String endDate = sdf.format(tooEarlyEndDate);

    if (DateTimeRules.isNowWithinDate(startDate, endDate)) {
      fail("Method improperly approved a time before a window began.");
    }

    // Test 2: Too late (window has passed by)
    startDate = sdf.format(tooLateStartDate);
    endDate = sdf.format(tooLateEndDate);

    if (DateTimeRules.isNowWithinDate(startDate, endDate)) {
      fail("Method improperly approved a time after a window ended.");
    }

    // Test 3: Just right within window)
    startDate = sdf.format(justRightStartDate);
    endDate = sdf.format(justRightEndDate);

    if (!DateTimeRules.isNowWithinDate(startDate, endDate)) {
      fail("Method improperly failed a time in a valid window.");
    }

    return;
  }
  
  /**
   * Tests the method that determines if the present date is within a range of dates.
   *
   */
  @Test
  public final void testForIssue() {
    
    Date aDate = new Date();
   
    System.out.println("The long number for right now is: " + aDate.getTime());
    
    GregorianCalendar gmtCalendar = DateTimeRules.getGMTDateNow();
    
    Date gmtDate = gmtCalendar.getTime();
    
    System.out.println("The long number for GMT now is: " + gmtDate.getTime());
    
    long baseTestTime = 1263988700000L; // Base date time (GMT): 01/20/2010 06:58:20.000
    //long baseTestTime = 1263902300000L; // Base date time (GMT): 01/19/2010 06:58:20.000
    //long baseTestTime = 1264075100000L; // Base date time (GMT): 01/21/2010 06:58:20.000
    
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
    
    System.out.println("Base date time (GMT): " + sdf.format(new Date(baseTestTime)));
    
    for (long i = baseTestTime; i < (baseTestTime + 25200000);) {
     
      Date gmtNow = new Date(i);
      boolean result = DateTimeRulesTestCase.isNowWithinGMTTime(gmtNow, "11:05", "07:00");

      String resultString;
      if (result)
        resultString = "true";
      else 
        resultString = "false";
      
      System.out.println("Time " + sdf.format(gmtNow) + " = " + resultString);
      
      i+=300000;
    }
    
    return;
  }

  
  /**
   * Determines if now is within the GMT time.
   * 
   * @param startTime
   *          Start time
   * @param endTime
   *          End time
   * @return True if so, false if not.
   */
  public static boolean isNowWithinGMTTime(Date todayGMT, String startTime, String endTime) {

    if ((startTime == null) || (endTime == null)) {
      return true;
    }

    // Validate entity time
    StringTokenizer strTknStart = new StringTokenizer(startTime, ":");
    StringTokenizer strTknEnd = new StringTokenizer(endTime, ":");

    String startTimeHour = strTknStart.nextToken();
    String startTimeMinute = strTknStart.nextToken();
    int startTimeInt = Integer.parseInt(startTimeHour + startTimeMinute);

    String endTimeHour = strTknEnd.nextToken();
    String endTimeMinute = strTknEnd.nextToken();
    int endTimeInt = Integer.parseInt(endTimeHour + endTimeMinute);

    SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
    String curTimeGMT = sdf.format(todayGMT);
    int curTimeInt = Integer.parseInt(curTimeGMT);

    if (startTimeInt < endTimeInt) {
      if ((curTimeInt > endTimeInt) || (curTimeInt < startTimeInt))
        return false;
      else
        return true;
    } else {
      if ((endTimeInt < curTimeInt) && (startTimeInt > curTimeInt))
        return false;
      else
        return true;
    }

  }
  
}
