/**
 * 
 */
package br.com.silvio.everis.contacts.exceptions;

/**
 * Class that extends InvalidInput, mounting common exception message.
 * Used when an element does not belong to another one, when it is
 * expected to belong, so the operation becomes invalid.
 * 
 * @author silvio.araujo
 *
 */
public class SuppliedDoesNotBelongTo extends InvalidInput {

	private static final long serialVersionUID = 1L;

	/**
	 * Class constructor.
	 * 
	 * @param elementSupplied	the element supplied.
	 * @param elementOwner	the element which MUST BE owner of supplied element.
	 */
	public SuppliedDoesNotBelongTo(String elementSupplied, String elementOwner) {
		super("supplied ".concat(elementSupplied).concat(" does not belong to ")
				.concat(elementOwner));
	}
}
