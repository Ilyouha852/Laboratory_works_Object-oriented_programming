import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Какое задание выполнить?\n1. В введенной матрице переставить местами элементы первого и последнего столбцов, если элементы первого столбца меньше элемента последнего столбца\n2. На основе введенной двумерной матрицы составить новую матрицу из четных строк исходной матрицы\n3. Отсортировать слова в предложении в порядке убывания их длины\n4. Найти минимальный положительный элемент введенной двумерной матрицы");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 3) {
            System.out.println("Введите текст для сортировки (или нажмите Enter для использования заготовленного предложения):");
            String text = sc.nextLine().trim();

            Text textObject = text.isEmpty() ? new Text() : new Text(text);


            // Печать исходного текста
            textObject.outputText(true);

            // Печать отсортированного текста
            Text sortedText = new Text(textObject.sortWordsByLength());
            sortedText.outputText(false);
        } else {
            System.out.println("Введите число строк массива: ");
            int numberOfRows = sc.nextInt();
            System.out.println("Введите число столбцов массива: ");
            int numberOfColumns = sc.nextInt();
            sc.nextLine();

            String choiceInputMethod;
            do {
                System.out.println("Инициализировать матрицу автоматически? [Y/n]");
                choiceInputMethod = sc.nextLine().trim().toLowerCase();
            } while (!choiceInputMethod.equals("y") && !choiceInputMethod.equals("n"));

            Matrix matrix;
            if (choiceInputMethod.equals("y")) {
                matrix = new Matrix(numberOfRows, numberOfColumns, true);
            } else {
                matrix = new Matrix(numberOfRows, numberOfColumns, false);
            }

            matrix.outputMatrix(true);

            switch (choice) {
                case 1:
                    Matrix result = new Matrix(matrix.swapFirstLastColumns());
                    result.outputMatrix(false);
                    break;
                case 2:
                    result = new Matrix(matrix.createEvenRowsMatrix());
                    result.outputMatrix(false);
                    break;
                case 4:
                    matrix.findMinimalPositive();
                    break;
                default:
                    System.out.println("Неверный выбор задания.");
            }
        }
        sc.close();
    }
}