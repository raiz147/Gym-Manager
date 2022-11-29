package project2;
import java.util.Calendar;

/**
 *  Date class is used to deal with dates related to month, day, and year.
 *  @author James Liu, Ramazan Azimov
 */

public class Date implements Comparable<Date> {
    private int year;
    private int month;
    private int day;
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;
    public static final int MAX_DAYS_IN_MONTH = 31;
    public static final int DAYS_IN_NONKNUCKLE_MONTH = 30;
    public static final int MONTHS_IN_YEAR = 12;
    public static final int DAYS_IN_FEB = 28;
    public static final int DAYS_IN_FEB_LEAP = 29;
    public static final int FEBRUARY = 2;
    public static final int APRIL = 4;
    public static final int JUNE = 6;
    public static final int SEPTEMBER = 9;
    public static final int NOVEMBER = 11;
    public static final int ADULT_AGE = 18;

    /**
     * creates a Date object with today’s date
     */
    public Date() {
        Calendar today = Calendar.getInstance();
        this.year = today.get(Calendar.YEAR);
        this.month = today.get(Calendar.MONTH) + 1;
        this.day = today.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * takes "mm/dd/yyyy" and creates a Date object with those month, day, and year values
     * @param date string format of a date
     */
    public Date(String date) {
        this.month = Integer.parseInt(date.substring(0,date.indexOf("/")));
        this.day = Integer.parseInt(date.substring(date.indexOf("/")+1,date.lastIndexOf("/")));
        this.year = Integer.parseInt(date.substring(date.lastIndexOf("/")+1));
    }

    /**
     * Compares an object's date to the input date
     * @return integer representing greater, less, or equal to date object
     * @param date date object
     */
    @Override
    public int compareTo(Date date) {
        int d = date.getDay();
        int m = date.getMonth();
        int y = date.getYear();

        if (this.year - y > 0)
            return 1;
        else if (this.year - y < 0)
            return -1;
        else {
            if (this.month - m > 0)
                return 1;
            else if (this.month - m < 0)
                return -1;
            else{
                if (this.day - d > 0)
                    return 1;
                else if (this.day - d < 0)
                    return -1;
                else
                    return 0;
            }
        }
    }

    /**
     *  Checks if the date is a valid calendar date
     * @return boolean if calendar is valid
     */
    public boolean isValid() {
        if (this.year <= 0)
            return false;
        if (this.month < 1 || this.month > MONTHS_IN_YEAR)
            return false;
        if (this.day < 1 || this.day > MAX_DAYS_IN_MONTH)
            return false;
        switch (this.month)
        {
            case FEBRUARY:
            {
                if (isLeapYear() && this.day <= DAYS_IN_FEB_LEAP)
                    return true;
                else return !isLeapYear() && this.day <= DAYS_IN_FEB;

            }
            case APRIL:
            case JUNE:
            case SEPTEMBER:
            case NOVEMBER:
                return this.day <= DAYS_IN_NONKNUCKLE_MONTH;
            default: return true;
        }
    }

    /**
     *  Checks if a date is 18 years old
     * @return boolean
     */
    public boolean isAdult()
    {
        Date today = new Date();
        if (today.year - this.year < ADULT_AGE)
            return false;
        else if (today.year - this.year > ADULT_AGE)
            return true;
        else {
            if (today.month - this.month < 0)
                return false;
            else if (today.month - this.month > 0)
                return true;
            else
            {
                return today.day - this.day >= 0;
            }
        }
    }

    /**
     * get year
     * @return integer value of year
     */
    public int getYear() {
        return year;
    }

    /**
     * get month
     * @return integer value of month
     */
    public int getMonth() {
        return month;
    }

    /**
     * get day
     * @return integer value of day
     */
    public int getDay() {
        return day;
    }

    /**
     * checks if the year of a date is a leap year
     * @return boolean if year is a leap year
     */
    public boolean isLeapYear()
    {
        if (this.year % QUADRENNIAL != 0)
            return false;
        else if (this.year % QUATERCENTENNIAL == 0)
            return true;
        else
            return this.year % CENTENNIAL != 0;
    }

    /**
     * converts this Date object into a string format
     * @return string implementation of Date object
     */
    @Override
    public String toString()
    {
        return this.month + "/" + this.day + "/" + this.year;
    }
}