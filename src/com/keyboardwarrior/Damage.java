package com.keyboardwarrior;

public class Damage {
	private int damage;

	public Damage () {
		
	}
	
	public Damage(int damage) {
		this.setDamage(damage);
	}
	
	public int toInt() {
		return this.getDamage();
	}
	
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
}
