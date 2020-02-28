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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.silvio.everis.contacts.model.Address;
import br.com.silvio.everis.contacts.model.Contact;
import br.com.silvio.everis.contacts.model.Phone;
import br.com.silvio.everis.contacts.service.ContactService;

@RestController
@Transactional
@RequestMapping(value="/contacts")
public class ContactsController {
	
	@Autowired
	ContactService service;
	
	private static final Logger logger = LoggerFactory.getLogger(ContactsController.class);

	private void fillContactLinks(final Contact contact) {
		var contactId = contact.getId();
		Link selfLink = linkTo(ContactsController.class).slash(contactId).withSelfRel();
		
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
	
	@GetMapping(value="/", produces={"application/hal+json"})
	public CollectionModel<Contact> getContacts() {
		var contacts = service.loadContacts();
		
//		for (var contact : contacts) {
//			fillContactLinks(contact);
//		}

		contacts.forEach(contact -> fillContactLinks(contact));
		
		Link link = linkTo(ContactsController.class).withSelfRel();
		var result = new CollectionModel<Contact>(contacts, link);
		
		return result;
	}
	
	@GetMapping(value="/{contactId}/addresses", produces={"application/hal+json"})
	public CollectionModel<Address> getAddressesForContact(@PathVariable final Long contactId) {
		var addresses = service.loadContactAddresses(contactId);
		
		for (var address : addresses) {
			Link selfLink = linkTo(methodOn(ContactsController.class)
					.getAddressById(contactId, address.getId())).withSelfRel();
			address.add(selfLink);
		}
		
		Link link = linkTo(methodOn(ContactsController.class)
				.getAddressesForContact(contactId)).withSelfRel();
		var result = new CollectionModel<Address>(addresses, link);
		
		return result;
	}

	@GetMapping(value="/{contactId}/phones", produces={"application/hal+json"})
	public CollectionModel<Phone> getPhonesForContact(@PathVariable final Long contactId) {
		var phones = service.loadContactPhones(contactId);
		
		for (var phone : phones) {
			Link selfLink = linkTo(methodOn(ContactsController.class)
					.getPhoneById(contactId, phone.getId())).withSelfRel();
			phone.add(selfLink);
		}
		
		Link link = linkTo(methodOn(ContactsController.class)
				.getPhonesForContact(contactId)).withSelfRel();
		var result = new CollectionModel<Phone>(phones, link);
		
		return result;
	}

	@GetMapping(value="/{contactId}", produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Contact>> getContactById(
								@PathVariable final Long contactId) {
		var contact = service.loadContactById(contactId);
		
		if (contact != null) {
			fillContactLinks(contact);
			
			Link link = linkTo(methodOn(ContactsController.class)
					.getContactById(contactId)).withSelfRel();
			var entityModel = new EntityModel<Contact>(contact, link);
			
			return ResponseEntity.ok(entityModel);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping(value="/{contactId}/address/{addressId}",
				produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Address>> getAddressById(
								@PathVariable final Long contactId,
								@PathVariable final Long addressId) {
		var address = service.loadAddressById(addressId);
		
		if (address != null) {
			Link link = linkTo(methodOn(ContactsController.class)
					.getAddressById(contactId, addressId)).withSelfRel();
			var entityModel = new EntityModel<Address>(address, link);
			
			return ResponseEntity.ok(entityModel);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping(value="/{contactId}/phone/{phoneId}",
				produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Phone>> getPhoneById(
								@PathVariable final Long contactId,
								@PathVariable final Long phoneId) {
		var phone = service.loadPhoneById(phoneId);
		
		if (phone != null) {
			Link link = linkTo(methodOn(ContactsController.class)
					.getPhoneById(contactId, phoneId)).withSelfRel();
			var entityModel = new EntityModel<Phone>(phone, link);
			
			return ResponseEntity.ok(entityModel);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping(value="/",
				 consumes={"application/json"},
				 produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Contact>> addContact(
								@RequestBody final Contact contact) {
		try {
			var newContact = service.addContact(contact);
	
			Link link = linkTo(methodOn(ContactsController.class)
					.getContactById(newContact.getId())).withSelfRel();
			var entityModel = new EntityModel<Contact>(newContact, link);
			
			return ResponseEntity.ok(entityModel);
		} catch (Exception e) {
			logger.error("addContact fails: ".concat(e.getMessage()));
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping(value="/{contactId}/address",
				 consumes={"application/json"},
				 produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Address>> addAddress(
								@PathVariable final Long contactId,
								@RequestBody final Address address) {
		try {
			var contact = service.loadContactById(contactId);
			
			if (contact != null) {
				address.setContact(contact);
				
				var newAddress = service.addAddress(address);
				
				Link link = linkTo(methodOn(ContactsController.class)
						.getAddressById(contactId, newAddress.getId())).withSelfRel();
				var entityModel = new EntityModel<Address>(newAddress, link);
				
				return ResponseEntity.ok(entityModel);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			logger.error("addAddress fails: ".concat(e.getMessage()));
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping(value="/{contactId}/phone",
				 consumes={"application/json"},
				 produces={"application/hal+json"})
	public ResponseEntity<EntityModel<Phone>> addPhone(
								@PathVariable final Long contactId,
								@RequestBody final Phone phone) {
		try {
			var contact = service.loadContactById(contactId);
			
			if (contact != null) {
				phone.setContact(contact);
				
				var newPhone = service.addPhone(phone);
				
				Link link = linkTo(methodOn(ContactsController.class)
						.getPhoneById(contactId, newPhone.getId())).withSelfRel();
				var entityModel = new EntityModel<Phone>(newPhone, link);
				
				return ResponseEntity.ok(entityModel);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			logger.error("addPhone fails: ".concat(e.getMessage()));
			return ResponseEntity.badRequest().build();
		}
	}
}
