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

    // File path to store person data
    private static final String FILE_PATH = "people.txt";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Default constructor
    public Person() {}

    // Parameterized constructor with birthdate parsing
    public Person(String personID, String firstName, String lastName, String address, String birthdate, boolean isSuspended) {
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        try {
            this.birthdate = parseDate(birthdate); // Parse and validate date format
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        this.isSuspended = isSuspended;
    }

    // Adds a person to the TXT file after performing validations.
    public String addPerson() {
        String isPersonIDValid = validatePersonID(personID);
        String isAddressValid = validateAddress(address);
        boolean isIDUnique = validateUniqueID(personID);

        if (isPersonIDValid != null) return isPersonIDValid;
        if (!isIDUnique) return "Person ID already exists, please use a unique ID.";
        if (isAddressValid != null) return isAddressValid;

        int age = calculateAge(birthdate);
        if (age <= 0) return "Age needs to be greater than zero!";
        if (age < 16) return "Age needs to be atleast 16 years for driving a car!";

        writeToFile();
        return "The test data is valid and successfully written to file.";
    }

    // Updates a person's details in the file if all constraints are met.
    public String updatePersonalDetails(String newPersonID, String newFirstName, String newLastName, String newAddress, String newBirthdate) {
        try {
            String oldId = this.personID;
            int age = calculateAge(this.birthdate);
            boolean isBirthdateChanged = newBirthdate != null && !newBirthdate.equals(DATE_FORMAT.format(this.birthdate));

            // If birthdate changes, no other fields should change
            if (isBirthdateChanged && (!newPersonID.equals(this.personID) || !newFirstName.equalsIgnoreCase(this.firstName) ||
                !newLastName.equalsIgnoreCase(this.lastName) || !newAddress.equals(this.address))) {
                return "If birthdate changes, no other details can be updated.";
            }

            // Under 18s cannot change address
            if (age < 18 && !newAddress.equals(this.address)) return "Under 18s cannot change address.";

            // IDs starting with even digit cannot be changed
            if (Character.getNumericValue(this.personID.charAt(0)) % 2 == 0 && !newPersonID.equals(this.personID)) return "Cannot change ID if it starts with an even digit.";

            if (!newPersonID.equals(this.personID)) {
                if (!validateUniqueID(newPersonID)) return "New ID already exists.";
            }

            // Read and update data in the file
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

            if (!found) return "Person not found.";

            // Write updated lines to the file
            Files.write(Paths.get(FILE_PATH), lines);

            // Update current object
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

    // Adds demerit points to a person's record and evaluates suspension status.
    public String addDemeritPoints(String dateStr, int points) {
        try {
            LocalDate date = parseDate(dateStr);
            if (points < 1 || points > 6) return "Demerit points must be between 1 and 6.";

            demeritPoints.put(date, points);

            // Calculate suspension status
            int age = calculateAge(birthdate);
            LocalDate twoYearsAgo = LocalDate.now().minusYears(2);
            int totalPoints = demeritPoints.entrySet().stream()
                    .filter(e -> !e.getKey().isBefore(twoYearsAgo))
                    .mapToInt(Map.Entry::getValue)
                    .sum();

            boolean newSuspensionStatus = (age < 21 && totalPoints > 6) || (age >= 21 && totalPoints > 12);
            if (newSuspensionStatus != isSuspended) isSuspended = newSuspensionStatus;

            writeDemeritToFile(date, points);
            return "Success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // Parses and validates date in DD-MM-YYYY format
    private LocalDate parseDate(String input) throws Exception {
        try {
            return LocalDate.parse(input, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new Exception("Birthdate must be in DD-MM-YYYY format.");
        }
    }

    // Appends demerit record to demerits.txt
    private void writeDemeritToFile(LocalDate date, int points) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("demerits.txt", true))) {
            writer.write(String.join("|", personID, DATE_FORMAT.format(date), String.valueOf(points)));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing demerit: " + e.getMessage());
        }
    }

    // Returns age based on birthdate
    public int calculateAge(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    // Validates person ID format and structure
    private String validatePersonID(String id) {
        if (id == null || id.length() != 10) return "Person ID must be exactly 10 characters long.";
        if (!Character.isDigit(id.charAt(0)) || !Character.isDigit(id.charAt(1))) return "First two characters of Person ID must be digits.";
        if (id.charAt(0) < '2' || id.charAt(0) > '9' || id.charAt(1) < '2' || id.charAt(1) > '9') return "First two characters of Person ID must be digits between 2 and 9.";

        int count = 0;
        for (int i = 2; i <= 7; ++i) {
            if (!Character.isLetterOrDigit(id.charAt(i))) count++;
        }
        if (count < 2) return "There should be at least two special characters between characters 3 and 8.";

        if (!Character.isUpperCase(id.charAt(8)) || !Character.isUpperCase(id.charAt(9))) return "Last two characters of Person ID must be uppercase letters.";

        return null;
    }

    // Checks if person ID is already in file
    private boolean validateUniqueID(String id) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            return lines.stream().noneMatch(line -> line.startsWith(id + "|"));
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return true;
        }
    }

    // Validates address format
    private String validateAddress(String address) {
        String[] parts = address.split("\\|");
        if (parts.length != 5) return "Address must be in the format: 'number|street|city|state|country'.";
        if (!parts[3].equals("Victoria")) return "State must be 'Victoria'.";
        if (!parts[4].equals("Australia")) return "Country must be 'Australia'.";
        return null;
    }

    // Appends valid person data to the people.txt file
    private void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(String.join("|", personID, firstName, lastName, address, DATE_FORMAT.format(birthdate), String.valueOf(isSuspended)));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
