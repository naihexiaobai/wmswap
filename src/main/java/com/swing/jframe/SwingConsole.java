package com.swing.jframe;

import javax.swing.*;

/**
 * Created by admin on 2017/10/31.
 */
public class SwingConsole {
    public static void run(final JFrame frame, String title, final int width, final int height) {
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setVisible(true);
    }
}
