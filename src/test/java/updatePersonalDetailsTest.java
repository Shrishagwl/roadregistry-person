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
        Files.write(Paths.get("people.txt"), new byte[0]);
        person = new Person("56@_7!%&AB", "Rohan", "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "01-01-2008", false);
        person.addPerson();
    }

    @Test
    public void testChangeAddressUnder18Fails() {
        String result = person.updatePersonalDetails(
                "56@_7!%&AB", "Rohan", "Patel",
                "45|New Street|Melbourne|Victoria|Australia",
                "01-01-2008"
        );
        assertEquals("Under 18s cannot change address.", result);
    }

    @Test
    public void testChangeIDStartsWithEvenFails() {
        Person evenIDPerson = new Person("42@_7!%&CD", "Rohan", "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "01-01-2008", false);
        evenIDPerson.addPerson();

        String result2 = evenIDPerson.updatePersonalDetails(
                "84@_7!%&CD", "Rohan", "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "01-01-2008"
        );
        assertEquals("Cannot change ID if it starts with an even digit.", result2);
    }

    @Test
    public void testChangeBirthdateOnlyAllowed() {
        String result = person.updatePersonalDetails(
                "56@_7!%&AB", "Rohan", "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "01-01-2004"
        );
        assertEquals("Success", result);
    }

    @Test
    public void testChangeBirthdateAndOthersFails() {
        String result = person.updatePersonalDetails(
                "56@_7!%&AB", "Rohit", "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "01-01-2005"
        );
        assertEquals("If birthdate changes, no other details can be updated.", result);
    }

    @Test
    public void testValidUpdateSuccess() {
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
