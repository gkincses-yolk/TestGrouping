package yolk.digital;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import yolk.digital.model.*;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class BusinessApplication {

    private int cutoff;

    public static void main(String[] args) throws Exception {
        BusinessApplication a = new BusinessApplication();
        a.workhorseAllArgs(100, 50, 50);
    }

    void workhorseAllArgs(int cutoff, int test1, int test2) throws BusinessException {
        BusinessObject o = new BusinessObject(cutoff);
        if (o.isSmaller(test1)) {
            throw new BusinessException("smaller :(");
        } else {
            System.out.println(o.isSmaller(test2));
        }
    }

    void workhorseTwoArgs(int test1, int test2) throws BusinessException {
        BusinessObject o = new BusinessObject(cutoff);
        if (o.isGreater(test1)) {
            log.info("is greater");
        } else {
            log.info("is not greater");
        }
    }
}
