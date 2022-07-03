package Jade;

import java.util.ArrayList;
import java.util.List;

public class Grid {
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

    // Getter and setters
    public String getCell(int row, int col) {
        return grid[row][col];
    }

    public String getCell(int num) {
        int row = num / 3;
        int col = num % 3;
        return grid[row][col];
    }

    public boolean setCell(int row, int col, String value) {
        // Check for availability
        if (this.isLegal(row, col)) {
            grid[row][col] = value;
            // update free cells
            freeCells.remove((Integer) (row * 3 + col));
            return true;
        } else {
            return false;
        }
    }

    // Check if the grid is full
    public boolean isFull() {
        return freeCells.size() == 0;
    }

    // Check if the grid is empty
    public boolean isEmpty() {
        return freeCells.size() == 9;
    }

    // Check if the grid is a draw
    public boolean isDraw() {
        return isFull() && !isWinner();
    }

    // Check if the grid is a winner
    public boolean isWinner() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (grid[i][0].equals(grid[i][1]) && grid[i][1].equals(grid[i][2])) {
                return true;
            }
        }
        // Check columns
        for (int i = 0; i < 3; i++) {
            if (grid[0][i].equals(grid[1][i]) && grid[1][i].equals(grid[2][i])) {
                return true;
            }
        }
        // Check diagonals
        if (grid[0][0].equals(grid[1][1]) && grid[1][1].equals(grid[2][2])) {
            return true;
        }
        if (grid[0][2].equals(grid[1][1]) && grid[1][1].equals(grid[2][0])) {
            return true;
        }
        return false;
    }

    public String getWinner(){
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
        }
        else{
            return " ";
        }
        return null;
    }

    // Check if a move is legal
    public boolean isLegal(int row, int col) {
        return freeCells.contains((Integer) (row * 3 + col));
    }

    public String[][] getGrid() {
        return this.grid;
    }

    public List<Integer> getFreeCells() {
        return this.freeCells;
    }

    public void setGrid(Grid grid) {
        this.grid = grid.getGrid();
        this.freeCells = grid.getFreeCells();
    }

    // Print the grid in a nice way
    public void printGrid() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("-------------");
    }

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
