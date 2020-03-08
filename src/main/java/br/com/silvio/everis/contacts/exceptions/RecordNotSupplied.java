/**
 * 
 */
package br.com.silvio.everis.contacts.exceptions;

/**
 * @author silvio.araujo
 *
 */
public class RecordNotSupplied extends InvalidInput {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RecordNotSupplied(String element) {
		super(element.concat(" record was not supplied"));
	}


}
