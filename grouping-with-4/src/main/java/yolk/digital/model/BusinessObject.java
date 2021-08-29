package yolk.digital.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BusinessObject {

    private int threshold;

    public boolean isSmaller(int input) {
        if (input < threshold) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGreater(int input) {
        if (input > threshold) {
            return true;
        } else {
            return false;
        }
    }
}
