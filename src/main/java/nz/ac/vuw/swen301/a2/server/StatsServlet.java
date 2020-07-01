package nz.ac.vuw.swen301.a2.server;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class StatsServlet extends HttpServlet{

    public StatsServlet(){

    }

    //logs.stream.map(logEvent -> logEvent.getLogger()).collect(Collectors.toList())
    //Make them distinct

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //First <String = name<String = date, Integer = occurence>>
        HashMap<String,HashMap<String,Integer>> table = LogsServlet.getTable();
        if(!table.isEmpty()) {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();

            out.println("<html><body>");
            out.println("<table style=\"width:100%\">");
            //First row => name and dates/column headers
            out.println("<tr>");
            out.println("<td> name </td>");
            Map.Entry<String, HashMap<String, Integer>> entry = table.entrySet().iterator().next();
            for (Map.Entry<String, Integer> childPair : entry.getValue().entrySet()) {
                out.println("<td>" + childPair.getKey() + "</td>");
            }
            out.println("</tr>");

            //Data Entry => names and occurence entry
            for (Map.Entry<String, HashMap<String, Integer>> parentPair : table.entrySet()) {
                out.println("<tr>");
                out.println("<td>" + parentPair.getKey() + "</td>");
                for (Map.Entry<String, Integer> childPair : parentPair.getValue().entrySet()) {

                    out.println("<td>" + childPair.getValue() + "</td>");
                }
                out.println("</tr>");
            }
            out.println("</html></body>");
        }
    }
}
