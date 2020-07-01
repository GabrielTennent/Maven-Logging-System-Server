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
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class LogsServlet extends HttpServlet {

    private static ArrayList<JSONLayout> list = new ArrayList<JSONLayout>();
    private static final ArrayList<String> LEVELS = new ArrayList<>(Arrays.asList("ALL","TRACE","DEBUG","INFO","WARN","ERROR","FATAL","OFF"));

    //Default constructor
    public LogsServlet(){

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        String limitReq = "";
        String levelReq = "";

        PrintWriter out = resp.getWriter();
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

        list.sort((d1,d2) -> d2.getTimestamp().compareTo(d1.getTimestamp()));

        try {
            if (req.getParameter("limit") == null) {
                throw new IOException("Invalid/No limit parameter provided");
            } else if(req.getParameter("level") == null){
                throw new IOException("Invalid/No level parameter provided");
            }else {
                limitReq = req.getParameter("limit");
                levelReq = req.getParameter("level");
            }

            int limit = Integer.parseInt(limitReq), count = 0;
            if (limit < 1 || limit > 42){
                throw new IOException("Invalid limit value");
            }

            boolean levelValid = false;
            for(String levelCheck: LEVELS){
                if(levelCheck.equals(levelReq)) levelValid = true;
            }
            if(!levelValid){
                throw new IOException("Invalid logger level required");
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
        } catch (IOException x) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Formatting and creation of JSON object
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        //Check format of the string before you create the JSON object
        try {
            if(req.getReader()!=null) {

                String request = (req.getReader().lines().collect(Collectors.joining()));
                JSONLayout jsonObject = gson.fromJson(request, JSONLayout.class);

                if (validUUID(jsonObject.getId())) {
                    if (validDate(jsonObject.getTimestamp())) {
                        if (LEVELS.contains(jsonObject.getLevel())) {
                            if(uniqueUUID(jsonObject.getId())) {
                                list.add(jsonObject);
                            } else {
                                throw new IOException("Duplicate ID from JSON input");
                            }
                        } else {
                            throw new IOException("Illegal Level type from JSON input");
                        }
                    } else {
                        throw new IOException("Illegal date format from JSON input");
                    }
                } else {
                    throw new IOException("Illegal ID format from JSON input");
                }
            }
        } catch (IOException x) {
            System.out.println(x.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        //Printing
        PrintWriter out = resp.getWriter();
        out.println(gson.toJson(list));
    }

    public static HashMap<String, HashMap<String,Integer>> getTable(){
        HashMap<String, HashMap<String,Integer>> table = new HashMap<>();

        list.sort((d1,d2) -> d2.getTimestamp().compareTo(d1.getTimestamp()));

        List<String> keys = list
                .stream()
                .map(logEvent -> logEvent.getLogger()).collect(Collectors.toList());
        keys.addAll(list
                .stream()
                .map(logEvent -> logEvent.getLevel()).collect(Collectors.toList()));
        keys.addAll(list
                .stream()
                .map(logEvent -> logEvent.getThread()).collect(Collectors.toList()));
        List<String> datesToAdd = list
                .stream()
                .map(logEvent -> logEvent.getTimestamp().substring(0,10)).collect(Collectors.toList());
        for(String key : keys){
            HashMap<String,Integer> dates = new HashMap<>();
            for(String date : datesToAdd){
                dates.put(date,0);
            }
            table.put(key,dates);
        }

        for(JSONLayout log : list){
            String date = log.getTimestamp().substring(0,10);
            String logger = log.getLogger();
            String level = log.getLevel();
            String thread = log.getThread();
            table.get(logger).put(date,table.get(logger).get(date)+1);
            table.get(level).put(date,table.get(level).get(date)+1);
            table.get(thread).put(date,table.get(thread).get(date)+1);
        }
        System.out.print(table + "\n");
        return table;
    }

    public boolean validUUID(String uuid) {
        return uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
    }

    public boolean uniqueUUID(String uuid) {
        for(JSONLayout jsonLayout : list){
            if(jsonLayout.getId().equals(uuid)) return false;
        }
        return true;
    }

    public boolean validDate(String string) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setLenient(false);
        try {
            format.parse(string);
        } catch (ParseException x){
            System.out.println(x.getMessage());
            return false;
        }
        return true;
    }

    public ArrayList<JSONLayout> getList(){
        return this.list;
    }

    public void setList(ArrayList<JSONLayout> list){
        this.list = list;
    }
}
