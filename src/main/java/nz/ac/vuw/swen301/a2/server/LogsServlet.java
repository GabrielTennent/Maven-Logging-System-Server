package nz.ac.vuw.swen301.a2.server;

import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.stream.Collectors;

public class LogsServlet extends HttpServlet {

    private ArrayList<JSONLayout> list = new ArrayList<JSONLayout>();
    private ArrayList<String> LEVELS = new ArrayList<>(Arrays.asList("ALL","TRACE","DEBUG","INFO","WARN","ERROR","FATAL","OFF"));

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        String limitReq = req.getParameter("limit");
        String levelReq = req.getParameter("level");

        PrintWriter out = resp.getWriter();
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

        list.sort((d1,d2) -> d2.getTimestamp().compareTo(d1.getTimestamp()));

        try {
            int limit = Integer.parseInt(limitReq), count = 0;
            if (limit < 1 || limit > 42){
                resp.sendError(400);
                return;
            }
            List results = new ArrayList<JSONLayout>();

            if(levelReq.equals("ALL")){ //If the user wants to display logs of all levels
                if(limit < list.size()) results = list.subList(0, limit);
                else results = list.subList(0, list.size());
            } else { //Searching for a specific log level
                while(results.size() <= limit){
                    if(count < list.size()){
                        if(list.get(count).getLevel().equals(levelReq)){ //Checking level
                            results.add(list.get(count));
                        }
                    } else break;
                    count++;
                }
            }
            //Printing of the list
            out.println(gson.toJson(results));
        } catch (NumberFormatException x) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Formatting and creation of JSON object
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        //Check format of the string before you create the JSON object
        try {
            String request = (req.getReader().lines().collect(Collectors.joining()));
            JSONLayout jsonObject = gson.fromJson(request, JSONLayout.class);

            if(validUUID(jsonObject.getId())){
                if(validDate(jsonObject.getTimestamp())){
                    if(LEVELS.contains(jsonObject.getLevel())){
                        list.add(jsonObject);
                    }
                }
            }
            //Add json class from assignment 1 then check input is correct:
            //Date format and ID format matches the structure in the swagger hub and strings
        } catch (IllegalFormatException x) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        //Printing
        PrintWriter out = resp.getWriter();
        out.println(gson.toJson(list));
    }

    public boolean validUUID(String uuid) {
        return uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
    }

    public boolean validDate(String string) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setLenient(false);
        try {
            format.parse(string);
        } catch (ParseException x){
            System.out.println(x.getStackTrace());
            return false;
        }
        return true;
    }
}
