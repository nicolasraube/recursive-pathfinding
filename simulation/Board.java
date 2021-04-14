package pathfinding.simulation;

import java.awt.*;

public class Board {
    private final int[][] CELLS;

    private final int WIDTH, HEIGHT;

    public Board(int[][] cells) {
        CELLS = cells;
        WIDTH = cells.length;
        HEIGHT = cells[0].length;
    }

    public boolean isPositionInBounds(Point position) {
        return
                (position.x >= 0 && position.x < WIDTH) &&
                (position.y >= 0 && position.y < HEIGHT);
    }

    public int getHeightAt(Point position) {
        return CELLS[position.x][position.y];
    }

    public void removeIceBlockAt(Point position) {
        CELLS[position.x][position.y] = getHeightAt(position) - 1;
    }

    public void addIceBlockAt(Point position) {
        CELLS[position.x][position.y] = getHeightAt(position) + 1;
    }

    public Board clone() {
        int[][] newCells = new int[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                newCells[x][y] = CELLS[x][y];
            }
        }
        return new Board(newCells);
    }
}
