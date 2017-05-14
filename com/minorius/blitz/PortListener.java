package com.minorius.blitz;

import com.minorius.blitz.model.TransferableData;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static com.minorius.blitz.Main.poolWithSockets;

public class PortListener {

    public static void portListenerOn(){

        System.out.println("Start");

        int PORT = 1701;

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Waiting...");

            while(true){
                System.out.println("********************************");
                System.out.println("Threads"+Thread.activeCount());
                System.out.println("Ready");
                System.out.println("IN MAIN"+Main.poolWithSockets+". Sessions - "+Main.sessionsOfGame);
                Socket socket = serverSocket.accept();
                System.out.println("Connecting...");
                Session.addSocketToPool(socket);


                //*******************

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ObjectOutputStream p1 = null;
                            TransferableData t = new TransferableData();
                            t.check = true;
                            try {
                                p1 = new ObjectOutputStream(socket.getOutputStream());
                                p1.writeObject(t);
                                p1.flush();
                                System.out.println(socket+"Checked +");

                            } catch (Exception e) {
                                System.out.println("111");
                                return;
                            }
                        }
                    }
                });

                //thread.start();
            }

        } catch(Exception e) {
            System.out.println("PortListener exception " +e);
        }
    }
}
