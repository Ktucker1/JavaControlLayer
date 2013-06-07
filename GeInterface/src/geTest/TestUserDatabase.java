package geTest;

import static org.junit.Assert.*;
import geSrcCode.UserDatabase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestUserDatabase {
	UserDatabase userdatabase = new UserDatabase();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// maybe use this later? nothing to use it for now.
	}

	@Before
	public void setUp() throws Exception {
		// maybe use this later? nothing to use it for now.
	}

	@Test
	public void testAuthorize() {
		// Expected return codes
		final int invalidUserName = 0;
		final int badPW = 1;
		final int student = 2;
		final int educator = 3;
		
		String studentID = new String("kyle");
		String studentPW = new String("456");
		String studentWrongPW = new String("455");
		
		String educatorID = new String("han");
		String educatorPW = new String("999");
		String educatorWrongPW = new String("111");
		
		String intruderID = new String("qwerty");
		String intruderPW = new String("hackme");
		String noID_or_PWgiven = null;
		
		// test a student
		assertEquals(2,userdatabase.authorize(studentID, studentPW)); //student with correct password
		assertTrue(userdatabase.authorize(studentID, studentPW) == student); // test correct user type
		assertFalse(userdatabase.authorize(studentID, studentPW) == educator); // test incorrect user type
		assertTrue(userdatabase.authorize(studentID, studentWrongPW) == badPW); //student with invalid password
	
		
		// test an educator
		assertEquals(3,userdatabase.authorize(educatorID, educatorPW)); //educator with correct password
		assertTrue(userdatabase.authorize(educatorID, educatorPW) == educator); // test correct user type
		assertFalse(userdatabase.authorize(educatorID, educatorPW) == student); // test incorrect user type
		assertTrue(userdatabase.authorize(educatorID, educatorWrongPW) == badPW); //educator with invalid password
		
		// test an intruder
		assertEquals(0,userdatabase.authorize(intruderID, intruderPW)); //educator with correct password
		
		//test missing fields
		assertEquals(0,userdatabase.authorize(noID_or_PWgiven, studentPW)); // No ID given
		assertEquals(1,userdatabase.authorize(studentID, noID_or_PWgiven)); // No password given

	}

}
