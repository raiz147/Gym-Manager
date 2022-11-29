package project2;
/**
 * Family class is used to store member information with a Family Membership type
 * @author Ramazan Azimov, James Liu
 */
public class Family extends Member
{
    private int guestPass;
    public static final int QUARTERLY = 3;
    public static final double ONE_TIME_FEE = 29.99;
    public static final double MONTHLY_FEE = 59.99;
    public static final int FAMILY_GUESTPASS_AMOUNT = 1;

    /**
     * Takes a Family member's information and store them in our private variables.
     * @param f family object
     */
    public Family(Family f)
    {
        super(f);
        this.guestPass = f.getGuestPass();
    }

    /**
     * Takes Fitness member information and store them in this object
     * @param f first name
     * @param l last name
     * @param d date of birth
     * @param exp Expiration date
     * @param loc Location
     */
    public Family(String f, String l, Date d, Date exp, Location loc)
    {
        super(f, l, d, exp, loc);
        this.guestPass = FAMILY_GUESTPASS_AMOUNT;
    }

    /**
     * formats family object into a string with membership fee
     * @return string format of member
     */
    @Override
    public String toStringMembership() {
        return super.toString() + ", (Family) guest-pass remaining "+ this.guestPass + ", Membership fee: $" + this.membershipFee();
    }

    /**
     * formats family object into a string
     * @return string format of member
     */
    @Override
    public String toString() {
        return super.toString() + ", (Family) guest-pass remaining: " + this.guestPass;
    }

    /**
     * get Membership Fee of this member
     * @return membership fee
     */
    @Override
    public double membershipFee()
    {
        return ONE_TIME_FEE + (MONTHLY_FEE * QUARTERLY);
    }

    /**
     * uses 1 guest pass of this Family member
     */
    public void useGuestPass()
    {
        guestPass -= 1;
    }

    /**
     * get number of guest passes this member has
     * @return integer number of guest passes
     */
    public int getGuestPass()
    {
        return this.guestPass;
    }

    /**
     * sets the guest pass amount for this member
     * @param num new guest pass amount
     */
    public void setGuestPass(int num)
    {
        this.guestPass = num;
    }

    /**
     * adds 1 guest pass to this Family member
     */
    public void addGuestPass()
    {
        this.guestPass += 1;
    }
}
