package nz.ac.vuw.swen301.a2.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LogsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        //String userAgentHeader = req.getHeader( "User-Agent");

        resp.setContentType("application/json");
        String limitReq = req.getParameter("limit");
        String levelReq = req.getParameter("level");

        //String request = req.toString();

//        try{
//        int limit = Integer.parseInt(limitReq);
//
//        for (int i = 0; i < limit; i++){
//            if(list.size() >= limit) {
//                Object s = list.get(i);
//            }
//        }
//        } catch (NumberFormatException x){
//            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
//            return;
//        }

        resp.setContentType("text/html");
        //resp.setContentType("plain/text");
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("Search Query: <br>"
                + "limit: " + limitReq + "<br>"
                + "level: " + levelReq);
        //out.println("");
        //Print line for displaying on the screen
        out.println("</body>");
        out.println("</html>");
        //out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
