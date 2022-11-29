package project2;
/**
 * This is the Time enum class
 *  @author James Liu, Ramazan Azimov
 */
public enum Time {
    Morning("9:30"),
    Afternoon("14:00"),
    Evening("18:30");

    private final String time;

    /**
     * Constructor for public class Time
     * @param time time
     */
    Time(String time){
        this.time = time;
    }

    /**
     * Forming a String of output in this specific format
     * @return string format
     */
    public String toString()
    {
        return this.time;
    }

    /**
     * match time
     * @param s time
     * @return Time based on given string
     */
    public Time match(String s)
    {
        if (s.toUpperCase().compareTo("MORNING") == 0)
            return Morning;
        if (s.toUpperCase().compareTo("AFTERNOON") == 0)
            return Afternoon;
        if (s.toUpperCase().compareTo("EVENING") == 0)
            return Evening;
        return null;
    }
}
