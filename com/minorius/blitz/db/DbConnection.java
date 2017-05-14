package com.minorius.blitz.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection DbConnection;
    private static Connection connection;

    private DbConnection() {

    }

//    public static DbConnection getInstance(){
//        if(DbConnection == null){
//            DbConnection = new DbConnection();
//        }
//        return DbConnection;
//    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {

        if(connection == null){
            String host = "jdbc:h2:tcp://localhost/~/blitz";
            String username = "";
            String password = "";
            String JDBC_DRIVER = "org.h2.Driver";
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection( host, username, password );
        }
        return connection;
    }
}
