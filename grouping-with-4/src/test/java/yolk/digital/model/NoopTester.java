package yolk.digital.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import yolk.digital.test.runner.YolkRunner;
import yolk.digital.test.group.TestGroup;

import static org.assertj.core.api.Assertions.*;

@RunWith(YolkRunner.class)
@TestGroup(value = { "NoopTests", "SmallerTests" })
public class NoopTester {

    @Test
    public void testNoop() {
        assertThat(true);
    }
}
