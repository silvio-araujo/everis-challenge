/**
 * 
 */
package br.com.silvio.everis.contacts.exceptions;

/**
 * Class that extends InvalidInput, mounting common exception message.
 * Used when an element is mandatory for an operation, and it was omitted.
 * 
 * @author silvio.araujo
 *
 */
public class Mandatory extends InvalidInput {

	private static final long serialVersionUID = 1L;

	/**
	 * Class constructor.
	 * 
	 * @param element	the element that is mandatory and provokes exception.
	 */
	public Mandatory(String element) {
		super(element.concat(" is mandatory"));
	}
}
