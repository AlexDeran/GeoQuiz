package com.example.geoquiz.model;/* Created by Alexandre Labreveux */

import java.util.Collections;
import java.util.List;

public class ImgBank {

    private List<ImgQuestion> mImgQuestionList;
    private int mNextImgQuestionIndex;

    public ImgBank(List<ImgQuestion> imgQuestionList) {
        mImgQuestionList = imgQuestionList;

        // Shuffle the question list
        Collections.shuffle(mImgQuestionList);

        mNextImgQuestionIndex = 0;
    }

    public ImgQuestion getImgQuestion() {
        // Ensure we loop over the questions
        if (mNextImgQuestionIndex == mImgQuestionList.size()) {
            mNextImgQuestionIndex = 0;
        }

        // Please note the post-incrementation
        return mImgQuestionList.get(mNextImgQuestionIndex++);
    }

}
