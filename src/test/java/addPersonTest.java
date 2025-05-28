import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class addPersonTest {



    @Test
    public void testPersonIDSuccess() {
        LocalDate dob = LocalDate.parse("01-01-2090", DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Person p = new Person("56@_7!%&AB","Rohan","Patel","32|Highland Street|Melbourne|Victoria|Australia",dob,false);
 		assertEquals(true,p.addPerson());
    }

    @Test
    public void testPersonIDFailure() {
        LocalDate dob = LocalDate.parse("05-03-2000", DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Person p = new Person("12345678","Ram","Lakhan","3|Lonsdale Street|Melbourne|Victoria|Australia",dob,false);
 		assertTrue(p.addPerson());

    }

    @Test
    public void testAddressFailure() {
        LocalDate dob = LocalDate.parse("12-09-1990", DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Person p = new Person("59@)7o%&ZB","Raj","Shah","12|McKanzie Street|Sydney|New South Wales|Australia",dob,false);
 		assertFalse(p.addPerson());
    }

    @Test
    public void testAddressSuccess() {
        LocalDate dob = LocalDate.parse("12-09-1990", DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Person p = new Person("51@)8*%&PT","Ramesh","Dadhaniya","12|McKanzie Street|Sydney|Victoria|Australia",dob,false);
 		assertFalse(p.addPerson());
    }

    @Test
    public void testDate() {
        LocalDate dob = LocalDate.parse("05-08-1990", DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Person p = new Person("51@)8*%&PT","Ramesh","Dadhaniya","12|McKanzie Street|Sydney|Victoria|Australia",dob,false);
 		assertFalse(p.addPerson());
    }
}