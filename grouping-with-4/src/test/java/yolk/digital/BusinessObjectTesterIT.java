package yolk.digital;

import org.junit.*;

public class BusinessObjectTesterIT {

    BusinessApplication a;

    @Before
    public void before() {
        a = new BusinessApplication(100);
    }
    @Test(expected = BusinessException.class)
    public void testWorkhorse1() throws Exception {
        a.workhorseAllArgs(100, 50, 50);
    }
    @Test
    public void testWorkhorse2() throws Exception {
        a.workhorseAllArgs(100, 150, 50);
    }
    @Test
    public void testWorkhorse3() throws Exception {
        a.workhorseTwoArgs(50, 50);
    }
    @Test
    public void testWorkhorse4() throws Exception {
        a.workhorseTwoArgs(150, 50);
    }
}
