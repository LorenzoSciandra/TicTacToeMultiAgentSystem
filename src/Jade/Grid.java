package Jade;

import java.util.ArrayList;
import java.util.List;

import jade.util.leap.Serializable;

public class Grid implements Serializable {
    // Tic-Tac-Toe 3x3 Grid
    private String[][] grid = new String[3][3];
    private List<Integer> freeCells = new ArrayList<Integer>();

    // Constructor
    public Grid() {
        // Initialize the grid with empty cells
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j] = " ";
            }
        }
        // Initialize the free cells list
        for (int i = 0; i < 9; i++) {
            freeCells.add(i);
        }
    }

    
    /** 
     * Returns the cell symbol at the specified position in the grid.
     * @param row
     * @param col
     * @return String
     */
    public String getCell(int row, int col) {
        return grid[row][col];
    }

    
    /** 
     * Returns the cell symbol at the specified position in the grid, using the
     * grid indexing convention.
     * @param num
     * @return String
     */
    public String getCell(int num) {
        int row = num / 3;
        int col = num % 3;
        return grid[row][col];
    }

    
    /** 
     * Sets the cell symbol at the specified position in the grid.
     * First, we check if the cell is free, using the isLegal(row, col) method.
     * If it is, we set the cell symbol.
     * @param row
     * @param col
     * @param value
     * @return boolean
     */
    public boolean setCell(int row, int col, String value) {
        // Check for availability
        // System.out.println("STO CERCANDO DI METTERE IL SIMBOLO " + value + " IN (" + row + "," + col + ")");
        if (this.isLegal(row, col)) {
            grid[row][col] = value;
            // update free cells
            freeCells.remove((Integer) (row * 3 + col));
            return true;
        } else {
            return false;
        }
    }

    
    /** 
     * Sets the cell symbol at the specified position in the grid, using the
     * grid indexing convention.
     * @param num
     * @param value
     * @return boolean
     */
    public boolean setCell(int num, String value) {
        int row = num / 3;
        int col = num % 3;
        return setCell(row, col, value);
    }

    
    /** 
     * Checks if the grid is full.
     * @return boolean
     */
    public boolean isFull() {
        return freeCells.size() == 0;
    }

    
    /** 
     * Checks if the grid is empty.
     * @return boolean
     */
    public boolean isEmpty() {
        return freeCells.size() == 9;
    }

    
    /**
     * Check if the grid contains a draw. 
     * @return boolean
     */
    public boolean isDraw() {
        return isFull() && !isWinner();
    }

    
    /** 
     * Checks if somebody has won in the grid.
     * @return boolean
     */
    public boolean isWinner() {
        // Check rows
        boolean horizontally = grid[0][0].equals(grid[0][1])
                && grid[0][0].equals(grid[0][2])
                && !grid[0][0].equals(" ")
                || grid[1][0].equals(grid[1][1]) && grid[1][0].equals(grid[1][2]) && !grid[1][0].equals(" ")
                || grid[2][0].equals(grid[2][1]) && grid[2][0].equals(grid[2][2]) && !grid[2][0].equals(" ");
        // Check columns
        boolean vertically = grid[0][0].equals(grid[1][0])
                && grid[0][0].equals(grid[2][0])
                && !grid[0][0].equals(" ")
                || grid[0][1].equals(grid[1][1]) && grid[0][1].equals(grid[2][1]) && !grid[0][1].equals(" ")
                || grid[0][2].equals(grid[1][2]) && grid[0][2].equals(grid[2][2]) && !grid[0][2].equals(" ");
        // Check diagonals
        boolean diagonally = grid[0][0].equals(grid[1][1])
                && grid[0][0].equals(grid[2][2])
                && !grid[0][0].equals(" ")
                || grid[0][2].equals(grid[1][1])
                        && grid[0][2].equals(grid[2][0])
                        && !grid[0][2].equals(" ");
        return horizontally || vertically || diagonally;
    }

    
    /** 
     * Returns the symbol of the winner in the grid.
     * @return String
     */
    public String getWinner() {
        if (isWinner()) {
            if (grid[0][0].equals(grid[1][1]) && grid[1][1].equals(grid[2][2])) {
                return grid[0][0];
            }
            if (grid[0][2].equals(grid[1][1]) && grid[1][1].equals(grid[2][0])) {
                return grid[0][2];
            }
            for (int i = 0; i < 3; i++) {
                if (grid[i][0].equals(grid[i][1]) && grid[i][1].equals(grid[i][2])) {
                    return grid[i][0];
                }
            }
            for (int i = 0; i < 3; i++) {
                if (grid[0][i].equals(grid[1][i]) && grid[1][i].equals(grid[2][i])) {
                    return grid[0][i];
                }
            }
        } else {
            return " ";
        }
        return null;
    }

    
    /** 
     * Checks if a move is legal in the grid.
     * @param row
     * @param col
     * @return boolean
     */
    public boolean isLegal(int row, int col) {
        return freeCells.contains((Integer) (row * 3 + col));
    }

    
    /** 
     * Checks if a move is legal in the grid, using the grid indexing convention.
     * @param row
     * @param col
     * @return boolean
     */
    public boolean isFree(int row, int col) {
        return grid[row][col].equals(" ");
    }

    
    /** 
     * Check if the symbol "symbol" is in the grid at the specified position.
     * @param row
     * @param col
     * @param symbol
     * @return boolean
     */
    public boolean isMySymbolThere(int row, int col, String symbol) {
        return grid[row][col].equals(symbol);
    }

    
    /** 
     * Returns the grid.
     * @return String[][]
     */
    public String[][] getGrid() {
        return this.grid;
    }

    
    /** 
     * Returns the free cells.
     * @return List<Integer>
     */
    public List<Integer> getFreeCells() {
        return this.freeCells;
    }

    
    /** 
     * Sets the grid.
     * @param grid
     */
    public void setGrid(Grid grid) {
        this.grid = grid.getGrid();
        this.freeCells = grid.getFreeCells();
    }

    
    /** 
     * Prints the grid in a nice way.
     * @return String
     */
    public void printGrid() {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    
    /** 
     * Returns the grid as a string.
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(grid[i][j]);
            }
        }
        return sb.toString();
    }

}
