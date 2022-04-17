package com.orhunakkan.dbTests;

import java.sql.*;

public class LoopThroughResultSet {

    public static void main(String[] args) {

        String connectionUrl = "jdbc:sqlserver://orhunakkan.database.windows.net:1433;database=my-sample-database;user=orhunakkan@orhunakkan;password=o428423o@O;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        System.out.println("Successfully Connected");

        try {
            Connection connection = DriverManager.getConnection(connectionUrl);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from [SalesLT].[Customer]");
            while (resultSet.next()) {
                System.out.println("rs.getString(\"LastName\") = " + resultSet.getString("LastName"));
            }
        } catch (SQLException e) {
            System.out.println("Exception occurred " + e.getMessage());
            e.printStackTrace();
        }
    }
}