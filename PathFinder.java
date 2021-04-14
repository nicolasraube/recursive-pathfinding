package pathfinding;

import pathfinding.simulation.Board;
import pathfinding.simulation.Player;
import pathfinding.simulation.Simulation;
import pathfinding.util.Utils;

import java.awt.*;

public class PathFinder {
	private final static char[] possibleInstructions = new char[] { 's', 'r', 'l', 'n', 'p' };

	public static void main(String[] args) {
		int[][] playground = new int[][] { //
				{ 0, 0, 0, 2, 0, 0, 2 }, //
				{ 0, 2, 0, 2, 0, 0, 0 }, //
				{ 0, 0, 0, 0, 0, 2, 0 }, //
				{ 2, 0, 2, 2, 0, 0, 0 }, //
				{ 0, 0, 0, 2, 0, 0, 0 }, //
				{ 0, 2, 2, 0, 0, 2, 2 }, //
				{ 0, 2, 0, 0, 2, 0, 0 }, //
				{ 0, 0, 0, 0, 0, 2, 0 }, //
				{ 2, 0, 0, 2, 0, 0, 0 }, //
		};

		char[] solution = PathFinder.findOptimalSolution(playground, 0, 0, Player.DOWN, 0, 6, 5, 40);

		System.out.println(solution);
	}

	public static char[] findOptimalSolution(int[][] playground, int x, int y, int direction, int blocks, int findX, int findY, int searchLimit) {
		int currentTestLength = Utils.getMinimalStepsAndTurns(x, y, direction, findX, findY);

		while (currentTestLength <= searchLimit) {
			char[] instructions = new char[currentTestLength];

			if (findInstructions(playground, x, y, direction, blocks, findX, findY, instructions)) {
				return instructions;
			}

			currentTestLength++;
		}

		return new char[0];
	}

	private static boolean findInstructions(int[][] playground, int x, int y, int direction, int blocks, int findX, int findY, char[] instructions) {
		Board board = new Board(playground);
		Player player = new Player(x, y, direction, blocks);
		Point targetPos = new Point(findX, findY);

		Simulation simulation = new Simulation(board, player, targetPos);

		return findPath(simulation, instructions, 0);
	}

	public static boolean findPath(Simulation simulation, char[] instructions, int filled) {
		if (isImpossibleForCurrentPath(simulation, instructions, filled)) {
			return false;
		}

		for (char possibleInstruction : possibleInstructions) {
			if (simulation.willInstructionLeadToTargetPosition(possibleInstruction, instructions, filled)) {
				return true;
			}
		}

		return false;
	}

	private static boolean isImpossibleForCurrentPath(Simulation simulation, char[] instructions, int filled) {
		int numberOfInstructionsLeft = instructions.length - filled;

		return numberOfInstructionsLeft < Utils.getMinimalStepsAndTurns(simulation);
	}
}
