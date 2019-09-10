package at.austriapro;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.annotation.Nullable;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Common interface, shared among all mappings implemented by AUSTRIAPro
 */
public abstract class Mapping {

  public final MappingLog mLog = new MappingLog(MappingLogLevel.WARNING);

  /**
   * Performs a mapping from ebInterface to a given target format, specified by the implementing
   * class
   */
  public abstract byte[] mapFromebInterface(String ebinterface) throws MappingException;

  public String getMappingLog() {
    return mLog.toString();
  }

  public String getMappingLogHTML() {
    return mLog.toHTML();
  }


  /**
   * Convert the passed {@link XMLGregorianCalendar} to a
   * {@link GregorianCalendar}.
   *
   * @param aCal
   *        Source calendar. May be <code>null</code>.
   * @return <code>null</code> if the parameter is <code>null</code>.
   */
  @Nullable
  public static GregorianCalendar getGregorianCalendar (@Nullable final XMLGregorianCalendar aCal) {
    if (aCal == null)
      return null;
    return aCal.toGregorianCalendar (aCal.getTimeZone (aCal.getTimezone ()),
                                     Locale.getDefault (Locale.Category.FORMAT),
                                     null);
  }

  /**
   * Get the passed {@link XMLGregorianCalendar} as {@link LocalDate}.
   *
   * @param aCal
   *        The source {@link XMLGregorianCalendar}. May be <code>null</code>.
   * @return <code>null</code> if the parameter is <code>null</code>.
   */
  @Nullable
  public static LocalDate getLocalDate (@Nullable final XMLGregorianCalendar aCal) {
    if (aCal == null)
      return null;
    return getGregorianCalendar (aCal).toZonedDateTime ().toLocalDate ();
  }

  /**
   * Get the passed {@link XMLGregorianCalendar} as {@link LocalDateTime}.
   *
   * @param aCal
   *        The source {@link XMLGregorianCalendar}. May be <code>null</code>.
   * @return <code>null</code> if the parameter is <code>null</code>.
   */
  @Nullable
  public static LocalDateTime getLocalDateTime (@Nullable final XMLGregorianCalendar aCal) {
    if (aCal == null)
      return null;
    return getGregorianCalendar (aCal).toZonedDateTime ().toLocalDateTime ();
  }
}
