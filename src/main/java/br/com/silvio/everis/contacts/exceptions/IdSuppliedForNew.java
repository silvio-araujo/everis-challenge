/**
 * 
 */
package br.com.silvio.everis.contacts.exceptions;

/**
 * Class that extends InvalidInput, mounting common exception message.
 * Used when an element was sent to be inserted, but containing its ID,
 * what is not expected for the insert operation.
 * 
 * @author silvio.araujo
 *
 */
public class IdSuppliedForNew extends InvalidInput {

	private static final long serialVersionUID = 1L;

	/**
	 * Class constructor.
	 * 
	 * @param element	the element whose ID was erroneously supplied for a new record.
	 */
	public IdSuppliedForNew(String element) {
		super(element.concat(" ID must not be supplied for new records"));
	}
}
