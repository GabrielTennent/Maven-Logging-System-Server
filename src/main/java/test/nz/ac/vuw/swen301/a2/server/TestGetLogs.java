package test.nz.ac.vuw.swen301.a2.server;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import nz.ac.vuw.swen301.a2.server.JSONLayout;
import nz.ac.vuw.swen301.a2.server.LogsServlet;
import org.junit.Test;

import junit.framework.TestCase;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;


public class TestGetLogs extends TestCase {

    private ArrayList<String> LEVELS = new ArrayList<>(Arrays.asList("ALL","TRACE","DEBUG","INFO","WARN","ERROR","FATAL","OFF"));

    //different values - return size - logging levels
    @Test
    public void testInValidLimitParameterToSmall() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("limit", "-5");
        request.setParameter("level", "ALL");
        LogsServlet service = new LogsServlet();
        ArrayList<JSONLayout> list = generateList(1);
        service.setList(list);
        service.doGet(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInValidLimitParameterToBig() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("limit", "80");
        request.setParameter("level", "ALL");
        LogsServlet service = new LogsServlet();
        ArrayList<JSONLayout> list = generateList(1);
        service.setList(list);
        service.doGet(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInValidLevelParameter() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("limit", "2");
        request.setParameter("level", "invalidlol");
        LogsServlet service = new LogsServlet();
        ArrayList<JSONLayout> list = generateList(1);
        service.setList(list);
        service.doGet(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInValidParameterNames() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("invalidlol", "2");
        request.setParameter("level", "ALL");
        LogsServlet service = new LogsServlet();
        ArrayList<JSONLayout> list = generateList(1);
        service.setList(list);
        service.doGet(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testInValidParameterNames2() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("limit", "2");
        request.setParameter("invalidlol", "ALL");
        LogsServlet service = new LogsServlet();
        ArrayList<JSONLayout> list = generateList(1);
        service.setList(list);
        service.doGet(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testMissingParameters() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        LogsServlet service = new LogsServlet();
        ArrayList<JSONLayout> list = generateList(1);
        service.setList(list);
        service.doGet(request, response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testValidRequestResponse() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("limit", "1");
        request.setParameter("level", "ALL");
        LogsServlet service = new LogsServlet();
        ArrayList<JSONLayout> list = generateList(1);
        service.setList(list);
        service.doGet(request, response);
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
        assertEquals(gson.toJson(list), response.getContentAsString().trim());
    }

    @Test
    public void testValidManyRequestResponse() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("limit", "10");
        request.setParameter("level", "ALL");
        LogsServlet service = new LogsServlet();
        ArrayList<JSONLayout> list = generateList(10);
        service.setList(list);
        service.doGet(request, response);
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
        assertEquals(gson.toJson(list), response.getContentAsString().trim());
    }

    @Test
    public void testValidLimitLargerThanSize() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setParameter("limit", "20");
        request.setParameter("level", "ALL");
        LogsServlet service = new LogsServlet();
        ArrayList<JSONLayout> list = generateList(10);
        service.setList(list);
        service.doGet(request, response);
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
        assertEquals(gson.toJson(list), response.getContentAsString().trim());
    }

    public ArrayList<JSONLayout> generateList(int size){
        ArrayList<JSONLayout> list = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            JSONLayout json1 = new JSONLayout(
                    UUID.randomUUID().toString(),
                    "application started",
                    "2019-07-29T09:12:33.001Z",
                    "main",
                    "com.example.Foo",
                    "WARN",
                    "string"
            );
            list.add(json1);
        }
        return list;
    }

}
