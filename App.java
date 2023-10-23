import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Введите выражение (например, 2 + 3 или IV * 2) или 'exit' для выхода: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            try {
                String result = calc(input);
                System.out.println("Результат: " + result);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    public static String calc(String input) {
        // Разбиваем строку на операнды и операцию
        String[] parts = input.split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Неверный формат выражения");
        }

        String operand1 = parts[0];
        String operator = parts[1];
        String operand2 = parts[2];

        int num1, num2;
        boolean isRoman = false;

        try {
            // Попытаемся распарсить операнды как арабские числа
            num1 = Integer.parseInt(operand1);
            num2 = Integer.parseInt(operand2);
        } catch (NumberFormatException e) {
            // Если не удалось, попробуем как римские
            num1 = RomanConverter.romanToArabic(operand1);
            num2 = RomanConverter.romanToArabic(operand2);
            isRoman = true;
        }

        if (num1 < 1 || num1 > 10 || num2 < 1 || num2 > 10) {
            throw new IllegalArgumentException("Числа должны быть от 1 до 10 включительно");
        }

        int result;
        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 == 0) {
                    throw new IllegalArgumentException("Деление на ноль");
                }
                result = num1 / num2;
                break;
            default:
                throw new IllegalArgumentException("Неверная операция");
        }

        if (isRoman) {
            if (result <= 0) {
                throw new IllegalArgumentException("Результат работы с римскими числами меньше 1");
            }
            return RomanConverter.arabicToRoman(result);
        } else {
            return Integer.toString(result);
        }
    }
}

class RomanConverter {
    public static int romanToArabic(String roman) {
        int result = 0;
        int previousValue = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {
            char currentRoman = roman.charAt(i);
            int currentValue = romanDigitValue(currentRoman);

            if (currentValue < previousValue) {
                result -= currentValue;
            } else {
                result += currentValue;
            }

            previousValue = currentValue;
        }

        return result;
    }

    public static String arabicToRoman(int arabic) {
        if (arabic < 1 || arabic > 3999) {
            throw new IllegalArgumentException("Арабское число должно быть в диапазоне от 1 до 3999.");
        }

        String[] romanSymbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int[] arabicValues = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        StringBuilder roman = new StringBuilder();

        int i = 0;
        while (arabic > 0) {
            if (arabic >= arabicValues[i]) {
                roman.append(romanSymbols[i]);
                arabic -= arabicValues[i];
            } else {
                i++;
            }
        }

        return roman.toString();
    }

    private static int romanDigitValue(char roman) {
        switch (roman) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                throw new IllegalArgumentException("Неверный символ римской цифры: " + roman);
        }
    }
}