import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class addPersonTest {

    @Test
    public void testPersonIDSuccess() {
        Person p = new Person("56@_7!%&AB","Rohan","Patel","32|Highland Street|Melbourne|Victoria|Australia","01-01-2090",false);
 		assertEquals(true,p.addPerson());
    }

    @Test
    public void testPersonIDFailure() {
        Person p = new Person("12345678","Ram","Lakhan","3|Lonsdale Street|Melbourne|Victoria|Australia","05-03-2000",false);
 		assertFalse(p.addPerson());

    }

    @Test
    public void testAddressFailure() {
        Person p = new Person("59@o%&ZB","Raj","Shah","12|McKanzie Street|Sydney|New South Wales|Australia","12-09-1990",false);
 		assertFalse(p.addPerson());
    }

    @Test
    public void testAddressSuccess() {
        Person p = new Person("51@)8*%&PT","Ramesh","Dadhaniya","12|McKanzie Street|Sydney|Victoria|Australia","12-09-1990",false);
 		assertFalse(p.addPerson());
    }

    @Test
    public void testDate() {
        Person p = new Person("3351yt%&PT","Ramesh","Dadhaniya","12|McKanzie Street|Sydney|Victoria|Australia","5-08-1990",false);
 		assertEquals("Invalid date format. Use DD-MM-YYYY.",p.addPerson());
    }
}