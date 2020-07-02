package nz.ac.vuw.swen301.a2.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class StatsCSVServlet extends HttpServlet {

    public StatsCSVServlet(){

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, HashMap<String,Integer>> table = LogsServlet.getTable();
        if(!table.isEmpty()) {
            resp.setContentType("text/csv");
            resp.setHeader("Content-Disposition","stats.csv");

            OutputStream outPutStream = resp.getOutputStream();

            StringBuilder result = new StringBuilder();
            Map.Entry<String, HashMap<String, Integer>> entry = table.entrySet().iterator().next();
            result.append("name\t");
            for (Map.Entry<String, Integer> childPair : entry.getValue().entrySet()) {
                result.append(childPair.getKey()).append("\t");
            }

            for (Map.Entry<String, HashMap<String, Integer>> parentPair : table.entrySet()) {
                result.append("\n");
                result.append(parentPair.getKey()).append("\t");
                for (Map.Entry<String, Integer> childPair : parentPair.getValue().entrySet()) {
                    result.append(childPair.getValue()).append("\t");
                }
            }

            outPutStream.write(result.toString().getBytes());
            outPutStream.close();
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }
}
