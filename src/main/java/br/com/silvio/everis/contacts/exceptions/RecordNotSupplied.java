/**
 * 
 */
package br.com.silvio.everis.contacts.exceptions;

/**
 * Class that extends InvalidInput, mounting common exception message.
 * Used when a record for a specific element was expected and was not supplied.
 * 
 * @author silvio.araujo
 *
 */
public class RecordNotSupplied extends InvalidInput {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Class constructor.
	 * 
	 * @param element	the element whose record was not supplied and provokes exception.
	 */
	public RecordNotSupplied(String element) {
		super(element.concat(" record was not supplied"));
	}


}
