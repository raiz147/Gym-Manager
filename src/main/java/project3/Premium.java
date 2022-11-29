package project2;
/**
 * Premium class is used to store member information with a Premium Membership type
 * @author Ramazan Azimov, James Liu
 */
public class Premium  extends Family
{
    public static final int ANNUALLY = 12;
    public static final double MONTHLY_FEE = 59.99;
    public static final int PREMIUM_GUESTPASS_AMOUNT = 3;

    /**
     * Takes a Premium member's information and store them in our private variables.
     * @param p premium object
     */
    public Premium(Premium p)
    {
        super(p);
        super.setGuestPass(p.getGuestPass());
    }

    /**
     * Takes Fitness member information and store them in this object
     * @param f first name
     * @param l last name
     * @param d date of birth
     * @param exp Expiration date
     * @param loc Location
     */
    public Premium(String f, String l, Date d, Date exp, Location loc)
    {
        super(f, l, d, exp, loc);
        super.setGuestPass(PREMIUM_GUESTPASS_AMOUNT);
    }

    /**
     * formats premium object into a string with membership fee
     * @return string format of member
     */
    @Override
    public String toStringMembership() {
        return super.getFname() + " " + super.getLname() + ", DOB: " + super.getDob().toString() + ", Membership expires " + super.getExpire().toString()
                + ", Location: " + super.getLocation().toString() + ", (Premium) guest-pass remaining "+ super.getGuestPass() + ", Membership fee: $" + this.membershipFee();
    }

    /**
     * formats family object into a string
     * @return string format of member
     */
    @Override
    public String toString() {
        return this.getFname() + " " + this.getLname() + ", DOB: " + this.getDob().toString() + ", Membership expires "
                + this.getExpire().toString() + ", Location: " + this.getLocation().toString() + ", (Premium) guest-pass remaining: " + super.getGuestPass();
    }

    /**
     * get Membership Fee of this member
     * @return membership fee
     */
    @Override
    public double membershipFee()
    {
    return (MONTHLY_FEE * ANNUALLY) - MONTHLY_FEE;
    }

    /**
     * uses 1 guest pass of this Premium member
     */
    public void useGuestPass()
    {
        super.useGuestPass();
    }
}
