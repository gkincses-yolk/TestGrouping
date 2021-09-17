package yolk.digital.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import yolk.digital.test.InnerTestBase;
import yolk.digital.test.YolkTestInstanceFactory;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(YolkTestInstanceFactory.class)
@EnabledIfSystemProperty(named = "yolk.digital.junit.optional", matches="true")
public class BusinessObjectGreaterTester {

    @Test
    public void testFoo() {

    }

    @ExtendWith(YolkTestInstanceFactory.class)
    @EnabledIfSystemProperty(named = "yolk.digital.junit.optional", matches="true")
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
