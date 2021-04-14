package pathfinding.simulation;

import pathfinding.PathFinder;
import pathfinding.util.Utils;

import java.awt.*;

public class Simulation {
    private final Board board;
    private final Player player;

    private final Point target;

    public Simulation(Board board, Player player, Point target) {
        this.board = board;
        this.player = player;
        this.target = target;
    }

    public boolean willInstructionLeadToTargetPosition(char instruction, char[] instructions, int filled) {
        if (isInstructionUseless(instruction, instructions, filled)) {
            return false;
        }

        if (isTargetReached()) {
            fillUp(instructions, filled);

            return true;
        }

        instructions[filled] = instruction;
        filled++;

        if (tookStepAndWasThereBefore(instruction, instructions, filled)) {
            return false;
        }

        Simulation nextState = tryInstruction(instruction);

        return PathFinder.findPath(nextState, instructions, filled);
    }

    private boolean isInstructionUseless(char instruction, char[] instructions, int filled) {
        if (instruction == 'p' &&
                (getLastInstruction(instructions, filled) == 'n' ||
                !isPlaceDownPossible())
        ) {
            return true;
        }

        if (instruction == 'n' &&
                (getLastInstruction(instructions, filled) == 'p' ||
                !isPickupPossible())
        ) {
            return true;
        }

        if (instruction == 's' &&
            !isMoveForwardPossible()
        ) {
            return true;
        }

        if (Utils.lastTurnsAreUseless(instructions, filled)) {
            return true;
        }

        return false;
    }

    private char getLastInstruction(char[] instructions, int filled) {
        if (filled > 0) {
            return instructions[filled - 1];
        }
        return '-';
    }

    private boolean isTargetReached() {
        return player.getCurrentPosition().equals(target);
    }

    private Simulation tryInstruction(char instruction) {
        Simulation attempt = clone();
        attempt.executeInstruction(instruction);

        return attempt;
    }

    public void executeInstruction(char instruction) {
        switch (instruction) {
            case 'r', 'l' -> player.turn(instruction);
            case 's' -> processMoveForwardInstruction();
            case 'n' -> processPickupInstruction();
            case 'p' -> processPlaceDownInstruction();

            default -> System.out.println("Unbekannte Anweisung!");
        }
    }

    private boolean tookStepAndWasThereBefore(char instruction, char[] instructions, int filled) {
        return instruction == 's' && Utils.wasThereBefore(instructions, filled);
    }

    public int getPlayerX() {
        return player.getCurrentPosition().x;
    }

    public int getPlayerY() {
        return player.getCurrentPosition().y;
    }

    public int getPlayerDirection() {
        return player.getViewDirection();
    }

    public Point getTarget() {
        return target;
    }

    protected Simulation clone() {
        return new Simulation(board.clone(), player.clone(), target);
    }

    private void processMoveForwardInstruction() {
        if(isMoveForwardPossible()) {
            player.moveForward();
        }
    }

    private boolean isMoveForwardPossible() {
        Point nextPos = player.getNextPosition();

        boolean nextPositionIsInBounds = board.isPositionInBounds(nextPos);
        boolean nextPositionIsReachableHeight = false;

        if (nextPositionIsInBounds) {
            Point currentPos = player.getCurrentPosition();

            int currentHeight = board.getHeightAt(currentPos);
            int nextHeight = board.getHeightAt(nextPos);

            nextPositionIsReachableHeight = Math.abs(nextHeight - currentHeight) <= 1;
        }

        return nextPositionIsInBounds && nextPositionIsReachableHeight;
    }

    private void processPickupInstruction() {
        if (isPickupPossible()) {
            board.removeIceBlockAt(player.getNextPosition());
            player.pickupIceBlock();
        }
    }

    private boolean isPickupPossible() {
        boolean isPickupPositionOutOfBounds = !board.isPositionInBounds(player.getNextPosition());

        boolean isCarryingCapacityReached = player.getIceBlockCount() == 10;

        boolean pickupPositionIsWater = !isPickupPositionOutOfBounds && board.getHeightAt(player.getNextPosition()) == -1;

        return !isPlayerInWater() && !isPickupPositionOutOfBounds && !isCarryingCapacityReached && !pickupPositionIsWater;
    }

    private boolean isPlayerInWater() {
        return board.getHeightAt(player.getCurrentPosition()) == -1;
    }

    private void processPlaceDownInstruction() {
        if (isPlaceDownPossible()) {
            board.addIceBlockAt(player.getNextPosition());
            player.placeDownIceBlock();
        }
    }

    private boolean isPlaceDownPossible() {
        boolean isPlayerHavingBlocks = player.getIceBlockCount() > 0;

        boolean isPlaceDownPositionOutOfBounds = !board.isPositionInBounds(player.getNextPosition());

        boolean isPlaceDownPositionTooHigh = !isPlaceDownPositionOutOfBounds && board.getHeightAt(player.getNextPosition()) == 9;

        return isPlayerHavingBlocks && !isPlayerInWater() && !isPlaceDownPositionOutOfBounds && !isPlaceDownPositionTooHigh;
    }

    private void fillUp(char[] instructions, int filled) {
        while (filled < instructions.length) {
            instructions[filled] = 'e';
            filled++;
        }
    }
}
