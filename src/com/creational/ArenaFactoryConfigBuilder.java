package com.creational;

public class ArenaFactoryConfigBuilder {
	private int monsterHp, playerDamage;
	
	public int getMonsterHP() {
		return monsterHp;
	}

	public void setMonsterHP(int monsterHp) {
		this.monsterHp = monsterHp;
	}

	public int getPlayerDamage() {
		return playerDamage;
	}

	public void setPlayerDamage(int playerDamage) {
		this.playerDamage = playerDamage;
	}
	
	public ArenaFactoryConfig build() {
		ArenaFactoryConfig config = new ArenaFactoryConfig();
		config.setMonsterHP(monsterHp);
		config.setPlayerDamage(playerDamage);
		
		return config;
	}
}
