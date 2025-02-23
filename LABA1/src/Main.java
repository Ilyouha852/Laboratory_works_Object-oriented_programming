import java.util.Scanner;
import java.util.Random;

public class Main
{
    public static Scanner sc = new Scanner(System.in);

    public static void input_Matrix_and_select_Method(int selected_Method) {
        System.out.println("Введите число строк массива: ");
        int number_of_Rows = sc.nextInt();
        System.out.println("Введите число столбцов массива: ");
        int number_of_Columns = sc.nextInt();
        int[][] matrix = new int[number_of_Rows][number_of_Columns];

        String choice_Input_Method;
        System.out.println("Инициализировать матрицу автоматически? [Y/n]");
        do {
            choice_Input_Method = sc.nextLine().trim().toLowerCase();
        } while (!choice_Input_Method.equals("y") && !choice_Input_Method.equals("n"));

        if (choice_Input_Method.equals("y")) {
            Random rnd = new Random();
            for (int i = 0; i < number_of_Rows; i++) {
                for (int j = 0; j < number_of_Columns; j++){
                    matrix[i][j] = rnd.nextInt(201) - 100;
                }
            }
        } else {
            System.out.println("Введите элементы матрицы:");
            for (int i = 0; i < number_of_Rows; i++) {
                for (int j = 0; j < number_of_Columns; j++){
                    matrix[i][j] = sc.nextInt();
                }
            }
            sc.nextLine();
        }

        switch (selected_Method) {
            case (1):
                Task_1(matrix, number_of_Rows, number_of_Columns);
                break;
            case (2):
                Task_2(matrix, number_of_Rows, number_of_Columns);
                break;
            case (4):
                Task_4(matrix, number_of_Rows, number_of_Columns);
                break;
        }
    }

    public static void output_Matrix(int[][] T_matrix, int rows, int columns) {
        for (int i = 0; i <= rows - 1; i++){
            for (int j = 0; j <= columns - 1; j++){
                System.out.print(T_matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void Task_1(int[][] T1_matrix, int rows, int columns) {

        System.out.println("\nИсходящая матрица:");
        output_Matrix(T1_matrix, rows, columns);

        for (int i = 0; i <= rows - 1; i++) {
            if (T1_matrix[i][0] < T1_matrix[i][columns - 1]) {
                int temp = T1_matrix[i][0];
                T1_matrix[i][0] = T1_matrix[i][columns - 1];
                T1_matrix[i][columns - 1] = temp;
            }
        }

        System.out.println("\nПолученная матрица:");
        output_Matrix(T1_matrix, rows, columns);
    }

    public static void Task_2(int[][] T2_matrix, int rows, int columns) {

        System.out.println("\nИсходящая матрица:");
        output_Matrix(T2_matrix, rows, columns);

        int[][] result = new int[rows / 2][columns];
        for (int i = 1; i < rows; i += 2) {
            for (int j = 0; j <= columns - 1; j++) {
                result[i / 2][j] = T2_matrix[i][j];
            }
        }

        System.out.println("\nПолученная матрица:");
        output_Matrix(result, rows / 2, columns);
    }

    public static void Task_3(String text) {
        String[] words = text.split("[\\s]+|(?=[\\W])|(?<=[\\W])");
        int n = words.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (words[j].length() < words[j + 1].length()) {
                    String temp = words[j];
                    words[j] = words[j + 1];
                    words[j + 1] = temp;
                }
            }
        }

        System.out.println("\nОтсортированные слова по длине:");
        for (String word : words) {
            if (Character.isLetter(word.charAt(0))) {
                System.out.print(word + " ");
            }
        }
        System.out.println();
    }

    public static void Task_4(int[][] T4_matrix, int rows, int columns) {

        System.out.println("\nИсходящая матрица:");
        output_Matrix(T4_matrix, rows, columns);

        int minimal_Number = Integer.MAX_VALUE;
        for (int i = 0; i <= rows - 1; i++) {
            for (int j = 0; j <= columns - 1; j++) {
                if (T4_matrix[i][j] > 0 && T4_matrix[i][j] < minimal_Number)
                    minimal_Number = T4_matrix[i][j];
            }
        }
        System.out.println("\nНаименьший положительный элемент матрицы = " + minimal_Number);
    }
    public static void main(String[] args) {

        System.out.println("Какое задание выполнить?\n1. В введенной матрице переставить местами элементы первого и последнего столбцов, если элементы первого столбца меньше элемента последнего столбца\n2. На основе введенной двумерной матрицы составить новую матрицу из четных строк исходной матрицы\n3. Отсортировать слова в предложении в порядке убывания их длины\n4. Найти минимальный положительный элемент введенной двумерной матрицы");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 3) {
            System.out.println("Введите текст для сортировки:");
            String text = sc.nextLine();
            Task_3(text);
        } else {
            input_Matrix_and_select_Method(choice);
        }
    }
}
