package project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * MemberDatabase is used to store Members that have memberships at a gym
 *  Features include adding, removing members, and printing members
 *  @author James Liu, Ramazan Azimov
 */

public class MemberDatabase {
    private Member [] mlist;
    private int size;
    private final int NOT_FOUND = -1;
    private final int GROW = 4;

    /**
     * initializes member list to size 0
     */
    public MemberDatabase() {
        this.mlist = new Member[0];
    }

    /**
     * finds the index of a member
     * @return index of member in database
     * @param member member object
     */
    private int find(Member member) {
        for(int i = 0; i < size; i++){
            if(member.equals(mlist[i].getFname(), mlist[i].getLname(), mlist[i].getDob().toString())){
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * returns member from the database if member exists based on first name, last name, and date of birth
     * @return member in database
     * @param member member object
     */
    public Member exists(Member member)
    {
        for(int i = 0; i < size; i++){
            if(mlist[i].equals(member.getFname(), member.getLname(), member.getDob().toString())){
                return mlist[i];
            }
        }
        return null;
    }

    /**
     * increases the member list by 4
     */
    private void grow() {
        Member[] newmlist = new Member[mlist.length + GROW];
        for(int i = 0; i < size; i++){
            newmlist[i] = mlist[i];
        }
        mlist = newmlist;
    }

    /**
     * adds a member to the member database
     * @return boolean if the member has been added
     * @param member member object
     */
    public boolean add(Member member) {
        if (size >= mlist.length) {
            grow();
        }

        for (int i = 0; i < size; i++)
        {
            if (mlist[i].compareTo(member) == 0 && mlist[i].compareToDob(member) == 0)
                return false;
        }

        if(member instanceof Premium) {
            Premium p = (Premium) member;
            mlist[size++] = new Premium(p);
        }
        else if (member instanceof Family) {
            Family f = (Family) member;
            mlist[size++] = new Family(f);
        }
        else {
            mlist[size++] = new Member(member);
        }
        return true;
    }

    /**
     * removes a member from the member database
     * @return boolean if the member has been removed
     * @param member member object
     */
    public boolean remove(Member member) {
        int index = find(member);
        if (index == NOT_FOUND)
            return false;
        if (size == 1)
        {
            mlist[index] = null;
        }
        else {
            Member temp = new Member(mlist[index]);
            for (int i = index; i < size-1; i++) {
                mlist[i] = new Member(mlist[i + 1]);
            }
            mlist[size - 1] = new Member(temp);
        }
        size--;
        return true;
    }

    /**
     * loads members from a file into member database
     */
    public void loadMemberList()
    {
        try {
            File myFile = new File("memberList.txt");
            Scanner sc = new Scanner(myFile);
            while (sc.hasNextLine()) {
                String inputLine = sc.nextLine();
                String[] split = inputLine.split(" ");
                if (split.length < 4)
                    break;
                Member temp = new Member(split[0], split[1], new Date(split[2]), new Date(split[3]), Location.match(split[4]));
                this.add(temp);
            }
            sc.close();
        } catch (FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * prints the member database
     */
    public void print () {
        if (size == 0)
            System.out.println("Member datable is empty");
        else {
            System.out.println("~list of members~");
            for (int i = 0; i < size; i++)
            {
                System.out.println(mlist[i].toString());
            }
            System.out.println("~end of list~\n\n");
        }
    }

    /**
     * String version of member database
     */
    public String toString()
    {
        StringBuilder result = new StringBuilder("");
        if (size == 0) {
            result.append("Member datable is empty\n");
        }
        else {
            result.append("~list of members~\n");
            for (int i = 0; i < size; i++)
            {
                result.append("\t" + mlist[i].toString() + "\n");

            }
            result.append("~end of list~\n\n");
        }
        return result.toString();
    }

    /**
     * prints the member database with membership fees
     */
    public void printMembershipFees()
    {
        if (size == 0)
            System.out.println("Member datable is empty");
        else {
            System.out.println("~list of members with membership fees~");
            for (int i = 0; i < size; i++)
            {
                System.out.println(mlist[i].toStringMembership());
            }
            System.out.println("~end of list~\n\n");
        }
    }

    /**
     * String version of member database with membership fees
     */
    public String toStringMembershipFees()
    {
        StringBuilder result = new StringBuilder("");
        if (size == 0) {
            result.append("Member datable is empty\n");
        }
        else {
            result.append("~list of members with membership fees~\n");
            for (int i = 0; i < size; i++)
            {
                result.append("\t" + mlist[i].toStringMembership() + "\n");
            }
            result.append("~end of list~\n\n");
        }
        return result.toString();
    }

    /**
     * prints the member database sorted by county and then zipcode
     */
    public void printByCounty() {
        if (size == 0)
            System.out.println("Member datable is empty");
        else {
            System.out.println("~list of members sorted by county and zipcode~");
            Member[] temp = this.copyArray();
            for (int i = 0; i < size - 1; i++) {
                int min_idx = i;
                for (int j = i + 1; j < size; j++) {
                    if (temp[j].compareToLoc(temp[min_idx]) < 0) {
                        min_idx = j;
                    }
                }
                if(temp[min_idx] instanceof Premium)
                {
                    Premium tempEle = (Premium)temp[min_idx];
                    temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Premium(tempEle);
                }
                else if(temp[min_idx] instanceof Family)
                {
                    Family tempEle = (Family)temp[min_idx];
                    temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Family(tempEle);
                }
                else {
                    Member tempEle = temp[min_idx];
                    temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Member(tempEle);
                }
            }
            for (int i = 0; i < size; i++) {
                System.out.println(temp[i].toString());
            }
            System.out.println("~end of list~\n\n");
        }
    }

    /**
     * String version of member database sorted by county
     */
    public String toStringByCounty()
    {
        StringBuilder result = new StringBuilder("");
        if (size == 0) {
            result.append("Member datable is empty\n");
        }
        else {
            result.append("~list of members sorted by county and zipcode~\n");
            Member[] temp = this.copyArray();
            for (int i = 0; i < size - 1; i++) {
                int min_idx = i;
                for (int j = i + 1; j < size; j++) {
                    if (temp[j].compareToLoc(temp[min_idx]) < 0) {
                        min_idx = j;
                    }
                }
                if(temp[min_idx] instanceof Premium)
                {
                    Premium tempEle = (Premium)temp[min_idx];
                    temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Premium(tempEle);
                }
                else if(temp[min_idx] instanceof Family)
                {
                    Family tempEle = (Family)temp[min_idx];
                    temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Family(tempEle);
                }
                else {
                    Member tempEle = temp[min_idx];
                    temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Member(tempEle);
                }
            }
            for (int i = 0; i < size; i++) {
                result.append("\t" + temp[i].toString() + "\n");
            }
            result.append("~end of list~\n\n");
        }
        return result.toString();
    }

    /**
     * prints the member database sorted by the expiration date
     */
    public void printByExpirationDate() {
        if (size == 0)
            System.out.println("Member datable is empty");
        else {
            System.out.println("~list of members sorted by membership expiration date~");
            Member[] temp = this.copyArray();
            int n = size;
            for (int i = 0; i < n - 1; i++) {
                int min_idx = i;
                for (int j = i + 1; j < n; j++)
                    if (temp[j].getExpire().compareTo(temp[min_idx].getExpire()) < 0)
                        min_idx = j;
                if(temp[min_idx] instanceof Premium)
                {
                    Premium tempEle = (Premium)temp[min_idx];
                    temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Premium(tempEle);
                }
                else if(temp[min_idx] instanceof Family)
                {
                    Family tempEle = (Family)temp[min_idx];
                    temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Family(tempEle);
                }
                else {
                    Member tempEle = temp[min_idx];
                    temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Member(tempEle);
                }
            }
            for (int i = 0; i < size; i++) {
                System.out.println(temp[i].toString());
            }
            System.out.println("~end of list~\n\n");
        }
    }

    /**
     * String version of member database sorted by expiration date
     */
    public String toStringByExpDate()
    {
        StringBuilder result = new StringBuilder("");
        if (size == 0) {
            result.append("Member datable is empty\n");
        }
        else {
            result.append("~list of members sorted by membership expiration date~\n");
            Member[] temp = this.copyArray();
            int n = size;
            for (int i = 0; i < n - 1; i++) {
                int min_idx = i;
                for (int j = i + 1; j < n; j++)
                    if (temp[j].getExpire().compareTo(temp[min_idx].getExpire()) < 0)
                        min_idx = j;
                if(temp[min_idx] instanceof Premium)
                {
                    Premium tempEle = (Premium)temp[min_idx];
                    temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Premium(tempEle);
                }
                else if(temp[min_idx] instanceof Family)
                {
                    Family tempEle = (Family)temp[min_idx];
                    temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Family(tempEle);
                }
                else {
                    Member tempEle = temp[min_idx];
                    temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Member(tempEle);
                }
            }
            for (int i = 0; i < size; i++) {
                result.append("\t" + temp[i].toString() + "\n");
            }
            result.append("~end of list~\n\n");
        }
        return result.toString();
    }

    /**
     * prints the member database sorted by last name and then first name
     */
    public void printByName() {
        if (size == 0)
            System.out.println("Member datable is empty");
        else {
            System.out.println("~list of members sorted by last name, and first name~");
            Member[] temp = this.copyArray();
            int n = size;
            for (int i = 0; i < n - 1; i++) {
                int min_idx = i;
                for (int j = i + 1; j < n; j++)
                    if (temp[j].compareTo(temp[min_idx]) < 0)
                        min_idx = j;
                if(temp[min_idx] instanceof Premium) {
                    Premium tempEle = (Premium)temp[min_idx];
                    if (temp[i] instanceof Premium) {temp[min_idx] = new Premium((Premium) temp[i]);}
                    else if (temp[i] instanceof Family) {temp[min_idx] = new Family((Family) temp[i]);}
                    else temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Premium(tempEle);
                }
                else if(temp[min_idx] instanceof Family) {
                    Family tempEle = (Family)temp[min_idx];
                    if (temp[i] instanceof Premium) {temp[min_idx] = new Premium((Premium) temp[i]);}
                    else if (temp[i] instanceof Family) {temp[min_idx] = new Family((Family) temp[i]);}
                    else temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Family(tempEle);
                }
                else {
                    Member tempEle = temp[min_idx];
                    if (temp[i] instanceof Premium) {temp[min_idx] = new Premium((Premium) temp[i]);}
                    else if (temp[i] instanceof Family) {temp[min_idx] = new Family((Family) temp[i]);}
                    else {temp[min_idx] = new Member(temp[i]);}
                    temp[i] = new Member(tempEle);
                }
            }
            for (int i = 0; i < size; i++) {
                System.out.println(temp[i].toString());
            }
            System.out.println("~end of list~\n\n");
        }
    }

    /**
     * String version of member database sorted by last name then first name
     */
    public String toStringByName()
    {
        StringBuilder result = new StringBuilder("");
        if (size == 0) {
            result.append("Member datable is empty\n");
        }
        else {
            result.append("~list of members sorted by last name, and first name~\n");
            Member[] temp = this.copyArray();
            int n = size;
            for (int i = 0; i < n - 1; i++) {
                int min_idx = i;
                for (int j = i + 1; j < n; j++)
                    if (temp[j].compareTo(temp[min_idx]) < 0)
                        min_idx = j;
                if(temp[min_idx] instanceof Premium) {
                    Premium tempEle = (Premium)temp[min_idx];
                    if (temp[i] instanceof Premium) {temp[min_idx] = new Premium((Premium) temp[i]);}
                    else if (temp[i] instanceof Family) {temp[min_idx] = new Family((Family) temp[i]);}
                    else temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Premium(tempEle);
                }
                else if(temp[min_idx] instanceof Family) {
                    Family tempEle = (Family)temp[min_idx];
                    if (temp[i] instanceof Premium) {temp[min_idx] = new Premium((Premium) temp[i]);}
                    else if (temp[i] instanceof Family) {temp[min_idx] = new Family((Family) temp[i]);}
                    else temp[min_idx] = new Member(temp[i]);
                    temp[i] = new Family(tempEle);
                }
                else {
                    Member tempEle = temp[min_idx];
                    if (temp[i] instanceof Premium) {temp[min_idx] = new Premium((Premium) temp[i]);}
                    else if (temp[i] instanceof Family) {temp[min_idx] = new Family((Family) temp[i]);}
                    else {temp[min_idx] = new Member(temp[i]);}
                    temp[i] = new Member(tempEle);
                }
            }
            for (int i = 0; i < size; i++) {
                result.append("\t" + temp[i].toString() + "\n");
            }
            result.append("~end of list~\n\n");
        }
        return result.toString();
    }

    /**
     * copies current member database into a new list
     * @return copied list of members
     */
    private Member[] copyArray()
    {
        Member[] copy = new Member[mlist.length];
        for (int i = 0; i < size; i++) {
            if(mlist[i] instanceof Premium) {
                Premium p = (Premium) mlist[i];
                copy[i] = new Premium(p);
            }
            else if (mlist[i] instanceof Family) {
                Family f = (Family) mlist[i];
                copy[i] = new Family(f);
            }
            else {
                copy[i] = new Member(mlist[i]);
            }
        }
        return copy;
    }
}