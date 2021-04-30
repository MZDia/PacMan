package com.zetcode;

import javafx.application.Application;
import javax.swing.*;
import java.awt.*;
import java.awt.EventQueue;
import javax.swing.JFrame;

public class Main extends JFrame {

public Main(){

	  initUI();


}
  private void initUI() {

        add(new Pacman_game());

        setResizable(false);
        pack();

        setTitle("PacMan");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
       Main mna = new Main();
        EventQueue.invokeLater(() -> {
            JFrame ex = new Main();
            ex.setVisible(true);
        });

    }




}