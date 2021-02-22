package performance_testing_framework;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : Adam Klekowski, Dominik Bober, Szymon Duda, Przemysław Ziaja
 * AGH University of Science and Technology
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)

public @interface TestMethod {
    Tester.TestStrategy[] testedValue();
    int[] indicesOfParameters();
    String dbUser() default "";
}