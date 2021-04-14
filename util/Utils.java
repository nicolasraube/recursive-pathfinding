package pathfinding.util;

import pathfinding.simulation.Player;
import pathfinding.simulation.Simulation;

import java.awt.*;
import java.util.HashSet;

public class Utils {
    public static boolean lastTurnsAreUseless(char[] instr, int filled) {
        if (filled <= 1) {
            return false;
        }

        char charBeforeLastChar = instr[filled - 2];
        char lastChar = instr[filled - 1];

        if (charBeforeLastChar == 'r' && lastChar == 'l') {
            return true;
        }

        if (charBeforeLastChar == 'l' && lastChar == 'r') {
            return true;
        }

        if (charBeforeLastChar == 'r' && lastChar == 'r') {
            return true;
        }

        if (filled >= 3 && instr[filled - 3] == 'l' && charBeforeLastChar == 'l' && lastChar == 'l') {
            return true;
        }

        return false;
    }

    public static int getMinimalStepsAndTurns(Simulation simulation) {
        return Utils.getMinimalStepsAndTurns(simulation.getPlayerX(), simulation.getPlayerY(), simulation.getPlayerDirection(), simulation.getTarget().x, simulation.getTarget().y);
    }

    public static int getMinimalStepsAndTurns(int x, int y, int direction, int findX, int findY) {
        int deltaX = Math.abs(findX - x);
        int deltaY = Math.abs(findY - y);

        int minSteps = deltaX + deltaY;

        if (minSteps == 0) {
            return 0;
        }

        return minSteps + getNecessaryTurns(x, y, direction, findX, findY);
    }

    private static int getNecessaryTurns(int x, int y, int direction, int findX, int findY) {
        int minTurns = 0;

        if (isTargetTopRight(x, y, findX, findY)) {
            minTurns = direction == Player.UP || direction == Player.RIGHT ? 1 : 2;
        } else if (isTargetTopLeft(x, y, findX, findY)) {
            minTurns = direction == Player.UP || direction == Player.LEFT ? 1 : 2;
        } else if (isTargetBottomRight(x, y, findX, findY)) {
            minTurns = direction == Player.DOWN || direction == Player.RIGHT ? 1 : 2;
        } else if (isTargetBottomLeft(x, y, findX, findY)) {
            minTurns = direction == Player.DOWN || direction == Player.LEFT ? 1 : 2;
        } else if (
            isTargetSameXAndShouldTurnOnce(x, direction, findX) ||
            isTargetSameYAndShouldTurnOnce(y, direction, findY)
        ) {
            minTurns = 1;
        } else if (
            isTargetSameXAndShouldTurnTwice(x, y, direction, findX, findY) ||
            isTargetSameYAndShouldTurnTwice(x, y, direction, findX, findY)
        ) {
            minTurns = 2;
        }

        return minTurns;
    }

    private static boolean isTargetTopRight(int x, int y, int findX, int findY) {
        return findX > x && findY > y;
    }

    private static boolean isTargetTopLeft(int x, int y, int findX, int findY) {
        return findX < x && findY > y;
    }

    private static boolean isTargetBottomRight(int x, int y, int findX, int findY) {
        return findX > x && findY < y;
    }

    private static boolean isTargetBottomLeft(int x, int y, int findX, int findY) {
        return findX < x && findY < y;
    }

    private static boolean isTargetSameXAndShouldTurnOnce(int x, int direction, int findX) {
        return findX == x && (direction == Player.LEFT || direction == Player.RIGHT);
    }

    private static boolean isTargetSameXAndShouldTurnTwice(int x, int y, int direction, int findX, int findY) {
        return
                (findX == x && findY < y && direction == Player.UP) ||
                (findX == x && findY > y && direction == Player.DOWN);
    }

    private static boolean isTargetSameYAndShouldTurnOnce(int y, int direction, int findY) {
        return (findY == y && (direction == Player.UP || direction == Player.DOWN));
    }

    private static boolean isTargetSameYAndShouldTurnTwice(int x, int y, int direction, int findX, int findY) {
        return
                (findY == y && findX < x && direction == Player.RIGHT) ||
                (findY == y && findX > x && direction == Player.LEFT);
    }

    public static boolean wasThereBefore(char[] instr, int filled) {
        Point currentPosition = new Point(0, 0);
        int viewDirection = Player.UP;

        HashSet<Point> visitedPositions = new HashSet<>(filled);
        visitedPositions.add(currentPosition);

        int currentInstructionIndex = getIndexOfLastPickupOrPlaceDown(instr, filled);

        while (currentInstructionIndex <= filled - 1) {
            char currentInstruction = instr[currentInstructionIndex];
            boolean isLastInstruction = currentInstructionIndex == filled - 1;

            if (currentInstruction == 's') {
                currentPosition = Player.getNextPosition(currentPosition, viewDirection);

                if (!isLastInstruction) {
                    visitedPositions.add(currentPosition);
                }
            } else {
                viewDirection = Player.getViewDirectionAfterTurning(currentInstruction, viewDirection);
            }

            currentInstructionIndex++;
        }

        return visitedPositions.contains(currentPosition);
    }

    private static int getIndexOfLastPickupOrPlaceDown(char[] instructions, int instructionCount) {
        for (int i = instructionCount - 1; i >= 0; i--) {
            if (instructions[i] == 'n' || instructions[i] == 'p') {
                return i;
            }
        }

        return 0;
    }
}