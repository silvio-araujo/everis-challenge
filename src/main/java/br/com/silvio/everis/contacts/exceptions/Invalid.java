/**
 * 
 */
package br.com.silvio.everis.contacts.exceptions;

/**
 * @author silvio.araujo
 *
 */
public class Invalid extends InvalidInput {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Invalid(String element) {
		super(element.concat(" is invalid"));
	}
}
