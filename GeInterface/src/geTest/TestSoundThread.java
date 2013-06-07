package geTest;

import static org.junit.Assert.*;
import geSrcCode.SoundThread;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSoundThread {
    SoundThread soundThread = new SoundThread();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // maybe use this later? nothing to use it for now.
    }

    @Before
    public void setUp() throws Exception {
        // maybe use this later? nothing to use it for now.
    }


    @Test
    public void testLoad() {
        // working on this one
        assertEquals(0,soundThread.load());
        assertFalse(soundThread.load() == -1);  // UnsupportedAudioFileException
        assertFalse(soundThread.load() == -2);  // IOException
        assertFalse(soundThread.load() == -3);    // LineUnavailableException
    }

}