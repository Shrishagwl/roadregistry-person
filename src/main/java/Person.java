import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Person {
    private String personID;
    private String firstName;
    private String lastName;
    private String address;
    private LocalDate birthdate;
    private Map<LocalDate, Integer> demeritPoints = new HashMap<>();
    private boolean isSuspended;


    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Person() {}

    public Person(String personID, String firstName, String lastName, String address, LocalDate birthdate, boolean isSuspended) {
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.birthdate = birthdate;
        this.isSuspended = isSuspended;
    }

    public boolean addPerson() {
        if (personID == null || personID.length() != 10) {
            System.out.println("Person ID must be exactly 10 characters long.");
            return false;
        }

        if (!Character.isDigit(personID.charAt(0)) || !Character.isDigit(personID.charAt(1))) {
            System.out.println("First two characters of Person ID must be digits between 2 and 9.");
            return false;
        }

        if (personID.charAt(0) < '2' || personID.charAt(0) > '9' ||
            personID.charAt(1) < '2' || personID.charAt(1) > '9') {
            System.out.println("First two characters of Person ID must be digits between 2 and 9.");
            return false;
        }

        int count = 0;

        for (int i = 2; i <= 9; ++i) {
            if (Character.isDigit(personID.charAt(i)) == false && Character.isLetter(personID.charAt(i)) == false) {
                count += 1;
            }
        }

        if (count < 2) {
            System.out.println("There should be at least two special characters");
            return false;
        }

        if (!Character.isUpperCase(personID.charAt(8)) || !Character.isUpperCase(personID.charAt(9))) {
            System.out.println("Last two characters of Person ID must be upper case letters.");
            return false;
        }

        String[] addressInParts = address.split("\\|");

        if (addressInParts.length != 5 || !addressInParts[3].equals("Victoria")) {
            return false;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String bd = birthdate.format(formatter);

        if (bd.length() != 10) {
            return false;
        }

        if (bd.charAt(2) != '-' || bd.charAt(5) != '-') {
            return false;
        }

        String day = bd.substring(0, 2);
        String month = bd.substring(3, 5);
        String year = bd.substring(6,10);

        for (int i = 0; i < day.length(); ++i) {
            if (!Character.isDigit(day.charAt(i))) {
                return false;
            }
        }

        for (int i = 0; i < month.length(); ++i) {
            if (!Character.isDigit(month.charAt(i))) {
                return false;
            }
        }

        for (int i = 0; i < year.length(); ++i) {
            if (!Character.isDigit(year.charAt(i))) {
                return false;
            }
        }

        int d = Integer.valueOf(day);
        int m = Integer.valueOf(month);
        int y = Integer.valueOf(year);
        boolean isLeap = false;

        if (y % 1000 == 0) {
            if (y % 400 == 0) {
                isLeap = true;
            }
        } else {
            if (y % 4 == 0) {
                isLeap = true;
            }
        }

        if (m == 2) {
            if (isLeap) {
                if (d < 1 || d > 29) {
                    return false;
                }
            } else {
                if (d < 1 || d > 28) {
                    return false;
                }
            }
        }

        if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
            if (d < 1 || d > 31) {
                return false;
            }
        }

        if (m == 2 || m == 4 || m == 6 || m == 9 || m == 11) {
            if (d < 1 || d > 30) {
                return false;
            }
        }

        try {
            FileWriter fw = new FileWriter("try.txt", true);
            fw.write(personID + "," + firstName + "," + lastName + "," + address + "," + birthdate + "\n");
            fw.close();
            return true;
        } catch (IOException e) {
            System.out.println("File Error: " + e.getMessage());
            return false;
        }
    }

    public int calculateAge(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthDate = LocalDate.parse(birthdate, formatter);
        LocalDate today = LocalDate.now();
        return Period.between(birthDate, today).getYears();
    }

    public String updatePersonalDetails(String newPersonID, String newFirstName, String newLastName, String newAddress, String newBirthdate) {
        try {
            int age = Period.between(this.birthdate, LocalDate.now()).getYears();
            boolean isBirthdateChanged = newBirthdate != null && !newBirthdate.equals(DATE_FORMAT.format(this.birthdate));

            if (isBirthdateChanged) {
                if (!newPersonID.equals(this.personID) || !newFirstName.equalsIgnoreCase(this.firstName) || !newLastName.equalsIgnoreCase(this.lastName) || !newAddress.equals(this.address)) {
                    return "If birthdate changes, no other details can be updated.";
                }
            }

            if (age < 18 && !newAddress.equals(this.address)) {
                return "Under 18s cannot change address.";
            }

            if (Character.getNumericValue(this.personID.charAt(0)) % 2 == 0 && !newPersonID.equals(this.personID)) {
                return "Cannot change ID if it starts with an even digit.";
            }

            this.personID = newPersonID;
            this.firstName = newFirstName;
            this.lastName = newLastName;
            this.address = newAddress;
            this.birthdate = parseDate(newBirthdate);

            return "Success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String addDemeritPoints(String dateStr, int points) {
        try {
            LocalDate date = parseDate(dateStr);
            if (points < 1 || points > 6) {
                return "Demerit points must be between 1 and 6.";
            }

            demeritPoints.put(date, points);
            int age = Period.between(this.birthdate, LocalDate.now()).getYears();
            LocalDate twoYearsAgo = LocalDate.now().minusYears(2);
            int totalPoints = demeritPoints.entrySet().stream()
                    .filter(e -> !e.getKey().isBefore(twoYearsAgo))
                    .mapToInt(Map.Entry::getValue)
                    .sum();

            boolean currentSuspensionStatus = (age < 21 && totalPoints > 6) || (age >= 21 && totalPoints > 12);
            if (currentSuspensionStatus != isSuspended) {
                isSuspended = currentSuspensionStatus;
            }

            writeDemeritToFile(date, points);
            return "Success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    private LocalDate parseDate(String input) throws Exception {
        try {
            return LocalDate.parse(input, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new Exception("Invalid date format. Use DD-MM-YYYY.");
        }
    }

    private void writeDemeritToFile(LocalDate date, int points) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("demerits.txt", true))) {
            writer.write(String.join("|", personID, DATE_FORMAT.format(date), String.valueOf(points)));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing demerit: " + e.getMessage());
        }
    }
}
