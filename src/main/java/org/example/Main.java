package org.example;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        JFrame jFrame=new JFrame();
        jFrame.setBounds(0,0,740,416);
        jFrame.add(new ManualScreen(0,0,740,416));
        jFrame.setLayout(null);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
    }
}