package project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * ClassSchedule is used to store fitness classes that are provided by the gym
 *  @author Ramazan Azimov, James Liu
 */
public class ClassSchedule {
    private FitnessClass[] classes;
    private int numClasses;
    private final int NOT_FOUND = -1;
    private final int GROW = 4;

    /**
     * creates a classSchedule object with 0 classes
     */
    public ClassSchedule() {
        this.classes = new FitnessClass[0];
    }

    /**
     * loads fitness classes from a file into class schedule
     */
    public String loadFitnessClasses(){
        StringBuilder result = new StringBuilder("");
        try {
            File myFile = new File("classSchedule.txt");
            Scanner sc = new Scanner(myFile);
            while (sc.hasNextLine()) {
                String inputLine = sc.nextLine();
                String[] split = inputLine.split(" ");
                if (split.length < 4)
                    break;
                FitnessClass temp = new FitnessClass(split[0], split[1], getTime(split[2]), Location.match(split[3]));
                this.addClass(temp);
            }
            sc.close();
        } catch (FileNotFoundException e){
            result.append("An error occurred importing Fitness Class Schedule.\n");
            e.printStackTrace();
        }
        result.append("-Fitness classes loaded-\n");
        for (int i = 0; i < numClasses; i++) {
            result.append(classes[i].toString() + "\n");
        }
        result.append("-end of class list-\n\n");
        return result.toString();
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
     * finds index of fitness class
     * @param fitnessclass fitness class
     * @return index of fitness class
     */
    public int find(FitnessClass fitnessclass)
    {
        for(int i = 0; i < numClasses; i++){
            if(fitnessclass.equals(classes[i])){
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * finds if fitness class is provided by the gym
     * @param fitnessClass fitness class
     * @return fitness class object
     */
    public FitnessClass exists(FitnessClass fitnessClass)
    {
        for(int i = 0; i < numClasses; i++){
            if(fitnessClass.equals(classes[i])){
                return classes[i];
            }
        }
        return null;
    }

    /**
     * adds a fitness class to the fitness class list
     * @param fitnessClass fitness class
     */
    public void addClass(FitnessClass fitnessClass)
    {
        if (numClasses >= classes.length) {
            grow();
        }
        if (this.find(fitnessClass) == NOT_FOUND) {
            classes[numClasses++] = new FitnessClass(fitnessClass.getActivity(), fitnessClass.getInstructor(), fitnessClass.getTime(), fitnessClass.getLocation());
        }
    }

    /**
     * increases the fitness class list by 4
     */
    private void grow() {
        FitnessClass[] newclassList = new FitnessClass[classes.length + GROW];
        for(int i = 0; i < numClasses; i++){
            newclassList[i] = classes[i];
        }
        classes = newclassList;
    }

    /**
     * prints all the participants and guests in all the classes
     */
    public void print()
    {
        if (classes.length == 0)
        {
            System.out.println("Fitness class schedule is empty.");
            return;
        }
        System.out.println("-Fitness classes-");
        for (int i = 0; i < numClasses; i++)
        {
            int memberSize = classes[i].getSize();
            System.out.println(classes[i].toString());
            if (memberSize > 0)
            {
                System.out.println("** participants **");
                for (int j = 0; j < memberSize; j++)
                {
                    System.out.println("\t" + classes[i].getMlist().get(j).toString());
                }
            }
            if (classes[i].getGlist().size() > 0)
            {
                System.out.println("** guests **");
                for (int j = 0; j < classes[i].getGlist().size(); j++)
                {
                    System.out.println("\t" + classes[i].getGlist().get(j).toString());
                }
            }
        }
        System.out.println("~end of list~\n\n");
    }

    /**
     * String version of member database
     */
    public String toString()
    {
        StringBuilder result = new StringBuilder("");
        if (classes.length == 0) {
            result.append("Fitness class schedule is empty.\n");
            return result.toString();
        }
        else
            result.append("-Fitness classes-\n");
        for (int i = 0; i < numClasses; i++)
        {
            int memberSize = classes[i].getSize();
            result.append(classes[i].toString() + "\n");
            if (memberSize > 0)
            {
                result.append("** participants **\n");
                for (int j = 0; j < memberSize; j++)
                {
                    result.append("\t" + classes[i].getMlist().get(j).toString() + "\n");
                }
            }
            if (classes[i].getGlist().size() > 0)
            {
                result.append("** guests **\n");
                for (int j = 0; j < classes[i].getGlist().size(); j++)
                {
                    result.append("\t" + classes[i].getGlist().get(j).toString() + "\n");
                }
            }
        }
        result.append("-end of class list-\n\n");
        return result.toString();
    }

    /**
     * prints all the participants and guests in a specific fitness class
     * @param fitnessClass fitness class
     */
    public void printClass(FitnessClass fitnessClass) {
        int classIndex = this.find(fitnessClass);
        int memberSize = classes[classIndex].getSize();
        int guestSize = classes[classIndex].getGlist().size();
        if (memberSize > 0)
        {
            System.out.println("** participants **");
            for (int j = 0; j < memberSize; j++)
            {
                System.out.println("\t" + classes[classIndex].getMlist().get(j).toString());
            }
        }
        if (guestSize > 0)
        {
            System.out.println("** guests **");
            for (int j = 0; j < guestSize; j++)
            {
                System.out.println("\t" + classes[classIndex].getGlist().get(j).toString());
            }
        }
        System.out.println();
    }

    /**
     * String version of all participants and guests in a specific class
     */
    public String toStringClass(FitnessClass fitnessClass)
    {
        StringBuilder result = new StringBuilder("");
        int classIndex = this.find(fitnessClass);
        int memberSize = classes[classIndex].getSize();
        int guestSize = classes[classIndex].getGlist().size();
        if (memberSize > 0)
        {
            result.append("** participants **\n");
            for (int j = 0; j < memberSize; j++)
            {
                result.append("\t" + classes[classIndex].getMlist().get(j).toString() + "\n");
            }
        }
        if (guestSize > 0)
        {
            result.append("** guests **\n");
            for (int j = 0; j < guestSize; j++)
            {
                result.append("\t" + classes[classIndex].getGlist().get(j).toString() + "\n");
            }
        }
        return result.toString();
    }

    /**
     * checks in participant into a specific fitness class
     * @param fitnessClass fitness class
     * @return boolean if participant checked in
     */
    public boolean checkIn(FitnessClass fitnessClass) {
        int classIndex = this.find(fitnessClass);
        if (classIndex >= 0)
        {
            return classes[classIndex].add(fitnessClass.getSingleMember());
        }
        return false;
    }

    /**
     * checks in guest into a specific fitness class
     * @param fitnessClass fitness class
     * @return boolean if guest checked in
     */
    public boolean checkInGuest(FitnessClass fitnessClass)
    {
        int classIndex = this.find(fitnessClass);
        if (classIndex >= 0)
        {
            Family f = (Family) fitnessClass.getSingleGuest();
            f.useGuestPass();
            return classes[classIndex].addGuest(f);
        }
        return false;
    }

    /**
     * drops a guest from a specific fitness class
     * @param fitnessClass fitness class
     * @return String if guest checked out
     */
    public String dropGuest(FitnessClass fitnessClass)
    {
        int classIndex = this.find(fitnessClass);
        if (classIndex >= 0)
        {
            if (classes[classIndex].removeGuest(fitnessClass.getSingleGuest())) {
                Family f = (Family) fitnessClass.getSingleGuest();
                f.addGuestPass();
                return fitnessClass.getSingleGuest().getFname() + " " + fitnessClass.getSingleGuest().getLname() + " Guest done with the class.\n";
            }
            return fitnessClass.getSingleGuest().getFname() + " " + fitnessClass.getSingleGuest().getLname() + " guest did not check in.\n";
        }
        return null;
    }

    /**
     * drops a member from a specific fitness class
     * @param fitnessClass fitness class
     * @return String if member checked out
     */
    public String drop(FitnessClass fitnessClass) {
        int classIndex = this.find(fitnessClass);
        if (classIndex >= 0)
        {
            if (classes[classIndex].remove(fitnessClass.getSingleMember()))
                return fitnessClass.getSingleMember().getFname() + " " + fitnessClass.getSingleMember().getLname() + " done with the class.";
            return fitnessClass.getSingleMember().getFname() + " " + fitnessClass.getSingleMember().getLname() + " did not check in.";
        }
        return null;
    }

    /**
     * gets the total number of fitness classes in list
     * @return number of classes
     */
    public int getNumClasses()
    {
        return numClasses;
    }

    /**
     * gets the list of fitness classes
     * @return fitness class list
     */
    public FitnessClass[] getFitnessClasses()
    {
        return classes;
    }
}
