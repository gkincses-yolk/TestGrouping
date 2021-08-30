package yolk.digital.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import yolk.digital.test.runner.YolkRunner;
import yolk.digital.test.InnerTestBase;
import yolk.digital.test.group.TestGroup;

import static org.assertj.core.api.Assertions.*;

@RunWith(YolkRunner.class)
@TestGroup(value = "GreaterTests")
public class BusinessObjectGreaterTester {

    @RunWith(YolkRunner.class)
    @TestGroup(value = "GreaterTests")
    public abstract static class InnerTests extends InnerTestBase {

        @Test
        public void testGreaterWhenGreater() {
            BusinessObject bo = new BusinessObject(100);
            boolean b = bo.isGreater(150);
            assertThat(b);
        }

        @Test
        public void testGreaterWhenEquals() {
            BusinessObject bo = new BusinessObject(1000);
            boolean b = bo.isGreater(1000);
            assertThat(!b);
        }

        @Test
        public void testGreaterWhenSmaller() {
            BusinessObject bo = new BusinessObject(50);
            boolean b = bo.isGreater(49);
            assertThat(!b);
        }

    }
}
