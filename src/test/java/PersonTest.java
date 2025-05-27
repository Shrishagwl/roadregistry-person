import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    @Test
    public void testAddPersonFailure() {
        Person p = new Person("56@_!%&AB","Rohan","Patel","32|Highland Street|Melbourne|Victoria|Australia","15-11-1990",false);
 		assertTrue(p.addPerson());

    }

    @Test
    public void testAddPersonSuccess() {
        Person p = new Person("56@_7!%&AB","Rohan","Patel","32|Highland Street|Melbourne|Victoria|Australia","15-11-1990",false);
 		assertTrue(p.addPerson());

    }
}