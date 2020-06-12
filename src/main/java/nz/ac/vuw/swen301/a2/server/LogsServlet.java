package nz.ac.vuw.swen301.a2.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class LogsServlet extends HttpServlet {

    private ArrayList<JSONLayout> list = new ArrayList<JSONLayout>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        String limitReq = req.getParameter("limit");
        String levelReq = req.getParameter("level");

        JSONLayout obj = null;
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

        //Implements checks for parameters

        try {
            int limit = Integer.parseInt(limitReq);
            if (limit < 1 || limit > 42) resp.sendError(400);
            out.println(gson.toJson(list));

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
        JSONLayout jsonObject = gson.fromJson((req.getReader().lines().collect(Collectors.joining())), JSONLayout.class);
        //Add json class from assignment 1 then check input is correct:
        //Date format and ID format matches the structure in the swagger hub and strings
        list.add(jsonObject);

        //Printing
        PrintWriter out = resp.getWriter();
        out.println(gson.toJson(list));
    }
}
