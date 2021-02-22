package performance_testing_framework;

import performance_testing_framework.subscribers.DatabaseSubscriber;
import performance_testing_framework.subscribers.Subscriber;
import performance_testing_framework.tests.AbstractTest;
import performance_testing_framework.tests.ClockTimeTester;
import performance_testing_framework.tests.DecoratorTester;
import performance_testing_framework.tests.ProcessorTimeTester;
import performance_testing_framework.tests.QueryTimeTester;
import performance_testing_framework.tests.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * @author : Adam Klekowski, Dominik Bober, Szymon Duda, Przemysław Ziaja
 * AGH University of Science and Technology
 **/

public class Tester {
    public enum TestStrategy {PROCESSOR, CLOCK, QUERY}

    protected ArrayList<AbstractTest> methodsToTest;
    protected ArrayList<HashMap<String, HashMap<String, String>>> testResults;
    protected ArrayList<Subscriber> subscribers;

    private Logger log;

    public static class Builder {
        protected Object testedObject = null;
        protected Connection dbConnector = null;
        protected Object[] parameters = null;
        protected ArrayList<Subscriber> subscribers = new ArrayList<>();

        public Builder testedClass(Object to) {
            this.testedObject = to;
            return this;
        }

        public Builder parameters(Object[] p) {
            this.parameters = p;
            return this;
        }

        public Builder dbConnector(Connection dbc) {
            this.dbConnector = dbc;
            return this;
        }

        public Builder subscriber(Subscriber sub) {
            this.subscribers.add(sub);
            return this;
        }

        public Tester build() {
            Tester tester = new Tester();
            System.setProperty("java.util.logging.SimpleFormatter.format",
                    "[%1$tl:%1$tM:%1$tS] [%2$s] %4$s: %5$s%n");
            tester.log = Logger.getLogger(DatabaseSubscriber.class.getName());
            try {
                Method[] classMethods = this.testedObject.getClass().getDeclaredMethods();
                Annotation[] methodsAnnotations = new Annotation[classMethods.length];
                for (int i = 0; i < classMethods.length; i++) {
                    methodsAnnotations[i] = classMethods[i].getDeclaredAnnotation(TestMethod.class);
                }

                tester.methodsToTest = new ArrayList<>();
                for(int i=0;i<methodsAnnotations.length;i++) {
                    if(methodsAnnotations[i] instanceof TestMethod) {
                        Test emptyTest = new Test();
                        DecoratorTester decTester = new DecoratorTester(emptyTest);
                        for(TestStrategy str : ((TestMethod)methodsAnnotations[i]).testedValue()) {
                            if(str == TestStrategy.QUERY && this.dbConnector == null) {
                                throw new IllegalArgumentException("Connection to database not provided.");
                            }

                            Object[] params = new Object[((TestMethod)methodsAnnotations[i]).indicesOfParameters().length + 1];
                            int k = 0;
                            for (int ind : ((TestMethod)methodsAnnotations[i]).indicesOfParameters()) {
                                params[k] = parameters[ind];
                                k++;
                            }
                            Clock baseclock = Clock.systemDefaultZone();

                            Instant instantstart = baseclock.instant();
                            String in = instantstart.toString().replaceAll("\\-|\\:|\\.", "");
                            System.out.printf(in + "\n\n");

                            params[params.length - 1] = Optional.of(in);
                            if(str == TestStrategy.QUERY) {
                                String dbUser = ((TestMethod)methodsAnnotations[i]).dbUser();
                                if (dbUser.equals("")) {
                                    throw new IllegalArgumentException("Username to database not provided.");
                                }
                                decTester = new QueryTimeTester(decTester, this.testedObject, classMethods[i], params, dbConnector, dbUser, "Optional" + in);
                            }
                            else if (str == TestStrategy.PROCESSOR) {
                                decTester = new ProcessorTimeTester(decTester, this.testedObject, classMethods[i], params);
                            }
                            else if (str == TestStrategy.CLOCK) {
                                decTester = new ClockTimeTester(decTester, this.testedObject, classMethods[i], params);
                            }
                        }
                        tester.methodsToTest.add(decTester);
                    }
                }
            } catch (Throwable e) {
                System.err.println(e.getMessage());
            }

            tester.subscribers = this.subscribers;

            return tester;
        }
    }

    private Tester() {}

    public void performTest() {
        log.info("Performing tests.");
        testResults = new ArrayList<>();
        for (AbstractTest at : methodsToTest) {
            try {
                HashMap<String, HashMap<String, String>> result = at.runTest();
                testResults.add(result);
            }
            catch(Throwable e){
                    System.err.println(e.getMessage());
            }
        }
        notifySubscribers();
    }

    private void notifySubscribers() {
        log.info("Notifying subscribers.");
        for (Subscriber sub : subscribers) {
            sub.saveResults(testResults);
        }
    }

    public void showResults() {
        System.out.println("Show Results");
        for (HashMap<String, HashMap<String, String>> hm : testResults) {
            System.out.println(hm.keySet().toString() + " -> " + hm.values());
        }
    }
}
