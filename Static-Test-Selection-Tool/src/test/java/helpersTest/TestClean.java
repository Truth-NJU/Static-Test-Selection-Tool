package helpersTest;

import command.Clean;
import org.junit.Test;

import java.io.IOException;

public class TestClean {
    @Test
    public void testClean() throws IOException {
        Clean clean=new Clean();
        clean.cleanSuccess();
    }
}
