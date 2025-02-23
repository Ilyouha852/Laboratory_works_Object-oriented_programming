import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    // Метод для записи матрицы в текстовый файл
    public static void writeMatrixToFile(int[][] matrix, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int[] row : matrix) {
                for (int i = 0; i < row.length; i++) {
                    writer.write(String.valueOf(row[i]));
                    if (i < row.length - 1) {
                        writer.write(" ");
                    }
                }
                writer.newLine();
            }
        }
    }

    // Метод для записи текста в текстовый файл
    public static void writeTextToFile(String text, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(text);
        }
    }

    // Метод для чтения матрицы из текстового файла
    public static int[][] readMatrixFromFile(String fileName) throws IOException {
        List<int[]> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");
                int[] row = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    row[i] = Integer.parseInt(tokens[i]);
                }
                rows.add(row);
            }
        }
        return rows.toArray(new int[0][]); 
    }

    // Метод для чтения текста из текстового файла
    public static String readTextFromFile(String fileName) throws IOException {
        String str = new String();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                str += line + "\n";
            }
        }
        return str.trim();
    }

    // Метод для записи матрицы в бинарный файл
    public static void writeMatrixToBinaryFile(int[][] matrix, String fileName) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName))) {
            dos.writeInt(matrix.length);
            dos.writeInt(matrix[0].length);
            for (int[] row : matrix) {
                for (int value : row) {
                    dos.writeInt(value);
                }
            }
        }
    }

    // Метод для записи текста в бинарный файл
    public static void writeTextToBinaryFile(String text, String fileName) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName))) {
            dos.writeUTF(text);
        }
    }

    // Метод для чтения матрицы из бинарного файла
    public static int[][] readMatrixFromBinaryFile(String fileName) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileName))) {
            int rows = dis.readInt();
            int cols = dis.readInt();
            int[][] matrix = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrix[i][j] = dis.readInt();
                }
            }
            return matrix;
        }
    }

    // Метод для чтения текста из бинарного файла
    public static String readTextFromBinaryFile(String fileName) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileName))) {
            return dis.readUTF();
        }
    }
}