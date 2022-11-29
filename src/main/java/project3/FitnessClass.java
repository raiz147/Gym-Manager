package project2;
import java.util.ArrayList;

/**
 FitnessClass is used to store fitness class information, along with its corresponding members and guests
 @author Ramazan Azimov, James Liu
 */
public class FitnessClass {
    private String activity;
    private String instructor;
    private Time time;
    private Location location;
    private ArrayList<Member> mlist;
    private ArrayList<Member> glist;

    /**
     * takes fitness class information and stores them in this object
     * @param activity activity name
     * @param instructor instructor name
     * @param time time of activity
     * @param location location of activity
     */
    public FitnessClass(String activity, String instructor, Time time, Location location)
    {
        this.mlist = new ArrayList<>();
        this.glist = new ArrayList<>();
        this.activity = activity;
        this.instructor = instructor;
        this.time = time;
        this.location = location;
    }

    /**
     * compares two fitness classes using activity, instructor, and location as conditions
     * @param fitnessClass fitness class object
     * @return boolean if the two classes are equal
     */
    public boolean equals(FitnessClass fitnessClass)
    {
        if (this.activity.toUpperCase().compareTo(fitnessClass.getActivity().toUpperCase()) == 0)
        {
            if (this.instructor.toUpperCase().compareTo(fitnessClass.getInstructor().toUpperCase()) == 0)
            {
                if (this.location.getCity().toUpperCase().compareTo(fitnessClass.getLocation().getCity().toUpperCase()) == 0)
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * finds member in member list
     * @param member member
     * @return index of member
     */
    private int findMember(Member member) {
        return this.mlist.indexOf(member);
    }

    /**
     * finds guest in guest list
     * @param member guest
     * @return index of guest
     */
    public int findGuest(Member member) {
        return this.glist.indexOf(member);
    }

    /**
     * returns the first member in the member list
     * @return member
     */
    public Member getSingleMember()
    {
        return this.mlist.get(0);
    }

    /**
     * returns the first guest in the guest list
     * @return guest
     */
    public Member getSingleGuest()
    {
        return this.glist.get(0);
    }

    /**
     * adds a member to the member list
     * @param member member
     * @return boolean if member was added
     */
    public boolean add(Member member) {
        if (findMember(member) < 0)
        {
            return mlist.add(member);
        }
        return false;
    }

    /**
     * adds a guest to the guest list
     * @param member guest
     * @return boolean if guest was added
     */
    public boolean addGuest(Member member) {
        return glist.add(member);
    }

    /**
     * removes a guest from the guest list
     * @param member guest
     * @return boolean if guest was removed
     */
    public boolean removeGuest(Member member) {
        return glist.remove(member);
    }

    /**
     * Removes a member out of the member list.
     * @param member member
     * @return boolean if member was removed
     */
    public boolean remove(Member member) {
        return mlist.remove(member);
    }

    /**
     * formats this fitness class into a string
     * @return string format of fitness class
     */
    @Override
    public String toString()
    {
        return this.activity.toUpperCase() + " - " + this.instructor.toUpperCase() + ", " + this.time + ", " + this.location.getCity().toUpperCase();
    }

    /**
     * gets size of member list
     * @return member list size
     */
    public int getSize()
    {
        return  this.mlist.size();
    }

    /**
     * gets activity of this fitness class
     * @return activity
     */
    public String getActivity()
    {
        return this.activity;
    }

    /**
     * gets instuctor of this fitness class
     * @return instructor name
     */
    public String getInstructor()
    {
        return this.instructor;
    }

    /**
     * gets location of this fitness class
     * @return location enum
     */
    public Location getLocation()
    {
        return this.location;
    }

    /**
     * gets time of this fitness class
     * @return time enum
     */
    public Time getTime()
    {
        return this.time;
    }

    /**
     * gets member list of this fitness class
     * @return member list
     */
    public ArrayList<Member> getMlist()
    {
        return mlist;
    }

    /**
     * gets guest list of this fitness class
     * @return guest list
     */
    public ArrayList<Member> getGlist()
    {
        return glist;
    }
}