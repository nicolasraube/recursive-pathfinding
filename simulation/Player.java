package pathfinding.simulation;

import java.awt.*;

public class Player {
    private Point position;

    private int viewDirection;
    private int iceBlockCount;

    public static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;

    public Player(int x, int y, int direction, int iceBlockCount) {
        position = new Point(x, y);
        viewDirection = direction;
        this.iceBlockCount = iceBlockCount;
    }

    public int getViewDirection() {
        return viewDirection;
    }

    public int getIceBlockCount() {
        return iceBlockCount;
    }

    public void turn(char turnDirection) {
        this.viewDirection = Player.getViewDirectionAfterTurning(turnDirection, getViewDirection());
    }

    public static int getViewDirectionAfterTurning(char turnDirection, int currentViewDirection) {
        int newDirection = currentViewDirection;

        if (turnDirection == 'r') {
            newDirection = currentViewDirection - 1;
        } else if (turnDirection == 'l') {
            newDirection = currentViewDirection + 1;
        }

        return correctPosition(newDirection);
    }

    public static int correctPosition(int viewDirection) {
        int newDirection = viewDirection;

        if (newDirection == -1) {
            newDirection = 3;
        } else if (newDirection == 4) {
            newDirection = 0;
        }

        return newDirection;
    }

    public Point getCurrentPosition() {
        return position;
    }

    public Point getNextPosition() {
        return Player.getNextPosition(getCurrentPosition(), viewDirection);
    }

    public static Point getNextPosition(Point currentPosition, int viewDirection) {
        Point nextPosition = (Point) currentPosition.clone();

        switch (viewDirection) {
            case RIGHT -> nextPosition.x += 1;
            case LEFT -> nextPosition.x -= 1;
            case UP -> nextPosition.y += 1;
            case DOWN -> nextPosition.y -= 1;
        }

        return nextPosition;
    }

    public void moveForward() {
        position = getNextPosition();
    }

    public void pickupIceBlock() {
        iceBlockCount++;
    }

    public void placeDownIceBlock() {
        iceBlockCount--;
    }

    public Player clone() {
        return new Player(getCurrentPosition().x, getCurrentPosition().y, getViewDirection(), getIceBlockCount());
    }
}
