package com.example.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private String result = "";
    private boolean isError = false;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getResult(final String expression) {
        if (expression.equals("-")) {
            result = "";
            return result;
        }
        recurse(expression, 0);
        return result;
    }

    private void recurse(final String expression, int countOperation) {
        /*Считаем количество действий в выражении*/
        String str = expression;
        if (countOperation == 0) {
            Matcher matcher = Pattern.compile("(sin)|(cos)|(tan)|[\\^\\*\\+\\/\\-]").matcher(expression);
            while (matcher.find()) {
                countOperation++;
            }
            /*Удаляем все пробелы*/
            str = expression.replace(" ", "");
        }

        /*Округляем до 2 знаков после запятой*/
        if (Pattern.matches("-?\\d+(\\.\\d+)?", str)) {
            String[] strArray = str.split("\\.");
            if (strArray[0].length() > 14) {
                result = "Error!";
                isError = true;
                return;
            }
            if (!str.contains(".")) {
                BigDecimal bd = new BigDecimal(str).stripTrailingZeros();
                result = String.valueOf(bd.toPlainString());
                return;
            } else {
                String[] strArr = str.split("\\.");
                int len = 14 - strArr[0].length();
                BigDecimal bd = new BigDecimal(str).setScale(len, RoundingMode.HALF_UP).stripTrailingZeros();
//            System.out.println(bd + " " + countOperation);
                result = String.valueOf(bd.toPlainString());
                return;
            }

        }

        /*Находим sin, cos, tan, внутри которых находятся только числа, при нахождении таковых скобки удаляем и рекурсивно запускаем метод с исправленной строкой*/
        Matcher m = Pattern.compile("(sin|cos|tan)\\(-?\\d+(\\.\\d+)?\\)").matcher(str);
        if (m.find()) {
            String substring = str.substring(m.start(), m.end());
            double result = 0;
            if (substring.startsWith("sin")) {
                result = Math.sin(Math.toRadians(Double.parseDouble(substring.substring(4, substring.length() - 1))));
            }
            if (substring.startsWith("cos")) {
                result = Math.cos(Math.toRadians(Double.parseDouble(substring.substring(4, substring.length() - 1))));
            }
            if (substring.startsWith("tan")) {
                result = Math.tan(Math.toRadians(Double.parseDouble(substring.substring(4, substring.length() - 1))));
            }
            str = str.replace(substring, "" + result);
            recurse(str, countOperation);
            return;
        }

        /*Убираем +- и --*/
        if (str.contains("+-")) {
            str = str.replace("+-", "-");
            recurse(str, countOperation);
            return;
        }
        if (str.contains("-+")) {
            str = str.replace("-+", "-");
            recurse(str, countOperation);
            return;
        }
        if (str.contains("--")) {
            str = str.replace("--", "+");
            recurse(str, countOperation);
            return;
        }
        if (str.contains("++")) {
            str = str.replace("++", "+");
            recurse(str, countOperation);
            return;
        }
        if (str.startsWith("+")) {
            str = str.substring(1);
            recurse(str, countOperation);
            return;
        }
        if (str.contains("(+")) {
            str = str.replace("(+", "(");
            recurse(str, countOperation);
            return;
        }

        /*Находим скобки, внутри которых находятся только числа, при нахождении таковых скобки удаляем и рекурсивно запускаем метод с исправленной строкой*/
        m = Pattern.compile("(\\(-?\\d+(\\.\\d+)?\\))").matcher(str);
        if (m.find()) {
            String substring = str.substring(m.start(), m.end());
            if (str.equals(substring)) {
                recurse(str.substring(1, str.length() - 1), countOperation);
                return;
            }
            String[] array = str.split(substring);
            substring = substring.substring(1, substring.length() - 1);
            String strIzm = str.substring(0, m.start()) + substring + str.substring(m.end());
            recurse(strIzm, countOperation);
            return;
        }

        /*Находим возведение в степень, возводим, меняем строку выражения и рекурсивно запускаем метод с исправленной строкой*/
        m = Pattern.compile("(^\\d+(\\.\\d+)?\\^-?\\d+(\\.\\d+)?)").matcher(str);
        if (m.find()) {
            String substring = str.substring(m.start(), m.end());
            String[] array = substring.split("\\^");
            double result = Math.pow(Double.parseDouble(array[0]), Double.parseDouble(array[1]));
            String res = String.valueOf(result);
            str = res + str.substring(m.end());
            recurse(str, countOperation);
            return;
        }
        m = Pattern.compile("([^\\d]\\d+(\\.\\d+)?\\^-?\\d+(\\.\\d+)?)").matcher(str);
        if (m.find()) {
            String substring = str.substring(m.start() + 1, m.end());
            String[] array = substring.split("\\^");
            double result = Math.pow(Double.parseDouble(array[0]), Double.parseDouble(array[1]));
            String res = String.valueOf(result);
            str = str.substring(0, m.start() + 1) + res + str.substring(m.end());
            recurse(str, countOperation);
            return;
        }

        /*Находим умножение или деление, вычисляем, меняем строку выражения и рекурсивно запускаем метод с исправленной строкой*/
        m = Pattern.compile("(^-?\\d+(\\.\\d+)?(\\/|\\*)-?\\d+(\\.\\d+)?)").matcher(str);
        while (m.find()) {
            if (!str.substring(m.end()).startsWith("^")) {
                String substring = str.substring(m.start(), m.end());
                String res = divideOrMultiply(substring);
                str = res + str.substring(m.end());
                recurse(str, countOperation);
                return;
            }
        }
        m = Pattern.compile("([^\\d]-?\\d+(\\.\\d+)?(\\/|\\*)-?\\d+(\\.\\d+)?)").matcher(str);
        while (m.find()) {
            if (!str.substring(m.end()).startsWith("^")) {
                String substring = str.substring(m.start() + 1, m.end());
                String res = divideOrMultiply(substring);
                str = str.substring(0, m.start() + 1) + res + str.substring(m.end());
                recurse(str, countOperation);
                return;
            }
        }

        /*Находим сложение или вычитание, вычисляем, меняем строку выражения и рекурсивно запускаем метод с исправленной строкой*/
        m = Pattern.compile("(^-?\\d+(\\.\\d+)?(\\+|\\-)-?\\d+(\\.\\d+)?)").matcher(str);
        while (m.find()) {
            if (!str.substring(m.end()).startsWith("^") && !str.substring(m.end()).startsWith("/") && !str.substring(m.end()).startsWith("*")) {
                String substring = str.substring(m.start(), m.end());
                String res = addOrSubstract(substring);
                str = res + str.substring(m.end());
                recurse(str, countOperation);
                return;
            }
        }
        m = Pattern.compile("([^\\d]-?\\d+(\\.\\d+)?(\\+|\\-)-?\\d+(\\.\\d+)?)").matcher(str);
        while (m.find()) {
            if (!str.substring(m.end()).startsWith("^") && !str.substring(m.end()).startsWith("/") && !str.substring(m.end()).startsWith("*")) {
                String substring = str.substring(m.start() + 1, m.end());
                String res = addOrSubstract(substring);
                str = str.substring(0, m.start() + 1) + res + str.substring(m.end());
                recurse(str, countOperation);
                return;
            }
        }
    }

    private String divideOrMultiply(String substring) {
        String[] array = substring.split("\\/|\\*");
        BigDecimal result;
        if (substring.contains("/")) {
            result = new BigDecimal(array[0]).divide(new BigDecimal(array[1]), 30, RoundingMode.HALF_UP);
        } else {
            result = new BigDecimal(array[0]).multiply(new BigDecimal(array[1]));
        }
        return String.valueOf(result);
    }

    private String addOrSubstract(String substring) {
        Matcher mForFirstNumber = Pattern.compile("(^-?\\d+(\\.\\d+)?(\\+|\\-))").matcher(substring);
        mForFirstNumber.find();
        String firstNumber = substring.substring(0, mForFirstNumber.end() - 1);
        String znak = substring.substring(mForFirstNumber.end() - 1, mForFirstNumber.end());
        String secondNumber = substring.substring(mForFirstNumber.end());
        BigDecimal result;
        if (znak.contains("+")) {
            result = new BigDecimal(firstNumber).add(new BigDecimal(secondNumber));
        } else {
            result = new BigDecimal(firstNumber).subtract(new BigDecimal(secondNumber));
            if (result.compareTo(BigDecimal.ZERO) == 0 || result.toString().contains("E")) {
                result = new BigDecimal("0");
            }
        }
        return String.valueOf(result);
    }
}
