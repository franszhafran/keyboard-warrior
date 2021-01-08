package com.creational;

import com.keyboardwarrior.*;

public class ArenaFactory {
	public static Arena createFromConfig(ArenaFactoryConfig config) {
		Player player = new Player();
		player.setDamage(new Damage(config.getPlayerDamage()));
		
		Monster monster = new Monster();
		monster.setHp(config.getMonsterHP());
		
		Arena arena = Arena.getInstance();
		arena.setMonster(monster);
		arena.setPlayer(player);
		
		return arena;
	}
}
