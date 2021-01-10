package com.keyboardwarrior;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.creational.ArenaFactory;
import com.creational.ArenaFactoryConfigBuilder;

public class Game extends JPanel {
	private int level;
	private Arena arena;
	private ArrayList<Integer> pattern;
	private ArrayList<Integer> inputArr;
	private ArrayList<Key> keys;
	private int input;
	private char inputKey;
	private boolean isPlayingSoundAndPattern = false;

	/**
	 * Set up the game
	 */
	public Game() {
		initKeys();
		initArrays();
		initGame();

		windowSet();

		KeyListener l = new GameKeyboardListener();
		this.addKeyListener(l);

		this.setFocusable(true);

		inputKey = 'a';

		playSoundAndPattern();
	}

	private void initArrays() {
		pattern = new ArrayList<Integer>();
		inputArr = new ArrayList<Integer>();
	}

	private void initKeys() {
		this.keys = new ArrayList<Key>();
		this.keys.add(new Key(0, 0, "s"));
		this.keys.add(new Key(60, 0, "d"));
		this.keys.add(new Key(120, 0, "f"));
		this.keys.add(new Key(180, 0, "j"));
		this.keys.add(new Key(240, 0, "k"));
	}

	private void initGame() {
		ArenaFactoryConfigBuilder arenaConfigB = new ArenaFactoryConfigBuilder();
		arenaConfigB.setMonsterHP(500);
		arenaConfigB.setPlayerDamage(50);

		ArenaFactory.createFromConfig(arenaConfigB.build());
		level = 1;

		generatePattern();
		System.out.println("Monster HP: " + Arena.getInstance().getMonster().getHp());
		System.out.println(pattern);
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

		for (Key key : keys) {
			key.render(g);
		}
	}

	public void upLevel() {
		Arena.getInstance().getPlayer().getDamage().setDamage(level);
		this.level++;
		generatePattern();
		Arena.getInstance().playerAttack();
		System.out.println("Monster HP: " + Arena.getInstance().getMonster().getHp());
		System.out.println(pattern);
	}

	public void reset() {
		level = 1;
		Arena.getInstance().getPlayer().getDamage().setDamage(level);
		Arena.getInstance().getMonster().setHp(500);
		pattern.clear();
		resetInput();
		generatePattern();
		System.out.println("You Lose ;(");
		System.out.println(pattern);
		playError();
		ActionListener togglePlayPattern = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				playSoundAndPattern();
				repaint();
			}
		};
		Timer timer = new Timer((1500), togglePlayPattern);
		timer.setRepeats(false);
		timer.start();
	}

	public void resetInput() {
		inputArr.clear();
	}

	public void playError() {
		for (final Key key : keys) {
			key.setState(Key.ERROR);

			ActionListener toggleOff = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					key.setState(Key.RELEASED);
					repaint();
				}
			};

			Timer timer = new Timer((1000), toggleOff);
			timer.setRepeats(false);
			timer.start();
		}
	}

	public void playSoundAndPattern() {
		isPlayingSoundAndPattern = true;
		for (int i = 0; i < pattern.size(); i++) {
			final int e = pattern.get(i);

			ActionListener toggleOn = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					keys.get(e).setState(Key.PRESSED);
					repaint();
				}
			};
			Timer timer = new Timer(1000 * (i) + 500, toggleOn);
			timer.setRepeats(false);
			timer.start();

			ActionListener toggleOff = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					keys.get(e).setState(Key.RELEASED);
					repaint();
				}
			};
			Timer timer2 = new Timer((1000 * (i) + 1000), toggleOff);
			timer2.setRepeats(false);
			timer2.start();
		}
		ActionListener toggleOff = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				isPlayingSoundAndPattern = false;
			}
		};
		Timer timer2 = new Timer((1000 * (pattern.size() - 1) + 1000), toggleOff);
		timer2.setRepeats(false);
		timer2.start();
	}

	/**
	 * Read user's input and compare with the pattern
	 */
	public void readAndCheckInput() {
		try {
//			System.out.println(getKeyIndex(inputKey));
			input = getKeyIndex(inputKey);
			inputArr.add(input);
		} catch (Exception e) {
			return;
		}

		if (inputArr.get(inputArr.size() - 1) == pattern.get(inputArr.size() - 1)) {
			System.out.println("Ok");
		} else {
			reset();
			System.out.println("WRONG!");
		}

		if (inputArr.size() == pattern.size()) {
			upLevel();
			ActionListener toggleOn = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					playSoundAndPattern();
				}
			};
			Timer timer = new Timer(1000, toggleOn);
			timer.setRepeats(false);
			timer.start();
			resetInput();
		}

		repaint();
	}

	private void generatePattern() {
		generatePattern(1);
	}

	private void generatePattern(int gen) {
		for (int i = 0; i < gen; i++) {
			pattern.add(generateRandomNumber(0, 5));
		}
	}

	private int generateRandomNumber(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min) + min;
	}

	/**
	 * Convert input char to an integer [0,3]. Throws error when input outside
	 * bounds
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private int getKeyIndex(char key) throws Exception {
		String i = String.valueOf(key);
		if (i.equals("s")) {
			return 0;
		} else if (i.equals("d")) {
			return 1;
		} else if (i.equals("f")) {
			return 2;
		} else if (i.equals("j")) {
			return 3;
		} else if (i.equals("k")) {
			return 4;
		} else {
			throw new Exception("unknown_letter");
		}
	}

	/**
	 * The game's keyboard listener
	 * 
	 * @author frans
	 *
	 */
	private class GameKeyboardListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			if (!isPlayingSoundAndPattern) {
				inputKey = e.getKeyChar();
				readAndCheckInput();
				if (String.valueOf(inputKey).equals("p")) {
					playSoundAndPattern();
				}
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (isPlayingSoundAndPattern) {
				return;
			}

			for (Key key : keys) {
				if (key.getCharacter().equals(String.valueOf(e.getKeyChar()))) {
					key.setState(key.PRESSED);
				}
			}

			repaint();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (isPlayingSoundAndPattern) {
				return;
			}

			for (Key key : keys) {
//				if error sign is currently on
				if (key.getState() != key.ERROR) {
					if (key.getCharacter().equals(String.valueOf(e.getKeyChar()))) {
						key.setState(key.RELEASED);
					}
				}
			}

			repaint();
		}
	}
}
