package br.com.silvio.everis.contacts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.silvio.everis.contacts.exceptions.InvalidInputException;
import br.com.silvio.everis.contacts.exceptions.ResourceNotFoundException;
import br.com.silvio.everis.contacts.model.Address;
import br.com.silvio.everis.contacts.model.Contact;
import br.com.silvio.everis.contacts.model.Phone;
import br.com.silvio.everis.contacts.service.ContactService;

/**
 * Contacts controller class.
 * 
 * @author silvio.araujo
 *
 */
@RestController
@Transactional
@RequestMapping(value="/contacts")
public class ContactsController {
	
	@Autowired
	ContactService service;
	
	private static final Logger logger = LoggerFactory.getLogger(ContactsController.class);

	/**
	 * Treats exceptions, logging message and returning the same exception,
	 * due to be rethrown.
	 * 
	 * @param contact	the contact to be filled
	 */
	private RuntimeException treatException(String methodName, RuntimeException e) {
		logger.error(String.format("%s fails: %s", methodName, e.getMessage()));
		return e;
	}
	
	/**
	 * Logs success message, formatted and parameterized as used in String.format
	 * 
	 * @param format	the format string to be used in String.format
	 * @param args	the list of object arguments for String.format
	 */
	private void logSuccess(String format, Object... args) {
		logger.info(String.format(format, args));
	}
	
	/**
	 * Fills contact links, required by HATEOAS
	 * 
	 * @param contact	the contact to be filled
	 */
	private void fillContactLinks(final Contact contact) {
		final var contactId = contact.getId();
		final var selfLink = linkTo(ContactsController.class).slash(contactId).withSelfRel();
		
		contact.add(selfLink);
		
		if (service.loadContactAddresses(contactId).size() > 0) {
			Link addressLink = linkTo(methodOn(ContactsController.class)
					.getAddressesForContact(contactId)).withRel("addresses");
			contact.add(addressLink);
		}
		
		if (service.loadContactPhones(contactId).size() > 0) {
			Link phoneLink = linkTo(methodOn(ContactsController.class)
					.getPhonesForContact(contactId)).withRel("phones");
			contact.add(phoneLink);
		}
	}
	
