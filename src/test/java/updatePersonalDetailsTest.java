import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class updatePersonalDetailsTest {
    private Person person;

    @BeforeEach
    public void setup() throws IOException {
        // Clear the people.txt file before each test to ensure a clean environment
        Files.write(Paths.get("people.txt"), new byte[0]);

        // Create a new Person object (under 18) and add to file for testing
        person = new Person("56@_7!%&AB", "Rohan", "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "01-01-2008", false);
        person.addPerson();
    }

    @Test
    public void testChangeAddressUnder18Fails() {
        // Attempt to change address for a person under 18 — should be rejected
        String result = person.updatePersonalDetails(
                "56@_7!%&AB", "Rohan", "Patel",
                "45|New Street|Melbourne|Victoria|Australia",
                "01-01-2008"
        );
        assertEquals("Under 18s cannot change address.", result);
    }

    @Test
    public void testChangeIDStartsWithEvenFails() {
        // Create a new person with an ID that starts with an even number
        Person evenIDPerson = new Person("42@_7!%&CD", "Rohan", "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "01-01-2008", false);
        evenIDPerson.addPerson();

        // Attempt to change their ID — should fail if it starts with an even digit
        String result2 = evenIDPerson.updatePersonalDetails(
                "84@_7!%&CD", "Rohan", "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "01-01-2008"
        );
        assertEquals("Cannot change ID if it starts with an even digit.", result2);
    }

    @Test
    public void testChangeBirthdateOnlyAllowed() {
        // Change only the birthdate (no other fields) — should succeed
        String result = person.updatePersonalDetails(
                "56@_7!%&AB", "Rohan", "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "01-01-2004"
        );
        assertEquals("Success", result);
    }

    @Test
    public void testChangeBirthdateAndOthersFails() {
        // Attempt to change birthdate AND first name — should be rejected
        String result = person.updatePersonalDetails(
                "56@_7!%&AB", "Rohit", "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "01-01-2005"
        );
        assertEquals("If birthdate changes, no other details can be updated.", result);
    }

    @Test
    public void testValidUpdateSuccess() {
        // Add a new adult person and attempt to update their address — should succeed
        Person newPerson = new Person("56@_7!%&AB", "Raj", "Sharma",
                "10|King Street|Melbourne|Victoria|Australia",
                "01-01-2000", false);
        newPerson.addPerson();

        String result = newPerson.updatePersonalDetails(
                "56@_7!%&AB", "Raj", "Sharma",
                "11|Queen Street|Melbourne|Victoria|Australia",
                "01-01-2000"
        );
        assertEquals("Success", result);
    }
}
