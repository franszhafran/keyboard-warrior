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

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.creational.ArenaFactory;
import com.creational.ArenaFactoryConfigBuilder;

public class Game extends JPanel {
	private int patternLevel;
	private int gameLevel = 1;
	private Arena arena;
	private ArrayList<Integer> pattern;
	private ArrayList<Integer> inputArr;
	private ArrayList<Key> keys;
	private SoundPlayer error;
	private ProgressBar monsterHp;
	private int input;
	private char inputKey;
	private boolean isPlayingSoundAndPattern = false;
	private boolean isWinning = false;

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
		this.keys.add(new Key(110, 300, "S"));
		this.keys.add(new Key(170, 300, "D"));
		this.keys.add(new Key(230, 300, "F"));
		this.keys.add(new Key(290, 300, "J"));
		this.keys.add(new Key(350, 300, "K"));
		for (int i = 0; i < keys.size(); i++) {
			System.out.println(keys.get(i).getCharacter());
		}
	}

	private void initGame() {
		ArenaFactoryConfigBuilder arenaConfig = new ArenaFactoryConfigBuilder();
		arenaConfig.setMonsterHP(15);
		arenaConfig.setPlayerDamage(1);

		ArenaFactory.createFromConfig(arenaConfig.build());
		patternLevel = 1;

		generatePattern();
		monsterHp = new ProgressBar(Arena.getInstance().getMonster().getHp(), Arena.getInstance().getMonster().getHp(),
				360, 100, 120, 25);
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

		g.drawString("Monster HP: " + Arena.getInstance().getMonster().getHp(), 200, 380);
		g.drawString("Level: " + gameLevel, 200, 400);

		for (Key key : keys) {
			key.render(g);
		}
		monsterHp.render(g);
	}

	private void setPlayerDamageBasedOnPatternLevel() {
		Arena.getInstance().getPlayer().getDamage().setDamage(patternLevel);
	}
	
	private void gamePlayerAttackAndDrawHp() {
		Arena.getInstance().playerAttack();
		drawMonsterHp();
	}
	
	public void upPatternLevel() {
		this.patternLevel++;
		
		setPlayerDamageBasedOnPatternLevel();
		generatePattern();
		gamePlayerAttackAndDrawHp();
		
		System.out.println("Monster HP: " + Arena.getInstance().getMonster().getHp());

		if (Arena.getInstance().getMonster().getHp() <= 0) {
//			ActionListener toggleDialog = new ActionListener() {
//				public void actionPerformed(ActionEvent evt) {
//					int x = 5;
//					x = JOptionPane.showOptionDialog(null, "Congratulations, You Won!", "Congratulations",
//							JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
//							new String[] { "Next Level" }, "default");
//					System.out.println("dialog callback");
//					System.out.println(x);
//					if(x == 0) {
//						System.out.println("X is");
//						System.out.println(x);
//						gameLevel++;
//						isWinning = true;
//						reset();
//					}
//				}
//			};
//			Timer timer = new Timer(1000, toggleDialog);
//			timer.setRepeats(false);
//			timer.start();
			int x = -1;
			x = JOptionPane.showOptionDialog(null, "Congratulations, You Won!", "Congratulations",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
					new String[] { "Next Level" }, "default");
			
			if(x == 0) {
				upGameLevel();
			}
		}
	}

	public void setMonsterHp(int gameLevel) {
		if (gameLevel == 1) {
			Arena.getInstance().getMonster().setHp(15);
		} else if (gameLevel == 2) {
			Arena.getInstance().getMonster().setHp(55);
		} else if (gameLevel == 3) {
			Arena.getInstance().getMonster().setHp(200);
		}
	}

	private void drawMonsterHp() {
		if (Arena.getInstance().getMonster().getHp() > monsterHp.getMaxValue()) {
			monsterHp.setMaxValue(Arena.getInstance().getMonster().getHp());
			monsterHp.setValue(Arena.getInstance().getMonster().getHp());
		} else {
			monsterHp.setValue(Arena.getInstance().getMonster().getHp());
		}
	}
	
	private void setAndDrawMonsterHpBasedOnGameLevel() {
		setAndDrawMonsterHp(gameLevel);
	}
	
	private void setAndDrawMonsterHp(int gameLevel) {
		setMonsterHp(gameLevel);
		drawMonsterHp();
	}
	
	
	
	public void upGameLevel() {
		gameLevel++;
		resetPatternAndPlayNewPattern();
		playError();
	}

	public void resetPatternAndPlayNewPattern() {
		// reset pattern
		patternLevel = 1;
		pattern.clear();
		
		// prepare new game with new pattern level
		setPlayerDamageBasedOnPatternLevel();
		setAndDrawMonsterHpBasedOnGameLevel();
		resetInput();
		
		// create new pattern
		generatePattern();
		
		// play the new pattern
		ActionListener togglePlayPattern = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				playInitialSound();
				playSoundAndPattern();
				repaint();
			}
		};
		Timer timer = new Timer((1500), togglePlayPattern);
		timer.setRepeats(false);
		timer.start();
	}
	
	private void clearAndDrawLastInput() {
		keys.get(pattern.get(pattern.size()- 1)).setState(Key.RELEASED);
		repaint();
	}

	public void resetInput() {
		inputArr.clear();
	}

	public void playError() {
		error = new SoundPlayer("/Sound/error.wav");
		error.play();

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

	public void playInitialSound() {

		for (final Key key : keys) {
			key.setState(Key.PRESSED);

			ActionListener toggleOff = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					key.setState(Key.RELEASED);

					repaint();
				}
			};

			Timer timer = new Timer((100), toggleOff);
			timer.setRepeats(false);
			timer.start();
		}

	}

	public void playSoundAndPattern() {
		isPlayingSoundAndPattern = true;
		int time = 500 - pattern.size() * 20;
		for (int i = 0; i < pattern.size(); i++) {
			final int e = pattern.get(i);

			ActionListener toggleOn = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					keys.get(e).setState(Key.PRESSED);
					keys.get(e).play();
					repaint();
				}
			};
			Timer timer = new Timer(time * (2 * i + 1), toggleOn);