	/**
	 * Get all contacts from database.
	 * 
	 * URL (GET): http://localhost:8080/contacts
	 * 
	 * @return	the response, filled with the collection model of contacts.
	 */
	@GetMapping(value="",
				produces={"application/hal+json"})
	public ResponseEntity<CollectionModel<Contact>> getContacts() {
		final var methodName = new Object() {}
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			var contacts = service.loadContacts();
			
			contacts.forEach(this::fillContactLinks);
			
			Link link = linkTo(ContactsController.class).withSelfRel();
			var collectionModel = new CollectionModel<Contact>(contacts, link);
			
			logSuccess("%s: %d contact(s) loaded\n%s",
					   methodName, contacts.size(), contacts.toString());
			
			return ResponseEntity.ok(collectionModel);
		} catch (RuntimeException e) {
			throw treatException(methodName, e);
		}
	}
	
	/**
	 * Get all addresses from a contact.
	 * 
	 * URL (GET): http://localhost:8080/contacts/{contactId}/addresses
	 * 
	 * @return	the response, filled with the collection model of contacts.
	 * @throws ResourceNotFoundException, InvalidInputException 
	 */
	@GetMapping(value="/{contactId}/addresses",
				produces={"application/hal+json"})
	public ResponseEntity<CollectionModel<Address>> getAddressesForContact(
								@PathVariable final Long contactId) {
		final var methodName = new Object() {}
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			var addresses = service.loadContactAddresses(contactId);
			
			for (var address : addresses) {
				Link selfLink = linkTo(methodOn(ContactsController.class)
						.getAddressById(contactId, address.getId())).withSelfRel();
				address.add(selfLink);
			}
			
			Link link = linkTo(methodOn(ContactsController.class)
					.getAddressesForContact(contactId)).withSelfRel();
			var collectionModel = new CollectionModel<Address>(addresses, link);
			
			logSuccess("%s: %d address(es) loaded\n%s",
					   methodName, addresses.size(), addresses);
			
			return ResponseEntity.ok(collectionModel);
		} catch (RuntimeException e) {
			throw treatException(methodName, e);
		}
	}

	/**
	 * Get all phones from a contact.
	 * 
	 * URL (GET): http://localhost:8080/contacts/{contactId}/phones
	 * 
	 * @return	the response, filled with the collection model of phones.
	 * @throws ResourceNotFoundException, InvalidInputException 
	 */
	@GetMapping(value="/{contactId}/phones",
				produces={"application/hal+json"})
	public ResponseEntity<CollectionModel<Phone>> getPhonesForContact(
							@PathVariable final Long contactId) throws ResourceNotFoundException, InvalidInputException {
		final var methodName = new Object() {}
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			var phones = service.loadContactPhones(contactId);
			
			for (var phone : phones) {
				Link selfLink = linkTo(methodOn(ContactsController.class)
						.getPhoneById(contactId, phone.getId())).withSelfRel();
				phone.add(selfLink);
			}
			
			Link link = linkTo(methodOn(ContactsController.class)
					.getPhonesForContact(contactId)).withSelfRel();
			var collectionModel = new CollectionModel<Phone>(phones, link);
			
			logSuccess("%s: %d phone(s) loaded\n%s",
					   methodName, phones.size(), phones.toString());

			return ResponseEntity.ok(collectionModel);
		} catch (RuntimeException e) {
			throw treatException(methodName, e);
		}
	}

	/**
	 * Get a contact by its ID.
	 * 
	 * URL (GET): http://localhost:8080/contacts/{contactId}
	 * 
	 * @return	the response, filled with the collection model of phones.
	 */
	@GetMapping(value="/{contactId}",
				produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Contact>> getContactById(
								@PathVariable final Long contactId) throws ResourceNotFoundException, InvalidInputException {
		final var methodName = new Object() {}
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			var contact = service.loadContactById(contactId);
			
			if (contact != null) {
				fillContactLinks(contact);
				
				Link link = linkTo(methodOn(ContactsController.class)
						.getContactById(contactId)).withSelfRel();
				var entityModel = new EntityModel<Contact>(contact, link);
				
				logSuccess("%s: contact loaded\n%s", methodName, contact.toString());

				return ResponseEntity.ok(entityModel);
			} else {
				throw new ResourceNotFoundException(Contact.class, contactId);
			}
		} catch (RuntimeException e) {
			throw treatException(methodName, e);
		}
	}
	
	/**
	 * Get an address by its ID.
	 * 
	 * URL (GET): http://localhost:8080/contacts/{contactId}/address/{addressId}
	 * 
	 * @param contactId	the contact ID of the owner of wanted address.
	 * @param addressId	the address ID.
	 * 
	 * @return	the response, filled with the entity model of address.
	 * @throws ResourceNotFoundException, InvalidInputException 
	 */
	@GetMapping(value="/{contactId}/address/{addressId}",
				produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Address>> getAddressById(
								@PathVariable final Long contactId,
								@PathVariable final Long addressId) throws ResourceNotFoundException, InvalidInputException {
		final var methodName = new Object() {}
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			var address = service.loadAddressById(addressId);
			
			if (address != null) {
				if (address.getContact().getId().equals(contactId)) {
					Link link = linkTo(methodOn(ContactsController.class)
							.getAddressById(contactId, addressId)).withSelfRel();
					var entityModel = new EntityModel<Address>(address, link);
					
					logSuccess("%s: address loaded\n%s", methodName, address.toString());

					return ResponseEntity.ok(entityModel);
				} else {
					throw new InvalidInputException("supplied address ID does not belong to supplied contact ID");
				}
			} else {
				throw new ResourceNotFoundException(Address.class, addressId);
			}
		} catch (RuntimeException e) {
			throw treatException(methodName, e);
		}
	}

	/**
	 * Get a phone by its ID.
	 * 
	 * URL (GET): http://localhost:8080/contacts/{contactId}/phone/{phoneId}
	 * 
	 * @param contactId	the contact ID of the owner of wanted phone.
	 * @param phoneId	the phone ID.
	 * 
	 * @return	the response, filled with the entity model of phone.
	 * @throws ResourceNotFoundException, InvalidInputException 
	 */
	@GetMapping(value="/{contactId}/phone/{phoneId}",
				produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Phone>> getPhoneById(
								@PathVariable final Long contactId,
								@PathVariable final Long phoneId) throws ResourceNotFoundException, InvalidInputException {
	    final var methodName = new Object() {}
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			var phone = service.loadPhoneById(phoneId);
			
			if (phone != null) {
				Link link = linkTo(methodOn(ContactsController.class)
						.getPhoneById(contactId, phoneId)).withSelfRel();
				var entityModel = new EntityModel<Phone>(phone, link);
				
				logSuccess("%s: phone loaded\n%s", methodName, phone.toString());

				return ResponseEntity.ok(entityModel);
			} else {
				throw new ResourceNotFoundException(Phone.class, phoneId);
			}
		} catch (RuntimeException e) {
			throw treatException(methodName, e);
		}
	}
	
	/**
	 * Adds a new contact in database.
	 * 
	 * URL (POST): http://localhost:8080/contacts
	 * 
	 * @param contact	the new contact.
	 * @return	the response, filled with the entity model of contact.
	 * @throws ResourceNotFoundException, InvalidInputException 
	 */
	@PostMapping(value="",
				 consumes={"application/json"},
				 produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Contact>> addContact(
								@RequestBody final Contact contact) throws ResourceNotFoundException, InvalidInputException {
	    final var methodName = new Object() {}
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			var newContact = service.addContact(contact);
	
			Link link = linkTo(methodOn(ContactsController.class)
					.getContactById(newContact.getId())).withSelfRel();
			var entityModel = new EntityModel<Contact>(newContact, link);
			
			logSuccess("%s: contact added\n%s", methodName, contact.toString());

			return ResponseEntity.ok(entityModel);
		} catch (RuntimeException e) {
			throw treatException(methodName, e);
		}
	}
	
	/**
	 * Adds a new address for a given contact.
	 * 
	 * URL (POST): http://localhost:8080/contacts/{contactId}/address
	 * 
	 * @param contactId	the contact ID for whom we want to add an address.
	 * @param address	the address to be added.
	 * @return	the response, filled with the entity model of address.
	 * @throws ResourceNotFoundException, InvalidInputException 
	 */
	@PostMapping(value="/{contactId}/address",
				 consumes={"application/json"},
				 produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Address>> addAddress(
								@PathVariable final Long contactId,
								@RequestBody final Address address) throws ResourceNotFoundException, InvalidInputException {
	    final var methodName = new Object() {}
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			var contact = service.loadContactById(contactId);
			
			if (contact != null) {
				address.setContact(contact);
				
				var newAddress = service.addAddress(address);
				
				Link link = linkTo(methodOn(ContactsController.class)
						.getAddressById(contactId, newAddress.getId())).withSelfRel();
				var entityModel = new EntityModel<Address>(newAddress, link);

				logSuccess("%s: address added\n%s", methodName, address.toString());
				
				return ResponseEntity.ok(entityModel);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (RuntimeException e) {
			throw treatException(methodName, e);
		}
	}

	/**
	 * Adds a new phone for a given contact.
	 * 
	 * URL (POST): http://localhost:8080/contacts/{contactId}/phone
	 * 
	 * @param contactId	the contact ID for whom we want to add a phone.
	 * @param phone	the phone to be added.
	 * @return	the response, filled with the entity model of phone.
	 * @throws ResourceNotFoundException, InvalidInputException 
	 */
	@PostMapping(value="/{contactId}/phone",
				 consumes={"application/json"},
				 produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Phone>> addPhone(
								@PathVariable final Long contactId,
								@RequestBody final Phone phone) throws ResourceNotFoundException, InvalidInputException {
	    final var methodName = new Object() {}
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			var contact = service.loadContactById(contactId);
			
			if (contact != null) {
				phone.setContact(contact);
				
				var newPhone = service.addPhone(phone);
				
				Link link = linkTo(methodOn(ContactsController.class)
						.getPhoneById(contactId, newPhone.getId())).withSelfRel();
				var entityModel = new EntityModel<Phone>(newPhone, link);
				
				logSuccess("%s: phone added\n%s", methodName, phone.toString());
				
				return ResponseEntity.ok(entityModel);
			} else {
				throw new ResourceNotFoundException(Contact.class, contactId);
			}
		} catch (RuntimeException e) {
			throw treatException(methodName, e);
		}
	}

	/**
	 * Updates a contact.
	 * 
	 * URL (PUT): http://localhost:8080/contacts
	 * 
	 * @param contact	the contact to be updated.
	 * @return	the response, filled with the entity model of contact.
	 * @throws ResourceNotFoundException, InvalidInputException 
	 */
	@PutMapping(value="",
				consumes={"application/json"},
				produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Contact>> updateContact(
								@RequestBody final Contact contact) throws ResourceNotFoundException, InvalidInputException {
	    final var methodName = new Object() {}
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			var updatedContact = service.updateContact(contact);
	
			Link link = linkTo(methodOn(ContactsController.class)
					.getContactById(updatedContact.getId())).withSelfRel();
			var entityModel = new EntityModel<Contact>(updatedContact, link);
			
			logSuccess("%s: contact updated\n%s", methodName, updatedContact.toString());
			
			return ResponseEntity.ok(entityModel);
		} catch (RuntimeException e) {
			throw treatException(methodName, e);
		}
	}

	/**
	 * Updates an address.
	 * 
	 * URL (PUT): http://localhost:8080/contacts/{contactId}/address
	 * 
	 * @param contactId	the contact ID of the owner of address.
	 * @param address	the address to be updated.
	 * @return	the response, filled with the entity model of address.
	 * @throws ResourceNotFoundException, InvalidInputException 
	 */
	@PutMapping(value="/{contactId}/address",
				consumes={"application/json"},
				produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Address>> updateAddress(
								@PathVariable final Long contactId,
								@RequestBody final Address address) throws ResourceNotFoundException, InvalidInputException {
	    final var methodName = new Object() {}
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			var updatedAddress = service.updateAddress(contactId, address);
				
			Link link = linkTo(methodOn(ContactsController.class)
					.getAddressById(contactId, updatedAddress.getId())).withSelfRel();
			var entityModel = new EntityModel<Address>(updatedAddress, link);
				
			logSuccess("%s: address updated\n%s", methodName, updatedAddress.toString());
			
			return ResponseEntity.ok(entityModel);
		} catch (RuntimeException e) {
			throw treatException(methodName, e);
		}
	}

	/**
	 * Updates a phone.
	 * 
	 * URL (PUT): http://localhost:8080/contacts/{contactId}/phone
	 * 
	 * @param contactId	the contact ID of the owner of phone.
	 * @param phone	the phone to be updated.
	 * @return	the response, filled with the entity model of phone.
	 * @throws ResourceNotFoundException, InvalidInputException 
	 */
	@PutMapping(value="/{contactId}/phone",
				consumes={"application/json"},
				produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Phone>> updatePhone(
								@PathVariable final Long contactId,
								@RequestBody final Phone phone) throws ResourceNotFoundException, InvalidInputException {
	    final var methodName = new Object() {}
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			var updatedPhone = service.updatePhone(contactId, phone);
			
			Link link = linkTo(methodOn(ContactsController.class)
					.getPhoneById(contactId, updatedPhone.getId())).withSelfRel();
			var entityModel = new EntityModel<Phone>(updatedPhone, link);
			
			logSuccess("%s: phone updated\n%s", methodName, updatedPhone.toString());

			return ResponseEntity.ok(entityModel);
		} catch (RuntimeException e) {
			throw treatException(methodName, e);
		}
	}
	
	/**
	 * Deletes a contact.
	 * 
	 * URL (DELETE): http://localhost:8080/contacts/{contactId}
	 * 
	 * @param contactId	the contact to be deleted.
	 * @return	the response status.
	 */
	@DeleteMapping(value="/{contactId}")
	public ResponseEntity<Void> deleteContact(@PathVariable final Long contactId) {
	    final var methodName = new Object() {}
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			service.deleteContact(contactId);

			logSuccess("%s: contact with ID = %d deleted\n", methodName, contactId);

			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			throw treatException(methodName, e);
		}
	}

	/**
	 * Deletes an address.
	 * 
	 * URL (DELETE): http://localhost:8080/contacts/{contactId}/address/{addressId}
	 * 
	 * @param contactId	the contact ID of address owner.
	 * @param addressId	the address ID to be deleted.
	 * @return	the response status.
	 */
	@DeleteMapping(value="/{contactId}/address/{addressId}")
	public ResponseEntity<Void> deleteAddress(
					@PathVariable final Long contactId,
					@PathVariable final Long addressId) {
	    final var methodName = new Object() {}
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			service.deleteAddress(contactId, addressId);

			logSuccess("%s: address with ID = %d deleted\n", methodName, addressId);

			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			throw treatException(methodName, e);
		}
	}

	/**
	 * Deletes a phone.
	 * 
	 * URL (DELETE): http://localhost:8080/contacts/{contactId}/phone/{phoneId}
	 * 
	 * @param contactId	the contact ID of phone owner.
	 * @param addressId	the phone ID to be deleted.
	 * @return	the response status.
	 */
	@DeleteMapping(value="/{contactId}/phone/{phoneId}")
	public ResponseEntity<Void> deletePhone(
					@PathVariable final Long contactId,
					@PathVariable final Long phoneId) {
	    final var methodName = new Object() {}
	    	.getClass().getEnclosingMethod()
	      .getClass()
	      .getEnclosingMethod()
	      .getName();
	      
		try {
			service.deletePhone(contactId, phoneId);

			logSuccess("%s: phone with ID = %d deleted\n", methodName, phoneId);

			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			logger.error("deletePhone fails: ".concat(e.getMessage()));
			throw e;
		}
	}
}
