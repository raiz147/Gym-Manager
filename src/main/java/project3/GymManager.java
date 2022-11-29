package project2;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 GymManager is our main interface, where we are able to use many methods we build by using commands.
 @author James Liu, Ramazan Azimov
 */
public class GymManager
{
    /**
     * run method will initiate the interface on console where user can use commands.
     */
    public void run() {
        System.out.println("Gym Manager running...");
        Scanner sc = new Scanner(System.in);
        MemberDatabase members = new MemberDatabase();
        ClassSchedule classSchedule = new ClassSchedule();
        while(true) {
            String inputLine = sc.nextLine();
            String[] split = inputLine.split(" ");
            if (split[0].compareTo("Q") == 0){break;}
            else if (split[0].compareTo("A") == 0) {addMember(split, members);}
            else if (split[0].compareTo("R") == 0) {removeMember(split, members);}
            else if (split[0].compareTo("P") == 0) {members.print();}
            else if (split[0].compareTo("PC") == 0) {members.printByCounty();}
            else if (split[0].compareTo("PN") == 0) {members.printByName();}
            else if (split[0].compareTo("PD") == 0) {members.printByExpirationDate();}
            else if (split[0].compareTo("S") == 0) {classSchedule.print();}
            else if (split[0].compareTo("C") == 0) {checkIn(split, members, classSchedule);}
            else if (split[0].compareTo("D") == 0) {drop(split, members, classSchedule);}
            else if (split[0].compareTo("LS") == 0) {loadFitnessClasses(classSchedule);}
            else if (split[0].compareTo("LM") == 0) {loadMemberList(members);}
            else if (split[0].compareTo("AF") == 0) {addFamilyMember(split, members);}
            else if (split[0].compareTo("AP") == 0) {addPremiumMember(split, members);}
            else if (split[0].compareTo("PF") == 0) {members.printMembershipFees();}
            else if (split[0].compareTo("CG") == 0) {checkGuestCheckIn(split, members, classSchedule);}
            else if (split[0].compareTo("DG") == 0) {dropGuest(split, members, classSchedule);}
            else
                System.out.println(split[0] + " is an invalid command!");
        }
        System.out.println("Gym Manager terminated.");
    }

    /**
     * drops a guest from a fitness class
     * checks if input is valid
     * checks if guest is registered for that class
     * @param input input string
     * @param members member database
     * @param classSchedule fitness class schedule
     */
    private void dropGuest(String[] input, MemberDatabase members, ClassSchedule classSchedule)
    {
        if (!(new Date(input[6]).isValid()))
        {
            System.out.println("DOB " + input[6] + ": invalid calendar date!");
            return;
        }
        if (checkFitnessClass(input))
        {
            FitnessClass fitnessClass = new FitnessClass(input[1], input[2], null, getLocation(input[3]));
            Time t = classSchedule.exists(fitnessClass).getTime();
            fitnessClass = new FitnessClass(input[1], input[2], t, getLocation(input[3]));
            Member guest = members.exists(new Member(input[4], input[5], new Date(input[6]), new Date(), Location.Edison));
            fitnessClass.addGuest(guest);
            if (classSchedule.exists(fitnessClass).findGuest(guest) < 0) {
                System.out.println(input[4] + " " + input[5] + " " + input[6] + " guest is not checked in.");
            }
            else {
                int classIndex = classSchedule.find(fitnessClass);
                if (classIndex < 0) {
                    System.out.println(input[1] + " by " + input[2] + " does not exist at " + input[3]);
                } else {
                    System.out.println(classSchedule.dropGuest(fitnessClass));
                }
            }
        }
    }

    /**
     * Drops a member from a fitness class if that member is registered for that class.
     Checks if member's date of birth is valid, and if the class exists.
     * @param m FitnessClass
     * @param input input list String
     * @param classSchedule class schedule
     */
    private void drop(String[] input, MemberDatabase m, ClassSchedule classSchedule) {
        if (!(new Date(input[6]).isValid()))
        {
            System.out.println("DOB " + input[6] + ": invalid calendar date!");
            return;
        }
        if (checkFitnessClass(input))
        {
            FitnessClass temp = new FitnessClass(input[1], input[2], null, getLocation(input[3]));
            Member test = m.exists(new Member(input[4], input[5], new Date(input[6]), new Date(), Location.Edison));
            if (test == null)
            {
                System.out.println(input[4] + " " + input[5] + " " + input[6] + " is not in the database.");
            }
            else {
                temp.add(test);
                int classIndex = classSchedule.find(temp);
                if (classIndex < 0) {
                    System.out.println(input[1] + " by " + input[2] + " does not exist at " + input[3]);
                } else {
                    System.out.println(classSchedule.drop(temp));
                }
            }
        }
    }

