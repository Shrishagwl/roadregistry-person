import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class updatePersonalDetailsTest {
    private Person person;

    @BeforeEach
    public void setup() {
        person = new Person("56@_7!%&AB", "Rohan", "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "01-01-2005", false);
    }

    @Test
    public void testChangeAddressUnder18Fails() {
        String result = person.updatePersonalDetails("56@_7!%&AB", "Rohan", "Patel",
                "99|New Street|Melbourne|Victoria|Australia", "01-01-2005");
        assertEquals("Under 18s cannot change address.", result);
    }

    @Test
    public void testChangeIDStartsWithEvenFails() {
        String result = person.updatePersonalDetails("76@_7!%&XY", "Rohan", "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia", "01-01-2005");
        assertEquals("Cannot change ID if it starts with an even digit.", result);
    }

    @Test
    public void testChangeBirthdateOnlyAllowed() {
        String result = person.updatePersonalDetails("56@_7!%&AB", "Rohan", "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia", "01-01-2000");
        assertEquals("Success", result);
    }

    @Test
    public void testChangeBirthdateAndOthersFails() {
        String result = person.updatePersonalDetails("56@_7!%&CD", "Rohit", "Patel",
                "45|Changed St|Melbourne|Victoria|Australia", "01-01-2000");
        assertEquals("If birthdate changes, no other details can be updated.", result);
    }

    @Test
    public void testValidUpdateSuccess() {
        person = new Person("57@_7!%&AB", "John", "Smith",
                "10|King Street|Melbourne|Victoria|Australia", "01-01-2000", false);
        String result = person.updatePersonalDetails("57@_7!%&AB", "John", "Smith",
                "10|King Street|Melbourne|Victoria|Australia", "01-01-2000");
        assertEquals("Success", result);
    }
}