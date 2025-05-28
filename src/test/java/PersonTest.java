import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    @Test
    public void testAddPersonSuccess() {
        Person p = new Person("56@_!%&AB","Rohan","Patel","32|Highland Street|Melbourne|Victoria|Australia","15-11-1990");
 		String result = p.addPerson();
        assertEquals("Success", result);
        Person plol = new Person("56@_B","Rohan","Patel","32|Highland Street|Melbourne|Victoria|Australia","15-11-1990");
 		String resultlol = plol.addPerson();
        assertEquals("Invalid person ID .", resultlol);
    }

    @Test
    public void testAddPersonSucc() {
        Person p = new Person("58@_!%&AB","Rohan","Patel","32|Highland Street|Melbourne|Victoria|Australia","15-11-1990");
 		String result = p.addPerson();
        assertEquals("Success", result);

    }

}


// import static org.junit.Assert.*;

// import org.junit.Test;

// public class PersonTest {

// 	@Test
// 	public void test() {
// 		Person p = new Person("1234567890","Rohan","Patel","32|Highland Street|Melbourne|Victoria|Australia","15-11-1990",false);
// 		assertTrue(p.addPerson());
// 	}

// }