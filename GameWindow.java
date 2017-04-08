package gameXOswing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Java. Swing game "Cross-zero"
 * @author Aleksey Malyshev
 * @version 1.0.1 dated March 15, 2017
 */
public class GameWindow extends JFrame{

    public static final int SIZE_WIDTH = 855;
    public static final int SIZE_HEGHT = 900;

    public WindowGameSelection windowGameSelection;
    public Map map;



    GameWindow (){

        setTitle("Крестики нолики");
        setSize(SIZE_WIDTH, SIZE_HEGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        windowGameSelection = new WindowGameSelection(this);



        JPanel butonPanel = new JPanel();
        map = new Map();
        add(butonPanel, BorderLayout.SOUTH);
        add(map, BorderLayout.CENTER);

        butonPanel.setLayout(new GridLayout( 1,2));

        JButton btnStart = new JButton("Создать новую игру");
        JButton btnExit = new JButton("Выход");

        butonPanel.add(btnStart);
        butonPanel.add(btnExit);

        btnStart.addActionListener(new ActionListener(){
            @Override
                public void actionPerformed(ActionEvent e) {
                windowGameSelection.setVisible(true);
            }
        });

        btnExit.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);
        windowGameSelection.setVisible(true);
    }


    public void startNewGame (int mode, int fieldSizeX, int fieldSizeY, int winLen){
        map.startNewGame(mode, fieldSizeX, fieldSizeY, winLen);
        map.checkTurn = false;
    }



}