    /**
     * checks if the guest checking in is valid
     * @param input input string
     * @param members member database
     * @param classSchedule fitness class schedule
     */
    private void checkGuestCheckIn(String[] input, MemberDatabase members, ClassSchedule classSchedule) {
        if (checkCheckInDate(input)) {
            if(checkFitnessClass(input)) {
                if (checkCheckInMember(input, members)) {
                    if(checkGuestLocation(input, members)) {
                        FitnessClass fitnessClass = new FitnessClass(input[1], input[2], null, getLocation(input[3]));
                        if (classSchedule.exists(fitnessClass) == null) {
                            System.out.println(fitnessClass.getActivity() + " by " + fitnessClass.getInstructor() + " does not exist at " + fitnessClass.getLocation().getCity());
                            return;
                        }
                        Time t = classSchedule.exists(fitnessClass).getTime();
                        fitnessClass = new FitnessClass(input[1], input[2], t, getLocation(input[3]));
                        Member member = members.exists(new Member(input[4], input[5], new Date(input[6]), new Date(), Location.Edison));
                        fitnessClass.addGuest(member);
                        if (member instanceof Premium || member instanceof Family) {
                            if (checkGuestPassNum(fitnessClass)) {
                                if (classSchedule.checkInGuest(fitnessClass)) {
                                    System.out.println(member.getFname() + " " + member.getLname() + " (guest) checked in " + fitnessClass.getActivity().toUpperCase()
                                            + " - " + fitnessClass.getInstructor().toUpperCase() + ", " + fitnessClass.getTime() + ", " + fitnessClass.getLocation().getCity().toUpperCase());
                                    classSchedule.printClass(fitnessClass);
                                }
                            }
                        } else
                            System.out.println("Standard membership - guest check-in is not allowed.");
                    }
                }
            }
        }
    }

    /**
     * checks if the member has run out of guest passes
     * @param fitnessClass fitness class
     * @return boolean if member has run out of guest passes
     */
    private boolean checkGuestPassNum(FitnessClass fitnessClass)
    {
        Member guest = fitnessClass.getSingleGuest();
        if (guest instanceof Family)
        {
            if(((Family) guest).getGuestPass() <= 0)
            {
                System.out.println(guest.getFname() + " " + guest.getLname() + " ran out of guest passes.");
                return false;
            }
        }
        return true;
    }

    /**
     * checks if the guest is checking in at valid location
     * @param input input string
     * @param members member database
     * @return boolean if guest check in location is valid
     */
    private boolean checkGuestLocation(String[] input, MemberDatabase members)
    {
        Member member = members.exists(new Member(input[4], input[5], new Date(input[6]), new Date(), Location.Edison));
        if (!(member instanceof Family)) {
            System.out.println("Standard membership - guest check-in is not allowed.");
            return false;
        }
        if (getLocation(input[3]).toString().compareTo(member.getLocation().toString()) != 0)
        {
            System.out.println(member.getFname() + " " + member.getLname() + " Guest checking in " + getLocation(input[3]).toString().toUpperCase()
                               + " - guest location restriction");
            return false;
        }
        return true;
    }

