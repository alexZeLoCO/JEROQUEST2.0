package jeroquest.units;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import jeroquest.boardgame.Dice;
import jeroquest.logic.Game;
import jeroquest.utils.DynamicVectorCharacters;

public class Zombie extends Monster {

	// initial values for the attributes
	protected static final int MOVEMENT = 4;
	protected static final int ATTACK = 3;
	protected static final int DEFENCE = 0;
	protected static final int BODY = 3;

	/**
	 * Create a zombie from its name
	 * 
	 * @param name name of the zombie
	 */
	public Zombie(String name) {
		super(name, MOVEMENT, ATTACK, DEFENCE, BODY);
	}

	/**
	 * Reduces body points by one
	 * 
	 */
	public void degradacion () {
		if (this.getBody()>0) {
			System.out.printf("%s loses 1 HP due to degradation.\n", this.getName());
			setBody(getBody()-1);
		}
	}
	
	/**
	 * A zombie does not receive any damage
	 * 
	 */
	@Override
	public int defend (int impacts) {
		return 0;
	}
	
	/**
	 * Returns an array with the valid targets for this character
	 * 
	 * @param currentGame the current game
	 * @return the valid targets for the character in its current position
	 */
	@Override
	public DynamicVectorCharacters validTargets(Game currentGame) {
		// search targets
		DynamicVectorCharacters validTargets = new DynamicVectorCharacters();

		// It needs to iterate the characters in the game and add the valid targets,
		// that is:
		// 1.- the ones alive
		// 2.- that are enemies
		// 3.- that are at attack range
		for (Character character : currentGame.getCharacters()) {
			if (character.isAlive()) {
				if (isEnemy(character)) {
					if (isAtRange(character.getPosition()))
						if (character instanceof Barbarian) {
							validTargets.insert(0, character);
						} else {
							validTargets.add(character);
						}
				}
			}
		}

		return validTargets;
	}
	
	/**
	 * Combat action for the character
	 * 
	 * @param currentGame game in which the character has to act
	 * @return true if the character has to fight an enemy, false otherwise
	 */
	@Override
	public boolean actionCombat(Game currentGame) {
		// Attack a random enemy
		DynamicVectorCharacters targets = validTargets(currentGame);

		if (targets.length() >= 0) {
			Character target;
			
			if (barbarianTarget(targets)>0) {
				target = targets.get(Dice.roll(barbarianTarget(targets))-1);
			} else {
				target = targets.get(Dice.roll(targets.length()) - 1);
			}
			
			System.out.println(
					this.getName() + this.getPosition() + " attacks to " + target.getName() + target.getPosition());
			this.combat(target, currentGame);
			return true;
		}
		return false;
	}
	
	/**
	 * Returns number of barbarians in target vector
	 * @param targets vector
	 * @return int number of barbarians
	 */
	public int barbarianTarget (DynamicVectorCharacters targets) {
		int i=0;
		if (targets.length()>=0) {
			while (!(targets.get(i) instanceof Barbarian)) {
				i++;
			}
		} 
		return i;
	}
	
	/************************************************
	 * Interface Piece implementation
	 **********************************************/

	/**
	 * Generate a text representation of the character in the board (implementing an
	 * abstract method)
	 * 
	 * @return the text representation of the object in the board
	 */
	public char toChar() {
		return 'Z';
	}

	/************************************************
	 * Interface GraphicElement implementation
	 **********************************************/

	// Icon of a zombie
	private static Icon icon = new ImageIcon(ClassLoader.getSystemResource("jeroquest/gui/images/zombi.gif"));

	public Icon getImage() {
		return icon;
	}
	
	

}
