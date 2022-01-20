package edu.ics211.h09;

import java.util.ArrayList;

/**
 * Class for recursively finding a solution to a Sudoku problem.
 *
 * @author Biagioni, Edoardo, Cam Moore
 */
public class Sudoku {

  /**
   * Find an assignment of values to sudoku cells that makes the sudoku valid.
   * @author Mahie Crabbe, Christine Nakano, Londy Tong-Lee, Johnothan Means
   * @param sudoku the sudoku to be solved
   * @return whether a solution was found if a solution was found, the sudoku is 
   *     filled in with the solution if no
   *     solution was found, restores the sudoku to its original value
   */
  public static boolean solveSudoku(int[][] sudoku) {
    // loop through the sudoku double array (columns and rows)
    for (int x = 0; x < sudoku.length; x++) {
      for (int y = 0; y < sudoku.length; y ++) {
        // check if specified cell is equal to 0 
        if (sudoku[x][y] == 0) {
          // new ArrayList of solutions from legal values
          ArrayList<Integer> solution = legalValues(sudoku, x, y);
          // looping through every legal value
          for (int z = 0; z < solution.size(); z++) {
            // set legal value in the cell
            sudoku[x][y] = solution.get(z);
            
            //System.out.println(sudoku[x][y]); --> prints every solution
            
            // if true then true
            if (solveSudoku(sudoku)) {
              return true;
            }
          }
          // if no solution is found, reset cell back to zero
          sudoku[x][y] = 0;
          // no valid solution
          return false;
        }
      }
    }
    // checks that sudoku is successful
    return checkSudoku(sudoku, true);
  }

  /**
   * Find the legal values for the given sudoku and cell.
   * @author Mahie Crabbe, Christine Nakano, Londy Tong-Lee, Johnothan Means
   * @param sudoku sudoku, the sudoku being solved.
   * @param row the row of the cell to get values for.
   * @param column the column of the cell.
   * @return an ArrayList of the valid values.
   */
  public static ArrayList<Integer> legalValues(int[][] sudoku, int row, int column) {
    // check if specified cell is equal to 0 and if not return null
    if (sudoku[row][column] != 0) {
      return null;
    }
    // initialize an ArrayList
    ArrayList<Integer> list = new ArrayList<Integer>();
    // loop through the entire double array
    for (int i = 0; i < sudoku.length; i++) {
      int test = i + 1; // test value auto-updates
      boolean valid = true;
      // check colum
      for (int j = 0; j < 9; j++) {
        if (sudoku[j][column] == test) {
          valid = false;
        }
      }
      // checks row
      for (int k = 0; k < 9; k++) {
        if (sudoku[row][k] == test) {
          valid = false;
        }
      }
      // borrowed code from chackSudoku method
      // checks the 3x3 block
      for (int l = 0; l < 3; l++) {
        for (int m = 0; m < 3; m++) {
          int rows = (row / 3 * 3) + l; /* test this row */
          int columns = (column / 3 * 3) + m; /* test this col */
          if (sudoku[rows][columns] == test) {
            valid = false;
          }
        }
      }
      // checks if true then add legal value to ArrayList
      if (valid) {
        list.add(test);
      }
    }
    // return legal solutions
    return list;
  }


  /**
   * checks that the sudoku rules hold in this sudoku puzzle. cells that contain 0 are not checked.
   *
   * @param sudoku the sudoku to be checked
   * @param printErrors whether to print the error found, if any
   * @return true if this sudoku obeys all of the sudoku rules, otherwise false
   */
  public static boolean checkSudoku(int[][] sudoku, boolean printErrors) {
    if (sudoku.length != 9) {
      if (printErrors) {
        System.out.println("sudoku has " + sudoku.length + " rows, should have 9");
      }
      return false;
    }
    for (int i = 0; i < sudoku.length; i++) {
      if (sudoku[i].length != 9) {
        if (printErrors) {
          System.out.println("sudoku row " + i + " has " + sudoku[i].length + " cells, should have 9");
        }
        return false;
      }
    }
    /* check each cell for conflicts */
    for (int i = 0; i < sudoku.length; i++) {
      for (int j = 0; j < sudoku.length; j++) {
        int cell = sudoku[i][j];
        if (cell == 0) {
          continue; /* blanks are always OK */
        }
        if ((cell < 1) || (cell > 9)) {
          if (printErrors) {
            System.out.println("sudoku row " + i + " column " + j + " has illegal value " + cell);
          }
          return false;
        }
        /* does it match any other value in the same row? */
        for (int m = 0; m < sudoku.length; m++) {
          if ((j != m) && (cell == sudoku[i][m])) {
            if (printErrors) {
              System.out.println("sudoku row " + i + " has " + cell + " at both positions " + j + " and " + m);
            }
            return false;
          }
        }
        /* does it match any other value it in the same column? */
        for (int k = 0; k < sudoku.length; k++) {
          if ((i != k) && (cell == sudoku[k][j])) {
            if (printErrors) {
              System.out.println("sudoku column " + j + " has " + cell + " at both positions " + i + " and " + k);
            }
            return false;
          }
        }
        /* does it match any other value in the 3x3? */
        for (int k = 0; k < 3; k++) {
          for (int m = 0; m < 3; m++) {
            int testRow = (i / 3 * 3) + k; /* test this row */
            int testCol = (j / 3 * 3) + m; /* test this col */
            if ((i != testRow) && (j != testCol) && (cell == sudoku[testRow][testCol])) {
              if (printErrors) {
                System.out.println("sudoku character " + cell + " at row " + i + ", column " + j
                    + " matches character at row " + testRow + ", column " + testCol);
              }
              return false;
            }
          }
        }
      }
    }
    return true;
  }


  /**
   * Converts the sudoku to a printable string.
   *
   * @param sudoku the sudoku to be converted
   * @param debug whether to check for errors
   * @return the printable version of the sudoku
   */
  public static String toString(int[][] sudoku, boolean debug) {
    if ((!debug) || (checkSudoku(sudoku, true))) {
      String result = "";
      for (int i = 0; i < sudoku.length; i++) {
        if (i % 3 == 0) {
          result = result + "+-------+-------+-------+\n";
        }
        for (int j = 0; j < sudoku.length; j++) {
          if (j % 3 == 0) {
            result = result + "| ";
          }
          if (sudoku[i][j] == 0) {
            result = result + "  ";
          } else {
            result = result + sudoku[i][j] + " ";
          }
        }
        result = result + "|\n";
      }
      result = result + "+-------+-------+-------+\n";
      return result;
    }
    return "illegal sudoku";
  }
}