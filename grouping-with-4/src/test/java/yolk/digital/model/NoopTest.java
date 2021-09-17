package yolk.digital.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import yolk.digital.test.group.*;
import yolk.digital.test.runner.YolkRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(YolkRunner.class)
@DisabledIfSystemProperty(property = "yolk.digital.junit.local")
public class NoopTest {

    @Test
    public void testNoop() {
        assertThat(true);
    }
}
