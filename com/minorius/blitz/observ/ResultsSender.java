package com.minorius.blitz.observ;

import com.minorius.blitz.model.TransferableData;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ResultsSender implements Observer {

    private Socket socketForPlayer_1;
    private Socket socketForPlayer_2;

    public ResultsSender(Socket socketForPlayer_1, Socket socketForPlayer_2) {
        this.socketForPlayer_1 = socketForPlayer_1;
        this.socketForPlayer_2 = socketForPlayer_2;
    }

    @Override
    public void update(Observable o, Object arg) {

        //получаем ответы игроков
        int inputAnswerFromPlayer = ((ObservableAnswers) o).getAnswersPlayer1();
        int inputAnswerFromPlayer2 = ((ObservableAnswers) o).getAnswersPlayer2();

        System.out.println("Observer data 1 - "+inputAnswerFromPlayer+", "+socketForPlayer_1);
        System.out.println("Observer data 2 - "+inputAnswerFromPlayer2+", "+socketForPlayer_2);

        //отправить результат противнику
        sendResults(inputAnswerFromPlayer, inputAnswerFromPlayer2, socketForPlayer_1,socketForPlayer_2);

    }

    private void sendResults(int one, int two, Socket socketForPlayer1, Socket socketForPlayer2) {

        TransferableData transferableData;
        try {
            transferableData = new TransferableData();
            transferableData.updateResult = true;
            transferableData.updateView = false;

            transferableData.setResult(two);
            System.out.println(transferableData);
            ObjectOutputStream p = new ObjectOutputStream(socketForPlayer1.getOutputStream());
            p.writeObject(transferableData);
            p.flush();
            System.out.println("Output for "+socketForPlayer1+" - "+two);


            transferableData.setResult(one);
            ObjectOutputStream p1 = new ObjectOutputStream(socketForPlayer2.getOutputStream());
            p1.writeObject(transferableData);
            p1.flush();
            System.out.println("Output for "+socketForPlayer2+" - " +one);
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
