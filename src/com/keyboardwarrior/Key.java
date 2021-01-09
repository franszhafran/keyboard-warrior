package com.keyboardwarrior;

import java.awt.Color;
import java.awt.Graphics;

public class Key {
	private boolean pressed;
	private int x, y;
	private String character;
	
	public Key(int x, int y, String character, boolean pressed) {
		this.character = character;
		this.pressed = pressed;
		this.x = x;
		this.y = y;
	}
	
	public void render(Graphics g) {
		//render tuts sesuai dengan state
		if (this.pressed) {
			g.setColor(Color.BLUE);
		} else {
			g.setColor(Color.WHITE);
		}
		
		g.fillRect(this.x, this.y, 50, 50);
		
		//render text
		int stringPosX = (int)this.x + (50 / 3);
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

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
}
