import org.junit.Assert;
import org.junit.Test;

import model.Date;
import model.Day;

/**
 * Tests methods in Date class.
 */
public class DateTests {
  @Test
  public void testInvalidDateCreation() {
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new Date(Day.Monday, "abc");
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new Date(Day.Monday, "105a");
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new Date(Day.Monday, "105a12");
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new Date(null, "105a12");
    });

    Assert.assertThrows(IllegalArgumentException.class, () -> {
      new Date(Day.Monday, null);
    });
  }

  @Test
  public void testDoesSingleDateOccurBetweenTwoDates() {
    Date date = new Date(Day.Monday, "1130");

    Date startDate1 = new Date(Day.Monday, "1230");
    Date endDate1 = new Date(Day.Wednesday, "1400");

    Date startDate2 = new Date(Day.Monday, "0930");
    Date endDate2 = new Date(Day.Monday, "1000");

    Date startDate3 = new Date(Day.Monday, "1130");
    Date endDate3 = new Date(Day.Monday, "1200");

    Date startDate4 = new Date(Day.Monday, "0930");
    Date endDate4 = new Date(Day.Monday, "1130");

    Date startDate5 = new Date(Day.Monday, "1030");
    Date endDate5 = new Date(Day.Wednesday, "1130");

    Date startDate6 = new Date(Day.Sunday, "1300");
    Date endDate6 = new Date(Day.Thursday, "1500");

    Date startDate7 = new Date(Day.Friday, "1100");
    Date endDate7 = new Date(Day.Thursday, "1200");

    Assert.assertFalse(Date.doesSingleDateOccurBetweenTwoDates(startDate1, endDate1, date));
    Assert.assertFalse(Date.doesSingleDateOccurBetweenTwoDates(startDate2, endDate2, date));
    Assert.assertTrue(Date.doesSingleDateOccurBetweenTwoDates(startDate3, endDate3, date));
    Assert.assertTrue(Date.doesSingleDateOccurBetweenTwoDates(startDate4, endDate4, date));
    Assert.assertTrue(Date.doesSingleDateOccurBetweenTwoDates(startDate5, endDate5, date));
    Assert.assertTrue(Date.doesSingleDateOccurBetweenTwoDates(startDate6, endDate6, date));
    Assert.assertFalse(Date.doesSingleDateOccurBetweenTwoDates(startDate7, endDate7, date));
  }

  @Test
  public void testGetTotalMinutes() {
    Date d1 = new Date(Day.Monday, "1000");
    Assert.assertEquals(60 * 10, d1.getTotalMinutes());
    Date d2 = new Date(Day.Tuesday, "1043");
    Assert.assertEquals(60 * 10 + 43, d2.getTotalMinutes());
    Date d3 = new Date(Day.Wednesday, "1750");
    Assert.assertEquals(17 * 60 + 50, d3.getTotalMinutes());
  }
}
