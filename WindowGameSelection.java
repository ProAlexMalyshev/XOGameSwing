package gameXOswing;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 + * Java. Swing game "Cross-zero"
 + * @author Aleksey Malyshev
 + * @version 1.0.1 dated March 15, 2017
 + */
public class WindowGameSelection extends JFrame {

    public static final int SIZE_WIDTH = 400;
    public static final int SIZE_HEGHT = 300;
    public static final int MIN_WIN_LEN = 2;
    public static final int MIN_SIZE_MAP = 3;
    public static final int MAX_SIZE_MAP = 10;

    public final GameWindow gameWindow;

    public JRadioButton rbtn_ia_vs_player;
    public JRadioButton rbtn_player_vs_player;
    public JSlider sld_map_size;
    public JSlider sld_win_len;


    public WindowGameSelection (GameWindow gameWindow){

        this.gameWindow = gameWindow;
        setSize(SIZE_WIDTH, SIZE_HEGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Окно настроек игры");
        setLayout(new GridLayout(10 ,1));
        addGameModeSelection();
        addGameOptionsSelection();
        JButton btnStartGame = new JButton("Начать игру");
        btnStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                startGame();
            }
        });
        add(btnStartGame);


    }

    private void addGameModeSelection(){
        add(new JLabel("  Выбор режима игры"));
        rbtn_ia_vs_player = new JRadioButton("Игрок против компьютера", true);
        rbtn_player_vs_player = new JRadioButton("Игрок против игрока");
        ButtonGroup btnG = new ButtonGroup();
        btnG.add(rbtn_ia_vs_player);
        btnG.add(rbtn_player_vs_player);
        add(rbtn_ia_vs_player);
        add(rbtn_player_vs_player);

    }

    private void addGameOptionsSelection(){
        JLabel lbWinLen = new JLabel();
        sld_win_len = new JSlider(MIN_WIN_LEN, MIN_WIN_LEN, MIN_WIN_LEN);
        sld_win_len.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lbWinLen.setText("  Длина выигрышной линии " + sld_win_len.getValue());

            }
        });


        JLabel lbMapSize = new JLabel();
        sld_map_size = new JSlider(MIN_SIZE_MAP, MAX_SIZE_MAP);
        sld_map_size.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                lbMapSize.setText("  Размер поля " + sld_map_size.getValue() + " X " + sld_map_size.getValue());
                sld_win_len.setMaximum(sld_map_size.getValue());

            }
        });
        sld_map_size.setValue(MIN_SIZE_MAP);

        add(new JLabel("  Выберите размер поля:"));
        add(lbMapSize);
        add(sld_map_size);
        add(new JLabel("  Выберите длину выигрышной последовательности:"));
        add(lbWinLen);
        add(sld_win_len);


    }

    private void startGame (){

        int gameMode;
        if(rbtn_ia_vs_player.isSelected()){
            gameMode = Map.AI_VS_PLAYER;
        }else if(rbtn_player_vs_player.isSelected()){
            gameMode = Map.PLAYER_VS_PLAYER;
        }else {
            throw new RuntimeException ("Не выбранно не одного режима игры");
        }

        int fildSize = sld_map_size.getValue();
        int lenSize = sld_win_len.getValue();
        setVisible(false);

        gameWindow.startNewGame(gameMode, fildSize, fildSize, lenSize);

    }


}
