package com.project3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import project2.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class GymManagerController {

    ObservableList<String> locationList = FXCollections.observableArrayList("Bridgewater", "Edison", "Franklin", "Piscataway", "Somerville");
    @FXML
    private TextArea output;
    private StringBuilder outputString = new StringBuilder("");
    private MemberDatabase members;
    private ClassSchedule classSchedule;


    @FXML
    private TextField membershipFirstNameTextField;
    @FXML
    private TextField membershipLastNameTextField;
    @FXML
    private TextField memberhsipDateOfBirth;
    @FXML
    private ComboBox membershipLocationBox;
    @FXML
    private RadioButton rButtonStandardMembership, rButtonFamilyMembership, rButtonPremiumMembership;

    @FXML
    private TextField fitnessFirstNameTextField;
    @FXML
    private TextField fitnessLastNameTextField;
    @FXML
    private TextField fitnessDateOfBirth;
    @FXML
    private ComboBox fitnessActivity;
    @FXML
    private ComboBox fitnessInstructor;
    @FXML
    private ComboBox fitnessLocationBox;

    @FXML
    private void initialize() {
        membershipLocationBox.setItems(locationList);
        fitnessLocationBox.setItems(locationList);
        outputString.append("Gym Manager running...\n");

        members = new MemberDatabase();
        classSchedule = new ClassSchedule();

        output.setText(outputString.toString());
    }

    public void dropMember() {
        if (areFieldsEmptyCheckingIn()) {
            String dob = fitnessDateOfBirth.getText();
            if (isValidDate(dob)) {
                String firstName = fitnessFirstNameTextField.getText();
                String lastName = fitnessLastNameTextField.getText();
                Location location = Location.match(fitnessLocationBox.getValue().toString());
                if (members.exists(new Member(firstName, lastName, new Date(dob), new Date(), Location.Edison)) == null) {
                    outputString.append(firstName + " " + lastName + " " + dob + " is not in the database\n");
                    output.setText(outputString.toString());
                }
                else {
                    String activity = fitnessActivity.getValue().toString();
                    String instructor = fitnessInstructor.getValue().toString();
                    FitnessClass fitnessClass = new FitnessClass(activity, instructor, null, location);
                    if (classSchedule.exists(fitnessClass) == null) {
                        outputString.append(fitnessClass.getActivity() + " by " + fitnessClass.getInstructor() + " does not exist at " + fitnessClass.getLocation().getCity() + "\n");
                        output.setText(outputString.toString());
                        return;
                    }
                    Member test = members.exists(new Member(firstName, lastName, new Date(dob), new Date(), Location.Edison));
                    fitnessClass.add(test);
                    outputString.append(classSchedule.drop(fitnessClass));
                }
            }
        }
        output.setText(outputString.toString());
    }

    public void dropGuest() {
        if (areFieldsEmptyCheckingIn()) {
            String dob = fitnessDateOfBirth.getText();
            if (isValidDate(dob)) {
                String firstName = fitnessFirstNameTextField.getText();
                String lastName = fitnessLastNameTextField.getText();
                Location location = Location.match(fitnessLocationBox.getValue().toString());
                if (members.exists(new Member(firstName, lastName, new Date(dob), new Date(), Location.Edison)) == null) {
                    outputString.append(firstName + " " + lastName + " " + dob + " is not in the database\n");
                    output.setText(outputString.toString());
                }
                else {
                    String activity = fitnessActivity.getValue().toString();
                    String instructor = fitnessInstructor.getValue().toString();
                    FitnessClass fitnessClass = new FitnessClass(activity, instructor, null, location);
                    if (classSchedule.exists(fitnessClass) == null) {
                        outputString.append(fitnessClass.getActivity() + " by " + fitnessClass.getInstructor() + " does not exist at " + fitnessClass.getLocation().getCity() + "\n");
                        output.setText(outputString.toString());
                        return;
                    }
                    Time t = classSchedule.exists(fitnessClass).getTime();
                    fitnessClass = new FitnessClass(activity, instructor, t, location);
                    Member guest = members.exists(new Member(firstName, lastName, new Date(dob), new Date(), Location.Edison));
                    fitnessClass.addGuest(guest);
                    if (classSchedule.exists(fitnessClass).findGuest(guest) < 0) {
                        outputString.append(firstName + " " + lastName + " " + dob + " guest is not checked in.");
                    }
                    else {
                        outputString.append(classSchedule.dropGuest(fitnessClass));
                    }
                }
            }
        }
        output.setText(outputString.toString());
    }

    public void checkInMember() {
        if (areFieldsEmptyCheckingIn()) {
            String dob= fitnessDateOfBirth.getText();
            if (isValidDate(dob)) {
                String firstName = fitnessFirstNameTextField.getText();
                String lastName = fitnessLastNameTextField.getText();
                Location location = Location.match(fitnessLocationBox.getValue().toString());
                Member member = members.exists(new Member(firstName, lastName, new Date(dob), new Date(), Location.Edison));
                if (member == null) {
                    outputString.append(firstName + " " + lastName + " " + dob + " is not in the database\n");
                    output.setText(outputString.toString());
                    return;
                }
                if (!(checkCheckInLocation(firstName, lastName, dob, location, members))) {
                    output.setText(outputString.toString());
                    return;
                }
                String expDate = members.exists(new Member(firstName, lastName, new Date(dob), new Date(), Location.Edison)).getExpire().toString();
                if (isExpired(expDate, firstName, lastName, dob))
                {
                    String activity = fitnessActivity.getValue().toString();
                    String instructor = fitnessInstructor.getValue().toString();
                    FitnessClass fitnessClass = new FitnessClass(activity, instructor, null, location);
                    if (classSchedule.exists(fitnessClass) == null) {
                        outputString.append(fitnessClass.getActivity() + " by " + fitnessClass.getInstructor() + " does not exist at " + fitnessClass.getLocation().getCity() + "\n");
                        output.setText(outputString.toString());
                        return;
                    }
                    Time t = classSchedule.exists(fitnessClass).getTime();
                    fitnessClass = new FitnessClass(activity, instructor, t, location);
                    fitnessClass.add(member);
                    if(checkExists(classSchedule, fitnessClass)) {
                        if (checkCheckInTime(classSchedule, fitnessClass)) {
                            if (classSchedule.checkIn(fitnessClass))
                            {
                                outputString.append(member.getFname() + " " + member.getLname() + " checked in "
                                        + fitnessClass.getActivity().toUpperCase() + " - " + fitnessClass.getInstructor().toUpperCase() + ", " +
                                        fitnessClass.getTime() + ", " + fitnessClass.getLocation().getCity() + "\n");
                                outputString.append(classSchedule.toStringClass(fitnessClass));
                            }
                        }
                    }
                }
            }
        }
        output.setText(outputString.toString());
    }

    public void checkInGuest() {
        if (areFieldsEmptyCheckingIn()) {
            String dob= fitnessDateOfBirth.getText();
            if (isValidDate(dob)) {
                String firstName = fitnessFirstNameTextField.getText();
                String lastName = fitnessLastNameTextField.getText();
                Location location = Location.match(fitnessLocationBox.getValue().toString());
                if (!(checkGuestLocation(firstName, lastName, dob, location, members))) {
                    output.setText(outputString.toString());
                    return;
                }
                if (members.exists(new Member(firstName, lastName, new Date(dob), new Date(), Location.Edison)) == null) {
                    outputString.append(firstName + " " + lastName + " " + dob + " is not in the database\n");
                    output.setText(outputString.toString());
                    return;
                }
                String expDate = members.exists(new Member(firstName, lastName, new Date(dob), new Date(), Location.Edison)).getExpire().toString();
                if (isExpired(expDate, firstName, lastName, dob))
                {
                    String activity = fitnessActivity.getValue().toString();
                    String instructor = fitnessInstructor.getValue().toString();
                    FitnessClass fitnessClass = new FitnessClass(activity, instructor, null, location);
                    if (classSchedule.exists(fitnessClass) == null) {
                        outputString.append(fitnessClass.getActivity() + " by " + fitnessClass.getInstructor() + " does not exist at " + fitnessClass.getLocation().getCity() + "\n");
                        output.setText(outputString.toString());
                        return;
                    }
                    Time t = classSchedule.exists(fitnessClass).getTime();
                    fitnessClass = new FitnessClass(activity, instructor, t, location);
                    Member member = members.exists(new Member(firstName, lastName, new Date(dob), new Date(), Location.Edison));
                    fitnessClass.addGuest(member);
                    if (member instanceof Premium || member instanceof Family) {
                        if (checkGuestPassNum(fitnessClass)) {
                            if (classSchedule.checkInGuest(fitnessClass)) {
                                outputString.append(member.getFname() + " " + member.getLname() + " (guest) checked in "
                                        + fitnessClass.getActivity().toUpperCase() + " - " + fitnessClass.getInstructor().toUpperCase() + ", " +
                                        fitnessClass.getTime() + ", " + fitnessClass.getLocation().getCity() + "\n");
                                outputString.append(classSchedule.toStringClass(fitnessClass));
                            }
                        }
                    } else
                        outputString.append("Standard membership - guest check-in is not allowed.");
                }
            }
        }
        output.setText(outputString.toString());
    }

    /**
     * checks if the guest is checking in at valid location
     * @param members member database
     * @return boolean if guest check in location is valid
     */
    private boolean checkGuestLocation(String f, String l, String dob, Location loc, MemberDatabase members)
    {
        Member member = members.exists(new Member(f, l, new Date(dob), new Date(), Location.Edison));
        if (!(member instanceof Family)) {
            outputString.append("Standard membership - guest check-in is not allowed.\n");
            return false;
        }
        if (loc.toString().compareTo(member.getLocation().toString()) != 0) {
            outputString.append(member.getFname() + " " + member.getLname() + " Guest checking in " + loc.toString().toUpperCase()
                    + " - guest location restriction\n");
            return false;
        }
        return true;
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
                outputString.append(guest.getFname() + " " + guest.getLname() + " ran out of guest passes.\n");
                return false;
            }
        }
        return true;
    }

    /**
     * checks if check in location is at membership location
     * @param m member database
     * @return boolean if check in location is valid
     */
    private boolean checkCheckInLocation(String f, String l, String dob, Location location, MemberDatabase m)
    {
        Member test = m.exists(new Member(f, l, new Date(dob), new Date(), Location.Edison));
        if (test instanceof Premium)
            return true;
        else if (test instanceof Family)
            return true;
        else
        {
            if (location != test.getLocation())
            {
                outputString.append(test.getFname() + " " + test.getLname() + " checking in " + location.toString()
                        + " - standard membership location restriction\n");
                return false;
            }
            return true;
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
            outputString.append(fitnessClass.getSingleMember().getFname() + " " + fitnessClass.getSingleMember().getLname() + " already checked in\n");
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
                    outputString.append("Time conflict - " + fitnessClass.getActivity().toUpperCase() + " - " + fitnessClass.getInstructor().toUpperCase()
                            + ", " + fitnessClass.getTime() + ", " + fitnessClass.getLocation().toString().toUpperCase() + "\n");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if member is valid based on expiration date
     * @param date date String
     * @return true or false if input is valid
     */
    private boolean isExpired(String date, String f, String l, String dob)
    {
        Date today = new Date();
        Date memberExp = new Date(date);
        if (!memberExp.isValid()) {
            outputString.append("Expiration date:  " + date + " Invalid calendar date!\n");
            return false;
        }
        if (memberExp.compareTo(today) < 0){
            outputString.append(f + " " + l + " " + dob + " membership expired\n");
            return false;
        }
        return true;
    }

    public void addMember() {
        if (areFieldsEmptyAddingMember()) {
            String dob = memberhsipDateOfBirth.getText();
            if (isValidDate(dob)) {
                String firstName = membershipFirstNameTextField.getText();
                String lastName = membershipLastNameTextField.getText();
                Location location = Location.match(membershipLocationBox.getValue().toString());
                if (rButtonStandardMembership.isSelected()) {
                    if(members.add(new Member(firstName, lastName, new Date(dob), new Date(getNewExpDate().toString()), location)))
                        outputString.append(firstName + " " + lastName + " added\n");
                    else
                        outputString.append(firstName + " " + lastName + " is already in the database\n");
                }
                else if (rButtonFamilyMembership.isSelected()) {
                    if(members.add(new Family(firstName, lastName, new Date(dob), new Date(getNewExpDate().toString()), location)))
                        outputString.append(firstName + " " + lastName + " added\n");
                    else
                        outputString.append(firstName + " " + lastName + " is already in the database\n");
                }
                else if (rButtonPremiumMembership.isSelected()) {
                    Date today = new Date();
                    int newYear = today.getYear() + 1;
                    Date exp = new Date(today.getMonth() + "/" + today.getDay() + "/" + newYear);
                    if(members.add(new Premium(firstName, lastName, new Date(dob), exp, location)))
                        outputString.append(firstName + " " + lastName + " added\n");
                    else
                        outputString.append(firstName + " " + lastName + " is already in the database\n");
                }
                else
                    outputString.append("Please select a Membership Type\n");
            }
        }
        output.setText(outputString.toString());
    }

    public void removeMember() {
        if(areFieldsEmptyRemovingMember()) {
            String dob= memberhsipDateOfBirth.getText();
            if (isValidDate(dob)) {
                String firstName = membershipFirstNameTextField.getText();
                String lastName = membershipLastNameTextField.getText();
                if (members.remove(new Member(firstName, lastName, new Date(dob), new Date(), Location.Edison)))
                {
                    outputString.append(firstName + " " + lastName + " removed\n");
                }
                else
                    outputString.append(firstName + " " + lastName + " is not in the database\n");
            }
        }
        output.setText(outputString.toString());
    }

    public void print()
    {
        outputString.append(members.toString());
        output.setText(outputString.toString());
    }

    public void printMembershipFees()
    {
        outputString.append(members.toStringMembershipFees());
        output.setText(outputString.toString());
    }

    public void printByCounty()
    {
        outputString.append(members.toStringByCounty());
        output.setText(outputString.toString());
    }

    public void printByExpirationDate()
    {
        outputString.append(members.toStringByExpDate());
        output.setText(outputString.toString());
    }

    public void printByName()
    {
        outputString.append(members.toStringByName());
        output.setText(outputString.toString());
    }

    public void loadMemberDatabase()
    {
        members.loadMemberList();
        outputString.append("-list of members loaded-\n");
        outputString.append(members.toString());
        output.setText(outputString.toString());
    }

    public void loadFitnessDatabase()
    {
        outputString.append(classSchedule.loadFitnessClasses());
        ObservableList<String> activityList = FXCollections.observableArrayList();
        ObservableList<String> instructorList = FXCollections.observableArrayList();
        FitnessClass[] temp = classSchedule.getFitnessClasses();
        for (int i = 0; i < classSchedule.getNumClasses(); i++)
        {
           if (!activityList.contains(temp[i].getActivity())) {
                activityList.add(temp[i].getActivity());
            }
            if (!instructorList.contains(temp[i].getInstructor())) {
                instructorList.add(temp[i].getInstructor());
            }
        }
        fitnessActivity.setItems(activityList);
        fitnessInstructor.setItems(instructorList);
        output.setText(outputString.toString());
    }

    public void printFitnessClassSchedule()
    {
        outputString.append(classSchedule.toString());
        output.setText(outputString.toString());
    }

    /**
     * checks if input fields are empty when adding a member
     * @return boolean if fields are empty
     */
    private boolean areFieldsEmptyCheckingIn() {
        boolean result = true;
        if (fitnessFirstNameTextField.getText().isEmpty()) {
            outputString.append("First Name field empty\n");
            fitnessFirstNameTextField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.3))));
            result = false;
        } else
            fitnessFirstNameTextField.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0))));
        if (fitnessLastNameTextField.getText().isEmpty()) {
            outputString.append("Last Name field empty\n");
            fitnessLastNameTextField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.3))));
            result = false;
        } else
            fitnessLastNameTextField.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0))));
        if (fitnessDateOfBirth.getText().isEmpty()) {
            outputString.append("Date of Birth field empty\n");
            fitnessDateOfBirth.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.3))));
            result = false;
        } else
            fitnessDateOfBirth.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0))));
        if (fitnessActivity.getValue() == null) {
            outputString.append("Activity field empty\n");
            fitnessActivity.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.3))));
            result = false;
        } else
            fitnessActivity.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0))));
        if (fitnessInstructor.getValue() == null) {
            outputString.append("Instructor field empty\n");
            fitnessInstructor.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.3))));
            result = false;
        } else
            fitnessInstructor.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0))));
        if (fitnessLocationBox.getValue() == null) {
            outputString.append("Location field empty\n");
            fitnessLocationBox.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.3))));
            result = false;
        } else
            fitnessLocationBox.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0))));
        return result;
    }

    /**
     * checks if input fields are empty when removing a member
     * @return boolean if fields are empty
     */
    private boolean areFieldsEmptyRemovingMember() {
        boolean result = true;
        if (membershipFirstNameTextField.getText().isEmpty()) {
            outputString.append("First Name field empty\n");
            membershipFirstNameTextField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.3))));
            result = false;
        } else
            membershipFirstNameTextField.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0))));
        if (membershipLastNameTextField.getText().isEmpty()) {
            outputString.append("Last Name field empty\n");
            membershipLastNameTextField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.3))));
            result = false;
        } else
            membershipLastNameTextField.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0))));
        if (memberhsipDateOfBirth.getText().isEmpty()) {
            outputString.append("Date of Birth field empty\n");
            memberhsipDateOfBirth.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.3))));
            result = false;
        }
        else
            memberhsipDateOfBirth.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0))));
        return result;
    }

    /**
     * checks if input fields are empty when adding a member
     * @return boolean if fields are empty
     */
    private boolean areFieldsEmptyAddingMember() {
        boolean result = true;
        if (membershipFirstNameTextField.getText().isEmpty()) {
            outputString.append("First Name field empty\n");
            membershipFirstNameTextField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.3))));
            result = false;
        } else
            membershipFirstNameTextField.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0))));
        if (membershipLastNameTextField.getText().isEmpty()) {
            outputString.append("Last Name field empty\n");
            membershipLastNameTextField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.3))));
            result = false;
        } else
            membershipLastNameTextField.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0))));
        if (memberhsipDateOfBirth.getText().isEmpty()) {
            outputString.append("Date of Birth field empty\n");
            memberhsipDateOfBirth.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.3))));
            result = false;
        } else
            memberhsipDateOfBirth.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0))));
        if (membershipLocationBox.getValue() == null) {
            outputString.append("Location field empty\n");
            membershipLocationBox.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.3))));
            result = false;
        } else
            membershipLocationBox.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0))));
        return result;
    }

    /**
     * Checks if member is valid based on DOB
     * @param date date String
     * @return true or false if input is valid
     */
    private boolean isValidDate(String date)
    {
        Date today = new Date();
        Date memberDob = new Date(date);
        if (!memberDob.isValid()) {
            outputString.append("DOB " + date + " Invalid calendar date!\n");
            return false;
        }
        if (memberDob.compareTo(today) >= 0){
            outputString.append("DOB " + date + " cannot be today or a future date!\n");
            return false;
        }
        if (!memberDob.isAdult()){
            outputString.append("DOB " + date + " must be 18 or older to join!\n");
            return false;
        }
        return true;
    }

    /**
     * creates a new expiration date 3 months ahead of today's date
     * @return expiration date
     */
    private Date getNewExpDate()
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

    /**
     * gets the number of days in a specific month
     * @param month month
     * @return number of days
     */
    private int getDaysInMonth(int month)
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
}