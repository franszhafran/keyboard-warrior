package com.keyboardwarrior;

import com.interfaces.Attackable;;

public class Monster extends Creature implements Attackable {
	private int hp;
	
	public Monster() {
		
	}
	
	public Monster(int hp) {
		this.setHp(hp);
	}
	
	@Override
	public void attacked(Damage damage) {
		this.setHp(getHp() - damage.toInt());
	}
	
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
}