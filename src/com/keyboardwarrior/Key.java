package com.keyboardwarrior;

import java.awt.Color;
import java.awt.Graphics;

public class Key {
//	private boolean pressed;
	public static final int RELEASED = 1;
	public static final int PRESSED = 2;
	public static final int ERROR = 3;
	private int x, y, state;

	private String character;

	public Key(int x, int y, String character) {
		this.character = character;
		this.state = RELEASED;
		this.x = x;
		this.y = y;
	}

	public void render(Graphics g) {
		// render tuts sesuai dengan state
		if (this.state == PRESSED) {
			g.setColor(Color.BLUE);

		} else if (this.state == ERROR) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.WHITE);
		}

		g.fillRect(this.x, this.y, 50, 50);

		// render text
		int stringPosX = (int) this.x + (50 / 3);
		int stringPosY = this.y + (50 / 2);
		g.setColor(Color.BLACK);
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
}
