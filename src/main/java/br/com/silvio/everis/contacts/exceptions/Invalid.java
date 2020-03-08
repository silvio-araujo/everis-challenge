/**
 * 
 */
package br.com.silvio.everis.contacts.exceptions;

/**
 * Class that extends InvalidInput, mounting common exception message.
 * Used when an element is somehow invalid for that operation.
 * 
 * @author silvio.araujo
 *
 */
public class Invalid extends InvalidInput {

	private static final long serialVersionUID = 1L;

	/**
	 * Class constructor.
	 * 
	 * @param element	the element that is invalid and provokes exception.
	 */
	public Invalid(String element) {
		super(element.concat(" is invalid"));
	}
}
