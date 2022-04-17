package com.orhunakkan.dbTests;

import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) throws SQLException {
        
        String connectionUrl = "jdbc:sqlserver://orhunakkan.database.windows.net:1433;database=my-sample-database;user=orhunakkan@orhunakkan;password=o428423o@O;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        DriverManager.getConnection(connectionUrl);
        System.out.println("Successfully Connected");
    }
}
