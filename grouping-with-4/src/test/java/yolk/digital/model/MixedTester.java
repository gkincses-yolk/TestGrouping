package yolk.digital.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import yolk.digital.test.runner.YolkRunner;
import yolk.digital.test.group.TestGroup;

import static org.assertj.core.api.Assertions.*;

@RunWith(YolkRunner.class)
public class MixedTester {

    @Test
    @TestGroup(value = "NoopTests")
    public void testNoop() {
        assertThat(true);
    }

    @Test
    @TestGroup(value = "SmallerTests")
    public void testSmallerWhenSmaller() {
        BusinessObject bo = new BusinessObject(100);
        boolean b = bo.isSmaller(50);
        assertThat(b);
    }

}
