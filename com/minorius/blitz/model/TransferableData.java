package com.minorius.blitz.model;

import java.io.Serializable;

public class TransferableData implements Serializable {

    private static final long serialVersionUID = 4788632469733847506L;

    public boolean updateView;
    public boolean updateResult;
    public boolean over;
    public boolean check;

    private int result;

    private String question;
    private String answer;
    private String variant1;
    private String variant2;
    private String variant3;
    private String variant4;

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public boolean isUpdateView() {
        return updateView;
    }

    public boolean isUpdateResult() {
        return updateResult;
    }

    public void setUpdateView(boolean updateView) {
        this.updateView = updateView;
    }


    public void setUpdateResult(boolean updateResult) {
        this.updateResult = updateResult;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getVariant1() {
        return variant1;
    }

    public void setVariant1(String variant1) {
        this.variant1 = variant1;
    }

    public String getVariant2() {
        return variant2;
    }

    public void setVariant2(String variant2) {
        this.variant2 = variant2;
    }

    public String getVariant3() {
        return variant3;
    }

    public void setVariant3(String variant3) {
        this.variant3 = variant3;
    }

    public String getVariant4() {
        return variant4;
    }

    public void setVariant4(String variant4) {
        this.variant4 = variant4;
    }


    @Override
    public String toString() {
        return "TransferableData{" +
                "updateView=" + updateView +
                ", updateResult=" + updateResult +
                ", over=" + over +
                ", check=" + check +
                ", result=" + result +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", variant1='" + variant1 + '\'' +
                ", variant2='" + variant2 + '\'' +
                ", variant3='" + variant3 + '\'' +
                ", variant4='" + variant4 + '\'' +
                '}';
    }
}
