import javax.annotation.processing.SupportedSourceVersion;
import java.util.InputMismatchException;
import java.util.Scanner;

class Calculator
{
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args)
    {
        Main m = new Main();
        String input;

        for (;;)
        {
            System.out.print("Введите выражение или любой символ для выхода: ");
            input = scanner.nextLine();

            if (input.length() == 1)
                break;
            if (input.length() > 1 && input.length() < 3)
                throw new InputMismatchException("Строка не является математической операцией");

            m.calc(input);
            System.out.println();
        }
    }
}

enum Kind {
    plus('+'), minus('-'),
    mul('*'), div('/'), nul('0');
    private char ch;
    Kind(char ch) { this.ch = ch; }
    char getChar() { return ch; }
}
class Main
{
    int num1, num2;
    char op;
    void calc(String input)
    {
        char[] sign = new char[10];
        Kind k = Kind.nul;

        for (int i = 0; i < input.length(); ++i) {
            sign[i] = input.charAt(i);

            switch (sign[i]) {
                case '+' -> k = Kind.plus;
                case '-' -> k = Kind.minus;
                case '*' -> k = Kind.mul;
                case '/' -> k = Kind.div;
            }
        }

        String charToString = String.valueOf(sign);
        String[] reg = charToString.split("[+-/*]");

        if (reg.length >= 3) {
            throw new InputMismatchException("Формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }
        String operand1 = reg[0];
        String operand2 = reg[1];
        String operand3 = operand2.trim();

        if ((!operand1.chars().allMatch(Character::isDigit)) &&
                (operand3.chars().allMatch(Character::isDigit)) ||
                (operand1.chars().allMatch(Character::isDigit)) &&
                        (!operand3.chars().allMatch(Character::isDigit))) {
            throw new InputMismatchException("Используются одновременно разные системы счисления");
        }

        if ((!operand1.chars().allMatch(Character::isDigit)) && (!operand3.chars().allMatch(Character::isDigit))) {
            num1 = RomanToArabic(operand1);
            num2 = RomanToArabic(operand3);

            if ((num1 < num2) && (k == Kind.minus)) {
                throw new InputMismatchException("В римской системе нет отрицательных чисел");
            }

            System.out.println(operand1 + " " + k.getChar() + " " + operand3 + " = " + ArabicToRoman(calculate(num1, num2, k)));
        } else {
            num1 = Integer.parseInt(operand1);
            num2 = Integer.parseInt(operand3);

            if (num1 < 1 || num1 > 10 || num2 < 1 || num2 > 10) {
                throw new InputMismatchException("Формат не удовлетворяет заданию - цифры от 1 до 10");
            }

            int res = calculate(num1, num2, k);
            if (res == -1) {
                throw new ArithmeticException("Деление на ноль!");
            }

            System.out.println(operand1 + " " + k.getChar() + " " + operand3 + " = " + res);
        }
    }
    int calculate(int left, int right, Kind k)
    {
        return switch (k) {
            case plus -> left + right;
            case minus -> left - right;
            case mul -> left * right;
            case div -> left == 0 ? -1 : left / right;
            default -> throw new ArithmeticException("Не верный знак операции");
        };
    }
    String ArabicToRoman(int num)
    {
        String[] roman = {
                "O", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII",
                "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX",
                "XXI", "XXII", "XXIII", "XXIV", "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX", "XXXI",
                "XXXII", "XXXIII", "XXXIV", "XXXV", "XXXVI", "XXXVII", "XXXVIII", "XXXIX", "XL",
                "XLI", "XLII", "XLIII", "XLIV", "XLV", "XLVI", "XLVII", "XLVIII", "XLIX", "L", "LI", "LII",
                "LIII", "LIV", "LV", "LVI", "LVII", "LVIII", "LIX", "LX",
                "LXI", "LXII", "LXIII", "LXIV", "LXV", "LXVI", "LXVII", "LXVIII", "LXIX", "LXX",
                "LXXI", "LXXII", "LXXIII", "LXXIV", "LXXV", "LXXVI", "LXXVII", "LXXVIII", "LXXIX", "LXXX",
                "LXXXI", "LXXXII", "LXXXIII", "LXXXIV", "LXXXV", "LXXXVI", "LXXXVII", "LXXXVIII", "LXXXIX", "XC",
                "XCI", "XCII", "XCIII", "XCIV", "XCV", "XCVI", "XCVII", "XCVIII", "XCIX", "C"
        };

        return roman[num];
    }
    int RomanToArabic(String roman)
    {
        try {
            if(roman.equals("I"))
                return 1;
            else if (roman.equals("II"))
                return 2;
            else if (roman.equals("III"))
                return 3;
            else if (roman.equals("IV"))
                return 4;
            else if (roman.equals("V"))
                return 5;
            else if (roman.equals("VI"))
                return 6;
            else if (roman.equals("VII"))
                return 7;
            else if (roman.equals("VIII"))
                return 8;
            else if (roman.equals("IX"))
                return 9;
            else if (roman.equals("X"))
                return 10;
        } catch (InputMismatchException e) {
            throw new InputMismatchException("Неверный формат данных" + e);
        }

        return -1;
    }
}
































































