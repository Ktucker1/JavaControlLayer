package geTest;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import geSrcCode.UserInterface;

public class testCipher {
	UserInterface userInterface = new UserInterface();

	@Test
	public void testCypher() {
		byte[] testBytes1 = userInterface.cypher("CookoosEgg");
		byte[] testBytes2 = userInterface.cypher("CookoosEgg");
		byte[] testBytes3 = userInterface.cypher("CookosEgg");
		
		assertTrue(Arrays.equals(testBytes1, testBytes2));
		assertFalse(Arrays.equals(testBytes1, testBytes3));
	}
}
