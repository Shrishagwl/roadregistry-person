import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class addDemeritPointsTest {
    private Person person;

    @BeforeEach
    public void setup() {
        person = new Person("56@_7!%&AB", "Rohan", "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "01-01-2005", false); // Under 21 for testing suspension
    }

    @Test
    public void testValidPointsSuccess() {
        String result = person.addDemeritPoints("01-06-2023", 3);
        assertEquals("Success", result);
    }

    @Test
    public void testInvalidPointsTooHigh() {
        String result = person.addDemeritPoints("01-06-2023", 10);
        assertEquals("Demerit points must be between 1 and 6.", result);
    }

    @Test
    public void testInvalidPointsTooLow() {
        String result = person.addDemeritPoints("01-06-2023", 0);
        assertEquals("Demerit points must be between 1 and 6.", result);
    }

    @Test
    public void testInvalidDateFormat() {
        String result = person.addDemeritPoints("2023-06-01", 3);
        assertEquals("Birthdate must be in DD-MM-YYYY format.", result);
    }

    @Test
    public void testSuspensionLogic() {
        person.addDemeritPoints("01-01-2023", 3);
        person.addDemeritPoints("01-02-2023", 3);
        String result = person.addDemeritPoints("01-03-2023", 1);
        assertEquals("Success", result);
    }
}
