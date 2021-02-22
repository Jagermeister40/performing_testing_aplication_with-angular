package performance_testing_framework.tests;

import performance_testing_framework.subscribers.DatabaseSubscriber;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @author : Adam Klekowski, Dominik Bober, Szymon Duda, Przemysław Ziaja
 * AGH University of Science and Technology
 **/
public class ProcessorTimeTester extends DecoratorTester {
    private final Object testedObject;
    private final Method methodToTest;
    private final Object[] parameters;
    private final Logger log;

    public ProcessorTimeTester(AbstractTest at, Object testedObject, Method methodToTest, Object[] parameters) {
        super(at);
        this.testedObject = testedObject;
        this.methodToTest = methodToTest;
        this.parameters = parameters;

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tl:%1$tM:%1$tS] [%2$s] %4$s: %5$s%n");
        log = Logger.getLogger(DatabaseSubscriber.class.getName());
    }

    @Override
    public HashMap<String, HashMap<String, String>> runTest() throws IllegalAccessException, SQLException, InvocationTargetException {
        log.info("Running test.");
        HashMap<String, HashMap<String, String>> previous_results = super.runTest();

        HashMap<String, String> internal_result;
        if (!previous_results.containsKey(methodToTest.getName())) {
            previous_results.put(methodToTest.getName(), new HashMap<>());
        }
        internal_result = previous_results.get(methodToTest.getName());
        methodToTest.trySetAccessible();
        long startTime = System.nanoTime();
        methodToTest.invoke(testedObject, parameters);
        long searchTime = System.nanoTime();
        internal_result.put("PROCESSOR_TIME", (searchTime - startTime) + " ns");

        log.info("Ending test.");
        log.info("Returning results: " + previous_results);
        return previous_results;
    }
}
