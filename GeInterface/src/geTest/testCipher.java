package geTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import geSrcCode.UserInterface;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.Test;

public class testCipher {
	UserInterface userInterface = new UserInterface();

	@Test
	public void testCypher() throws UnsupportedEncodingException {
		String testBytes1 = userInterface.cypher("CookoosEgg");
		String testBytes2 = userInterface.cypher("CookoosEgg");
		String testBytes3 = userInterface.cypher("CookosEgg");
		
		assertTrue(testBytes1.equals(testBytes2));
		assertFalse(testBytes1.equals(testBytes3));
	}
}
