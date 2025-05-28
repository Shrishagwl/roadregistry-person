import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class addPersonTest {



    @Test
    public void testAddPersonSuccess() {
        Person p = new Person();
 		 assertEquals(true,p.addPerson("56@_7!%&AB","32|Highland Street|Melbourne|Victoria|Australia","15-11-1990"));
         assertEquals(true,p.addPerson("1234567890","32|Highland Street|Melbourne|Victoria|Australia","15-11-1990"));
    }

    // @Test
    // public void testAddPersonSuccess() {
    //     Person p = new Person("56@_7!%&AB","Rohan","Patel","32|Highland Street|Melbourne|Victoria|Australia","15-11-1990",false);
 	// 	assertTrue(p.addPerson());

    // }
}