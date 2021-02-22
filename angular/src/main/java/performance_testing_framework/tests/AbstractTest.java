package performance_testing_framework.tests;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author : Adam Klekowski, Dominik Bober, Szymon Duda, Przemysław Ziaja
 * AGH University of Science and Technology
 **/
public interface AbstractTest {
    HashMap<String, HashMap<String, String>> runTest() throws InvocationTargetException, IllegalAccessException, SQLException;
}
