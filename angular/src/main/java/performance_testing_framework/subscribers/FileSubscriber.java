package performance_testing_framework.subscribers;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author : Adam Klekowski, Dominik Bober, Szymon Duda, Przemysław Ziaja
 * AGH University of Science and Technology
 **/
public class FileSubscriber implements Subscriber {
    private final String fileName;
    private final Logger log;

    public FileSubscriber(String fileName){
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tl:%1$tM:%1$tS] [%2$s] %4$s: %5$s%n");
        log = Logger.getLogger(DatabaseSubscriber.class.getName());
        this.fileName = fileName;
    }

    @Override
    public void saveResults(ArrayList<HashMap<String, HashMap<String, String>>> results) {
        try {
            //String allLines = "METHOD,PROCESSOR_TIME,CLOCK_TIME,QUERY_TIME\n";
            String allLines ="";
            for (HashMap<String, HashMap<String, String>> hm : results) {
                HashMap<String, String> toPut = new HashMap<String, String>();
                toPut.put("METHOD", "-");
                toPut.put("PROCESSOR_TIME", "-");
                toPut.put("CLOCK_TIME", "-");
                toPut.put("QUERY_TIME", "-");

                for(Map.Entry mapElement : hm.entrySet())
                {
                    String key = (String)mapElement.getKey();
                    HashMap<String, String> tests = (HashMap<String, String>) mapElement.getValue();
                    toPut.put("METHOD", key);
                    for(Map.Entry test : tests.entrySet()){
                        String test_name = (String)test.getKey();
                        String test_result = (String) test.getValue();
                        toPut.put(test_name, test_result);
                    }
                }

                String line = toPut.get("METHOD") +','+ toPut.get("PROCESSOR_TIME") +','+ toPut.get("CLOCK_TIME") +','+ toPut.get("QUERY") + '\n';
                log.info("\"" + line.replace("\r","").replace("\n","") + "\" will be added to results file.");
                allLines = allLines.concat(line);
            }

            FileWriter myWriter = new FileWriter(fileName,true);
            myWriter.write(allLines);
            myWriter.close();
        } catch (IOException e) {
            log.warning("Error with file results occurred.");
            e.printStackTrace();
        }
    }
}
