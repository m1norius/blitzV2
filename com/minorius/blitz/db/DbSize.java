package com.minorius.blitz.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbSize {

    private Statement statement = null;

    public int getNumberOfQuestionsFromDb() throws SQLException {

        int num = 0;

        try {
            Connection connection = DbConnection.getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT ID FROM question");

            while ( resultSet.next() ) {
                int numColumns = resultSet.getMetaData().getColumnCount();
                for ( int i = 1 ; i <= numColumns ; i++ ) {
                    num++;
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            statement.close();
        }
        return num;
    }
}
