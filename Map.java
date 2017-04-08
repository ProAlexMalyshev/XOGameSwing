package gameXOswing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * Java. Swing game "Cross-zero"
 * @author Aleksey Malyshev
 * @version 1.0.1 dated March 15, 2017
 */
public class Map extends JPanel {

    static final int AI_VS_PLAYER = 0;
    static final int PLAYER_VS_PLAYER = 1;

    private static final int EMPTY_DOT = 0;
    private static final int PLAYER_DOT = 1;
    private static final int AI_DOT = 2;
    private static final int PLAYER_DOT2 = 3;

    private static final int DRAW = 0;
    private static final int PLAYER_WIN = 1;
    private static final int AI_WIN = 2;
    private static final int PLAYER_WIN2 = 3;

    private static final int DOTS_MARGIN = 20;

    private static final String DRAW_MSG = "Ничья!!!";
    private static final String PLAYER_WIN_MSG = "Выиграл 1 игрок!!";
    private static final String PLAYER_WIN_MSG2 = "Выиграл 2 игрок!!!";
    private static final String AI_WIN_MSG = "Выиграл компьютер!!!";

    private final Random rnd = new Random();
    private boolean initialized;
    public int[][] field;
    private int fieldSizeX;
    private int fieldSizeY;
    private int mode;
    private int winLen;
    private int cell_width;
    private int cell_height;
    private boolean gameOver;
    private int gameOverState;
    private final Font font = new Font("Arial", Font.BOLD, 40);
    boolean checkTurn;


    public Map() {
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                update(e);
            }
        });

    }

    public void update(MouseEvent e) {

        if (gameOver || !initialized) {
            return;
        }
        if (checkTurn == false) {

            int cell_x = e.getX() / cell_width;
            int cell_y = e.getY() / cell_height;

            if (!isBorderField(cell_x, cell_y) || !isEmptyCell(cell_x, cell_y)) return;

            field[cell_x][cell_y] = PLAYER_DOT;
            repaint();

            if (checkWin(PLAYER_DOT)) {
                gameOverState = PLAYER_WIN;
                gameOver = true;
                return;
            }

            if (checkFieldFull()) {
                gameOverState = DRAW;
                gameOver = true;
                return;
            }
            checkTurn = true;

        }


        if (mode == 1) {
            if (checkTurn == true) {

                if (gameOver || !initialized) {
                    return;
                }

                int x = e.getX() / cell_width;
                int y = e.getY() / cell_height;

                if (!isBorderField(x, y) || !isEmptyCell(x, y)) return;

                field[x][y] = PLAYER_DOT2;
                repaint();

                if (checkWin(PLAYER_DOT2)) {
                    gameOverState = PLAYER_WIN2;
                    gameOver = true;
                    return;
                }

                if (checkFieldFull()) {
                    gameOverState = DRAW;
                    gameOver = true;
                    return;
                }
                checkTurn = false;
            }

        } else {

            if (checkTurn == true) {

                moveAI();
                repaint();

                if (checkWin(AI_DOT)) {
                    gameOverState = AI_WIN;
                    gameOver = true;
                    return;
                }

                if (checkFieldFull()) {
                    gameOverState = DRAW;
                    gameOver = true;
                    return;
                }

                checkTurn = false;
            }

        }

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);

    }

    public void render(Graphics g) {
        if (!initialized) return;
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        cell_width = panelWidth / fieldSizeX;
        cell_height = panelHeight / fieldSizeY;
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                int x = i * cell_width;
                g.drawLine(0, x, panelWidth, x);
            }
        }
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                int y = i * cell_height;
                g.drawLine(y, 0, y, panelHeight);
            }
        }

        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {

                if (isEmptyCell(i, j)) {
                    continue;
                }
                if (field[i][j] == PLAYER_DOT) {
                    g.setColor(Color.BLUE);
                } else if (field[i][j] == PLAYER_DOT2) {
                    g.setColor(Color.GREEN);
                } else if (field[i][j] == AI_DOT) {
                    g.setColor(Color.RED);
                } else {
                    throw new RuntimeException("Неизвестное значение поля!!!" + field[i][j]);
                }


                g.fillOval(i * cell_width + DOTS_MARGIN, j * cell_height + DOTS_MARGIN,
                        cell_width - 2 * DOTS_MARGIN, cell_height - 2 * DOTS_MARGIN);

            }
        }

        if (gameOver) showGameOver(g);

    }

    private void showGameOver(Graphics g) {

        g.setColor(Color.GRAY);
        g.fillRect(0, 340, getWidth(), 130);
        g.setColor(Color.BLACK);
        g.setFont(font);

        switch (gameOverState) {
            case DRAW:
                g.drawString(DRAW_MSG, 295, getHeight() / 2);
                break;
            case PLAYER_WIN:
                g.drawString(PLAYER_WIN_MSG, 220, getHeight() / 2);
                break;
            case PLAYER_WIN2:
                g.drawString(PLAYER_WIN_MSG2, 220, getHeight() / 2);
                break;
            case AI_WIN:
                g.drawString(AI_WIN_MSG, 160, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("Неизвестный gameOverState" + gameOverState);
        }

    }

    public void startNewGame(int mode, int fieldSizeX, int fieldSizeY, int winLen) {
        this.mode = mode;
        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        this.winLen = winLen;
        field = new int[fieldSizeX][fieldSizeY];
        gameOver = false;
        initialized = true;
        repaint();


    }


    public void moveAI() {
        if (aiWin()) return;
        if (lockPlayer()) return;

        int x;
        int y;

        do {
            x = rnd.nextInt(fieldSizeX);
            y = rnd.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[x][y] = AI_DOT;

    }

    public boolean aiWin() {

        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isEmptyCell(i, j)) {
                    field[i][j] = AI_DOT;
                    if (checkWin(AI_DOT)) {
                        return true;
                    }
                    field[i][j] = EMPTY_DOT;
                }
            }
        }
        return false;
    }

    public boolean lockPlayer() {

        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isEmptyCell(i, j)) {
                    field[i][j] = PLAYER_DOT;
                    if (checkWin(PLAYER_DOT)) {
                        field[i][j] = AI_DOT;
                        return true;
                    }
                    field[i][j] = EMPTY_DOT;
                }
            }
        }
        return false;
    }

    public boolean isEmptyCell(int x, int y) {
        return field[x][y] == EMPTY_DOT;
    }

    public boolean isBorderField(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    public boolean checkWin(int dot) {

        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {

                if (line(i, j, 1, 0, winLen, dot)) return true;
                if (line(i, j, 1, 1, winLen, dot)) return true;
                if (line(i, j, 0, 1, winLen, dot)) return true;
                if (line(i, j, -1, 1, winLen, dot)) return true;
                if (line(i, j, -1, 0, winLen, dot)) return true;
                if (line(i, j, -1, -1, winLen, dot)) return true;
                if (line(i, j, 0, -1, winLen, dot)) return true;
                if (line(i, j, 1, -1, winLen, dot)) return true;
            }
        }

        return false;
    }

    public boolean line(int x, int y, int vx, int vy, int len, int dot) {

        int far_x = x + (len - 1) * vx;
        int far_y = y + (len - 1) * vy;
        if (!isBorderField(far_x, far_y)) return false;
        for (int i = 0; i < len; i++) {
            if (field[x + i * vx][y + i * vy] != dot) return false;
        }
        return true;
    }


    public boolean checkFieldFull() {

        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (field[i][j] == EMPTY_DOT) {
                    return false;
                }
            }
        }
        return true;
    }

}
