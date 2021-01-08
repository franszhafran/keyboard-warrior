package com.keyboardwarrior;

import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import com.creational.*;

public class Game extends JPanel  {
	private int level;
	private Arena arena;
	private ArrayList<Integer> pattern;
	private int input;
	private char inputKey;
	
	/**
	 * Set up the game
	 */
	public Game() {
		initArrays();
		initGame();
		windowSet();
		
		KeyListener l = new GameKeyboardListener();
		this.addKeyListener(l);
		
		this.setFocusable(true);
		
		inputKey = 'a';
	}
	
	private void initArrays() {
		pattern = new ArrayList<Integer>();
	}
	
	private void initGame() {
		ArenaFactoryConfigBuilder arenaConfigB = new ArenaFactoryConfigBuilder();
		arenaConfigB.setMonsterHP(500);
		arenaConfigB.setPlayerDamage(50);
		
		ArenaFactory.createFromConfig(arenaConfigB.build());
		level = 1;
		
		generatePattern();
	}
	private void windowSet() {
		int width = 500;
		int height = 500;
		this.setPreferredSize(new Dimension(width, height));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.drawString(String.valueOf(inputKey), 25, 25);
	}
	
	public void upLevel() {
		this.level++;
		generatePattern();
	}
	
	public void reset() {
		level = 1;
		pattern.clear();
		generatePattern();
	}
	
	public void playSoundAndPattern() {
		
	}
	
	/**
	 * Read user's input and compare with the pattern
	 */
	public void readAndCheckInput() {
		try {
			System.out.println(getKeyIndex(inputKey));
			input = getKeyIndex(inputKey);
		} catch (Exception e) {
			return;
		}
		
		if(pattern.get(level-1) == input) {
			upLevel();
		} else {
			reset();
		}
		
		repaint();
	}
	
	private void generatePattern() {
		generatePattern(1);
	}
	
	private void generatePattern(int gen) {
		for(int i=0;i<gen;i++) {
			pattern.add(generateRandomNumber(0, 3));
		}
	}
	
	private int generateRandomNumber(int min, int max) {
	    Random random = new Random();
	    return random.nextInt(max - min) + min;
	}
	
	/**
	 * Convert input char to an integer [0,3]. Throws error when input outside bounds
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private int getKeyIndex(char key) throws Exception {
		String i = String.valueOf(key);
		if(i.equals("w")) {
			return 0;
		} else if(i.equals("a")) {
			return 1;
		} else if(i.equals("s")) {
			return 2;
		} else if(i.equals("d")) {
			return 3;
		} else {
			throw new Exception("unknown_letter");
		}
	}
	
	/**
	 * The game's keyboard listener
	 * @author frans
	 *
	 */
	private class GameKeyboardListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			inputKey = e.getKeyChar();
			readAndCheckInput();
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			
		}
	}
}
