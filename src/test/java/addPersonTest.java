import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class addPersonTest {

    @BeforeEach
    public void clearFile() throws IOException {
        Files.write(Paths.get("people.txt"), new byte[0]);
    }

    @Test
    public void testPersonIDSuccess() {
        Person p1 = new Person("82%^89y$SU","Ram","Lakhan","3|Lonsdale Street|Melbourne|Victoria|Australia","05-03-2000",false);
 		assertEquals("The test data is valid and successfully written to file.",p1.addPerson());
        Person p2 = new Person("49i*^tygYH","Rajesh","Sharma","3|Lonsdale Street|Melbourne|Victoria|Australia","05-03-2000",false);
 		assertEquals("The test data is valid and successfully written to file.",p2.addPerson());
    }

    @Test
    public void testPersonIDFailure() {
        Person p3 = new Person("09%^89y$SU","Ram","Lakhan","3|Lonsdale Street|Melbourne|Victoria|Australia","05-03-2000",false);
 		assertEquals("First two characters of Person ID must be digits between 2 and 9.",p3.addPerson());
        Person p4 = new Person("27%^89y$gT","Ram","Lakhan","3|Lonsdale Street|Melbourne|Victoria|Australia","05-03-2000",false);
 		assertEquals("Last two characters of Person ID must be upper case letters.",p4.addPerson());
        Person p5 = new Person("^89y$gT","Ram","Lakhan","3|Lonsdale Street|Melbourne|Victoria|Australia","05-03-2000",false);
 		assertEquals("Person ID must be exactly 10 characters long.",p5.addPerson());
        Person p6 = new Person("28Ti89y$gT","Ram","Lakhan","3|Lonsdale Street|Melbourne|Victoria|Australia","05-03-2000",false);
 		assertEquals("There should be at least two special characters.",p6.addPerson());
        Person p7 = new Person("ab%^89y$gT","Ram","Lakhan","3|Lonsdale Street|Melbourne|Victoria|Australia","05-03-2000",false);
 		assertEquals("First two characters of Person ID must be digits.",p7.addPerson());
    }

    @Test
    public void testAddressFailure() {
        Person p8 = new Person("56@_7!%&BB","Raj","Shah","32|Melbourne|Victoria|Australia","12-09-1990",false);
 		assertEquals("Address must be in the format: 'number|street|city|state|country'.",p8.addPerson());
        Person p9 = new Person("56@_7!%&BB","Raj","Shah","12|Parramatta|McKanzie Street|New South Wales|Australia","12-09-1990",false);
 		assertEquals("State must be 'Victoria'.",p9.addPerson());
        Person p10 = new Person("58@_7!%&BD", "Priya", "Verma", "22|King Street|Melbourne|Victoria|USA", "11-11-1993", false);
        assertEquals("Country must be 'Australia'.", p10.addPerson());
    }

    @Test
    public void testAddressSuccess() {
        Person p11 = new Person("369^%!%&CB","Ramesh","Dadhaniya","12|McKanzie Street|Sydney|Victoria|Australia","12-09-1990",false);
 		assertEquals("The test data is valid and successfully written to file.",p11.addPerson());
        Person p12 = new Person("379^%!%&CB","Ramesh","Patel","54|Ridge Street|Melbourne|Victoria|Australia","12-09-1990",false);
 		assertEquals("The test data is valid and successfully written to file.",p12.addPerson());
    }

    @Test
    public void testValidDateFormatThrows() {
        RuntimeException exception1 = assertThrows(RuntimeException.class, () -> {
            new Person("28#87!%&DB", "Ramesh", "Dadhaniya", 
                    "12|McKanzie Street|Sydney|Victoria|Australia", 
                    "5-08-2009", false);
        });
        assertEquals("Birthdate must be in DD-MM-YYYY format.", exception1.getMessage());

        RuntimeException exception2 = assertThrows(RuntimeException.class, () -> {
            new Person("28#87!%&DB", "Ramesh", "Dadhaniya", 
                    "12|McKanzie Street|Sydney|Victoria|Australia", 
                    "5-09-2005", false);
        });
        assertEquals("Birthdate must be in DD-MM-YYYY format.", exception2.getMessage());
    }

}