//			System.out.println(time * (2 * i + 1));
			timer.setRepeats(false);
			timer.start();

			ActionListener toggleOff = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					keys.get(e).setState(Key.RELEASED);
					repaint();
				}
			};
			Timer timer2 = new Timer(time * (2 * i + 2), toggleOff);
//			System.out.println(time * (2 * i + 2));
//			System.out.println();
			timer2.setRepeats(false);
			timer2.start();
		}
		ActionListener toggleOff = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				isPlayingSoundAndPattern = false;

			}
		};
		Timer timer2 = new Timer(time * ((pattern.size() + 1) * 2 - 1), toggleOff);
//		System.out.println(time * ((pattern.size()) * 2 - 1));
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
			System.out.println("input");
			System.out.println(input);
			inputArr.add(input);
		} catch (Exception e) {
			return;
		}

		if (inputArr.get(inputArr.size() - 1) == pattern.get(inputArr.size() - 1)) {
			keys.get(input).play();
			System.out.println("Ok");
		} else {
			resetPatternAndPlayNewPattern();
			System.out.println("WRONG!");
		}

		// if pattern successfully played
		if (inputArr.size() == pattern.size()) {
			upPatternLevel();
			
			// play pattern
			ActionListener toggleOn = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					playSoundAndPattern();
				}
			};

			Timer timer = new Timer(1000, toggleOn);
			timer.setRepeats(false);
			if (monsterHp.getValue() != monsterHp.getMaxValue()) {
				timer.start();
			}
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
		String i = String.valueOf(key).toUpperCase();
		if (i.equals("S")) {
			return 0;
		} else if (i.equals("D")) {
			return 1;
		} else if (i.equals("F")) {
			return 2;
		} else if (i.equals("J")) {
			return 3;
		} else if (i.equals("K")) {
			return 4;
		} else {
			System.out.println("masuk errro");
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
//					key.play();
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
