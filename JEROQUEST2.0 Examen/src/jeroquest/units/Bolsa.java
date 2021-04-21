package jeroquest.units;

import jeroquest.boardgame.Dice;
import jeroquest.utils.DynamicVectorObjects;

public class Bolsa extends DynamicVectorObjects {

	private int size;		//Number of items in bag NOT ACTUAL SIZE
	
	/**
	 * Default constructor for the bag
	 * Content is set to 0
	 * Maximum size is set to a random int [3,5]
	 */
	public Bolsa () {
		super(Dice.roll(3)+2);
		this.setSize(0);		//contenido inicial 0
	}
	
	/**
	 * Changes the number of items in bag
	 * 
	 * @param size - number of items in bag
	 */
	public void setSize(int size)  {
		this.size = size;
	}
	
	/**
	 * Gets the number of items inside the bag
	 * 
	 * @return int number of items in the bag
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Checks wether the bag is full or not
	 * 
	 * @return boolean - true iff the number of items is different (expected lower) than the total size of the bag
	 */
	public boolean isFull() {
		return this.getSize() == super.length();
	}
	
	/**
	 * Gets the weapon inside the bag
	 * Only if the bag is not already full.
	 * 
	 * @param a - weapon to be put in the bag
	 */
	public void save (Weapon a) {
		if (!this.isFull()) {
			this.remove(this.getSize());
			this.insert(this.getSize(), a);
			
			this.setSize(this.getSize()+1);
		}
	}
	
	/**
	 * Returns the Weapon[] vector from the bag
	 * 
	 * @return Weapon [] - bag contents
	 */
	public Weapon[] getItems() {
		Weapon[] temp = new Weapon[this.length()];
		for (int i = 0; i < temp.length; i++)
			temp[i] = (Weapon) this.get(i);
		return temp;
	}
	
	/**
	 * Outputs all the information on the bag, total size and content.
	 */
	@Override
	public String toString () {
		String s = "Una bolsa de capacidad "+this.length() + " conteniendo:\n";
		for (Weapon w : this.getItems()) {
			if (w==null) {
				s+=" - ";
			} else {
				s+=w.toString() + "\t";
			}
		}
		return s;
	}
}
