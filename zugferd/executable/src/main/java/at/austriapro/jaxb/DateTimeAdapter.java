package at.austriapro.jaxb;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;

import javax.xml.bind.DatatypeConverter;

public class DateTimeAdapter {

  private static SimpleDateFormat sdf = new SimpleDateFormat(
      "yyyy-MM-dd'T'HH:mm:ss");

  public synchronized static DateTime parseDate(String s) {
    return new DateTime(DatatypeConverter.parseDate(s).getTime());
  }

  public synchronized static String printDate(DateTime dt) {
    String date = sdf.format(dt);
    return date;
  }
}
