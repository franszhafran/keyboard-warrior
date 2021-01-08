package com.keyboardwarrior;

import com.interfaces.*;

public class Player extends Creature implements CanAttack {
	private Damage damage;

	public void attack(Attackable any) {
		any.attacked(damage);
	}
	
	public Damage getDamage() {
		return damage;
	}

	public void setDamage(Damage damage) {
		this.damage = damage;
	}
}
