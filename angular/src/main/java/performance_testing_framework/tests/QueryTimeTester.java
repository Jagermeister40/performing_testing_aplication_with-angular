package performance_testing_framework.tests;

import performance_testing_framework.subscribers.DatabaseSubscriber;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author : Adam Klekowski, Dominik Bober, Szymon Duda, Przemysław Ziaja
 * AGH University of Science and Technology
 **/
public class QueryTimeTester extends DecoratorTester {
    private final Object testedObject;
    private final Method methodToTest;
    private final Object[] parameters;
    private final Connection dbConnection;
    private final String dbUser;
    private  final String token;
    private final Logger log;

    public QueryTimeTester(AbstractTest at, Object testedObject,
                           Method methodToTest, Object[] parameters,
                           Connection dbConnection, String dbUser, String token) {
        super(at);
        this.testedObject = testedObject;
        this.methodToTest = methodToTest;
        this.parameters = parameters;
        this.dbConnection = dbConnection;
        this.dbUser = dbUser;
        this.token = token;

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
        methodToTest.trySetAccessible();

        Statement myStmt = dbConnection.createStatement();
        String getLastLogTimeQuery = "SELECT start_time FROM mysql.slow_log ORDER BY start_time DESC LIMIT 1;";
        ResultSet rs = myStmt.executeQuery(getLastLogTimeQuery);

        String invoke_start = "";
        while (rs.next()) {
            invoke_start = rs.getString("start_time");
        }

        methodToTest.invoke(testedObject, parameters);

        String testSql = "SELECT start_time, sql_text, query_time FROM mysql.slow_log WHERE user_host LIKE '%" +
                dbUser + "%' AND start_time >= '" + invoke_start +"' OR LOWER(sql_text) LIKE '%" + toHex(token) + "%';";
        System.out.printf("\n"  + "\n");
        rs = myStmt.executeQuery(testSql);

        String queryExecutionTime, queryText;
        Pattern logTablePattern = Pattern.compile(".*slow_log.*", Pattern.CASE_INSENSITIVE);
        Pattern setPattern = Pattern.compile(".*autocommit.*", Pattern.CASE_INSENSITIVE);

        int seconds=0, milliseconds=0;
        while (rs.next()) {
            queryText = rs.getString("sql_text");
            if (logTablePattern.matcher(queryText).matches() || setPattern.matcher(queryText).matches() )
                continue;

            queryExecutionTime = rs.getString("query_time");
            String pattern = "^([0-9]{2})\\:([0-9]{2})\\:([0-9]{2})\\.([0-9]{9})$";

            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(queryExecutionTime);
            System.out.printf(queryExecutionTime + "\n");
            if (m.find( )) {
                int h = Integer.parseInt(m.group(1));
                int min = Integer.parseInt(m.group(2));
                int s = Integer.parseInt(m.group(3));
                int ms = Integer.parseInt(m.group(4));
                seconds += s;
                milliseconds += ms;
            }
            break;
        }

        String newResult = seconds + "." + milliseconds;
        internal_result.put("QUERY", newResult + " ms");

        log.info("Ending test.");
        log.info("Returning results: " + previous_results);
        return previous_results;
    }
    public String toHex(String arg) {
        return String.format("%x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }
}
