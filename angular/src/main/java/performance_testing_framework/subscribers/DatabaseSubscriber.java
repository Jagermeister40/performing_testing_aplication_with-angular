package performance_testing_framework.subscribers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author : Adam Klekowski, Dominik Bober, Szymon Duda, Przemysław Ziaja
 * AGH University of Science and Technology
 **/
public class DatabaseSubscriber implements Subscriber {
    private final Connection dbConnection;
    private final Logger log;

    public DatabaseSubscriber(Connection dbConnection) {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tl:%1$tM:%1$tS] [%2$s] %4$s: %5$s%n");
        log = Logger.getLogger(DatabaseSubscriber.class.getName());

        this.dbConnection = dbConnection;
    }

    @Override
    public void saveResults(ArrayList<HashMap<String, HashMap<String, String>>> results) {
        try {
            Statement InsertStm = dbConnection.createStatement();

            String key="", test_name="", test_result="";
            for (HashMap<String, HashMap<String, String>> hm : results) {
                for(Map.Entry mapElement : hm.entrySet())
                {
                    key = (String)mapElement.getKey();
                    HashMap<String, String> tests = (HashMap<String, String>) mapElement.getValue();
                    for(Map.Entry test : tests.entrySet()){
                        test_name = (String)test.getKey();
                        test_result = (String) test.getValue();

                        String sql = "INSERT INTO results (name_of_test, type_of_test, result_of_test) VALUES ('" + key + "', '" + test_name +"', '" + test_result + "');";
                        InsertStm.executeUpdate(sql);
                        log.info("\"" + key + ";" + test_name + ";" + test_result + "\" inserted into database.");
                    }
                }
            }
        } catch (SQLException e) {
            log.warning("Error with database occurred.");
        }

    }
}
