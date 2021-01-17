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

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.creational.ArenaFactory;

public class Game extends JPanel {
	private int patternLevel;
	private int gameLevel;
	private ArrayList<Integer> pattern;
	private ArrayList<Integer> inputArr;
	private ArrayList<Key> keys;
	private SoundPlayer error;
	private ProgressBar monsterHp;
	private int input;
	private char inputKey;
	private boolean isPlayingSoundAndPattern = false;
	private ImageIcon backgroundImg;
	private ArrayList<ImageIcon> monsterImgs;
	private ImageIcon logo = new ImageIcon("Resource/Image/logo.png");

	/**
	 * Set up the game
	 */
	public Game() {
		int x = -1;
		x = JOptionPane.showOptionDialog(null, "Are you ready?", "Keyboard Warrior", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, logo, new String[] { "Yes" }, "default");
		if (x == 0) {
			initArrays();
			initKeys();
			initGame();

			windowSet();

			KeyListener l = new GameKeyboardListener();
			this.addKeyListener(l);

			this.setFocusable(true);

			inputKey = 'a';

			playSoundAndPattern();
		} else {
			System.exit(0);
		}
	}

	private void initArrays() {
		pattern = new ArrayList<Integer>();
		inputArr = new ArrayList<Integer>();
		monsterImgs = new ArrayList<ImageIcon>();
		keys = new ArrayList<Key>();
	}

	private void initKeys() {
		keys.add(new Key(110, 410, "S"));
		keys.add(new Key(170, 410, "D"));
		keys.add(new Key(230, 410, "F"));
		keys.add(new Key(290, 410, "J"));
		keys.add(new Key(350, 410, "K"));
	}

	private void initGame() {
		ArenaFactory.create(1, 15);

		patternLevel = 1;
		gameLevel = 1;

		generatePattern();
		monsterHp = new ProgressBar(Arena.getInstance().getMonster().getHp(), Arena.getInstance().getMonster().getHp(),
				360, 220, 120, 25);
	}

	private void windowSet() {
		int width = 500;
		int height = 500;
		this.setPreferredSize(new Dimension(width, height));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		backgroundImg = new ImageIcon("Resource/Image/bg.jpg");
		backgroundImg.paintIcon(this, g, 0, 0);

		g.setColor(Color.WHITE);

		g.drawString("Monster HP: " + Arena.getInstance().getMonster().getHp(), 360, 260);
		g.drawString("Level: " + gameLevel, 220, 480);

		monsterImgs.add(new ImageIcon("Resource/Image/level1.png"));
		monsterImgs.add(new ImageIcon("Resource/Image/level2.png"));
		monsterImgs.add(new ImageIcon("Resource/Image/level3.png"));

		for (Key key : keys) {
			key.render(g);
		}
		monsterHp.render(g);

		monsterImgs.get(gameLevel - 1).paintIcon(this, g, 100, 95);
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

		if (Arena.getInstance().getMonster().getHp() <= 0) {
			int x = -1;
			x = JOptionPane.showOptionDialog(null, "Congratulations, You Won!", "Congratulations",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, logo, new String[] { "Next Level" },
					"default");

			if (x == 0) {
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
		blinkAndPlaySound("success");
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
				blinkAllKeys();
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

	public void blinkAndPlaySound(String type) {
		error = new SoundPlayer("/Sound/" + type + ".wav");
		error.play();

		for (final Key key : keys) {

			key.setState(Key.ERROR);
			if (type == "success")
				key.setState(Key.SUCCESS);

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

	public void blinkAllKeys() {
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

	/**
	 * Blink the key and play the sound for each pattern
	 */
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
			timer.setRepeats(false);
			timer.start();

			ActionListener toggleOff = new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					keys.get(e).setState(Key.RELEASED);
					repaint();
				}
			};
			Timer timer2 = new Timer(time * (2 * i + 2), toggleOff);
			timer2.setRepeats(false);
			timer2.start();
		}
		ActionListener toggleOff = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				isPlayingSoundAndPattern = false;

			}
		};
		Timer timer2 = new Timer(time * ((pattern.size() + 1) * 2 - 1), toggleOff);
		timer2.setRepeats(false);
		timer2.start();
	}

	/**
	 * Read user's input and compare with the pattern
	 */
	public void readAndCheckInput() {
		try {
			input = getKeyIndex(inputKey);
			inputArr.add(input);
		} catch (Exception e) {
			return;
		}

		if (inputArr.get(inputArr.size() - 1) == pattern.get(inputArr.size() - 1)) {
			keys.get(input).play();
		} else {
			resetPatternAndPlayNewPattern();
			blinkAndPlaySound("error");
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
			throw new Exception("unknown_letter");
		}
	}

	/**
	 * The game's keyboard listener
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
				if (key.getCharacter().equals(String.valueOf(e.getKeyChar()).toUpperCase())) {
					key.setState(Key.PRESSED);
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
				if (key.getState() != Key.ERROR) {
					if (key.getCharacter().equals(String.valueOf(e.getKeyChar()).toUpperCase())) {
						key.setState(Key.RELEASED);
					}
				}
			}

			repaint();
		}
	}
}
