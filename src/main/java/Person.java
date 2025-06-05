import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    
    private static final String FILE_PATH = "people.txt";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Person() {}

    public Person(String personID, String firstName, String lastName, String address, String birthdate, boolean isSuspended) {
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        try {
            this.birthdate = parseDate(birthdate);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        this.isSuspended = isSuspended;
    }

    public String addPerson() {

        boolean isPersonIDValid = validatePersonID(personID);
        boolean isAddressValid = validateAddress(address);
        boolean isIDUnique = validateUniqueID(personID);

        if (!isPersonIDValid) {
            return "Invalid Person ID, it does not fulfill the required conditions.";
        }

        if (!isIDUnique) {
            return "Person ID already exists, please use a unique ID.";
        }

        if (!isAddressValid) {
            return "Invalid Address, it does not fulfill the required conditions.";
        }

        if (calculateAge(birthdate) <= 0) {
            System.out.println();
            return "Age needs to be greater than zero!";
        }
        
        if (calculateAge(birthdate) > 0 && calculateAge(birthdate) < 16) {
            return "Age needs to be atleast 16 years for driving a car!";
        }

        writeToFile();

        return "The test data is valid and successfully written to file.";
    }

    public String updatePersonalDetails(String newPersonID, String newFirstName, String newLastName, String newAddress, String newBirthdate) {
        try {
            String oldId = this.personID;
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

            if (!newPersonID.equals(this.personID)) {
                validateUniqueID(newPersonID);
            }

            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            boolean found = false;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith(oldId + "|")) {
                    String birthdateStr = isBirthdateChanged ? newBirthdate : DATE_FORMAT.format(this.birthdate);
                    lines.set(i, String.join("|", newPersonID, newFirstName, newLastName, newAddress, birthdateStr, String.valueOf(isSuspended)));
                    found = true;
                    break;
                }
            }

            if (!found) {
                return "Person not found.";
            }

            Files.write(Paths.get(FILE_PATH), lines);

            this.personID = newPersonID;
            this.firstName = newFirstName;
            this.lastName = newLastName;
            this.address = newAddress;
            if (isBirthdateChanged) {
                this.birthdate = parseDate(newBirthdate);
            }
            
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
            throw new Exception("Birthdate must be in DD-MM-YYYY format.");
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
    
    public int calculateAge(LocalDate birthdate) {
        LocalDate today = LocalDate.now();
        return Period.between(birthdate, today).getYears();
    }

    private boolean validatePersonID(String id) {
        if (personID == null || personID.length() != 10) {
            System.out.println("Person ID must be exactly 10 characters long.");
            return false;
        }

        if (!Character.isDigit(personID.charAt(0)) || !Character.isDigit(personID.charAt(1))) {
            System.out.println("First two characters of Person ID must be digits.");
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
            System.out.println("There should be at least two special characters.");
            return false;
        }

        if (!Character.isUpperCase(personID.charAt(8)) || !Character.isUpperCase(personID.charAt(9))) {
            System.out.println("Last two characters of Person ID must be upper case letters.");
            return false;
        }

        return true;
    }
 
    private boolean validateUniqueID(String id){
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            for (String line : lines) {
                if (line.startsWith(id + "|")) {
                    return false;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return true;
    }

    private boolean validateAddress(String address) {

        String[] addressInParts = address.split("\\|");

        if (addressInParts.length != 5 || !addressInParts[3].equals("Victoria") || !addressInParts[4].equals("Australia")) {
            return false;
        }

        return true;
    }

    private void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(String.join("|", personID, firstName, lastName, address, DATE_FORMAT.format(birthdate), String.valueOf(isSuspended)));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
