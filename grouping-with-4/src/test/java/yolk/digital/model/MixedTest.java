package yolk.digital.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import yolk.digital.test.runner.YolkRunner;
import yolk.digital.test.group.EnabledIfSystemProperty;

import static org.assertj.core.api.Assertions.*;

@RunWith(YolkRunner.class)
@EnabledIfSystemProperty(property = "yolk.digital.junit.optional")
public class MixedTest {

    @Test
    public void testNoop() {
        assertThat(true);
    }

    @Test
    public void testSmallerWhenSmaller() {
        BusinessObject bo = new BusinessObject(100);
        boolean b = bo.isSmaller(50);
        assertThat(b);
    }

}
