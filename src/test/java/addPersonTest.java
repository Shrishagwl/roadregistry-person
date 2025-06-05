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
        Person p = new Person("82%^89y$SU","Ram","Lakhan","3|Lonsdale Street|Melbourne|Victoria|Australia","05-03-2000",false);
 		assertEquals("The test data is valid and successfully written to file.",p.addPerson());

    }

    @Test
    public void testAddressFailure() {
        Person p = new Person("56@_7!%&BB","Raj","Shah","12|McKanzie Street|Sydney|New South Wales|Australia","12-09-1990",false);
 		assertEquals("Invalid Address, it does not fulfill the required conditions.",p.addPerson());
    }

    @Test
    public void testAddressSuccess() {
        Person p = new Person("369^%!%&CB","Ramesh","Dadhaniya","12|McKanzie Street|Sydney|Victoria|Australia","12-09-1990",false);
 		assertEquals("The test data is valid and successfully written to file.",p.addPerson());
    }

    @Test
    public void testInvalidDateFormatThrows() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            new Person("28#87!%&DB", "Ramesh", "Dadhaniya", 
                    "12|McKanzie Street|Sydney|Victoria|Australia", 
                    "5-08-2009", false);
        });
        assertEquals("Birthdate must be in DD-MM-YYYY format.", exception.getMessage());
    }

}