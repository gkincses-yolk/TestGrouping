package yolk.digital.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import static org.assertj.core.api.Assertions.*;

@EnabledIfSystemProperty(named = "digital.yolk.test.group", matches = "SmallerTests")
public class BusinessObjectSmallerTester {

    @Test
    public void testSmallerWhenSmaller() {
        BusinessObject bo = new BusinessObject(100);
        boolean b = bo.isSmaller(50);
        assertThat(b);
    }

    @Test
    public void testSmallerWhenEquals() {
        BusinessObject bo = new BusinessObject(1000);
        boolean b = bo.isSmaller(1000);
        assertThat(!b);
    }

    @Test
    public void testSmallerWhenGreater() {
        BusinessObject bo = new BusinessObject(50);
        boolean b = bo.isSmaller(51);
        assertThat(!b);
    }
}
