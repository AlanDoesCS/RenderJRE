package rMath;

public class Matrix {
    private Float[][] matrix;

    public Matrix(Float[][] matrix) {
        this.matrix = matrix;
    }

    public static Matrix Multiply(Matrix A, Matrix B) {
        Matrix result = new Matrix(new Float[A.matrix.length][B.matrix[0].length]);

        for (int i = 0; i < A.matrix.length; i++) {
            for (int j = 0; j < B.matrix[0].length; j++) {
                float sum = 0;
                for (int k = 0; k < A.matrix[0].length; k++) {
                    sum += A.matrix[i][k] * B.matrix[k][j];
                }
                result.set(i, j, sum);
            }
        }
        return result;
    }

    public static Matrix Add(Matrix A, Matrix B) {
        Matrix result = new Matrix(new Float[A.matrix.length][A.matrix[0].length]);
        for (int i = 0; i < A.matrix.length; i++) {
            for (int j = 0; j < A.matrix[0].length; j++) {
                result.set(i, j, A.matrix[i][j] + B.matrix[i][j]);
            }
        }
        return result;
    }

    public static Matrix Subtract(Matrix A, Matrix B) {
        Matrix result = new Matrix(new Float[A.matrix.length][A.matrix[0].length]);
        for (int i = 0; i < A.matrix.length; i++) {
            for (int j = 0; j < A.matrix[0].length; j++) {
                result.set(i, j, A.matrix[i][j] - B.matrix[i][j]);
            }
        }
        return result;
    }

    public static Vertex Multiply(Matrix M, Vertex vertex) {
        return Multiply(M, vertex.toMatrix()).toVertex();
    }

    public float determinant() {
        if (matrix.length != matrix[0].length) {
            throw new IllegalStateException("Matrix must be square");
        }
        if (matrix.length == 2) { // we already know the matrix will be square
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }
        float sum = 0;
        for (int i = 0; i < matrix.length; i++) {
            sum += (float) (Math.pow(-1, i) * matrix[0][i] * subMatrix(0, i).determinant());
        }
        return sum;
    }

    private Matrix subMatrix(int i, int i1) {
        Float[][] subMatrix = new Float[matrix.length - 1][matrix.length - 1];
        int r = 0;
        for (int j = 0; j < matrix.length; j++) {
            if (j == i) {
                continue;
            }
            int c = 0;
            for (int k = 0; k < matrix.length; k++) {
                if (k == i1) {
                    continue;
                }
                subMatrix[r][c] = matrix[j][k];
                c++;
            }
            r++;
        }
        return new Matrix(subMatrix);
    }

    // Accessors and Mutators
    public void set(int row, int col, float value) {
        matrix[row][col] = value;
    }

    public float get(int row, int col) {
        return matrix[row][col];
    }
    private Vertex toVertex() {return new Vertex(matrix[0][0], matrix[1][0], matrix[2][0]);}

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Float[] row : matrix) {
            sb.append("| ");
            for (Float col : row) {
                sb.append(col).append(" ");
            }
            sb.append(" |");
            sb.append("\n");
        }
        return sb.toString();
    }
}
