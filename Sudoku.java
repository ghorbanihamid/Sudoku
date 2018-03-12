import java.io.*;
import java.util.Arrays;

/**
 * Created by Hamid Ghorbani on 8/29/15.
 */

public class Sudoku {

    private static String fileName = "sudoku.txt";
    private static String fileData[];

    private int testCaseCount = 0;
    private static final int resolvedSudokuGrid = 0;
    private static final int unResolvedSudokuGrid = 1;
    private static final int bothSudokuGrid = 2;

    private static final int rotateClockwise90 = 0;
    private static final int rotate180 = 1;
    private static final int rotateCounterClockwise90 = 2;

    private static final String zeroData = "000000000";

    int lastWeeksResolvedSudokuGrid[][]   = new int[9][9];
    int thisWeeksUnResolvedSudokuGrid[][] = new int[9][9];

    Sudoku(String inputData[]){

       this.fileData = inputData;
    }

    /*
     * resets specified Grid
     */
    void resetGrid(int grid) {
        int i, j;
        switch (grid){
            case resolvedSudokuGrid :
                for (i = 0; i < 9; i++)
                    for (j = 0; j < 9; j++) {
                        lastWeeksResolvedSudokuGrid[i][j] = 0;
                    }
                break;

            case unResolvedSudokuGrid :
                for (i = 0; i < 9; i++)
                    for (j = 0; j < 9; j++) {
                        thisWeeksUnResolvedSudokuGrid[i][j] = 0;
                    }
                break;

            case bothSudokuGrid :
                for (i = 0; i < 9; i++)
                    for (j = 0; j < 9; j++) {
                        lastWeeksResolvedSudokuGrid[i][j] = 0;
                        thisWeeksUnResolvedSudokuGrid[i][j] = 0;
                    }
                break;
        }
    }

    /*
     *  tries to match data between Resolved sudoku and new sudoku by
     *  setting valued of each index in Resolved sudoku to zero when the same index in new sudoku is zero
     */
    String matchUpData (String unResolvedData, String resolvedData) {

        StringBuilder tempData = new StringBuilder(resolvedData);

        for (int i = 0; i < 9; i++) {
            if(Character.getNumericValue(unResolvedData.charAt(i)) == 0){
                tempData.setCharAt(i,'0');
            }
        }
        return tempData.toString();
    }

    /*
     *  convert array of value to String
     */
    String getString(int[] inputData) {

        String inputDataStr = Arrays.toString(inputData);
        inputDataStr = inputDataStr.replaceAll(",", "").replaceAll(" ", "").substring(1,10);
        return inputDataStr;
    }


