import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}

class InsufficientElementsException extends Exception
{
    public InsufficientElementsException(String message) {
        super(message);
    }
}

class InvalidRowNumberException extends Exception
{
    public InvalidRowNumberException(String message){
        super(message);
    }
}

class InvalidColumnNumberException extends Exception{
    public InvalidColumnNumberException(String message){
        super(message);
    }
}

class DoubleMatrix
{
    double [][]a;
    int m;
    int n;

    public DoubleMatrix(double[] a, int m, int n) throws InsufficientElementsException {
        this.m = m;
        this.n = n;

        if(m*n>a.length) {
            throw new InsufficientElementsException("Insufficient number of elements");
        }

        if((m*n)==a.length) {
            int brojach = 0;
            this.a = new double[m][n];
            for (int i = 0; i < m; i++)
                for (int j=0; j<n; j++)
                    this.a[i][j] = a[brojach++];
        }

        else {
            int brojach = a.length - (m*n);

            this.a = new double[m][n];
            for (int i = 0; i < m; i++)
                for (int j=0; j<n; j++)
                    this.a[i][j] = a[brojach++];
        }
    }

    String getDimensions() {
        return "[" + m + " x " + n +"]";
    }

    int rows(){
        return m;
    }
    int columns(){
        return n;
    }

    double maxElementAtRow(int row) throws InvalidRowNumberException {
        if(row>m || row<1)
            throw new InvalidRowNumberException("Invalid row number");

        double max=0;
        boolean flag=true;
        for (int i=0; i<n; i++) {
            if(flag)
            {
                flag=false;
                max=a[row-1][i];
                continue;
            }
            if (a[row - 1][i] > max)
                max = a[row - 1][i];
        }

        return max;
    }
    double maxElementAtColumn(int column) throws InvalidColumnNumberException {
        if(column>n || column<1)
            throw new InvalidColumnNumberException("Invalid column number");

        double max=0;
        boolean flag=true;
        for (int i=0; i<m; i++) {
            if(flag)
            {
                max=a[i][column-1];
                flag=false;
                continue;
            }
            if (a[i][column - 1] > max)
                max = a[i][column - 1];
        }

        return max;
    }

    double sum()
    {
        double sum=0;

        for (int i=0; i<m; i++)
            for (int j=0; j<n; j++)
                sum+=a[i][j];

        return sum;
    }

    double[] toSortedArray(){
        double []niza;
        int brojach=0;

        niza = new double[m*n];

        for (int i=0; i<m; i++)
            for (int j=0; j<n; j++)
                niza[brojach++] = a[i][j];

        Arrays.sort(niza);

        for (int i=0; i<brojach/2; i++)
        {
            double temp = niza[i];
            niza[i] = niza[brojach-1-i];
            niza[brojach-1-i] = temp;
        }

        return niza;
    }

    @Override
    public String toString() {
        StringBuilder k = new StringBuilder();

        for (int i=0; i<m; i++) {
            for (int j = 0; j < n; j++) {
                k.append(String.format("%.2f", a[i][j]));
                if(j!=n-1)
                    k.append("\t");
            }
            if(i!=m-1)
                k.append("\n");
        }
        return String.valueOf(k);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleMatrix that = (DoubleMatrix) o;
        return m == that.m && n == that.n && Arrays.deepEquals(a, that.a);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(m, n);
        result = 31 * result + Arrays.deepHashCode(a);
        return result;
    }
}

class MatrixReader{
    public static DoubleMatrix read(InputStream input) throws InsufficientElementsException {
        Scanner k = new Scanner(input);

        int m,n,brojach=0;
        double []a;
        m = k.nextInt();
        n = k.nextInt();

        a = new double[m*n];
        for (int i=0; i<m; i++)
            for (int j=0; j<n; j++)
                a[brojach++] = k.nextDouble();

        return new DoubleMatrix(a,m,n);
    }
}