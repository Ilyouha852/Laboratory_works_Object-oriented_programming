public class Matrix {
    private int[][] matrix;

    public Matrix(int[][] enteredMatrix) {
        matrix = new int[enteredMatrix.length][enteredMatrix[0].length];
        for (int i = 0; i < enteredMatrix.length; i++) {
            for (int j = 0; j < enteredMatrix[i].length; j++) {
                matrix[i][j] = enteredMatrix[i][j];
            }
        }
    }

    public int[][] getMatrix(){
        return matrix;
    }

    public int[][] swapFirstLastColumns() {
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][0] < matrix[i][matrix[i].length - 1]) {
                int temp = matrix[i][0];
                matrix[i][0] = matrix[i][matrix[i].length - 1];
                matrix[i][matrix[i].length - 1] = temp;
            }
        }
        return matrix;
    }

    public int[][] createEvenRowsMatrix() {
        int rows = matrix.length / 2;
        int columns = matrix[0].length;
        int[][] result = new int[rows][columns];
        for (int i = 1; i < matrix.length; i += 2) {
            System.arraycopy(matrix[i], 0, result[i / 2], 0, columns);
        }
        return result;
    }

    public int findMinimalPositive() {
        int minimalNumber = Integer.MAX_VALUE;

        for (int[] row : matrix) {
            for (int value : row) {
                if (value > 0 && value < minimalNumber) {
                    minimalNumber = value;
                }
            }
        }
        return minimalNumber;
    }
}