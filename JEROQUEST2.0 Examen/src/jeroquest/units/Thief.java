package jeroquest.units;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import jeroquest.boardgame.Dice;
import jeroquest.boardgame.XYLocation;
import jeroquest.logic.Game;
import jeroquest.utils.DynamicVectorObjects;

public class Thief extends Goblin implements Invisible {

	// initial values for the attributes
	protected static final int MOVEMENT = 10;
	protected static final int ATTACK = 2;
	protected static final int DEFENCE = 2;
	protected static final int BODY = 1;

	private Bolsa bolsa;
	private boolean hidden;

	/**
	 * Create a Thief from its name
	 * 
	 * @param name Thief's name
	 */
	public Thief(String name) {
		super(name, DEFENCE);
		this.bolsa=new Bolsa ();
		this.show();
	}

	/**
	 * Gets the thief bag
	 * 
	 * @return This thief's bag
	 */
	public Bolsa getBolsa() {
		return this.bolsa;
	}

	/**
	 * Check wether this thief is hidden
	 * 
	 * @return true iff this thief is hidden
	 */
	public boolean isHidden() {
		return this.hidden;
	}

	/**
	 * Un-hides this thief
	 */
	public void show () {
		this.hidden = false;
	}

	/**
	 * Tries to hide the thief.
	 * Conditions:
	 * 	Thief cannot have any alive enemies 3 squares from his position
	 */
	public void hide (Game pa) {
		if (!this.threeSquaresAway(pa)) {
			System.out.printf("%s is now hidden!\n", this.getName());
			this.hidden=true;
		} else {
			if (Dice.roll(2)==1) {
				System.out.printf("%s is now hidden!\n", this.getName());
				this.hidden=true;
			}
		}
	}

	/**
	 * Resolves this thief's turn
	 * 
	 * If it is not hidden, it will try to hide
	 * else, it will play as a Goblin
	 */
	@Override
	public void resolveTurn(Game currentGame) {
		if (!((Invisible)this).isHidden()) {
			this.hide(currentGame);
		}
		super.resolveTurn(currentGame);
	}
	
	/**
	 * Used when the thief combats
	 * 
	 * If the thief is hidden, his bag is not full and he is attacking a Hero with a weapon (not null) ==> Weapon is stolen by the thief
	 * else, combats like a normal Goblin
	 */
	@Override
	public void combat(Character c, Game currentGame) { // attacks to c and c defends itself
		if (this.isHidden() && !this.getBolsa().isFull() && c instanceof Hero && ((Hero)c).getWeapon()!=null) {
			System.out.printf("%s has stolen %s's Weapon (%s)!\n", this.getName(), c.getName(), ((Hero)c).getWeapon().getName());
			this.getBolsa().save(((Hero)c).getWeapon());
			((Hero)c).setWeapon(null);
			this.show();
		} else {
			super.combat(c, currentGame);
		}
	}
	
	/**
	 * Prints this thief's information, including the bag.
	 */
	@Override
	public String toString () {
		return String.format("%s %s", super.toString(), this.getBolsa().toString());
	}
	
	/**
	 * Returns true iff there is a live enemy 3 squares away from caller
	 * @param pa
	 * @return
	 */
	public boolean threeSquaresAway (Game pa) {
		int j=0;
		for (int i=0;i<pa.getCharacters().length && 
				(pa.getCharacters()[i].isAlive() && this.isEnemy(pa.getCharacters()[i]) && XYLocation.distanceOrthogonal(this.getPosition(), pa.getCharacters()[i].getPosition())<3); i++) {
			j++;
		}
		return j==pa.getCharacters().length;
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
		return 'T';
	}

	/************************************************
	 * Interface GraphicElement implementation
	 **********************************************/

	// Goblin icon
	private static Icon icon = new ImageIcon(ClassLoader.getSystemResource("jeroquest/gui/images/goblin.png"));
	private static Icon hiddenIcon = new ImageIcon (ClassLoader.getSystemResource("jeroquest/gui/images/square.png"));

	public Icon getImage() {
		if (this.isHidden()) {
			return hiddenIcon;
		}
		return icon;
		
	}

}
