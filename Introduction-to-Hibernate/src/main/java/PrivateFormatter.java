import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PrivateFormatter {
    public static String format(BigDecimal number) {
        DecimalFormat result = new DecimalFormat("#.00");
        return result.format(number);
    }
}
