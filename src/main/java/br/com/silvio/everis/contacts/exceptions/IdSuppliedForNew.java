/**
 * 
 */
package br.com.silvio.everis.contacts.exceptions;

/**
 * @author silvio.araujo
 *
 */
public class IdSuppliedForNew extends InvalidInput {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IdSuppliedForNew(String element) {
		super(element.concat(" ID must not be supplied for new records"));
	}
}
