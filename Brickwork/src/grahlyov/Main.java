package grahlyov;

/*
My solution uses this logic:
Since N and M are defined as even, we can always divide each layer
into multiple 2x2 matrices. With the conditions set in the assignment
there are generally 3 possible variations for the bricks contained
in each 2x2 matrix:
Variant 1: only 1 whole brick is contained
Example:
1 1   or  1 2
2 3       1 3
Variant 2: 2 whole bricks are contained
Example:
1 1   or  1 2
2 2       1 2
Variant 3: there is no whole brick contained
Example:
1 2
3 4
If there's a valid solution for all possible variations of a 2x2 matrix, there
is a possible solution for every layer of bricks set with the conditions from the assignment,
because each 2x2 matrix is independent and we can always fill the whole layer of bricks with
2x2 matrices. The valid solution is to transpose each matrix and layout the new layer
according to the transposed matrix.
Variant 1:
1 1   ->   1 2
2 3        1 3  -> so on the second layer we lay 2 vertical bricks, or vice versa if 1 1 was vertical.
Variant 2:
1 2   ->   1 1
1 2        2 2  -> so on the second layer we lay 2 horizontal bricks, or vice versa if 1 1 was horizontal.
Variant 3:
Since there's no whole brick in this variant we can choose either 2 horizontal or 2 vertical bricks for the
second layer, and it will be always a valid solution.
Hence we can conclude that for every valid layer there's a solution.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner (System.in);      // A Scanner instance for the input
        Map<Integer, WholeBrick> bricks;                // A Hash Map used for storing the properties of each whole brick
        String nM;                                      // A String for the N and M input
        int n = 0;                                      // N - the rows of the first layer
        int m = 0;                                      // M - the columns of the first layer
        boolean qFlag = false;                          // QuitFlag - used in the while loops for validation of the input

        while (!qFlag) {                                // A while loop used for validating the N and M input
            System.out.println("Please select size of the layer in this format 'N M' \n(N - rows, M - columns, both need to be even numbers between 2 and 100)");
            nM = scanner.nextLine();
            String[] nMArr = nM.split(" ");      // A String Array that stores the values of N and M
            if (isNumeric(nMArr[0]) && isNumeric(nMArr[1]) && nMArr.length == 2) {
                n = Integer.parseInt(nMArr[0]);
                m = Integer.parseInt(nMArr[1]);
                if ((n>0) && (n<=100) && (n%2==0) && (m>0) && (m<=100) && (m%2==0)) {
                    qFlag = true;
                } else {
                    System.out.println("Incorrect number input.");
                }
            } else {
                System.out.println("Incorrect input format.");
            }
        }

        String[][] layer1 = new String[n][m];           // A String Array that will represent the first brick layer

        qFlag=false;
        while (!qFlag) {                                // A while loop used for validating the input for the first brick layer
            System.out.println("Please enter the layout of Layer 1: \n(Format: '# # # # #... #', where # is an integer greater than 1,\nand represents the number of the brick. Each brick takes 2 spaces)");
            for (int i = 0; i < n; i++) {
                boolean qFlag02 = false;                // A second QuitFlag, used again in a while loop for validating
                while (!qFlag02) {                      // A nested while loop used for validating the number of inputs for each row and storing it
                    System.out.println("Row #" + (i + 1) + ": ");
                    String[] row = scanner.nextLine().split(" ");
                    if (row.length == m) {
                        System.arraycopy(row, 0, layer1[i], 0, m);
                        qFlag02 = true;
                    } else {
                        System.out.println("Incorrect input. Please enter " + m + " values for bricks, separated by space.");
                    }
                }
            }
            bricks = createMap(layer1);                 // Creating the Hash Map for the first layer
            if (isValid(bricks)) {                      // Checking the entered input consist only of valid bricks (2x1/1x2 bricks with unique number)
                System.out.println("Layer 1 input data is valid");
//                System.out.println("Printing Layer 1:");
//                printLayer(layer1,bricks);              // Calling the print method for printing the first layer
//                System.out.println();                   // This is optional, just for easier visual comparison between the layers
//                System.out.println();
                String[][] layer2 = nextLayer(layer1, bricks);                          // A String Array that will represent the second brick layer
                Map<Integer, WholeBrick> nLBricks = createMap(layer2);                  // Creating a Hash Map for the second layer
                if (isValid(nLBricks)) {                                                // Checking and printing the second layer
                    System.out.println("Printing Layer 2:");
                    printLayer(layer2, nLBricks);
                } else {
                    System.out.println(-1);                                             // Included for completeness, I think there's always a solution
                }
                qFlag = true;
            } else {
                System.out.println("Layer 1 input data is not valid");
            }
        }

    }


    public static boolean isNumeric(String strNum) {                    // A method for catching a possible exception if the
        if (strNum == null) {                                           // user enters characters instead of integers
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }


    public static Map<Integer, WholeBrick> createMap (String[][] arr) {         // A method for creating a Hash Map for the bricks in a given layer
        Map<Integer, WholeBrick> nMap = new HashMap<>();                        // The key is the number of the brick, and the value is the brick properties
        if (arr != null) {
            int col = arr[0].length;                // A variable for going through the array
            int row = arr.length;                   // A variable for going through the array
            for (int i=0; i<row; i++) {
                for (int j=0; j<col; j++) {
                    if (isNumeric(arr[i][j])) {
                        int intValue = Integer.parseInt(arr[i][j]);     // A variable used only for shortening the following statements
                        if (nMap.get(intValue) == null) {
                            nMap.put(intValue, new WholeBrick().addHalfBrick(new HalfBrick(j, i)));
                        } else {
                            nMap.get(intValue).addHalfBrick(new HalfBrick(j, i));
                        }
                    } else {
                        return null;
                    }
                }
            }
        }
        return nMap;
    }


    public static boolean isValid (Map<Integer, WholeBrick> checkMap) {         // A method checking if the given Hash Map is for a valid layer of bricks
        if (checkMap != null) {
            for (int i : checkMap.keySet()) {                   // Checking if each brick has a positive number, contains exactly 2 halves,
                if ((checkMap.get(i).getHalfBricks().size() != 2) || (i<1)) {               // and the two halves are next to each other
                    return false;
                }
                if (checkMap.get(i).getHalfBricks().get(0).distance(checkMap.get(i).getHalfBricks().get(1)) != 1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static String[][] nextLayer (String[][] prLayer, Map<Integer, WholeBrick> prMap) {       // A method that creates the next brick layer,
        if ((prLayer != null) && (prMap != null)) {                                                 // from a given first layer and its Hash Map
            int col = prLayer[0].length;            // A variable for going through the array
            int row = prLayer.length;               // A variable for going through the array
            String[][] nLayer = new String[row][col];               // A String Array that will represent the second layer of bricks
            int brickCount = 1;                                     // A variable used for numbering the bricks on the second layer
            for (int i=0; i<row; i++) {
                System.arraycopy(prLayer[i], 0, nLayer[i], 0, col);
            }
            for (int i=0; i<(row-1); i+=2) {                        // Going through the array and checking each 2x2 matrix
                for (int j=0; j<(col-1); j+=2) {
                    swapTwo(nLayer, i, (j+1), (i+1), j);
                    if ((nLayer[i][j].equals(nLayer[i+1][j])) || (nLayer[i][j+1].equals(nLayer[i+1][j+1]))) {   // Solving the brick layout for each
                        nLayer[i][j] = nLayer[i+1][j] = Integer.toString(brickCount);                           // of the 2x2 matrices
                        brickCount++;
                        nLayer[i][j+1] = nLayer[i+1][j+1] = Integer.toString(brickCount);
                    } else {
                        nLayer[i][j] = nLayer[i][j+1] = Integer.toString(brickCount);
                        brickCount++;
                        nLayer[i+1][j] = nLayer[i+1][j+1] = Integer.toString(brickCount);
                    }
                    brickCount++;
                }
            }
            return nLayer;
        }
        return null;
    }

    public static void swapTwo (String[][] arr, int iA, int jA, int iB, int jB) {               // A simple swap method used for the matrices transposition
        String temp = arr[iA][jA];              // A temporary variable used for the swap
        arr[iA][jA] = arr[iB][jB];
        arr[iB][jB] = temp;
    }

    public static void printLayer (String[][] layer, Map<Integer, WholeBrick> lMap) {           // A print method for a given brick layer
        for (int i=0; i<(1+5*layer[0].length); i++) {
            System.out.print("*");
        }
        System.out.println();
        for (String[] strings : layer) {
            printRow(strings, lMap);                        // Calls the printRow method for each row in the array
        }
    }


    public static void printRow (String[] row, Map<Integer, WholeBrick> lMap) {                 // A print method for a given row
        StringBuilder rStr = new StringBuilder("*");            // A StringBuilder instance for the contents of the row
    StringBuilder uStr = new StringBuilder("*");                // A StringBuilder instance for the characters bellow the row
        for (String s : row) {
            int chKey = Integer.parseInt(s);                    // A variable used only for shortening the following statements
            String strFormat = String.format("%-4s", String.format("%" + (s.length() + (4 - s.length()) / 2) + "s", s));    // A variable used only for shortening the following statements
            if (lMap.get(chKey).isVertical()) {                 // Building the String according to each brick's orientation
                rStr.append(strFormat).append("*");
                if (lMap.get(chKey).isPassed()) {
                    uStr.append("*****");
                } else {
                    uStr.append("    *");
                }
            } else {
                if (lMap.get(chKey).isPassed()) {
                    rStr.append(strFormat).append("*");
                } else {
                    rStr.append(strFormat).append(" ");
                }
                uStr.append("*****");
            }
            lMap.get(chKey).setPassed(true);
        }
        System.out.println(rStr);
        System.out.println(uStr);                                   // The format chosen is optimized for numbers with up to 4 digits
    }
}