    /**
     * checks if participant checking in is valid
     * @param input input string
     * @param m member database
     * @param classSchedule fitness class schedule
     */
    private void checkIn(String[] input, MemberDatabase m, ClassSchedule classSchedule)
    {
        if (checkCheckInDate(input)) {
            if(checkFitnessClass(input)) {
                if (checkCheckInMember(input, m)) {
                    if(checkCheckInLocation(input, m)) {
                        FitnessClass fitnessClass = new FitnessClass(input[1], input[2], null, getLocation(input[3]));
                        if (classSchedule.exists(fitnessClass) == null)
                        {
                            System.out.println(fitnessClass.getActivity() + " by " + fitnessClass.getInstructor() + " does not exist at " + fitnessClass.getLocation().getCity());
                            return;
                        }
                        Time t = classSchedule.exists(fitnessClass).getTime();
                        fitnessClass = new FitnessClass(input[1], input[2], t, getLocation(input[3]));
                        Member member = m.exists(new Member(input[4], input[5], new Date(input[6]), new Date(), Location.Edison));
                        fitnessClass.add(member);
                        if(checkExists(classSchedule, fitnessClass)) {
                            if (checkCheckInTime(classSchedule, fitnessClass)) {
                                if (classSchedule.checkIn(fitnessClass))
                                {
                                    System.out.println(member.getFname() + " " + member.getLname() + " checked in "
                                            + fitnessClass.getActivity().toUpperCase() + " - " + fitnessClass.getInstructor().toUpperCase() + ", " +
                                            fitnessClass.getTime() + ", " + fitnessClass.getLocation().getCity());
                                    classSchedule.printClass(fitnessClass);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * loads members from a file into member database
     * @param members member database
     */
    private void loadMemberList(MemberDatabase members)
    {
        try {
            File myFile = new File("memberList.txt");
            Scanner sc = new Scanner(myFile);
            while (sc.hasNextLine()) {
                String inputLine = sc.nextLine();
                String[] split = inputLine.split(" ");
                if (split.length < 4)
                    break;
                Member temp = new Member(split[0], split[1], new Date(split[2]), new Date(split[3]), getLocation(split[4]));
                members.add(temp);
            }
            sc.close();
            System.out.println("-list of members loaded-");
            members.print();
        } catch (FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * loads fitness classes from a file into class schedule
     * @param classSchedule class schedule
     */
    private void loadFitnessClasses(ClassSchedule classSchedule){
        try {
            File myFile = new File("classSchedule.txt");
            Scanner sc = new Scanner(myFile);
            while (sc.hasNextLine()) {
                String inputLine = sc.nextLine();
                String[] split = inputLine.split(" ");
                if (split.length < 4)
                    break;
                FitnessClass temp = new FitnessClass(split[0], split[1], getTime(split[2]), getLocation(split[3]));
                classSchedule.addClass(temp);
            }
            sc.close();
            System.out.println("-Fitness classes loaded-");
            classSchedule.print();
        } catch (FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * checks if member already checked into a fitness class
     * @param classSchedule class schedule
     * @param fitnessClass fitness class
     * @return boolean if participant already checked in
     */
    private boolean checkExists(ClassSchedule classSchedule, FitnessClass fitnessClass)
    {
        if(classSchedule.exists(fitnessClass).getMlist().contains(fitnessClass.getSingleMember()))
        {
            System.out.println(fitnessClass.getSingleMember().getFname() + " " + fitnessClass.getSingleMember().getLname() + " already checked in");
            return false;
        }
        return true;

    }

    /**
     * checks if there is a time conflict for member checking in
     * @param classSchedule class schedule
     * @param fitnessClass fitness class
     * @return boolean if there is a time conflict
     */
    private boolean checkCheckInTime(ClassSchedule classSchedule, FitnessClass fitnessClass)
    {
        FitnessClass[] temp = classSchedule.getFitnessClasses();
        for (int i = 0; i < classSchedule.getNumClasses(); i++)
        {
            if (temp[i].getMlist().contains(fitnessClass.getSingleMember()))
            {
                if(temp[i].getTime().compareTo(fitnessClass.getTime()) == 0)
                {
                    System.out.println("Time conflict - " + fitnessClass.getActivity().toUpperCase() + " - " + fitnessClass.getInstructor().toUpperCase()
                            + ", " + fitnessClass.getTime() + ", " + fitnessClass.getLocation().toString().toUpperCase());
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * checks if check in location is at membership location
     * @param input input string
     * @param m member database
     * @return boolean if check in location is valid
     */
    private boolean checkCheckInLocation(String[] input, MemberDatabase m)
    {
        Member test = m.exists(new Member(input[4], input[5], new Date(input[6]), new Date(), Location.Edison));
        if (test instanceof Premium)
            return true;
        else if (test instanceof Family)
            return true;
        else
        {
            if (getLocation(input[3]) != test.getLocation())
            {
                System.out.println(test.getFname() + " " + test.getLname() + " checking in " + getLocation(input[3]).toString()
                                    + " - standard membership location restriction");
                return false;
            }
            return true;
        }
    }

    /**
     * checks if input is an actual fitness class
     * @param input input string
     * @return boolean if input fitness class if valid
     */
    private boolean checkFitnessClass(String[] input)
    {
        if(input[1].toUpperCase().compareTo("PILATES") != 0 && input[1].toUpperCase().compareTo("SPINNING") != 0 && input[1].toUpperCase().compareTo("CARDIO") != 0)
        {
            System.out.println(input[1] + " - class does not exist");
            return false;
        }
        if (input[2].toUpperCase().compareTo("JENNIFER") != 0 && input[2].toUpperCase().compareTo("KIM") != 0
                && input[2].toUpperCase().compareTo("DAVIS") != 0 && input[2].toUpperCase().compareTo("DENISE") != 0 && input[2].toUpperCase().compareTo("EMMA") != 0)
        {
            System.out.println(input[2] + " - instructor does not exist");
            return false;
        }
        if (input[3].toUpperCase().compareTo("BRIDGEWATER") != 0 && input[3].toUpperCase().compareTo("PISCATAWAY") != 0
                && input[3].toUpperCase().compareTo("SOMERVILLE") != 0 && input[3].toUpperCase().compareTo("FRANKLIN") != 0 && input[3].toUpperCase().compareTo("EDISON") != 0)
        {
            System.out.println(input[3] + " - invalid location");
            return false;
        }
        return true;
    }

    /**
     * checks if check in member is valid
     * member is in the database
     * membership is not expired and is valid
     * @param input input string
     * @param m membership database
     * @return boolean if check in member is valid
     */
    private boolean checkCheckInMember(String[] input, MemberDatabase m)
    {
        Member test = new Member(input[4], input[5], new Date(input[6]), new Date(), Location.Edison);
        Date today = new Date();
        if (m.exists(test) == null) {
            System.out.println(input[4] + " " + input[5] + " " + input[6] + " is not in the database");
            return false;
        }
        Date memberExp = new Date(m.exists(test).getExpire().toString());
        if(!memberExp.isValid()){
            System.out.println("Expiration date: " + input[6] + " invalid calendar date!");
            return false;
        }
        if(memberExp.compareTo(today) < 0){
            System.out.println(input[4] + " " + input[5] + " " + input[6]+ " membership expired");
            return false;
        }
        return true;
    }

    /**
     * checks if the date of birth of a member checking in is valid
     * @param input input string
     * @return boolean if date of birth is valid
     */
    private boolean checkCheckInDate(String[] input)
    {
        Date today = new Date();
        Date memberDob = new Date(input[6]);
        if (!memberDob.isValid()) {
            System.out.println("DOB " + input[6] + " Invalid calendar date!");
            return false;
        }
        if (memberDob.compareTo(today) >= 0){
            System.out.println("DOB " + input[6] + " cannot be today or a future date!");
            return false;
        }
        if (!memberDob.isAdult()){
            System.out.println("DOB " + input[6] + " must be 18 or older to join!");
            return false;
        }
        return true;
    }

    /**
     * adds a member to the member database
     * @param split input string
     * @param members member database
     */
    private void addMember(String[] split, MemberDatabase members)
    {
        if (checkAdd(split)) {
            if (members.add(new Member(split[1], split[2], new Date(split[3]), new Date(getNewExpDate().toString()), getLocation(split[4]))))
                System.out.println(split[1] + " " + split[2] + " added");
            else
                System.out.println(split[1] + " " + split[2] + " is already in the database");
        }
    }

    /**
     * adds a Family member to the member database
     * @param split input string
     * @param members member database
     */
    private void addFamilyMember(String[] split, MemberDatabase members)
    {
        if (checkAdd(split)) {
            if (members.add(new Family(split[1], split[2], new Date(split[3]), new Date(getNewExpDate().toString()), getLocation(split[4]))))
                System.out.println(split[1] + " " + split[2] + " added");
            else
                System.out.println(split[1] + " " + split[2] + " is already in the database");
        }
    }

    /**
     * adds a Premium member to the member database
     * @param split input string
     * @param members member database
     */
    private void addPremiumMember(String[] split, MemberDatabase members)
    {
        Date today = new Date();
        int newYear = today.getYear() + 1;
        Date exp = new Date(today.getMonth() + "/" + today.getDay() + "/" + newYear);
        if (checkAdd(split)) {
            if (members.add(new Premium(split[1], split[2], new Date(split[3]), exp, getLocation(split[4]))))
                System.out.println(split[1] + " " + split[2] + " added");
            else
                System.out.println(split[1] + " " + split[2] + " is already in the database");
        }
    }

    /**
     * removes a member from the member database
     * @param split input string
     * @param members member database
     */
    private void removeMember(String[] split, MemberDatabase members)
    {
        if(members.remove(new Member(split[1], split[2], new Date(split[3].toString()), new Date(), Location.Edison)))
            System.out.println(split[1] + " " + split[2] + " removed.");
        else
            System.out.println(split[1] + " " + split[2] + " is not in the database.");
    }

    /**
     * Returns location if input string matches a location enum.
     * @param s get the location String
     * @return loc return the location stored in the variable
     */
    public Location getLocation(String s)
    {
        Location[] l = Location.values();
        Location loc = null;
        if (l[0].match(s) == Location.Bridgewater)
            loc = Location.Bridgewater;
        else  if (l[1].match(s) == Location.Edison)
            loc = Location.Edison;
        else if (l[2].match(s) == Location.Franklin)
            loc = Location.Franklin;
        else if (l[3].match(s) == Location.Somerville)
            loc = Location.Somerville;
        else if (l[4].match(s) == Location.Piscataway)
            loc = Location.Piscataway;
        return loc;
    }

    /**
     * Returns time if input string matches a time enum.
     * @return loc return the location based on the time
     * @param s input time String
     */
    private Time getTime(String s)
    {
        Time[] t = Time.values();
        Time time = null;
        if (t[0].match(s) == Time.Morning)
            time = Time.Morning;
        else  if (t[1].match(s) == Time.Afternoon)
            time = Time.Afternoon;
        else if (t[2].match(s) == Time.Evening)
            time = Time.Evening;
        return time;
    }

    /**
     * Checks if member is valid based on DOB and Location
     * @param input input String
     * @return true or false if input is valid
     */
    private boolean checkAdd(String[] input)
    {
        Date today = new Date();
        Date memberDob = new Date(input[3]);
        if (!memberDob.isValid()) {
            System.out.println("DOB " + input[3] + " Invalid calendar date!");
            return false;
        }
        if (memberDob.compareTo(today) >= 0){
            System.out.println("DOB " + input[3] + " cannot be today or a future date!");
            return false;
        }
        if (!memberDob.isAdult()){
            System.out.println("DOB " + input[3] + " must be 18 or older to join!");
            return false;
        }

        if(input[4].toUpperCase().compareTo(Location.Bridgewater.getCity().toUpperCase()) != 0
                && input[4].toUpperCase().compareTo(Location.Edison.getCity().toUpperCase()) != 0
                && input[4].toUpperCase().compareTo(Location.Franklin.getCity().toUpperCase()) != 0
                && input[4].toUpperCase().compareTo(Location.Piscataway.getCity().toUpperCase()) != 0
                && input[4].toUpperCase().compareTo(Location.Somerville.getCity().toUpperCase()) != 0){
            System.out.println(input[4] + ": invalid location!");
            return false;
        }
        return true;
    }

    /**
     * gets the number of days in a specific month
     * @param month month
     * @return number of days
     */
    public int getDaysInMonth(int month)
    {
        switch (month)
        {
            case Date.FEBRUARY:
            {
                return Date.DAYS_IN_FEB;
            }
            case Date.APRIL:
            case Date.JUNE:
            case Date.SEPTEMBER:
            case Date.NOVEMBER:
                return Date.DAYS_IN_NONKNUCKLE_MONTH;
            default: return Date.MAX_DAYS_IN_MONTH;
        }
    }

    /**
     * creates a new expiration date 3 months ahead of today's date
     * @return expiration date
     */
    public Date getNewExpDate()
    {
        Date today = new Date();
        int newMonth = today.getMonth() + 3;
        int newDay = today.getDay();
        int newYear = today.getYear();
        if (newMonth > 12) {
            newMonth -= 12;
            newYear += 1;
        }
        int daysInMonth = getDaysInMonth(newMonth);
        if(newDay > daysInMonth)
        {
            newDay -= daysInMonth;
            newMonth += 1;
        }
        return new Date(newMonth + "/" + newDay + "/" + newYear);
    }
}