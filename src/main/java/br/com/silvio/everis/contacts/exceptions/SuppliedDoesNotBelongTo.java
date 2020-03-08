/**
 * 
 */
package br.com.silvio.everis.contacts.exceptions;

/**
 * @author silvio.araujo
 *
 */
public class SuppliedDoesNotBelongTo extends InvalidInput {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SuppliedDoesNotBelongTo(String elementSupplied, String elementOwner) {
		super("supplied ".concat(elementSupplied).concat(" does not belong to ")
				.concat(elementOwner));
	}
}
