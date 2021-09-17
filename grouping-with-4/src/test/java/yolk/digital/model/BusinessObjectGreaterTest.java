package yolk.digital.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import yolk.digital.test.group.*;
import yolk.digital.test.runner.YolkRunner;
import yolk.digital.test.InnerTestBase;

import static org.assertj.core.api.Assertions.*;

@RunWith(YolkRunner.class)
@DisabledIfSystemProperty(property = "yolk.digital.junit.local")
public class BusinessObjectGreaterTest {

    @RunWith(YolkRunner.class)
    @DisabledIfSystemProperty(property = "yolk.digital.junit.local")
    public abstract static class InnerTest extends InnerTestBase {

        @Test
        @EnabledIfSystemProperty(property = "yolk.digital.junit.optional")
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
