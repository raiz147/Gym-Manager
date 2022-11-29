package project2;
/**
 This is the Location enum class
 @author James Liu, Ramazan Azimov
 */
public enum Location{
    Bridgewater("Bridgewater","08807", "Somerset"),
    Edison ("Edison","08837", "Middlesex"),
    Franklin("Franklin","08873", "Somerset"),
    Piscataway("Piscataway","08854", "Middlesex"),
    Somerville("Somerville","08876", "Somerset");
    private final String city;
    private final String zip;
    private final String county;

    /**
     * Constructor for public class Location
     * @param city city
     * @param zip zip
     * @param county county
     */
    Location(String city, String zip, String county){
        this.city = city;
        this.zip = zip;
        this.county = county;
    }

    /**
     Forming a String of output in this specific format
     @return string format
     */
    public String toString()
    {
        return this.city + ", " + this.zip + ", " + this.county;
    }

    /**
     * match location
     * @param s string
     * @return Location based on given string
     */
    public static Location match(String s)
    {
        if (s.toUpperCase().compareTo(Bridgewater.getCity().toUpperCase()) == 0)
            return Bridgewater;
        if (s.toUpperCase().compareTo(Edison.getCity().toUpperCase()) == 0)
            return Edison;
        if (s.toUpperCase().compareTo(Franklin.getCity().toUpperCase()) == 0)
            return Franklin;
        if (s.toUpperCase().compareTo(Piscataway.getCity().toUpperCase()) == 0)
            return Piscataway;
        if (s.toUpperCase().compareTo(Somerville.getCity().toUpperCase()) == 0)
            return Somerville;
        return null;
    }

    /**
     * Public method for us to extract the data stored in the private variable county.
     * @return county
     */
    public String getCounty()
    {
        return this.county;
    }

    /**
     * Public method for us to extract the data stored in the private variable zip.
     * @return zip
     */
    public String getZip()
    {
        return this.zip;
    }

    /**
     * Public method for us to extract the data stored in the private variable city.
     * @return city
     */
    public String getCity()
    {
        return this.city;
    }
}