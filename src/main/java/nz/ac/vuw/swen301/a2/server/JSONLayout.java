package nz.ac.vuw.swen301.a2.server;

//Packages for formatting JSON
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

//Hashmap for keystorage
import java.util.HashMap;
import java.util.Map;

public class JSONLayout extends Layout {

    private String id, message, timestamp, thread, logger, level, errorDetails;

    public JSONLayout() {

    }

    public JSONLayout(String id){
        this.id = id;
    }

    public JSONLayout(String id, String message, String timestamp, String thread, String logger, String level, String errorDetails) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.thread = thread;
        this.logger = logger;
        this.level = level;
        this.errorDetails = errorDetails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    @Override
    public String format(LoggingEvent loggingEvent) {
        //Storing the log information into hashmap
        Map<String, String> logs = new HashMap<>();

        //Logging information into hashmap
        logs.put("logger",loggingEvent.getLoggerName());
        logs.put("level",loggingEvent.getLevel().toString());
        logs.put("time",Long.toString(loggingEvent.timeStamp));
        logs.put("thread", loggingEvent.getThreadName());
        logs.put("message", loggingEvent.getRenderedMessage());

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        return gson.toJson(logs); //Returning converted hashmap into JSON string
    }

    @Override
    public boolean ignoresThrowable() {
        return false;
    }

    @Override
    public void activateOptions() {

    }
}



