package yolk.digital.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import static org.assertj.core.api.Assertions.*;

@EnabledIfSystemProperty(named = "digital.yolk.test.group", matches="GreaterTests")
public class BusinessObjectGreaterTester {

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
