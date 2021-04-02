package com.example.calculator;

import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MyClickListener implements View.OnClickListener {
    private static volatile MyClickListener instance;
    private final ClickButtonsHandler handler;

    private MyClickListener(AppCompatActivity activity) {
        this.handler = new ClickButtonsHandler(activity);
    }

    public static MyClickListener getInstance(AppCompatActivity activity) {
        MyClickListener result = instance;
        if (result != null) {
            return result;
        }

        synchronized (MyClickListener.class) {
            if (instance == null) {
                instance = new MyClickListener(activity);
            }
            return instance;
        }
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        switch (button.getId()) {
            case R.id.button_cancel:
                handler.pressButtonCancel();
                break;
            case R.id.button_bs:
                handler.pressButtonBS();
                break;
            case R.id.button_percent:
                handler.pressButtonPercent();
                break;
            case R.id.button_div:
                handler.pressButtonDiv();
                break;
            case R.id.button_multiplication:
                handler.pressButtonMultiplication();
                break;
            case R.id.button_comma:
                handler.pressButtonComma();
                break;
            case R.id.button_equal:
                handler.pressButtonEqual();
                break;
            case R.id.button_sub:
                handler.pressButtonSub();
                break;
            case R.id.button_plus:
                handler.pressButtonPlus();
                break;
            case R.id.button_1:
                handler.pressButton1();
                break;
            case R.id.button_2:
                handler.pressButton2();
                break;
            case R.id.button_3:
                handler.pressButton3();
                break;
            case R.id.button_4:
                handler.pressButton4();
                break;
            case R.id.button_5:
                handler.pressButton5();
                break;
            case R.id.button_6:
                handler.pressButton6();
                break;
            case R.id.button_7:
                handler.pressButton7();
                break;
            case R.id.button_8:
                handler.pressButton8();
                break;
            case R.id.button_9:
                handler.pressButton9();
                break;
            case R.id.button_0:
                handler.pressButton0();
        }
     }
}
