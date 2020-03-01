package br.com.silvio.everis.contacts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class to treat exceptions caused by not found resources.
 * Aggregates "not found" status too, so when raised inside controller,
 * this exception returns this status to caller automatically.
 * 
 * @author silvio.araujo
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Class constructor.
	 * 
	 * @param clazz	the class of resource.
	 * @param id	the id of resource.
	 */
	public ResourceNotFoundException(Class<?> clazz, Long id) {
		super(String.format("Resource for %s with id = %d was not found",
							clazz.getSimpleName(), id));
	}
}
