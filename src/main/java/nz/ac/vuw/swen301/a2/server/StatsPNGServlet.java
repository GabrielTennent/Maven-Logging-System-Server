package nz.ac.vuw.swen301.a2.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class StatsPNGServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // the content type is jpg now !!!
        resp.setContentType("image/jpeg");

        // since an image contains bytes and no characters, we must output to a stream now!
        OutputStream outPutStream = resp.getOutputStream();


    }




}
