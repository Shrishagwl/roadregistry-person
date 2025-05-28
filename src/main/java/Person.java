import java.io.*;
import java.nio.file.*;
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

    // Constructor
    public Person(String personID, String firstName, String lastName, String address, String birthdateStr) {
        try {
            if (personID == null || firstName == null || lastName == null || address == null || birthdateStr == null) {
                System.out.println("All fields must be provided.");
                return;
            }

            this.personID = personID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.birthdate = parseDate(birthdateStr);
        } catch (Exception e) {
            System.out.println("Error creating person: " + e.getMessage());
        }
    }

    public String addPerson() {
        try {
            validatePersonID(this.personID);
            validateUniqueID(this.personID);
            validateAddress(this.address);
            writeToFile();
            return "Success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String updatePersonalDetails(String newPersonID, String newFirstName, String newLastName, String newAddress, String newBirthdate) {
        try {
            // Finding the age of the person based on the birthdate
            int age = Period.between(this.birthdate, LocalDate.now()).getYears();
            // Checking if user wishes to change his birthdate 
            boolean isBirthdateChanged = newBirthdate != null && !newBirthdate.equals(DATE_FORMAT.format(this.birthdate));

            // User cannot change any other detail after changing there birthday
            if (isBirthdateChanged) {
                if (!newPersonID.equals(this.personID) || !newFirstName.equalsIgnoreCase(this.firstName) || !newLastName.equalsIgnoreCase(this.lastName) || !newAddress.equals(this.address)) {
                    return "If birthdate changes, no other details can be updated.";
                }
            }

            // User cannot change their address if under 18 
            if (age < 18 && !newAddress.equals(this.address)) {
                return "Under 18s cannot change address.";
            }

            // If the first charector of user id is even then they cannot change their id
            if (Character.getNumericValue(this.personID.charAt(0)) % 2 == 0 && !newPersonID.equals(this.personID)) {
                return "Cannot change ID if it starts with an even digit.";
            }

            this.personID = newPersonID;
            this.firstName = newFirstName;
            this.lastName = newLastName;
            this.address = newAddress;
            this.birthdate = parseDate(newBirthdate);
            
            writeToFile();

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

    private void validatePersonID(String id) throws Exception {
        if (!id.matches("[2-9][2-9][^A-Za-z0-9]{2,}[^A-Za-z0-9]{0,4}[A-Z]{2}")) {
            throw new Exception("Invalid person ID format.");
        }
    }

    private void validateUniqueID(String id) throws Exception {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            for (String line : lines) {
                if (line.startsWith(id + "|")) {
                    throw new Exception("Duplicate ID found: " + id);
                }
            }
        } catch (IOException e) {
            throw new Exception("Error reading file: " + e.getMessage());
        }
    }

    private void validateAddress(String address) throws Exception {
        String[] parts = address.split("\\|");
        if (parts.length != 5 || !parts[3].equalsIgnoreCase("Victoria") || !parts[4].equalsIgnoreCase("Australia")) {
            throw new Exception("Address must be in 'StreetNo|Street|City|Victoria|Australia' format.");
        }
    }

    private LocalDate parseDate(String input) throws Exception {
        try {
            return LocalDate.parse(input, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new Exception("Invalid date format. Use DD-MM-YYYY.");
        }
    }

    private void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(String.join("|", personID, firstName, lastName, address, DATE_FORMAT.format(birthdate), String.valueOf(isSuspended)));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
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
