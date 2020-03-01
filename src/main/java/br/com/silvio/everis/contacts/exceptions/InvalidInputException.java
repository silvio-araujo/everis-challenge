package br.com.silvio.everis.contacts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class to treat exceptions caused by invalid inputs.
 * Aggregates "bad request" status too, so when raised inside controller,
 * this exception returns this status to caller automatically.
 * 
 * @author silvio.araujo
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Class constructor.
	 */
	public InvalidInputException() {
		super("Invalid input");
	}
}
