package com.minorius.blitz;

import com.minorius.blitz.game.GameManager;
import com.minorius.blitz.model.TransferableData;
import com.minorius.blitz.observ.ObservableAnswers;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import static com.minorius.blitz.Main.poolWithSockets;

public class Session {

    private static Socket mySocket = null;

    public static void addSocketToPool(Socket socket) throws SQLException {
        mySocket = socket;
        poolWithSockets.add(socket);
        System.out.println(poolWithSockets);
        System.out.println("Pool size "+ poolWithSockets.size());

        int numPlayersForCreatingSession = 2;
        if (poolWithSockets.size() >= numPlayersForCreatingSession){
            checkPoolWithSockets();
        }
    }
    private static void checkPoolWithSockets() throws SQLException {

        deleteRepeatableSocketsFromPool();

        if (poolWithSockets.size() >= 2){
            createSession(poolWithSockets);
        }
    }

    private static void deleteRepeatableSocketsFromPool(){
        for (int i = poolWithSockets.size(); i >= 1; i--){
            if(poolWithSockets.get(i-1).getInetAddress()
                    .equals(mySocket.getInetAddress()) && !poolWithSockets.get(i-1).equals(mySocket)){
                poolWithSockets.remove(poolWithSockets.get(i-1));
            }
        }
        System.out.println("size after cleaning "+poolWithSockets.size());
    }

    private static void createSession(List<Socket> poolWithSockets) throws SQLException {
        int positionInPoolFirstPlayer = 0;
        int positionInPoolSecondPlayer = 1;
        int sessionID = new Random().nextInt(10000);
        System.out.println("Creating session");
        Main.sessionsOfGame.put(sessionID, new ObservableAnswers(poolWithSockets.get(positionInPoolFirstPlayer), poolWithSockets.get(positionInPoolSecondPlayer)));
        removeSocketsFromPoolAfterCreatingSession(poolWithSockets);
        //!!!
        new GameManager(sessionID).start();
    }

    private static void removeSocketsFromPoolAfterCreatingSession(List<Socket> poolWithSockets){
        poolWithSockets.remove(0);
        poolWithSockets.remove(0);
        System.out.println("Deleting");
        System.out.println("Pool size "+poolWithSockets.size());
    }
}
