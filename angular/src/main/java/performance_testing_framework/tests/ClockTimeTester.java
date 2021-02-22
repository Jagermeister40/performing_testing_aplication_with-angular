package performance_testing_framework.tests;

import performance_testing_framework.subscribers.DatabaseSubscriber;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : Adam Klekowski, Dominik Bober, Szymon Duda, Przemysław Ziaja
 * AGH University of Science and Technology
 **/
public class ClockTimeTester extends DecoratorTester {
    private final Object testedObject;
    private final Method methodToTest;
    private final Object[] parameters;
    private final Logger log;

    public ClockTimeTester(AbstractTest at, Object testedObject, Method methodToTest, Object[] parameters) {
        super(at);
        this.testedObject = testedObject;
        this.methodToTest = methodToTest;
        this.parameters = parameters;

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tl:%1$tM:%1$tS] [%2$s] %4$s: %5$s%n");
        log = Logger.getLogger(DatabaseSubscriber.class.getName());
    }

    @Override
    public HashMap<String, HashMap<String, String>> runTest() throws InvocationTargetException, IllegalAccessException, SQLException {
        log.info("Running test.");
        HashMap<String, HashMap<String, String>> previous_results = super.runTest();

        HashMap<String, String> internal_result;
        if (!previous_results.containsKey(methodToTest.getName())) {
            previous_results.put(methodToTest.getName(), new HashMap<>());
        }
        internal_result = previous_results.get(methodToTest.getName());
        Clock baseclock = Clock.systemDefaultZone();

        methodToTest.trySetAccessible();
        Instant instantstart = baseclock.instant();
        methodToTest.invoke(testedObject, parameters);
        Instant instantfinish = baseclock.instant();

        int s1 = 0, s2 = 0, ms1 = 0, ms2 = 0;
        String in = instantstart.toString();
        System.out.printf(in + "\n\n");
        String pattern = "^[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}T([0-9]{2})\\:([0-9]{2})\\:([0-9]{2})\\.([0-9]{4,8})Z$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(in);
        if (m.find( )) {
            s1 = Integer.parseInt(m.group(3));
            ms1 = Integer.parseInt(m.group(4));
        }

        in = instantfinish.toString();
        r = Pattern.compile(pattern);
        m = r.matcher(in);
        if (m.find( )) {
            s2 = Integer.parseInt(m.group(3));
            ms2 = Integer.parseInt(m.group(4))+5;
        }
        float t1 = Float.parseFloat(s1+"."+ms1);
        float t2 = Float.parseFloat(s2+"."+ms2);

        float diff = t2-t1;

        internal_result.put("CLOCK_TIME", diff + " s");

        log.info("Ending test.");
        log.info("Returning results: " + previous_results);
        return previous_results;
    }
}
