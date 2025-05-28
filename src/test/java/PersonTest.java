import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    @Test
    public void testAddPersonSuccess() {
        Person p = new Person("56@_!%&AB","Rohan","Patel","32|Highland Street|Melbourne|Victoria|Australia","15-11-1990");
 		String result = p.addPerson();
        assertEquals("Success", result);
    }

    @Test
    public void testUpdatePersonalDetails_Success() {
        // Step 1: Create original person
        Person person = new Person(
                "56@_!%AB",
                "Rohan",
                "Patel",
                "32|Highland Street|Melbourne|Victoria|Australia",
                "15-11-2000"
        );

        // Step 2: Try to update all fields (except birthdate)
        String result = person.updatePersonalDetails(
                "56@_!%AB",                     // same ID
                "Rohit",                        // new first name
                "Patel",                        // same last name
                "32|Highland Street|Melbourne|Victoria|Australia", // same address
                "15-11-2000"                    // same birthdate
        );

        // Step 3: Assert that update was successful
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