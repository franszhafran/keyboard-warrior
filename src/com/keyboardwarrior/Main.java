package com.keyboardwarrior;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			 public void run() {
			 JFrame frame = new JFrame("Keyboard Warrior");

			 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 frame.setContentPane(new Game());
			 frame.pack();
			 frame.setVisible(true);
			 }
		});
	}
}

