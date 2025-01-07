//package assignments.ex2;
// Add your documentation below:

import java.util.Stack;

public class SCell implements Cell {
    private String line;
    private int type;
    // Add your code here

    public SCell(String s) {
        // Add your code here
        setData(s);
    }

    @Override
    public int getOrder() {
        // Add your code here

        return 0;
        // ///////////////////
    }

    //@Override
    @Override
    public String toString() {
        return getData();
    }

    @Override
public void setData(String s) {
        // Add your code here
        line = s;
        // ///////////////////
    }
    @Override
    public String getData() {
        return line;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int t) {
        type = t;
    }

    @Override
    public void setOrder(int t) {
        // Add your code here

    }
    public static boolean isNumber(String text) {
        boolean ans = true;
        try {
            double d = Double.parseDouble(text);

        } catch (Exception e) {
            ans = false;
        }
        return ans;
    }

    public static boolean isText(String text) {
        if (text == null) {
            return false;
        }
        return !isNumber(text.trim()) && !text.trim().startsWith("=");  // Return true if it's text
    }

    public static boolean isForm(String text) {
        if (text == null) {
            return false;
        }


        if (!text.trim().startsWith("=")) {
            return false;
        }

        text = text.trim().substring(1);

        if (doubleOperators(text) || !validParentheses(text)) {
            return false;
        }

        return containsValidComponents(text);
    }

    private static boolean containsValidComponents(String text) {
        String validFormulaRegex = "^[A-Za-z0-9()+\\-*/.]*$";
        return text.matches(validFormulaRegex);
    }


    public static boolean doubleOperators(String text) {
        String operators = "+-*/=";
        for (int i = 0; i < text.length() - 1; i++) {
            char currentChar = text.charAt(i);
            char nextChar = text.charAt(i + 1);
            if (operators.indexOf(currentChar) >= 0 && operators.indexOf(nextChar) >= 0) {
                return true;
            }
        }
        return false;
    }
    public static boolean validParentheses(String text) {
        int openParenthesesCount = 0;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '(') {
                openParenthesesCount++;
            } else if (c == ')') {
                openParenthesesCount--;
            }

            if (openParenthesesCount < 0) {
                return false;
            }
        }

        return openParenthesesCount == 0;
    }

    public static double computeForm(String form) {
        if (form == null || form.isEmpty()) {
            throw new IllegalArgumentException("error empty formula.");
        }

        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < form.length(); i++) {
            char currentChar = form.charAt(i);

            if (currentChar == ' ') {
                continue;
            }

            if (Character.isDigit(currentChar)) {
                StringBuilder number = new StringBuilder();
                while (i < form.length() && (Character.isDigit(form.charAt(i)) || form.charAt(i) == '.')) {
                    number.append(form.charAt(i));
                    i++;
                }
                values.push(Double.parseDouble(number.toString()));
                i--;
            }
            else if (currentChar == '(') {
                operators.push(currentChar);
            }
            else if (currentChar == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop();
            }

            else if (currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/') {
                while (!operators.isEmpty() && hasPrecedence(currentChar, operators.peek())) {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(currentChar);
            }
        }

        while (!operators.isEmpty()) {
            values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private static boolean hasPrecedence(char currentOp, char topOp) {
        if (topOp == '(' || topOp == ')') {
            return false;
        }
        if ((currentOp == '*' || currentOp == '/') && (topOp == '+' || topOp == '-')) {
            return false;
        }
        return true;
    }

    private static double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("חילוק באפס");
                }
                return a / b;
        }
        return 0;
    }

}
