package test.nz.ac.vuw.swen301.a2.server;

import com.google.gson.JsonObject;
import nz.ac.vuw.swen301.a2.server.JSONLayout;
import nz.ac.vuw.swen301.a2.server.LogsServlet;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestPostLogs {

    @Test
    public void testInValidDate() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        JsonObject jsonObject = new JsonObject() ;
        jsonObject.addProperty("id","d290f1ee-6c54-4b01-90e6-d701748f0851");
        jsonObject.addProperty("message","application started");
        jsonObject.addProperty("timestamp","{}");
        jsonObject.addProperty("thread","main");
        jsonObject.addProperty("logger","com.example.Foo");
        jsonObject.addProperty("level","WARN");
        jsonObject.addProperty("errorDetails","string");
        request.setContent(jsonObject.toString().getBytes("UTF-8"));
        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInValidLevel() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        JsonObject jsonObject = new JsonObject() ;
        jsonObject.addProperty("id","d290f1ee-6c54-4b01-90e6-d701748f0851");
        jsonObject.addProperty("message","application started");
        jsonObject.addProperty("timestamp","2019-07-29T09:12:33.001Z");
        jsonObject.addProperty("thread","main");
        jsonObject.addProperty("logger","com.example.Foo");
        jsonObject.addProperty("level","LESHGO");
        jsonObject.addProperty("errorDetails","string");
        request.setContent(jsonObject.toString().getBytes("UTF-8"));
        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInValidID() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        JsonObject jsonObject = new JsonObject() ;
        jsonObject.addProperty("id","omegalul");
        jsonObject.addProperty("message","application started");
        jsonObject.addProperty("timestamp","2019-07-29T09:12:33.001Z");
        jsonObject.addProperty("thread","main");
        jsonObject.addProperty("logger","com.example.Foo");
        jsonObject.addProperty("level","WARN");
        jsonObject.addProperty("errorDetails","string");
        request.setContent(jsonObject.toString().getBytes("UTF-8"));
        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testDuplicateIDResponse() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        JsonObject jsonObject = new JsonObject() ;
        jsonObject.addProperty("id","d290f1ee-6c54-4b01-90e6-d701748f0851");
        jsonObject.addProperty("message","application started");
        jsonObject.addProperty("timestamp","2019-07-29T09:12:33.001Z");
        jsonObject.addProperty("thread","main");
        jsonObject.addProperty("logger","com.example.Foo");
        jsonObject.addProperty("level","WARN");
        jsonObject.addProperty("errorDetails","string");
        request.setContent(jsonObject.toString().getBytes("UTF-8"));
        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        service.doPost(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testValidRequest() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id","d290f1ee-6c54-4b01-90e6-d701748f0851");
        jsonObject.addProperty("message","application started");
        jsonObject.addProperty("timestamp","2019-07-29T09:12:33.001Z");
        jsonObject.addProperty("thread","main");
        jsonObject.addProperty("logger","com.example.Foo");
        jsonObject.addProperty("level","WARN");
        jsonObject.addProperty("errorDetails","string");
        request.setContent(jsonObject.toString().getBytes("UTF-8"));
        LogsServlet service = new LogsServlet();
        service.doPost(request, response);

        ArrayList<JSONLayout> output = service.getList();
        JSONLayout outputObject = new JSONLayout("Object has not reach servlet!");
        if(output.size() == 1){
            outputObject = output.get(0);
        }
        assertEquals(outputObject.getId(),jsonObject.get("id").toString().replace("\"",""));
    }

    @Test
    public void testValidContentType() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        assertEquals("application/json", response.getContentType());
    }

    @Test
    public void testValidCharacterEncoding() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        LogsServlet service = new LogsServlet();
        service.doPost(request, response);
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    //Array compare test?

}
