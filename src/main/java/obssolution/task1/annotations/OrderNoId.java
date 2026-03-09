package obssolution.task1.annotations;


import obssolution.task1.util.OrderNumberGenerator;
import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@IdGeneratorType(OrderNumberGenerator.class) // Menghubungkan ke logic generator
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface OrderNoId {
}
