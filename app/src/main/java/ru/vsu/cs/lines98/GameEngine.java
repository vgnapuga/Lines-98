package ru.vsu.cs.lines98;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GameEngine {

    private static final int BOARD_SIZE = 9;
    private static final int MIN_MATCH_LENGTH = 5;
    private static final int NUM_COLORS = 6;
    private static final int START_BALLS_COUNT = 6;

    private int[][] board;
    private int score;

    public GameEngine() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        score = 0;

        initializeBoard();
    }

    private void initializeBoard() {
        Random random = new Random();

        int count = 0;

        for (int i = 0; i < START_BALLS_COUNT; i++) {
            addNewBall();
        }
    }

    public boolean isValidMove(int x1, int y1, int x2, int y2) {
        return _isValidMove(x1, y1, x2, y2);
    }

    private boolean _isValidMove(int x1, int y1, int x2, int y2) {
        return (x1 >= 0 && x1 < BOARD_SIZE && y1 >= 0 && y1 < BOARD_SIZE) &&
                (x2 >= 0 && x2 < BOARD_SIZE && y2 >= 0 && y2 < BOARD_SIZE) &&
                board[x1][y1] != 0 &&
                board[x2][y2] == 0 &&
                hasPath(x1, y1, x2, y2);
    }

    private boolean hasPath(int x1, int y1, int x2, int y2) {
        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{x1, y1});
        visited[x1][y1] = true;

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int currentX = current[0];
            int currentY = current[1];

            if (currentX == x2 && currentY == y2) {
                return true;
            }

            for (int[] direction : directions) {
                int newX = currentX + direction[0];
                int newY = currentY + direction[1];

                if (newX >= 0 && newX < BOARD_SIZE && newY >= 0 && newY < BOARD_SIZE && !visited[newX][newY] && board[newX][newY] == 0) {
                    queue.add(new int[]{newX, newY});
                    visited[newX][newY] = true;
                }
            }
        }

        return false;
    }

    public boolean moveBall(int x1, int y1, int x2, int y2) {
        if (!_isValidMove(x1, y1, x2, y2)) {
            return false;
        }

        board[x2][y2] = board[x1][y1];
        board[x1][y1] = 0;

        int matches = removeMatches();
        if (matches > 0) {
            calculateScore(matches);
            addNewBalls();

            return true;
        } else {
            addNewBalls();

            return true;
        }
    }

    private boolean isMatch() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isPartOfMatch(i, j)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isPartOfMatch(int x, int y) {
        int color = board[x][y];

        if (color == 0) {
            return false;
        }

        int count;

        count = 1;
        for (int i = 1; i < MIN_MATCH_LENGTH; i++) {
            if (y + i < BOARD_SIZE && board[x][y + i] == color) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < MIN_MATCH_LENGTH; i++) {
            if (y - i >= 0 && board[x][y - i] == color) {
                count++;
            } else {
                break;
            }
        }
        if (count >= MIN_MATCH_LENGTH) {
            return true;
        }

        count = 1;
        for (int i = 1; i < MIN_MATCH_LENGTH; i++) {
            if (x + i < BOARD_SIZE && board[x + i][y] == color) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < MIN_MATCH_LENGTH; i++) {
            if (x - i >= 0 && board[x - i][y] == color) {
                count++;
            } else {
                break;
            }
        }
        if (count >= MIN_MATCH_LENGTH) {
            return true;
        }

        count = 1;
        for (int i = 1; i < MIN_MATCH_LENGTH; i++) {
            if (x + i < BOARD_SIZE && y + i < BOARD_SIZE && board[x + i][y + i] == color) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < MIN_MATCH_LENGTH; i++) {
            if (x - i >= 0 && y - i >= 0 && board[x - i][y - i] == color) {
                count++;
            } else {
                break;
            }
        }
        if (count >= MIN_MATCH_LENGTH) {
            return true;
        }

        count = 1;
        for (int i = 1; i < MIN_MATCH_LENGTH; i++) {
            if (x + i < BOARD_SIZE && y - i >= 0 && board[x + i][y - i] == color) {
                count++;
            }
            else {
                break;
            }
        }
        for (int i = 1; i < MIN_MATCH_LENGTH; i++) {
            if (x - i >= 0 && y + i < BOARD_SIZE && board[x - i][y + i] == color) {
                count++;
            }
            else {
                break;
            }
        }
        if (count >= MIN_MATCH_LENGTH) {
            return true;
        }

        return false;
    }

    private int removeMatches() {
        int matches = 0;
        boolean[][] toRemove = new boolean[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (isPartOfMatch(i, j)) {
                    toRemove[i][j] = true;
                }
            }
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (toRemove[i][j]) {
                    board[i][j] = 0;
                    matches++;
                }
            }
        }

        return matches;
    }

    private void addNewBalls() {
        if (isEnd()) {
            return;
        }

        if (isLastOneOrTwo()) {
            addNewBall();
        } else {
            addNewBall();
            addNewBall();
            addNewBall();
        }
    }

    private void addNewBall() {
        Random random = new Random();

        while (true) {
            int x = random.nextInt(BOARD_SIZE);
            int y = random.nextInt(BOARD_SIZE);

            if (board[x][y] == 0) {
                board[x][y] = random.nextInt(NUM_COLORS) + 1;

                break;
            }
        }
    }

    private boolean isLastOneOrTwo() {
        int count = 0;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 0) {
                    count++;
                }
            }
        }

        if (count > 2) {
            return false;
        } else {
            return true;
        }
    }

    public void calculateScore(int matches) {
        score += matches * 10;
    }

    public int getScore() {
        return score;
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean isEnd() {
        return _isEnd();
    }

    private boolean _isEnd() {
        int count = 0;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }

        return true;
    }
}
