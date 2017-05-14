package com.minorius.blitz.game;

import com.minorius.blitz.model.TransferableData;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class QuestionSender {

    private Socket socketForPlayer;

    public QuestionSender(Socket socketForPlayer) {
        this.socketForPlayer = socketForPlayer;
    }

    public void sendNewQuestion(TransferableData question){
        try{

            System.out.println("***************************");
            System.out.println("out"+question+" - "+socketForPlayer);

            ObjectOutputStream p = new ObjectOutputStream(socketForPlayer.getOutputStream());
            p.writeObject(question);
            p.flush();

        }catch (Exception e){
            System.out.println("Не удалось отправить вопрос - QuestionSender.class");
        }
    }
}
