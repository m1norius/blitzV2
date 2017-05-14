package com.minorius.blitz.observ;

import com.minorius.blitz.model.TransferableData;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ObservableAnswers extends Observable {

    private List<TransferableData> listWithQuestionsForSession = new ArrayList<>();

    private int numOfQuestionPlayer1;
    private int numOfQuestionPlayer2;

    private Socket socketPlayer1;
    private Socket socketPlayer2;

    private int answersPlayer1;
    private int answersPlayer2;


    public ObservableAnswers(Socket socketPlayer1, Socket socketPlayer2) {
        this.socketPlayer1 = socketPlayer1;
        this.socketPlayer2 = socketPlayer2;
    }

    public int getNumOfQuestionPlayer1() {
        return numOfQuestionPlayer1;
    }

    public void setNumOfQuestionPlayer1(int numOfQuestionPlayer1) {
        this.numOfQuestionPlayer1 = numOfQuestionPlayer1;
    }

    public int getNumOfQuestionPlayer2() {
        return numOfQuestionPlayer2;
    }

    public void setNumOfQuestionPlayer2(int numOfQuestionPlayer2) {
        this.numOfQuestionPlayer2 = numOfQuestionPlayer2;
    }

    public int getAnswersPlayer1() {
        return answersPlayer1;
    }

    //!!!
    public void setAnswersPlayer1(int answersPlayer1) {
        this.answersPlayer1 = answersPlayer1;
        super.setChanged();
    }

    public int getAnswersPlayer2() {
        return answersPlayer2;
    }

    //!!!
    public void setAnswersPlayer2(int answersPlayer2) {
        this.answersPlayer2 = answersPlayer2;
        super.setChanged();
    }

    public Socket getSocketPlayer1() {
        return socketPlayer1;
    }

    public Socket getSocketPlayer2() {
        return socketPlayer2;
    }

    public List<TransferableData> getListWithQuestionsForSession() {
        return listWithQuestionsForSession;
    }

    public void setListWithQuestionsForSession(List<TransferableData> listWithQuestionsForSession) {
        this.listWithQuestionsForSession = listWithQuestionsForSession;
    }
}
