package productshop.utils;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface ValidatorUtil {

    <T> boolean validate(T entity);

    <T> Set<ConstraintViolation<T>> getViolations(T entity);
}
