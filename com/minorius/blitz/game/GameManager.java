package com.minorius.blitz.game;

import com.minorius.blitz.Main;
import com.minorius.blitz.db.OutputObject;
import com.minorius.blitz.model.TransferableData;
import com.minorius.blitz.observ.ResultsSender;
import com.minorius.blitz.observ.ObservableAnswers;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;

public class GameManager {

    private Socket socketForPlayer_1;
    private Socket socketForPlayer_2;

    private int id;

    public GameManager(int id) {
        this.id = id;
    }

    public void start() throws SQLException {

        //получаем объект по ид в котором хранятся 2 игрока которые были созданы в Session
        ObservableAnswers observableAnswers = Main.sessionsOfGame.get(id);
        //получаем сокеты этих игроков
        socketForPlayer_1 = observableAnswers.getSocketPlayer1();
        socketForPlayer_2 = observableAnswers.getSocketPlayer2();
        //******************

        sendCheckedMessage();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //отправить первый объект с вопросами
        if(Main.sessionsOfGame.containsKey(id)){
            System.out.println(Main.sessionsOfGame);
            //создаем для каждого пользователя отдельный поток для возможности независимого общения ответами
            //и передаем сокет и ид сессии для индентификации того кому передавать ответы
            Thread inputThreadForPlayer1 = new Thread(new CommunicationThreadsImpRunnable(socketForPlayer_1, id));
            inputThreadForPlayer1.start();

            Thread inputThreadForPlayer2 = new Thread(new CommunicationThreadsImpRunnable(socketForPlayer_2, id));
            inputThreadForPlayer2.start();
            //******************

            //создаем наблюдателя который будет фиксировать когда противники одной сессии дают ответы
            ResultsSender resultsSender = new ResultsSender(socketForPlayer_1, socketForPlayer_2);
            //получаем объект с игроками по ид
            //и вешам на него наблюдатель(так как этот объект наследуется от обсервебл)
            Main.sessionsOfGame.get(id).addObserver(resultsSender);
            sendFirstQuestionToPhone();
        }else {
            System.out.println("socket 1 "+socketForPlayer_1);
            System.out.println("socket 2 "+socketForPlayer_2);
            System.out.println("кинуть сокет опять в пул");
            if (socketForPlayer_1 != null){
                Main.poolWithSockets.add(socketForPlayer_1);
            }else if(socketForPlayer_2 != null) {
                Main.poolWithSockets.add(socketForPlayer_2);
            }else {
                System.out.println("hz");
            }
        }
    }

