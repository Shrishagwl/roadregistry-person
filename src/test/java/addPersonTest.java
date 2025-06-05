import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class addPersonTest {

    @Test
    public void testPersonIDSuccess() {
        Person p = new Person("56@_7!%&AB","Rohan","Patel","32|Highland Street|Melbourne|Victoria|Australia","01-01-2005",false);
 		assertEquals("The test data is valid and successfully written to file.",p.addPerson());
    }

    @Test
    public void testPersonIDFailure() {
        Person p = new Person("12345678","Ram","Lakhan","3|Lonsdale Street|Melbourne|Victoria|Australia","05-03-2000",false);
 		assertEquals("Invalid Person ID, it does not fulfill the required conditions.",p.addPerson());

    }

    @Test
    public void testAddressFailure() {
        Person p = new Person("56@_7!%&AB","Raj","Shah","12|McKanzie Street|Sydney|New South Wales|Australia","12-09-1990",false);
 		assertEquals("Invalid Address, it does not fulfill the required conditions.",p.addPerson());
    }

    @Test
    public void testAddressSuccess() {
        Person p = new Person("56@_7!%&AB","Ramesh","Dadhaniya","12|McKanzie Street|Sydney|Victoria|Australia","12-09-1990",false);
 		assertEquals("The test data is valid and successfully written to file.",p.addPerson());
    }

    @Test
    public void testDate() {
        Person p = new Person("56@_7!%&AB","Ramesh","Dadhaniya","12|McKanzie Street|Sydney|Victoria|Australia","05-08-2009",false);
 		assertEquals("Invalid date format. Use DD-MM-YYYY.",p.addPerson());
    }
}