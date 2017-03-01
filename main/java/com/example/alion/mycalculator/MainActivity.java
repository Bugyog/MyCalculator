package com.example.alion.mycalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    BigDecimal HUNDRED = new BigDecimal(100);
    MathContext MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_EVEN);

    TextView textView;
    Button zero;
    Button one;
    Button two;
    Button three;
    Button four;
    Button five;
    Button six;
    Button seven;
    Button eight;
    Button nine;
    Button ce;
    Button c;
    Button percent;
    Button division;
    Button multiply;
    Button minus;
    Button plus;
    Button dot;
    Button sign;
    Button equal;

    String text = "0";
    BigDecimal first;
    BigDecimal second;
    BigDecimal result;

    /* Stage -1 - ошибка, только кнопка "С" работает,
     *  набор нового числа, автоматически запускает метод clearAll()
     *  Stage 0 - результат вычислен.
     *  Stage 1 - задание первого операнда.
     *  Stage 2 - задание операции.
     *  Stage 3 - задание второго операнда.
     *
     *  Operation 1 - деление.
     *  Operation 2 - умножение.
     *  Operation 3 - вычитание.
     *  Operation 4 - сложение.
    */
    int stage;
    int operation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.zero: addZero();
                        break;
                    case R.id.one: addDigit('1');
                        break;
                    case R.id.two: addDigit('2');
                        break;
                    case R.id.three: addDigit('3');
                        break;
                    case R.id.four: addDigit('4');
                        break;
                    case R.id.five: addDigit('5');
                        break;
                    case R.id.six: addDigit('6');
                        break;
                    case R.id.seven: addDigit('7');
                        break;
                    case R.id.eight: addDigit('8');
                        break;
                    case R.id.nine: addDigit('9');
                        break;
                    case R.id.ce: clear();
                        break;
                    case R.id.c: clearAll();
                        break;
                    case R.id.percent: setPercent();
                        break;
                    case R.id.division: setOperation(1);
                        break;
                    case R.id.multiply: setOperation(2);
                        break;
                    case R.id.minus: setOperation(3);
                        break;
                    case R.id.plus: setOperation(4);
                        break;
                    case R.id.dot: addDot();
                        break;
                    case R.id.sign: setSign();
                        break;
                    case R.id.equal: calculate();
                        break;
                }
            }
        };

        textView = (TextView) findViewById(R.id.textView);

        zero = (Button) findViewById(R.id.zero);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        six = (Button) findViewById(R.id.six);
        seven = (Button) findViewById(R.id.seven);
        eight = (Button) findViewById(R.id.eight);
        nine = (Button) findViewById(R.id.nine);
        ce = (Button) findViewById(R.id.ce);
        c = (Button) findViewById(R.id.c);
        percent = (Button) findViewById(R.id.percent);
        division = (Button) findViewById(R.id.division);
        multiply = (Button) findViewById(R.id.multiply);
        minus = (Button) findViewById(R.id.minus);
        plus = (Button) findViewById(R.id.plus);
        dot = (Button) findViewById(R.id.dot);
        sign = (Button) findViewById(R.id.sign);
        equal = (Button) findViewById(R.id.equal);

        zero.setOnClickListener(listener);
        one.setOnClickListener(listener);
        two.setOnClickListener(listener);
        three.setOnClickListener(listener);
        four.setOnClickListener(listener);
        five.setOnClickListener(listener);
        six.setOnClickListener(listener);
        seven.setOnClickListener(listener);
        eight.setOnClickListener(listener);
        nine.setOnClickListener(listener);
        ce.setOnClickListener(listener);
        c.setOnClickListener(listener);
        percent.setOnClickListener(listener);
        division.setOnClickListener(listener);
        multiply.setOnClickListener(listener);
        minus.setOnClickListener(listener);
        plus.setOnClickListener(listener);
        dot.setOnClickListener(listener);
        sign.setOnClickListener(listener);
        equal.setOnClickListener(listener);
    }

    private void setPercent() {
        switch (stage)
        {
            case 1: clear();
                break;
            case 2:
            {
                text = first.multiply(first.divide(HUNDRED, MATH_CONTEXT)).toString();
                stage = 3;
                calculate();
            }
                break;
            case 3:
            {
                text = first.multiply(new BigDecimal(text).divide(HUNDRED, MATH_CONTEXT)).toString();
                calculate();
            }
                break;
        }
    }

    private void setOperation(int index) {

        if (stage == -1) return;
        if (stage == 3) calculate();
        operation = index;
        if (stage == 2) return;
        if (stage == 1)
        {
            first = new BigDecimal(text).stripTrailingZeros();
        } else
        {
            first = result;
        }
        stage = 2;
    }

    private void calculate() {

        if (stage == -1) return;

        second = new BigDecimal(text).stripTrailingZeros();


        if (stage == 2)
        {
            second = first;
            stage = 3;
        }

        if (stage == 3)
        {
            switch (operation)
            {
                case 1:
                {
                    try {
                        result = first.divide(second, new MathContext(9, RoundingMode.HALF_EVEN));
                    } catch (RuntimeException e) {
                        printError();
                        return;
                    }
                }
                    break;
                case 2: result = first.multiply(second, MATH_CONTEXT);
                    break;
                case 3: result = first.subtract(second, MATH_CONTEXT);
                    break;
                case 4: result = first.add(second, MATH_CONTEXT);
                    break;
            }
            printResult();
        }

        stage = 0;
    }

    private void printError() {
        stage = -1;
        text = "ERROR";
        textView.setText(text);
    }

    private void printResult() {
        BigDecimal intPart = result.setScale(0, BigDecimal.ROUND_FLOOR);
        BigDecimal fraction = result.setScale(9, BigDecimal.ROUND_HALF_EVEN).subtract(intPart);


        if (intPart.compareTo(BigDecimal.ZERO) == 0)
        {
            if (fraction.compareTo(BigDecimal.ZERO) == 0) {
                result = BigDecimal.ZERO;
            }
        }

        if (intPart.abs().compareTo(new BigDecimal(9999999999L)) == 1)
        {
            printError();
            return;
        }

        if (fraction.compareTo(BigDecimal.ZERO) == 0)
        {
            text = intPart.toString();
        } else
        {
            text = result.setScale(9, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().toString();
        }

        textView.setText(text);
    }


    private void checkStage() {
        if (stage == 0) clearAll();
        if (stage == 2)
        {
            clear();
            stage = 3;
        }
    }

    private void addZero() {
        checkStage();
        if (!text.equals("0"))
        {
            addDigit('0');
        }
    }

    private void addDot() {
        checkStage();
        if (!text.contains("."))
        {
            addDigit('.');
        }
    }

    private void addDigit(char digit) {
        if (stage == -1) return;
        checkStage();
        if (text.equals("0") && digit != '.')
        {
            text = "";
        }

        int length = text.length();
        if (text.contains(".")) length--;
        if (text.contains("-")) length--;

        if (length < 10)
        {
            text += digit;
            textView.setText(text);
        }
    }

    private void setSign()
    {
        if (!text.equals("0") && stage !=0 && stage != -1)
        {
            if (text.contains("-"))
            {
                text = text.substring(1);
                textView.setText(text);
            } else {
                text = "-" + text;
                textView.setText(text);
            }
        }
    }

    private void clear() {
        if (stage == -1) return;
        text = "0";
        textView.setText(text);
    }

    private void clearAll() {
        text = "0";
        stage = 1;
        textView.setText(text);
    }
}