    private void sendCheckedMessage(){
        TransferableData transferableData = new TransferableData();
        transferableData.check = true;

        Thread first = new Thread(() -> {
            try {
                for (int i = 0; i<3; i++){
                    ObjectOutputStream p1 = null;
                    p1 = new ObjectOutputStream(socketForPlayer_1.getOutputStream());
                    p1.writeObject(transferableData);
                    p1.flush();
                    System.out.println("check send");
                    Thread.sleep(500);
                }


            }catch (Exception e){
                System.out.println("Игрок отсутствует - GameManager.class/sendCheckedMessage "+socketForPlayer_1);
                try {
                    if(Main.sessionsOfGame.containsKey(id)){
                        Main.sessionsOfGame.remove(id);
                    }
                    socketForPlayer_1.close();
                    socketForPlayer_1 = null;

                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });

        Thread second = new Thread(() -> {
            try {
                for (int i = 0; i<3; i++){
                    ObjectOutputStream p2 = null;
                    p2 = new ObjectOutputStream(socketForPlayer_2.getOutputStream());
                    p2.writeObject(transferableData);
                    p2.flush();
                    System.out.println("check send");
                    Thread.sleep(500);
                }

            }catch (Exception e){
                System.out.println("Игрок отсутствует - GameManager.class/sendCheckedMessage "+socketForPlayer_2);
                try {
                    if(Main.sessionsOfGame.containsKey(id)){
                        Main.sessionsOfGame.remove(id);
                    }
                    socketForPlayer_2.close();
                    socketForPlayer_2 = null;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });

        second.start();
        first.start();

        try {
            first.join();
            second.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    private void sendFirstQuestionToPhone() throws SQLException {

        addToListNewQuestion();

        try {
            ObjectOutputStream p1 = new ObjectOutputStream(socketForPlayer_1.getOutputStream());
            p1.writeObject(Main.sessionsOfGame.get(id).getListWithQuestionsForSession().get(0));
            p1.flush();
        }catch (Exception e){
            System.out.println("Не удалось отправить первый вопрос - GameManager.class "+socketForPlayer_1);
        }

        try {
            ObjectOutputStream p2 = new ObjectOutputStream(socketForPlayer_2.getOutputStream());
            p2.writeObject(Main.sessionsOfGame.get(id).getListWithQuestionsForSession().get(0));
            p2.flush();
        }catch (Exception e){
            System.out.println("Не удалось отправить первый вопрос - GameManager.class"+socketForPlayer_2);
        }
    }



    private void addToListNewQuestion() throws SQLException {
        OutputObject outputObject = new OutputObject();
        TransferableData transferableData = outputObject.getOutputObject();
        Main.sessionsOfGame.get(id).getListWithQuestionsForSession().add(transferableData);
    }

    private class CommunicationThreadsImpRunnable implements Runnable{

        private Socket socket;
        private int id;
        private TransferableData result;

        CommunicationThreadsImpRunnable(Socket socket, Integer id) {
            this.socket = socket;
            this.id = id;
        }

        @Override
        public void run() {

            while (true){

                try {
                    InputStream inputStreamPlayer = socket.getInputStream();
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStreamPlayer);

                    result = (TransferableData) objectInputStream.readObject();
                    System.out.println("input "+result);
                    //зафиксировать ответ от игрока
                    setAnswerFromPlayer(result.getResult());
                    //проинформировать обсервер о том что результат игрока изменился
                    notifyInputObserver();
                    System.out.println(id+" - "+Main.sessionsOfGame.get(id).getAnswersPlayer1()+"; "+Main.sessionsOfGame.get(id).getAnswersPlayer2());


                }catch (ClassCastException e){
                    System.out.println("Check class cast exception "+e+", - "+socket);
                    closeSocket();
                    if(Main.sessionsOfGame.containsKey(id)){
                        Main.sessionsOfGame.remove(id);
                    }
                    return;
                }catch (EOFException e){
                    System.out.println("! Check EOF exception "+e+", - "+socket);

                    if (Main.sessionsOfGame.get(id)!=null && Main.sessionsOfGame.get(id)!=null){
                        if(!Main.sessionsOfGame.get(id).getSocketPlayer1().equals(socket)){
                            //отправить getSocketPlayer1 обьект противнику + овер = тру
                            QuestionSender sender = new QuestionSender(Main.sessionsOfGame.get(id).getSocketPlayer1());
                            TransferableData gameOver = new TransferableData();
                            gameOver.over = true;
                            sender.sendNewQuestion(gameOver);
                        }else {
                            QuestionSender sender = new QuestionSender(Main.sessionsOfGame.get(id).getSocketPlayer2());
                            TransferableData gameOver = new TransferableData();
                            gameOver.over = true;
                            sender.sendNewQuestion(gameOver);
                        }
                    }

                    if(Main.sessionsOfGame.containsKey(id)){
                        Main.sessionsOfGame.remove(id);
                    }
                    closeSocket();
                    return;
                }catch (SocketException e){
                    System.out.println("! Check SocketException "+e+", - "+socket);
                    if(Main.sessionsOfGame.containsKey(id)){
                        Main.sessionsOfGame.remove(id);
                    }
                    closeSocket();
                    return;
                }catch (Exception e){
                    System.out.println("Exception 1 "+e+", - "+socket);
                }
            }
        }

        private void setAnswerFromPlayer(int buffer) throws SQLException {
            if(isFirstPlayer()){

                //пишем результат в ObservableAnswers
                Main.sessionsOfGame.get(id).setAnswersPlayer1(buffer);
                //получаем текущий номер вопроса
                int numOfQuestionForFirstPlayer = Main.sessionsOfGame.get(id).getNumOfQuestionPlayer1();
                numOfQuestionForFirstPlayer++;
                //получаем размер листа
                int sizeOfList = Main.sessionsOfGame.get(id).getListWithQuestionsForSession().size();
                //если количество ответов игрока меньше чем количество ответов дргугого игрока,
                // то берем уже созданый вопрос
                if(numOfQuestionForFirstPlayer < sizeOfList){
                    TransferableData question = Main.sessionsOfGame.get(id).getListWithQuestionsForSession().get(numOfQuestionForFirstPlayer);
                    new QuestionSender(Main.sessionsOfGame.get(id).getSocketPlayer1()).sendNewQuestion(question);
                    //иначе создаем новый вопрос
                }else {
                    addToListNewQuestion();
                    TransferableData question = Main.sessionsOfGame.get(id).getListWithQuestionsForSession().get(numOfQuestionForFirstPlayer);
                    new QuestionSender(Main.sessionsOfGame.get(id).getSocketPlayer1()).sendNewQuestion(question);
                }

                //плюсуем 1, для отправки следующего вопроса в коллекции
                Main.sessionsOfGame.get(id).setNumOfQuestionPlayer1(numOfQuestionForFirstPlayer);
                //отправить новый вопрос тому кто прислал ответ
            } else {
                System.out.println(Main.sessionsOfGame.get(id).getSocketPlayer1().isConnected());
                System.out.println(Main.sessionsOfGame.get(id).getSocketPlayer2().isConnected());
                Main.sessionsOfGame.get(id).setAnswersPlayer2(buffer);
                int numOfQuestionForSecondPlayer = Main.sessionsOfGame.get(id).getNumOfQuestionPlayer2();
                numOfQuestionForSecondPlayer++;

                int sizeOfList = Main.sessionsOfGame.get(id).getListWithQuestionsForSession().size();
                if(numOfQuestionForSecondPlayer < sizeOfList){
                    TransferableData question = Main.sessionsOfGame.get(id).getListWithQuestionsForSession().get(numOfQuestionForSecondPlayer);
                    new QuestionSender(Main.sessionsOfGame.get(id).getSocketPlayer2()).sendNewQuestion(question);
                }else {
                    addToListNewQuestion();
                    TransferableData question = Main.sessionsOfGame.get(id).getListWithQuestionsForSession().get(numOfQuestionForSecondPlayer);
                    new QuestionSender(Main.sessionsOfGame.get(id).getSocketPlayer2()).sendNewQuestion(question);
                }

                Main.sessionsOfGame.get(id).setNumOfQuestionPlayer2(numOfQuestionForSecondPlayer);
            }

            System.out.println("Q1 "+Main.sessionsOfGame.get(id).getNumOfQuestionPlayer1());
            System.out.println("Q2 "+Main.sessionsOfGame.get(id).getNumOfQuestionPlayer2());
        }

        private Boolean isFirstPlayer(){
//            InetAddress myIP = socket.getInetAddress();
//            InetAddress checkedIP = Main.sessionsOfGame.get(id).getSocketPlayer1().getInetAddress();
//            return checkedIP.equals(myIP);

            Socket mySocket = socket;
            Socket checkedSocked = Main.sessionsOfGame.get(id).getSocketPlayer1();
            return checkedSocked.equals(mySocket);
        }

        private void notifyInputObserver(){
            Main.sessionsOfGame.get(id).notifyObservers();
        }

        private void closeSocket(){
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
                System.out.println("Socket close"+socket);
            } catch (IOException e1) {
                System.out.println("Error in closing socket ");
                e1.printStackTrace();
            }
        }
    }
}
