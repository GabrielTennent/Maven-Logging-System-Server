package nz.ac.vuw.swen301.a2.server;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.POIDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.IOUtils;

public class StatsXLSServlet extends HttpServlet {

    public StatsXLSServlet(){

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HashMap<String, HashMap<String,Integer>> table = LogsServlet.getTable();
        if(!table.isEmpty()) {
            resp.setContentType("application/vnd.ms-excel");
            resp.setHeader("Content-Disposition","stats.xls");

            HSSFWorkbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet("Java Books");

            int rowCount = 0;
            Row row = sheet.createRow(0);

            int columnCount = 0;
            Map.Entry<String, HashMap<String, Integer>> entry = table.entrySet().iterator().next();
            Cell cell = row.createCell(++columnCount);
            cell.setCellValue("name");
            for (Map.Entry<String, Integer> childPair : entry.getValue().entrySet()) {
                cell = row.createCell(++columnCount);
                cell.setCellValue(childPair.getKey());
            }

            for (Map.Entry<String, HashMap<String, Integer>> parentPair : table.entrySet()) {
                columnCount = 0;
                row = sheet.createRow(++rowCount);
                cell = row.createCell(++columnCount);
                cell.setCellValue(parentPair.getKey());
                for (Map.Entry<String, Integer> childPair : parentPair.getValue().entrySet()) {
                    cell=row.createCell(++columnCount);
                    cell.setCellValue(childPair.getValue());
                }
            }

            workbook.write(resp.getOutputStream());
            workbook.close();
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }
}
