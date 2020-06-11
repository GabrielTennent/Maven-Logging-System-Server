package nz.ac.vuw.swen301.a2.server;

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

public class LogsServlet extends HttpServlet {

    private ArrayList list = new ArrayList();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String limitReq = req.getParameter("limit");
        String levelReq = req.getParameter("level");

        JsonObject jsonObject = null;

        try{
        int limit = Integer.parseInt(limitReq);

        for (int i = 0; i < limit; i++){
            if(list.size() >= limit) {
                Object s = list.get(i);
                StringBuffer sb = new StringBuffer();
                String cl = null;
                BufferedReader reader = req.getReader();
                while((cl = reader.readLine()) != null){
                    sb.append(cl);
                }
                jsonObject = JsonParser.parseString(sb.toString()).getAsJsonObject();
            }
        }
        } catch (NumberFormatException x){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        resp.setContentType("text/html");
        //resp.setContentType("plain/text");
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("Search Query: <br>"
                + "limit: " + limitReq + "<br>"
                + "level: " + levelReq + "<br>");
        //out.println("<br> Matching Logs: " + jsonObject.toString());
        out.println("</body>");
        out.println("</html>");
        //out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        list.add(req.getReader());
    }
}
