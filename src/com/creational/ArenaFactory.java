package com.creational;

import com.keyboardwarrior.*;

public class ArenaFactory {	
	public static Arena create(int playerDamage, int monsterHp) {
		Player player = new Player();
		player.setDamage(new Damage(playerDamage));
		
		Monster monster = new Monster();
		monster.setHp(monsterHp);
		
		Arena arena = Arena.getInstance();
		arena.setMonster(monster);
		arena.setPlayer(player);
		
		return arena;
	}
}
