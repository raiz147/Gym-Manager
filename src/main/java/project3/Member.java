package project2;
/**
 *  Member class is used to store member information
 *  @author James Liu, Ramazan Azimov
 */
public class Member  implements Comparable<Member>{
    private String fname;
    private String lname;
    private Date dob;
    private Date expire;
    private Location location;
    public static final int QUARTERLY = 3;
    public static final double ONE_TIME_FEE = 29.99;
    public static final double MONTHLY_FEE = 39.99;

    /**
     * Takes a Member object's information and store them in our private variables.
     * @param m member object
     */
    public Member(Member m)
    {
        this.fname = m.fname;
        this.lname = m.lname;
        this.dob = m.dob;
        this.expire = m.expire;
        this.location = m.location;
    }

    /**
     * Takes Fitness member information and store them in a class called Member
     * @param f first name
     * @param l last name
     * @param d date of birth
     * @param exp Expiration date
     * @param loc Location
     */
    public Member(String f, String l, Date d, Date exp, Location loc)
    {
        this.fname = f;
        this.lname = l;
        this.dob = d;
        this.expire = exp;
        this.location = loc;
    }

    /**
     * formats member object into a string
     * @return string format of member
     */
    @Override
    public String toString() {
        return this.fname + " " + this.lname + ", DOB: " + this.dob.toString() + ", Membership expires " + this.expire.toString() + ", Location: " + this.location.toString();
    }

    /**
     * formats member object into a string with membership fee
     * @return string format of member
     */
    public String toStringMembership() {
        return this.toString() + ", Membership fee: $" + this.membershipFee();
    }

    /**
     * Comparing two Gym Member data using first name, last name, date of birth as conditions.
     Return 0 if they are the same, false otherwise.
     * @return true or false if params match
     * @param fname first name String
     * @param lname last name String
     * @param dob date of birth String
     */
    public boolean equals(String fname, String lname, String dob) {
        if(fname.toUpperCase().compareTo(this.fname.toUpperCase()) == 0)
        {
            if(lname.toUpperCase().compareTo(this.lname.toUpperCase()) == 0)
            {
                return dob.compareTo(this.dob.toString()) == 0;
            }
        }
        return false;
    }

    /**
     * Return integer value 0 as output if two Gym Member have the same identity(first name, last name, date of birth),
     and return positive or negative values if they are different.
     * @return integer values of CompareTo() fucntion
     * @param member Gym Member object
     */
    @Override
    public int compareTo(Member member) {
        if(this.lname.toUpperCase().compareTo(member.lname.toUpperCase()) == 0){
            return this.fname.toUpperCase().compareTo(member.fname.toUpperCase());
        }
        return this.lname.toUpperCase().compareTo(member.lname.toUpperCase());
    }

    /**
     * Comparing two Gym member with date of birth, 0 if equals, positive or negative Integer
     values if they are different.
     * @return integer values of CompareTo() fucntion
     * @param member Gym Member object
     */
    public int compareToDob(Member member) {
        return this.dob.compareTo(member.dob);
    }

    /**
     * Comparing two Gym member with the gym location, 0 if equals, positive or negative Integer
     values if they are different.
     * @return integer values of CompareTo() fucntion
     * @param member Gym Member object
     */
    public int compareToLoc(Member member){
        if(this.location.getCounty().compareTo(member.location.getCounty()) == 0) {
            return this.location.getZip().compareTo(member.location.getZip());
        }
        return this.location.getCounty().compareTo(member.location.getCounty());
    }

    /**
     * get first name of this member
     * @return first name String
     */
    public String getFname(){
        return this.fname;
    }

    /**
     * get last name of this member
     * @return last name String
     */
    public String getLname(){
        return this.lname;
    }

    /**
     * get date of birth of this member
     * @return date of birth Date enum
     */
    public Date getDob(){
        return this.dob;
    }

    /**
     * get expiration date of this member
     * @return expiration date Date enum
     */
    public Date getExpire(){
        return this.expire;
    }

    /**
     * get location of this member
     * @return location enum
     */
    public Location getLocation(){
        return this.location;
    }

    /**
     * get Membership Fee of this member
     * @return membership fee
     */
    public double membershipFee()
    {
        return ONE_TIME_FEE + (MONTHLY_FEE * QUARTERLY);
    }
}