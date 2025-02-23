import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.Locale;
import java.util.ResourceBundle;

public class TaskMenu {
    public static Scanner sc = new Scanner(System.in);
    private static ResourceBundle messages;

    public static void displayMenu() {
        selectLanguage();

        while (true) {
            try {
                System.out.println("1. " + messages.getString("task_1"));
                System.out.println("2. " + messages.getString("task_2"));
                System.out.println("3. " + messages.getString("task_3"));
                System.out.println("4. " + messages.getString("task_4"));
                System.out.println("5. " + messages.getString("task_5"));

                int choice = sc.nextInt();
                sc.nextLine(); // Clear buffer

                switch (choice) {
                    case 1, 2, 4 -> handleMatrixTasks(choice);
                    case 3 -> handleTextTask();
                    case 5 -> { return; }
                    default -> System.out.println(messages.getString("invalid_choice"));
                }
            } catch (InputMismatchException e) {
                System.out.println(messages.getString("input_error"));
                sc.nextLine(); // Очистка буфера
            }
        }
    }

    private static void handleMatrixTasks(int choice) {
        Matrix matrix = new Matrix(inputMatrix());

        outputMatrix(matrix.getMatrix(), true);

        // Обработка задачи
        switch (choice) {
            case 1:
                Matrix result = new Matrix(matrix.swapFirstLastColumns());
                outputMatrix(result.getMatrix(),false);
                break;
            case 2:
                result = new Matrix(matrix.createEvenRowsMatrix());
                outputMatrix(result.getMatrix(),false);
                break;
            case 4:
                int minValue = matrix.findMinimalPositive();
                if (minValue < Integer.MAX_VALUE) {
                    System.out.println(messages.getString("min_Value") + minValue);
                } else {
                    System.out.println(messages.getString("has_no_minValue"));
                }
                break;
            default:
                System.out.println(messages.getString("invalid_choice"));
        }
    }

    private static void handleTextTask() {
        Text textObject = new Text(inputText());

        if (!textObject.hasWords()) {
            System.out.println(messages.getString("has_no_literals"));
            return;
        }

        outputText(textObject.getText(), true);

        Text sortedText = textObject.sortWordsByLength();
        outputText(sortedText.getText() ,false);
    }

