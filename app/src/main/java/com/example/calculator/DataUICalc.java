package com.example.calculator;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DataUICalc {
    private String mathExpression;
    private String scoreboardText;
    private final TextView inputField;
    private final TextView scoreboard;

    public DataUICalc(AppCompatActivity activity) {
        mathExpression = "";
        scoreboardText = "0";
        inputField = activity.findViewById(R.id.input_field);
        scoreboard = activity.findViewById(R.id.scoreboard);
    }

    public String getMathExpression() {
        return mathExpression;
    }

    public void setMathExpression(String mathExpression) {
        this.mathExpression = mathExpression;
    }

    public String getScoreboardText() {
        return scoreboardText;
    }

    public void setScoreboardText(String scoreboardText) {
        this.scoreboardText = scoreboardText;
    }

    public TextView getInputField() {
        return inputField;
    }

    public TextView getScoreboard() {
        return scoreboard;
    }
}
