package com.keyboardwarrior;

import java.awt.Color;
import java.awt.Graphics;

public class Key {
//	private boolean pressed;
	public static final int RELEASED = 1;
	public static final int PRESSED = 2;
	public static final int ERROR = 3;
	public static final int SUCCESS = 4;

	private static final int width = 50;

	private int x, y, state;
	private String url, character;
	private SoundPlayer sound;

	public Key(int x, int y, String character) {
		this.character = character;
		this.state = RELEASED;
		this.url = "/Sound/" + character + "key.wav";
		this.x = x;
		this.y = y;
//		sound = new SoundPlayer(url);
	}

	public void render(Graphics g) {
		// render tuts sesuai dengan state
		if (this.state == PRESSED) {
			// blue
			g.setColor(new Color(1, 48, 118));

		} else if (this.state == ERROR) {
			// red
			g.setColor(new Color(139, 0, 0));
		} else if (this.state == SUCCESS) {
			// red
			g.setColor(new Color(45, 107, 79));

		} else {
			g.setColor(new Color(58, 58, 58));
		}

		g.fillRect(this.x, this.y, width, width);

		// render text
		int stringPosX = (int) this.x + (width / 3);
		int stringPosY = this.y + (width / 2);
		g.setColor(Color.WHITE);
		g.drawString(String.valueOf(this.character), stringPosX, stringPosY);
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void play() {
		sound = new SoundPlayer(url);
		sound.play();
	}
}
