package com.example.geoquiz.model;/* Created by Alexandre Labreveux */

import java.util.List;

public class ImgQuestion {
    private String mImgQuestion;
    private int mAnswerIndex;
    private int mImage;
    private List<String> mChoiceList;

    public ImgQuestion(String imgQuestion, int image, List<String> choiceList, int answerIndex) {
        mAnswerIndex = answerIndex;
        this.setImgQuestion(imgQuestion);
        this.setImage(image);
        this.setChoiceList(choiceList);
        this.setAnswerIndex(answerIndex);
    }

    public String getImgQuestion() {
        return mImgQuestion;
    }

    public void setImgQuestion(String question) {
        mImgQuestion = question;
    }

    public List<String> getChoiceList() {
        return mChoiceList;
    }

    public void setChoiceList(List<String> choiceList) {
        if (choiceList == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }

        mChoiceList = choiceList;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int image) {
        mImage = image;
    }

    public int getAnswerIndex() {
        return mAnswerIndex;
    }

    public void setAnswerIndex(int answerIndex) {
        if (answerIndex < 0 || answerIndex >= mChoiceList.size()) {
            throw new IllegalArgumentException("Answer index is out of bound");
        }

    }

    @Override
    public String toString() {
        return "ImgQuestion{" +
                "mImgQuestion='" + mImgQuestion + '\'' +
                ", mImage=" + mImage +
                ", mChoiceList=" + mChoiceList +
                ", mAnswerIndex=" + mAnswerIndex +
                '}';
    }

}

