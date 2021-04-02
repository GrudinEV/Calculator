package com.example.calculator;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClickButtonsHandler {
    private boolean isEqualPress = false;
    private final DataUICalc dataUICalc;
    private final Calculator calculator;
    private final LinkedList<String> computationQueue;
    private final TextView inputField;
    private final TextView scoreboard;

    public ClickButtonsHandler(AppCompatActivity activity) {
        this.dataUICalc = new DataUICalc(activity);
        this.calculator = new Calculator();
        computationQueue = new LinkedList<>();
        inputField = dataUICalc.getInputField();
        scoreboard = dataUICalc.getScoreboard();
    }

    public void pressButtonCancel() {
        if (dataUICalc.getMathExpression().length() > 0) {
            dataUICalc.setMathExpression("");
            if (isEqualPress) {
                isEqualPress = false;
            }
            if (calculator.isError()) {
                calculator.setError(false);
            }
            refreshInputField();
        }
    }

    public void pressButtonBS() {
        String mathExpression = dataUICalc.getMathExpression();
        if (mathExpression.length() > 0) {
            dataUICalc.setMathExpression(mathExpression.substring(0, mathExpression.length() -1));
            if (isEqualPress) {
                isEqualPress = false;
            }
            if (calculator.isError()) {
                calculator.setError(false);
            }
            refreshInputField();
        }
    }

    public void pressButtonPercent() {
        String mathExpressionFirst = dataUICalc.getMathExpression();
        String mathExpression = mathExpressionFirst.replace(",", ".");
        if (mathExpression.length() > 0 && !mathExpression.equals("0")) {
            if (Pattern.matches("-?\\d+(\\.\\d*)?", mathExpression)) {
                double result = Double.parseDouble(mathExpression) / 100;
                dataUICalc.setMathExpression("" + result);
                if (isEqualPress) {
                    isEqualPress = false;
                }
            } else {
                if (mathExpression.endsWith("\u00D7")
                        || mathExpression.endsWith("\u00F7")
                        || mathExpression.endsWith("+")
                        || mathExpression.endsWith("-")) {
                    return;
                } else {
                    String[] mathExpressionArr = mathExpression.split("[\\u00D7\\u00F7\\+\\-]");
                    String lastNumberStr = mathExpressionArr[mathExpressionArr.length - 1];
                    double lastNumber = Double.parseDouble(lastNumberStr);
                    int lengthLastNumber = lastNumberStr.length();
                    String adaptiveMathExpression = mathExpression.substring(0, mathExpression.length() - lengthLastNumber - 1).replace("\u00D7", "*")
                            .replace("\u00F7", "/")
                            .replace(",", ".");
                    String result = calculator.getResult(adaptiveMathExpression);
                    String lastNum = calculator.getResult(result + "*" + lastNumberStr + "/100");
                    lastNumberStr = lastNum.replace(".", ",");
                    mathExpression = mathExpressionFirst.substring(0, mathExpression.length() - lengthLastNumber) + lastNumberStr;
                    dataUICalc.setMathExpression(mathExpression);
                }
            }
            refreshInputField();
        }
    }

    public void pressButtonDiv() {
        String mathExpression = dataUICalc.getMathExpression();
        if (mathExpression.length() > 0) {
            String end = mathExpression.substring(mathExpression.length() - 1);
            if (end.equals("\u00F7") || calculator.isError()) {
                return;
            }
            String start = mathExpression.substring(0, mathExpression.length() - 1);
            if (end.equals("-") || end.equals("+") || end.equals("\u00D7") || end.equals(",")) {
                dataUICalc.setMathExpression(start + "\u00F7");
            } else {
                dataUICalc.setMathExpression(mathExpression + "\u00F7");
                if (isEqualPress) {
                    isEqualPress = false;
                }
            }
            refreshInputField();
        }
    }

    public void pressButtonMultiplication() {
        String mathExpression = dataUICalc.getMathExpression();
        if (mathExpression.length() > 0) {
            String end = mathExpression.substring(mathExpression.length() - 1);
            if (end.equals("\u00D7") || calculator.isError()) {
                return;
            }
            String start = mathExpression.substring(0, mathExpression.length() - 1);
            if (end.equals("-") || end.equals("+") || end.equals("\u00F7") || end.equals(",")) {
                dataUICalc.setMathExpression(start + "\u00D7");
            } else {
                dataUICalc.setMathExpression(mathExpression + "\u00D7");
                if (isEqualPress) {
                    isEqualPress = false;
                }
            }
            refreshInputField();
        }
    }

    public void pressButtonSub() {
        String mathExpression = dataUICalc.getMathExpression();
        if (mathExpression.length() == 0) {
            dataUICalc.setMathExpression("-");
            refreshInputField();
        } else {
            String end = mathExpression.substring(mathExpression.length() - 1);
            if (end.equals("-") || calculator.isError()) {
                return;
            }
            String start = mathExpression.substring(0, mathExpression.length() - 1);
            if (end.equals("\u00D7") || end.equals("+") || end.equals("\u00F7") || end.equals(",")) {
                dataUICalc.setMathExpression(start + "-");
            } else {
                dataUICalc.setMathExpression(mathExpression + "-");
                if (isEqualPress) {
                    isEqualPress = false;
                }
            }
            refreshInputField();
        }
    }

    public void pressButtonPlus() {
        String mathExpression = dataUICalc.getMathExpression();
        if (mathExpression.length() > 0) {
            String end = mathExpression.substring(mathExpression.length() - 1);
            if (end.equals("+")  || calculator.isError()) {
                return;
            }
            String start = mathExpression.substring(0, mathExpression.length() - 1);
            if (end.equals("\u00D7") || end.equals("-") || end.equals("\u00F7") || end.equals(",")) {
                dataUICalc.setMathExpression(start + "+");
            } else {
                dataUICalc.setMathExpression(mathExpression + "+");
                if (isEqualPress) {
                    isEqualPress = false;
                }
            }
            refreshInputField();
        }
    }

    public void pressButtonComma() {
        String mathExpression = dataUICalc.getMathExpression();
        if (calculator.isError()) {
            return;
        }
        if (mathExpression.equals("")
                || mathExpression.endsWith("\u00D7")
                || mathExpression.endsWith("\u00F7")
                || mathExpression.endsWith("+")
                || mathExpression.endsWith("-")) {
            dataUICalc.setMathExpression(mathExpression + "0,");
        } else {
            String[] arrayMathExpression = mathExpression.split("[\\u00D7\\u00F7\\+\\-]");
            int indexLastElement = arrayMathExpression.length - 1;
            if (arrayMathExpression[indexLastElement].contains(",")) {
                return;
            } else {
                dataUICalc.setMathExpression(mathExpression + ",");
                if (isEqualPress) {
                    isEqualPress = false;
                }
            }
        }
        refreshInputField();
    }

    public void pressButtonEqual() {
        String mathExpression = dataUICalc.getMathExpression();
        if (calculator.isError() || isDivideByZero() || isEqualPress) {
            return;
        }
        if (mathExpression.length() > 0) {
            if (computationQueue.size() >= MainActivity.MAX_LENGTH_COMPUTATION_QUEUE) {
                computationQueue.remove();
            }
            String adaptiveMathExpression = mathExpression.replace("\u00D7", "*")
                    .replace("\u00F7", "/")
                    .replace(",", ".");
            String result = calculator.getResult(adaptiveMathExpression);
            computationQueue.add(mathExpression + "\n=" + result);
            dataUICalc.setMathExpression(result);
            isEqualPress = true;
            refreshInputField();
        }
    }

    public void pressButton1() {
        pressDigitButton(String.valueOf(1));
    }

    public void pressButton2() {
        pressDigitButton(String.valueOf(2));
    }

    public void pressButton3() {
        pressDigitButton(String.valueOf(3));
    }

    public void pressButton4() {
        pressDigitButton(String.valueOf(4));
    }

    public void pressButton5() {
        pressDigitButton(String.valueOf(5));
    }

    public void pressButton6() {
        pressDigitButton(String.valueOf(6));
    }

    public void pressButton7() {
        pressDigitButton(String.valueOf(7));
    }

    public void pressButton8() {
        pressDigitButton(String.valueOf(8));
    }

    public void pressButton9() {
        pressDigitButton(String.valueOf(9));
    }

    public void pressButton0() {
        String mathExpression = dataUICalc.getMathExpression();
        if (calculator.isError()) {
            return;
        }
        if (mathExpression.length() == 0) {
            dataUICalc.setMathExpression("0");
            refreshInputField();
        } else {
            String[] arrayMathExpression = mathExpression.split("[\\u00D7\\u00F7\\+\\-]");
            int indexLastElement = arrayMathExpression.length - 1;
            if (!arrayMathExpression[indexLastElement].equals("0")) {
                if (!isEqualPress) {
                    dataUICalc.setMathExpression(mathExpression + "0");
                    refreshInputField();
                } else {
                    isEqualPress = false;
                    dataUICalc.setMathExpression("");
                    pressButton0();
                }
            }
        }
    }

    private void pressDigitButton(String s) {
        String mathExpression = dataUICalc.getMathExpression();
        if (calculator.isError()) {
            return;
        }
        if (mathExpression.length() == 0) {
            dataUICalc.setMathExpression(s);
        } else {
            if (mathExpression.equals("-")) {
                dataUICalc.setMathExpression(mathExpression + s);
            } else {
                String[] arrayMathExpression = mathExpression.split("[\\u00D7\\u00F7\\+\\-]");
                int indexLastElement = arrayMathExpression.length - 1;
                if (arrayMathExpression[indexLastElement].equals("0")
                        && !mathExpression.endsWith("\u00D7")
                        && !mathExpression.endsWith("\u00F7")
                        && !mathExpression.endsWith("+")
                        && !mathExpression.endsWith("-")) {
                    dataUICalc.setMathExpression(mathExpression.substring(0, mathExpression.length() - 1) + s);
                } else {
                    if (!isEqualPress) {
                        dataUICalc.setMathExpression(mathExpression + s);
                    } else {
                        isEqualPress = false;
                        dataUICalc.setMathExpression("");
                        pressDigitButton(s);
                    }
                }
            }
        }
        refreshInputField();
    }

    private void refreshInputField() {
        StringBuilder result = new StringBuilder();
        for (String str : computationQueue) {
            result.append(str).append("\n");
        }
        result.append(dataUICalc.getMathExpression());
        inputField.setText(result.toString());
        refreshScoreboard();
    }

    private void refreshScoreboard() {
        String mathExpressoin = dataUICalc.getMathExpression();
        if (!isDivideByZero()) {
            if (mathExpressoin.length() == 0 || mathExpressoin.equals("-")) {
                dataUICalc.setScoreboardText("0");
            } else {
                String adaptiveMathExpression = mathExpressoin.replace("\u00D7", "*")
                        .replace("\u00F7", "/")
                        .replace(",", ".");
                dataUICalc.setScoreboardText("=" + calculator.getResult(adaptiveMathExpression));
            }
        } else {
            dataUICalc.setScoreboardText("Divide By Zero!");
        }
        scoreboard.setText(dataUICalc.getScoreboardText());
    }

    private boolean isDivideByZero() {
        String mathExpression = dataUICalc.getMathExpression();
        Matcher m1 = Pattern.compile("\\u00F70$").matcher(mathExpression);
        Matcher m2 = Pattern.compile("\\u00F70[\\u00D7\\u00F7\\+\\-]").matcher(mathExpression);
        Matcher m3 = Pattern.compile("\\u00F70,$").matcher(mathExpression);
        if (m1.find() || m2.find() || m3.find()) {
            return true;
        }
        return false;
    }
}
