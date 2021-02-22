package performance_testing_framework.tests;


import performance_testing_framework.subscribers.DatabaseSubscriber;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @author : Adam Klekowski, Dominik Bober, Szymon Duda, Przemysław Ziaja
 * AGH University of Science and Technology
 **/
public class Test implements AbstractTest {
    private final Logger log;

    public Test() {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tl:%1$tM:%1$tS] [%2$s] %4$s: %5$s%n");
        log = Logger.getLogger(DatabaseSubscriber.class.getName());
    }

    @Override
    public HashMap<String, HashMap<String, String>> runTest() {
        log.info("Returning empty HashMap.");
        return new HashMap<>();
    }
}
