package com.keyboardwarrior;

public class Arena {
	private static Arena arena;
	private Monster monster;
	private Player player;
	
//	implement singleton pattern
	private Arena() {
		
	}
	
	public static Arena getInstance() {
		if(arena == null) {
			arena = new Arena();
		}
		
		return arena;
	}
	
	public void playerAttack() {
		monster.attacked(player.getDamage());
	}

	public static Arena getArena() {
		return arena;
	}

	public static void setArena(Arena arena) {
		Arena.arena = arena;
	}

	public Monster getMonster() {
		return monster;
	}

	public void setMonster(Monster monster) {
		this.monster = monster;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
