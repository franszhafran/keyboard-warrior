package com.keyboardwarrior;

import java.awt.Color;
import java.awt.Graphics;

public class ProgressBar {
	private int maxValue, value, x, y, height, width;
	private Color baseColor, foreColor;

	public ProgressBar(int maxValue, int value, int x, int y, int width, int height) {
		this.maxValue = maxValue;
		this.value = value;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		baseColor = Color.GRAY;
		foreColor = new Color(139, 0, 0);
	}

	private double getUnit() {
		return (double) width / maxValue;
	}

	public void render(Graphics g) {
		g.setColor(baseColor);
		g.fillRect(this.x, this.y, width, height);

//		System.out.println(getUnit());
//		System.out.println(value);
//		System.out.println((int) getUnit()*value);
		g.setColor(foreColor);
		g.fillRect(this.x, this.y, (int) (getUnit() * value), height);
		
		g.setColor(Color.WHITE);
		g.drawString(String.valueOf(value), this.x+5, this.y+16);
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void addValue(int value) {
		this.value = this.value + value;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Color getBaseColor() {
		return baseColor;
	}

	public void setBaseColor(Color baseColor) {
		this.baseColor = baseColor;
	}

	public Color getForeColor() {
		return foreColor;
	}

	public void setForeColor(Color foreColor) {
		this.foreColor = foreColor;
	}

}
