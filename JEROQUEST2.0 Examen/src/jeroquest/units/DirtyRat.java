package jeroquest.units;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import jeroquest.boardgame.Dice;
import jeroquest.logic.Game;
import jeroquest.utils.DynamicVectorCharacters;

public class DirtyRat extends Monster {

	// initial values for the attributes
	protected static final int MOVEMENT = 4;
	protected static final int ATTACK = 2;
	protected static final int DEFENCE = 2;
	protected static final int BODY = 5;
	private boolean fearful;

	/**
	 * Create a rat from its name
	 * 
	 * @param name name of the rat
	 */
	public DirtyRat(String name) {
		super(name, MOVEMENT, ATTACK, DEFENCE, BODY);
		setFearful(false);
	}
	
	public boolean isFearful() {
		return this.fearful;
	}
	
	public void setFearful (boolean bool) {
		this.fearful = bool;
	}
	
	public void toggleFear() {
		this.fearful = !this.fearful;
	}
	
	/**
	 * Check if the character given as argument is an enemy of the current one. An
	 * enemy will be all characters that are monsters (Overridden method)
	 * 
	 * @param c character to check
	 * @return true if it an enemy of the current character
	 */
	@Override
	public boolean isEnemy(Character c) {
		return (super.isEnemy(c) || c.getBody() < this.getBody());
	}
	
	/**
	 * The monster defends itself from an attack (Implementation of an abstract
	 * inherited method)
	 * 
	 * @param impacts the total number of impacts to try to block or suffer
	 * @return the number of suffered wounds
	 */
	@Override
	public int defend(int impacts) {
		int wounds = super.defend(impacts);
		int original_impacts = impacts;

		if (wounds > 0) {
			if (this.isFearful() && wounds >= this.getBodyInitial() / 2) {
				System.out.printf("%s died from fear!\n", this.getName());
				this.setBody(0);
			} else {
				System.out.printf("%s is now scared.\n", this.getName());
				this.setFearful(true);
			}
		} else {
			System.out.printf("%s is now not scared anymore.\n",this.getName());
			this.setFearful(false);
		}

		return wounds;
	}
	
	/**
	 * AI: character's Artificial Intelligence It does the actions in a given turn:
	 * attack and movement
	 * 
	 * @param currentGame game in which the character has to act
	 */
	@Override
	public void resolveTurn(Game currentGame) {

		if (!((DirtyRat)this).isFearful()) {
			super.resolveTurn(currentGame);
		} else {
			System.out.printf("%s is too afraid to play!\n",this.getName());
		}
		
	}
	
	/**
	 * Increases rat HP by 1 iff not full already.
	 * 
	 */
	public void regenerate() {
		if (this.getBody()<this.getBodyInitial()) {
			this.setBody(this.getBody()+1);
		}
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
		return 'R';
	}

	/************************************************
	 * Interface GraphicElement implementation
	 **********************************************/

	// Icon of a rat
	private static Icon icon = new ImageIcon(ClassLoader.getSystemResource("jeroquest/gui/images/rata.PNG"));
	private static Icon scared_icon = new ImageIcon (ClassLoader.getSystemResource("jeroquest/gui/images/rata_asustada.PNG"));

	public Icon getImage() {
		if (this.isFearful()) {
			return scared_icon;
		} else {
			return icon;
		}
	}

}
