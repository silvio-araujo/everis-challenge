/**
 * 
 */
package br.com.silvio.everis.contacts.exceptions;

/**
 * @author silvio.araujo
 *
 */
public class Mandatory extends InvalidInput {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Mandatory(String element) {
		super(element.concat(" is mandatory"));
	}
}