    public static int[][] inputMatrix() {
        int[][] matrix;
        System.out.println(messages.getString("input_from_text_file"));
        System.out.println(messages.getString("input_from_binary_file"));
        System.out.println(messages.getString("manual_input"));
        System.out.println(messages.getString("automatically_input"));

        while (true) {
            System.out.print("(1/2/3/4)");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        System.out.println(messages.getString("filepath_to_textfile"));
                        String textFilePath = sc.nextLine().trim();
                        matrix = FileHandler.readMatrixFromFile(textFilePath);
                        break;

                    case "2":
                        System.out.println(messages.getString("filepath_to_binaryfile"));
                        String binaryFilePath = sc.nextLine().trim();
                        matrix = FileHandler.readMatrixFromBinaryFile(binaryFilePath);
                        break;

                    case "3":
                        System.out.println(messages.getString("enter_rows_columns"));
                        int rows, columns;
                        while (true) {
                            try {
                                System.out.print(messages.getString("rows_prompt"));
                                rows = sc.nextInt();
                                System.out.print(messages.getString("columns_prompt"));
                                columns = sc.nextInt();
                                if (rows <= 0 || columns <= 0) {
                                    throw new InvalidArraySizeException(messages.getString("negative_size_error"));
                                }
                                sc.nextLine();
                                break;
                            } catch (InvalidArraySizeException e) {
                                System.out.println(e.getMessage());
                            } catch (InputMismatchException e) {
                                System.out.println(messages.getString("input_error"));
                                sc.nextLine();
                            }
                        }
                        System.out.println(messages.getString("enter_elements"));
                        matrix = new int[rows][columns];
                        for (int i = 0; i < rows; i++) {
                            for (int j = 0; j < columns; j++) {
                                while (true) {
                                    try {
                                        System.out.print(messages.getString("element_prompt") + " [" + i + "][" + j + "]: ");
                                        matrix[i][j] = sc.nextInt();
                                        break;
                                    } catch (InputMismatchException e) {
                                        System.out.println(messages.getString("input_error"));
                                        sc.nextLine(); // Очистка буфера
                                    }
                                }
                            }
                        }
                        break;

                    case "4":
                        System.out.println(messages.getString("generate_size_prompt"));
                        while (true) {
                            try {
                                System.out.print(messages.getString("rows_prompt"));
                                rows = sc.nextInt();
                                System.out.print(messages.getString("columns_prompt"));
                                columns = sc.nextInt();
                                if (rows <= 0 || columns <= 0) {
                                    throw new InvalidArraySizeException(messages.getString("negative_size_error"));
                                }
                                sc.nextLine();
                                matrix = new int[rows][columns];
                                Random rnd = new Random();
                                for (int i = 0; i < matrix.length; i++) {
                                    for (int j = 0; j < matrix[i].length; j++) {
                                        matrix[i][j] = rnd.nextInt(201) - 100;
                                    }
                                }
                                break;
                            } catch (InvalidArraySizeException e) {
                                System.out.println(e.getMessage());
                            } catch (InputMismatchException e) {
                                System.out.println(messages.getString("input_error"));
                                sc.nextLine();
                            }
                        }
                        System.out.println(messages.getString("matrix_generated"));
                        break;

                    default:
                        System.out.println(messages.getString("invalid_choice"));
                        continue;
                }
                return matrix;
            } catch (IOException e) {
                System.out.println(messages.getString("file_processing_error") + ": " + e.getMessage());
            }
        }
    }

    public static void outputMatrix(int[][] matrix, boolean isOriginal) {
        System.out.println(isOriginal ? messages.getString("initial_matrix") : messages.getString("processing_result"));
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }

        String saveChoice;

        do {
            System.out.println(messages.getString("save_result"));
            saveChoice = sc.nextLine().trim().toLowerCase();
            if (!saveChoice.equals("y") && !saveChoice.equals("n")) {
                System.out.println(messages.getString("wrong_choice"));
                saveChoice = sc.nextLine().trim().toLowerCase();
            }
        } while (!saveChoice.equals("y") && !saveChoice.equals("n"));

        if (saveChoice.equals("y")) {
            System.out.println(messages.getString("save_binary"));
            String binaryChoice = sc.nextLine().trim().toLowerCase();
            System.out.println(messages.getString("enter_filename"));
            String fileName = sc.nextLine().trim();
            try {
                if (binaryChoice.equals("y")) {
                    FileHandler.writeMatrixToBinaryFile(matrix, fileName);
                } else {
                    FileHandler.writeMatrixToFile(matrix, fileName);
                }
                System.out.println(messages.getString("result_saved") + fileName);
            } catch (IOException e) {
                System.out.println(messages.getString("filewriiter_error"));
            }
        } else {
            System.out.println(messages.getString("result_not_saved"));
        }
    }

    public static String inputText() {
        String text;
        System.out.println(messages.getString("input_from_text_file"));
        System.out.println(messages.getString("input_from_binary_file"));
        System.out.println(messages.getString("manual_input"));
        System.out.println(messages.getString("automatically_input"));

        while (true) {
            System.out.print("(1/2/3/4): ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        System.out.println(messages.getString("filepath_to_textfile"));
                        String textFilePath = sc.nextLine().trim();
                        text = FileHandler.readTextFromFile(textFilePath);
                        break;

                    case "2":
                        System.out.println(messages.getString("filepath_to_binaryfile"));
                        String binaryFilePath = sc.nextLine().trim();
                        text = FileHandler.readTextFromBinaryFile(binaryFilePath);
                        break;

                    case "3":
                        System.out.println(messages.getString("enter_text"));
                        text = sc.nextLine().trim();
                        break;

                    case "4":
                        text = "Today is a good day!";
                        System.out.println(messages.getString("text_generated"));
                        break;

                    default:
                        System.out.println(messages.getString("invalid_choice"));
                        continue;
                }
                return text;
            } catch (IOException e) {
                System.out.println(messages.getString("file_processing_error") + e.getMessage());
            }
        }
    }

    public static void outputText(String text, boolean isOriginal) {
        System.out.println(isOriginal ? messages.getString("original_Text") :  messages.getString("processing_result"));
        System.out.println(text);
    
        if (!isOriginal) {
            while (true) {
                System.out.println(messages.getString("save_result"));
                String saveChoice = sc.nextLine().trim().toLowerCase();
                if (saveChoice.equals("y")) {
                    System.out.println(messages.getString("save_binary"));
                    String binaryChoice = sc.nextLine().trim().toLowerCase();
                    System.out.println(messages.getString("enter_filename"));
                    String fileName = sc.nextLine().trim();
                    try {
                        if (binaryChoice.equals("y")) {
                            FileHandler.writeTextToBinaryFile(text, fileName);
                        } else {
                            FileHandler.writeTextToFile(text, fileName);
                        }
                        System.out.println(messages.getString("result_saved") + fileName);
                        break;
                    } catch (IOException e) {
                        System.out.println(messages.getString("filewritter_error"));
                    }
                } else if (saveChoice.equals("n")) {
                    break;
                } else {
                    System.out.println(messages.getString("invalid_choice"));
                }
            }
        }
    }

    private static void selectLanguage() {
        System.out.println("Выберите язык / Select a language:");
        System.out.println("1. Русский");
        System.out.println("2. English");
        System.out.println("3. Deutsch");
        System.out.println("4. Français");
        System.out.println("5. Polski");

        while (true) {
            try {
                System.out.print("Ваш выбор / Your choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1 -> messages = ResourceBundle.getBundle("message", Locale.forLanguageTag("ru"));
                    case 2 -> messages = ResourceBundle.getBundle("message", Locale.forLanguageTag("en"));
                    case 3 -> messages = ResourceBundle.getBundle("message", Locale.forLanguageTag("de"));
                    case 4 -> messages = ResourceBundle.getBundle("message", Locale.forLanguageTag("fr"));
                    case 5 -> messages = ResourceBundle.getBundle("message", Locale.forLanguageTag("pl"));
                    default -> {
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                    }
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println(messages.getString("input_error"));
                sc.nextLine();
            }
        }
    }
}