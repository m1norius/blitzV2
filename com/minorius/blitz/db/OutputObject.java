package com.minorius.blitz.db;

import com.minorius.blitz.Main;
import com.minorius.blitz.model.TransferableData;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class OutputObject {

    private Connection connection;
    private ResultSet resultSet = null;
    private Statement statement = null;

    public TransferableData getOutputObject() throws SQLException {

        generateRandomQuestion();
        TransferableData transferableData = new TransferableData();

        try {
            while ( resultSet.next() ) {
                int numColumns = resultSet.getMetaData().getColumnCount();
                for ( int i = 1 ; i <= numColumns ; i++ ) {
                    transferableData.setVariant1((String) resultSet.getObject(2));
                    transferableData.setVariant2((String) resultSet.getObject(3));
                    transferableData.setVariant3((String) resultSet.getObject(4));
                    transferableData.setVariant4((String) resultSet.getObject(5));

                    transferableData.setAnswer((String) resultSet.getObject(6));
                    transferableData.setQuestion((String) resultSet.getObject(7));

                    transferableData.updateResult = false;
                    transferableData.updateView = true;
                }
            }
        }finally {
            resultSet.getStatement().close();
        }

        return transferableData;
    }

    private ResultSet generateRandomQuestion() throws SQLException {

        Random r = new Random();
        int randomId = r.nextInt(Main.numberOfQuestionsFromDb)+1;
        resultSet = getQuestionById(randomId);

        return resultSet;

    }

    private ResultSet getQuestionById(int id) throws SQLException {

        try {
            String query = "SELECT * FROM question WHERE id =";
            connection = DbConnection.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query+id);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