    /*
     * Check If new sudoku is permutation of old sudoku
     * assuming a simple permutation function f(x) = a - b;
     * if all cells follow the permutation function, then returns true
     */
    boolean isPermutationAlgorithm () {

        int permutationValue = 0;

        for (int i = 0; i < 9; i++){
            if(thisWeeksUnResolvedSudokuGrid[i][0] != 0){
                permutationValue = thisWeeksUnResolvedSudokuGrid[i][0] - lastWeeksResolvedSudokuGrid[i][0];
                break;
            }
        }

        boolean isWholeValuesPermuted = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(thisWeeksUnResolvedSudokuGrid[i][j] != 0 &&
                        (thisWeeksUnResolvedSudokuGrid[i][j] - lastWeeksResolvedSudokuGrid[i][j] != permutationValue)){
                    isWholeValuesPermuted = false;
                }
            }
        }
        return isWholeValuesPermuted;
    }

    /*
     * Rotates a matrix with specified algorithm
     */
    int[][] rotateGrid(int gridNo, int rotationKind){

        int[][] rotatedMatrix = new int[9][9];
        if(gridNo == resolvedSudokuGrid){
            switch (rotationKind) {

                case rotateClockwise90:
                    for (int row = 0; row < 9 ; row++) {
                        for (int col = 0; col < 9; col++) {
                            rotatedMatrix[row][col] = lastWeeksResolvedSudokuGrid[9 - col - 1][row];
                        }
                    }
                    break;

                case rotate180:
                    for (int row = 0; row < 9 ; row++) {
                        for (int col = 0; col < 9; col++) {
                            int x = 9 - row - 1;
                            int y = 9 - col - 1;
                            rotatedMatrix[row][col] = lastWeeksResolvedSudokuGrid[x][y];
                        }
                    }
                    break;

                case rotateCounterClockwise90:
                    for (int row = 0; row < 9 ; row++) {
                        for (int col = 0; col < 9; col++) {
                            rotatedMatrix[row][col] = lastWeeksResolvedSudokuGrid[col][9 - row - 1];
                        }
                    }
                    break;
            }
        }
        else {
            switch (rotationKind) {

                case rotateClockwise90:
                    for (int row = 0; row < 9 ; row++) {
                        for (int col = 0; col < 9; col++) {
                            rotatedMatrix[row][col] = thisWeeksUnResolvedSudokuGrid[9 - col - 1][row];
                        }
                    }
                    break;

                case rotate180:
                    for (int row = 0; row < 9 ; row++) {
                        for (int col = 0; col < 9; col++) {
                            int x = 9 - row - 1;
                            int y = 9 - col - 1;
                            rotatedMatrix[row][col] = thisWeeksUnResolvedSudokuGrid[x][y];
                        }
                    }
                    break;

                case rotateCounterClockwise90:
                    for (int row = 0; row < 9 ; row++) {
                        for (int col = 0; col < 9; col++) {
                            rotatedMatrix[row][col] = thisWeeksUnResolvedSudokuGrid[col][9 - row - 1];
                        }
                    }
                    break;
            }
        }
        return rotatedMatrix;
    }

    /*
     * Rotates old sudoku with a specified rotation algorithm
     * and compares it with new sudoku
     * if they are completely equal , returns true
     */
    boolean compareRotatedAlgorithm (int rotationKind) {

        int equalLinesCount = 0;
        String newSudokuLineData;
        String oldSudokuLineData;
        int[][] rotatedGrid = rotateGrid(resolvedSudokuGrid,rotationKind);
//        System.out.println("rotatedGrid : ");
//        printGrid(rotatedGrid);
        for (int counter = 0; counter < 9; counter++) {

            newSudokuLineData = getString(thisWeeksUnResolvedSudokuGrid[counter]);
            if(!newSudokuLineData.equals(zeroData)){
                oldSudokuLineData = getString(rotatedGrid[counter]);
                oldSudokuLineData = matchUpData(newSudokuLineData,oldSudokuLineData); // matchup all zero cells in nsw sudoku with old sudoku
                if (newSudokuLineData.equals(oldSudokuLineData)){
                    equalLinesCount++;
                }
            }
        }
        return equalLinesCount == 9; // means all lines are equal, so rotated grid is equal with new grid
    }


    /*
     *  Checking algorithm number 1 : Rotating puzzle clockwise or counterclockwise
     *  Check If new sudoku is clockwise or counterclockwise rotated of old sudoku
     */
    boolean isRotateAlgorithm () {

        if(compareRotatedAlgorithm(rotateClockwise90)){
            System.out.println(" rotateClockwise90 used to build new Sudoku.");
            return true;
        }
        if(compareRotatedAlgorithm(rotate180)){
            System.out.println(" rotate180 used to build new Sudoku.");
            return true;
        }
        if(compareRotatedAlgorithm(rotateCounterClockwise90)){
            System.out.println(" rotateCounterClockwise90 used to build new Sudoku.");
            return true;
        }
        return false;
    }

    /*
     * this function converts a column to String
     */
    String getColumnDataOfGrid(int gridNo,int columnIndex, int columnCount) {

        String inputDataStr = "";
        if(gridNo == resolvedSudokuGrid){
            for (int i = 0; i < columnCount; i++) {
                inputDataStr += lastWeeksResolvedSudokuGrid[i][columnIndex];
            }
        }
        if(gridNo == unResolvedSudokuGrid){
            for (int i= 0; i < columnCount; i++) {
                inputDataStr += thisWeeksUnResolvedSudokuGrid[i][columnIndex];
            }
        }
        return inputDataStr;
    }

    boolean isSwapTwoColumnWithinAColSegmentAlgorithm () {

        for (int segmentCounter = 0; segmentCounter < 3; segmentCounter++) {

            if (compareColumnsWithinASegment(segmentCounter))
                return true;

        }
        return false;
    }

    /*
     * Checks If Swap Two Row Within A Row Segment Algorithm
     */
    boolean isSwapTwoRowWithinARowSegmentAlgorithm () {

        for (int segmentCounter = 0; segmentCounter < 3; segmentCounter++) {

            if (compareRowsWithinASegment(segmentCounter))
                return true;

        }
        return false;
    }

    /*
     * This function checks rows of segments to explore if they are equal
     * If first row of a segment in new sudoku was equal with
     * first row a segment in old sudoku then it check the next rows
     */
    boolean compareRowSegments(int newSudokuRow, int oldSudokuRow) {

        boolean compareResult = true;
        for(int i = 0; i < 3; i++) {
            String newSudokuLine = getString(thisWeeksUnResolvedSudokuGrid[newSudokuRow]);
            String oldSudokuLine = getString(lastWeeksResolvedSudokuGrid[oldSudokuRow]);
            oldSudokuLine = matchUpData(newSudokuLine, oldSudokuLine);
            if (!newSudokuLine.equals(oldSudokuLine)) {
                compareResult = false;
                break;
            }
        }
        return compareResult;
    }

    /*
     * This function checks rows of segments to explore if they are equal
     * If first row of a segment in new sudoku was equal with
     * first row a segment in old sudoku then it check the next rows
     */
    boolean compareRowsWithinASegment(int segmentNo) {

        for(int i = segmentNo * 3; i < 3 + (segmentNo * 3); i++) {
            for(int j = segmentNo * 3; j < 3 + (segmentNo * 3); j++) {
                if(i != j) {
                    String newSudokuLine = getString(thisWeeksUnResolvedSudokuGrid[i]);
                    String oldSudokuLine = getString(lastWeeksResolvedSudokuGrid[j]);
                    oldSudokuLine = matchUpData(newSudokuLine, oldSudokuLine);
                    if (newSudokuLine.equals(oldSudokuLine)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
     * This function checks rows of segments to explore if they are equal
     * If first row of a segment in new sudoku was equal with
     * the first row of a segment in old sudoku then it check the next rows
     */
    boolean compareColumnSegments(int newSudokuCol, int oldSudokuCol) {

        boolean compareResult = true;
        for(int i = 0; i < 3; i++) {
            String newSudokuColumnData = getColumnDataOfGrid(unResolvedSudokuGrid, newSudokuCol, 9);
            String oldSudokuColumnData = getColumnDataOfGrid(resolvedSudokuGrid, oldSudokuCol, 9);
            oldSudokuColumnData = matchUpData(newSudokuColumnData, oldSudokuColumnData);
            if (!newSudokuColumnData.equals(oldSudokuColumnData)) {
                compareResult = false;
                break;
            }
        }
        return compareResult;
    }

    /*
     * This function checks cols of segments to explore if they are equal
     * If first col of a segment in new sudoku was equal with
     * the first col of a segment in old sudoku then it check the next cols
     */
    boolean compareColumnsWithinASegment(int segmentNo) {

        for(int i = segmentNo * 3; i < 3 + (segmentNo * 3); i++) {
            for(int j = segmentNo * 3; j < 3 + (segmentNo * 3); j++) {
                if(i != j) {
                    String newSudokuColumnData = getColumnDataOfGrid(unResolvedSudokuGrid, i, 9);
                    String oldSudokuColumnData = getColumnDataOfGrid(resolvedSudokuGrid, j, 9);
                    oldSudokuColumnData = matchUpData(newSudokuColumnData, oldSudokuColumnData);
                    if (newSudokuColumnData.equals(oldSudokuColumnData)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
     * compares different segments of new sudoku with old sudoku.
     * for comparing segments, at first we compare the first row of each segment
     * if the result was true(the rows were equal), the function
     * compares the second rows of those segments and then the 3rd rows
     * if all rows of a segment was equal with another segment, the function
     * returns true.
     * Rows need to compare are :
     * (0,3),(0,6),(3,0),(3,6),(6,0),(6,3)
     */
    boolean isSwapEntireRowSegmentsAlgorithm () {

        for(int i = 0; i <= 6; i += 3) {
            for(int j = 0; j <= 6; j += 3) {
                if(i != j) {
                    if(compareRowSegments(i, j) == true)
                        return true;
                }
            }
        }
        return false;
    }

    /*
     * compares different segments of new sudoku with old sudoku.
     * for comparing segments, at first we compare the first col of each segment
     * if the result was true(the cols were equal), the function
     * compares the second col of those segments and then the 3rd cols
     * if all cols of a segment was equal with another segment, the function
     * returns true.
     * Rows need to compare are :
     * (0,3),(0,6),(3,0),(3,6),(6,0),(6,3)
     */
    boolean isSwapEntireColumnSegmentsAlgorithm () {

        for(int i = 0; i <= 6; i += 3) {
            for(int j = 0; j <= 6; j += 3) {
                if(i != j) {
                    if(compareColumnSegments(i, j) == true)
                        return true;
                }
            }
        }
        return false;
    }

    /*
     * This function is returns true, if any of the functions returns true.
     * It means current Sudoku derived From last week Sudoku
     */
    boolean isSudokuDerived() {

        // checking algorithm number 1 : Rotating puzzle clockwise or counterclockwise
        if(isRotateAlgorithm()){
            System.out.println(" rotate algorithm used to build new Sudoku.");
            return true;
        }

        // Checking algorithm number 2 : Swap Two Col Within A Row Segment Algorithm
        if(isSwapTwoColumnWithinAColSegmentAlgorithm()){
            System.out.println(" Swap Two Column Within A Segment Algorithm used to build new Sudoku.");
            return true;
        }

        // Checking algorithm number 3 : Swap Two Row Within A Row Segment Algorithm
        if(isSwapTwoRowWithinARowSegmentAlgorithm()){
            System.out.println(" Swap Two Row Within A Segment Algorithm used to build new Sudoku.");
            return true;
        }

        // checking algorithm number 4 : Swap a row segment
        if(isSwapEntireRowSegmentsAlgorithm()){
            System.out.println(" Entire Row Segment Swap Algorithm used to build new Sudoku.");
            return true;
        }

        // checking algorithm number 4 : Swap a column segment
        if(isSwapEntireColumnSegmentsAlgorithm()){
            System.out.println(" Entire column segment swap algorithm used to build new Sudoku.");
            return true;
        }

        // checking algorithm number 5 : Applying a permutation f(x)
        if(isPermutationAlgorithm()){
            System.out.println(" Permutation algorithm used to build new Sudoku.");
            return true;
        }

        return false;
    }

    /*
     *  prints grid in standard output
     */
    void printGrid() {

        int i, j;
        for (i = 0; i < 9; i++) {
            if (i % 3 == 0)
                System.out.println("+---+---+---+");
            for (j = 0; j < 9; j++) {
                if (j % 3 == 0)
                    System.out.print("|");
                System.out.print(lastWeeksResolvedSudokuGrid[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("+---+---+---+\n");
    }


    /*
     *  prints grid in standard output
     */
    void printGrid(int[][] grid) {

        int i, j;
        for (i = 0; i < 9; i++) {
            if (i % 3 == 0)
                System.out.println("+---+---+---+");
            for (j = 0; j < 9; j++) {
                if (j % 3 == 0)
                    System.out.print("|");
                System.out.print(grid[i][j]);
            }
            System.out.println("|");
        }
        System.out.println("+---+---+---+\n");
    }

    /*
     *  fills sudoku grids from memory
     */
    int fillSudokuGrid(int readTestCases) {

        resetGrid(bothSudokuGrid);
        int i=0;
        int j=0;
        try {
            for (i = 0; i < 18; i++) {
                String rowLine = fileData[i + ((readTestCases - 1) * 18) + readTestCases];
                if(i < 9){
                    for (j = 0; j < 9; j++) {
                        lastWeeksResolvedSudokuGrid[i][j] = Character.getNumericValue(rowLine.charAt(j));
                    }
                }
                else {
                    for (j = 0; j < 9; j++) {
                        thisWeeksUnResolvedSudokuGrid[i-9][j] = Character.getNumericValue(rowLine.charAt(j));
                    }
                }
            }
            return 0;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    /*
     * Reads data from file to memory.
     * for each test case, fills data to grids(lastWeeksResolvedSudokuGrid && thisWeeksUnResolvedSudokuGrid)
     * calls isSudokuDerived function to decide which algorithm is used for generating new sudoku
     * writes the answer in standard output
     */
    public void checkSudoku() {

        this.testCaseCount = Integer.parseInt(fileData[0].trim());
        if (this.testCaseCount <= 0) {
            System.out.println("Invalid number of test Cases!");
            return;
        }
        resetGrid(bothSudokuGrid);
        int readTestCases = 1;
        while (readTestCases <= this.testCaseCount){
            fillSudokuGrid(readTestCases);
//            printGrid();
            boolean isDerived = isSudokuDerived();
            System.out.println("Test Case number " + (readTestCases) + ": " + (isDerived ? "Yes" : "No"));
            readTestCases++;
        }
    }

    /*
     * Reads data from files to memory (fileData String array)
     */
    private static int readDataFromFile() {

        FileReader fileReader = null;
        try {
            File file = new File(fileName);
            if (!file.isFile() || !file.canRead()) {
                System.out.println("file not found!");
                return -1;
            }
            LineNumberReader lnr = new LineNumberReader(new FileReader(new File(fileName)));
            lnr.skip(Long.MAX_VALUE);
            int lineNumbers = lnr.getLineNumber() + 1;

            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String lineContent;
            fileData = new String[lineNumbers];
            for ( int i = 0; i < lineNumbers; i++) {
                lineContent = bufferedReader.readLine();
                if(lineContent == null)
                    break;
                fileData[i] = lineContent;
            }
            int testCaseCount = Integer.parseInt(fileData[0].trim());
            if(lineNumbers != testCaseCount * 18 + (testCaseCount)){
                System.out.println("Invalid file Lines!");
                return -1;
            }
            return 0;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (fileReader != null)
                    fileReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return -1;
    }

    public static void main(String[] argv) throws Exception {

        try {
            readDataFromFile();
            Sudoku sudoku = new Sudoku(fileData);
            sudoku.checkSudoku();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}