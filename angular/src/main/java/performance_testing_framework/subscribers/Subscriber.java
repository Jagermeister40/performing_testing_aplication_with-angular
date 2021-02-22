package performance_testing_framework.subscribers;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author : Adam Klekowski, Dominik Bober, Szymon Duda, Przemysław Ziaja
 * AGH University of Science and Technology
 **/
public interface Subscriber {
    void saveResults(ArrayList<HashMap<String, HashMap<String, String>>> results);
}
