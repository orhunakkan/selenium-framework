package com.orhunakkan.utilities;

import java.sql.*;
import java.util.*;

public class DatabaseUtils {

    // declaring at class level so all methods can access

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultset;
    private static ResultSetMetaData metadata;

    // @param url jdbc url for any database
    // @param username username for database
    // @param password password for database

    public static void createConnection(String url, String username, String password) {

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("CONNECTION SUCCESSFUL");
        } catch (Exception e) {
            System.out.println("CONNECTION HAS FAILED " + e.getMessage());
        }
    }

    // Run the sql query provided and set the
    // Statement, ResultSet, ResultSetMetaData object value
    // for all other methods to use
    // this method does not need to return ResultSet object
    // since we already have methods to handle returning values
    // @param sql the query to run

    public static void runQuery(String sql) {

        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultset = statement.executeQuery(sql); // setting the value of ResultSet object
            metadata = resultset.getMetaData(); // setting the value of ResultSetMetaData for reuse
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE RUNNING QUERY " + e.getMessage());
        }
    }

    // destroy method to clean up all the resources after being used
    // WE HAVE TO CHECK IF WE HAVE THE VALID OBJECT FIRST BEFORE CLOSING THE
    // RESOURCE
    // BECAUSE WE CAN NOT TAKE ACTION ON AN OBJECT THAT DOES NOT EXIST

    public static void destroy() {

        try {
            if (resultset != null)
                resultset.close();
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE CLOSING RESOURCES " + e.getMessage());
        }
    }

    // method will reset the cursor to before first location

    private static void resetCursor() {

        try {
            resultset.beforeFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getRowCount() {

        int rowCount = 0;
        try {
            resultset.last();
            rowCount = resultset.getRow();
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE GETTING ROW COUNT " + e.getMessage());
        } finally {
            resetCursor();
        }
        return rowCount;
    }

    public static int getColumnCount() {

        int columnCount = 0;
        try {
            columnCount = metadata.getColumnCount();
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE GETTING COLUMN COUNT " + e.getMessage());
        }
        return columnCount;
    }

    public static List<String> getAllColumnNamesAsList() {

        List<String> columnNameLst = new ArrayList<>();
        try {
            for (int colIndex = 1; colIndex <= getColumnCount(); colIndex++) {
                String columnName = metadata.getColumnName(colIndex);
                columnNameLst.add(columnName);
            }
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getAllColumnNamesAsList " + e.getMessage());
        }
        return columnNameLst;
    }

    public static List<String> getRowDataAsList(int rowNum) {

        List<String> rowDataAsLst = new ArrayList<>();
        int colCount = getColumnCount();
        try {
            resultset.absolute(rowNum);
            for (int colIndex = 1; colIndex <= colCount; colIndex++) {
                String cellValue = resultset.getString(colIndex);
                rowDataAsLst.add(cellValue);
            }
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getRowDataAsList " + e.getMessage());
        } finally {
            resetCursor();
        }
        return rowDataAsLst;
    }

    public static String getCellValue(int rowNum, int columnIndex) {

        String cellValue = "";
        try {
            resultset.absolute(rowNum);
            cellValue = resultset.getString(columnIndex);
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getCellValue " + e.getMessage());
        } finally {
            resetCursor();
        }
        return cellValue;
    }

    public static String getCellValue(int rowNum, String columnName) {

        String cellValue = "";
        try {
            resultset.absolute(rowNum);
            cellValue = resultset.getString(columnName);
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getCellValue " + e.getMessage());
        } finally {
            resetCursor();
        }
        return cellValue;
    }

    public static String getFirstRowFirstColumn() {
        return getCellValue(1, 1);
    }

    public int getFirstCellAsInt() {
        return Integer.parseInt(getCellValue(1, 1));
    }

    public static List<String> getColumnDataAsList(int columnNum) {

        List<String> columnDataLst = new ArrayList<>();
        try {
            resultset.beforeFirst(); // make sure the cursor is at before first location
            while (resultset.next()) {
                String cellValue = resultset.getString(columnNum);
                columnDataLst.add(cellValue);
            }
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getColumnDataAsList " + e.getMessage());
        } finally {
            resetCursor();
        }
        return columnDataLst;
    }

    public static List<String> getColumnDataAsList(String columnName) {

        List<String> columnDataLst = new ArrayList<>();
        try {
            resultset.beforeFirst(); // make sure the cursor is at before first location
            while (resultset.next()) {
                String cellValue = resultset.getString(columnName);
                columnDataLst.add(cellValue);
            }
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getColumnDataAsList " + e.getMessage());
        } finally {
            resetCursor();
        }
        return columnDataLst;
    }

    public static void displayAllData() {

        int columnCount = getColumnCount();
        resetCursor();

        try {
            while (resultset.next()) {
                for (int colIndex = 1; colIndex <= columnCount; colIndex++) {
                    // System.out.print( rs.getString(colIndex) + "\t" );
                    System.out.printf("%-25s", resultset.getString(colIndex));
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE displayAllData " + e.getMessage());
        } finally {
            resetCursor();
        }
    }

    public static Map<String, String> getRowMap(int rowNum) {

        Map<String, String> rowMap = new LinkedHashMap<>();
        int columnCount = getColumnCount();

        try {
            resultset.absolute(rowNum);
            for (int colIndex = 1; colIndex <= columnCount; colIndex++) {
                String columnName = metadata.getColumnName(colIndex);
                String cellValue = resultset.getString(colIndex);
                rowMap.put(columnName, cellValue);
            }
        } catch (Exception e) {
            System.out.println("ERROR OCCURRED WHILE getRowMap " + e.getMessage());
        } finally {
            resetCursor();
        }
        return rowMap;
    }

    public static List<Map<String, String>> getAllRowAsListOfMap() {

        List<Map<String, String>> allRowLstOfMap = new ArrayList<>();
        int rowCount = getRowCount();

        // move from first row till last row
        // get each row as map object and add it to the list

        for (int rowIndex = 1; rowIndex <= rowCount; rowIndex++) {
            Map<String, String> rowMap = getRowMap(rowIndex);
            allRowLstOfMap.add(rowMap);
        }
        resetCursor();
        return allRowLstOfMap;
    }
}