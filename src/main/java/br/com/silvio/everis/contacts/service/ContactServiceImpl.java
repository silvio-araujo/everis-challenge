package br.com.silvio.everis.contacts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.silvio.everis.contacts.dao.AddressDao;
import br.com.silvio.everis.contacts.dao.ContactDao;
import br.com.silvio.everis.contacts.dao.PhoneDao;
import br.com.silvio.everis.contacts.enums.PhoneType;
import br.com.silvio.everis.contacts.exceptions.InvalidInputException;
import br.com.silvio.everis.contacts.exceptions.ResourceNotFoundException;
import br.com.silvio.everis.contacts.model.Address;
import br.com.silvio.everis.contacts.model.Contact;
import br.com.silvio.everis.contacts.model.Phone;

/**
 * Contact service class.
 * 
 * @author silvio.araujo
 *
 */
@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	ContactDao contactDao;
	
	@Autowired
	AddressDao addressDao;
	
	@Autowired
	PhoneDao phoneDao;
	
	final String msgAddressNotSupplied = "address record was not supplied";
	final String msgPhoneNotSupplied = "phone record was not supplied";
	
	/**
	 * Validates contact record against rules (REGEX).
	 * 
	 * @param contact	the contact to be validated.
	 * @throws	InvalidInputException.
	 */
	private void validateContact(Contact contact) {
		var name = contact.getName();
		var cpf = contact.getCpf();
		
		if ((name != null) && (!name.isBlank())) {
			if (!name.matches("[A-ZÁÉÍÓÚÃÕÂ\\s]{10,}")) {
				throw new InvalidInputException("contact name is invalid");
			}
		} else {
			throw new InvalidInputException("contact name is mandatory");
		}
		
		if ((cpf != null) && (!cpf.matches("[0-9]{11}"))) {
			throw new InvalidInputException("contact CPF is invalid");
		}
	}
	
	/**
	 * Validates address record against rules (REGEX).
	 * 
	 * @param address	the address to be validated.	
	 * @throws	InvalidInputException.
	 */
	private void validateAddress(Address address) {
		var street = address.getStreet();
		var city = address.getCity();
		var zipCode = address.getZipCode();
		
		if ((street == null) || (street.isBlank())) {
			throw new InvalidInputException("address street is mandatory");
		}
		
		if ((city == null) || (city.isBlank())) {
			throw new InvalidInputException("address city is mandatory");
		}
		
		if ((zipCode != null) && (!zipCode.matches("\\d{5}-\\d{3}"))) {
				throw new InvalidInputException("address zip code is invalid");
		}
	}
	
	/**
	 * Validates phone record against rules (REGEX).
	 * 
	 * @param phone	the phone to be validated.	
	 * @throws	InvalidInputException.
	 */
	private void validatePhone(Phone phone) {
		var phoneType = phone.getPhoneType();
		var ddi = phone.getDdi();
		var ddd = phone.getDdd();
		var number = phone.getNumber();
		var phoneRegex = (phoneType == PhoneType.FIX) ? "[2-5]\\d{7}" : "9\\d{8}";

		if (ddi == null) {
			throw new InvalidInputException("phone ddi is mandatory");
		}
		
		if (ddd == null) {
			throw new InvalidInputException("phone ddd is mandatory");
		}
		
		if (number == null) {
			throw new InvalidInputException("phone number is mandatory");
		}
		
		if (!ddi.matches("\\d{1,3}")) {
			throw new InvalidInputException("phone ddi is invalid");
		}
		
		if (!ddd.matches("\\d{1,2}")) {
			throw new InvalidInputException("phone ddd is invalid");
		}

		if (!number.matches(phoneRegex)) {
			throw new InvalidInputException("phone number is invalid");
		}
	}
	
	/**
	 * Loads all contacts in database.
	 * 
	 * @return	a list of contacts.
	 */
	public List<Contact> loadContacts() {
		return contactDao.findAll();
	}

	/**
	 * Loads all addresses of a given contact.
	 * 
	 * @param contactId	the contact ID.
	 * @return	a list of addresses of a given contact
	 */
	@Override
	public List<Address> loadContactAddresses(Long contactId) {
		Contact contact = loadContactById(contactId);
		
		if (contact != null) {
			return contact.getAddresses();
		} else {
			throw new ResourceNotFoundException(Contact.class, contactId);
		}
	}

	/**
	 * Loads all phones of a given contact.
	 * 
	 * @param contactId	the contact ID.
	 * @return a list of phones of a given contact.
	 */
	@Override
	public List<Phone> loadContactPhones(Long contactId) {
		Contact contact = loadContactById(contactId);
		
		if (contact != null) {
			return contact.getPhones();
		} else {
			throw new ResourceNotFoundException(Contact.class, contactId);
		}
	}

	/**
	 * Loads a contact, given its ID.
	 * 
	 * @param contactId	the contact ID.
	 * @return	the contact.
	 * @throws	InvalidInputException.
	 */
	@Override
	public Contact loadContactById(Long contactId) {
		if (contactId != null) {
			Optional<Contact> oContact = contactDao.findById(contactId);
			return oContact.isPresent() ? oContact.get() : null;
		} else {
			throw new InvalidInputException("null contact ID");
		}
	}

	/**
	 * Loads an address by its ID.
	 * 
	 * @param addressId	the address ID.
	 * @return	the address.
	 * @throws	InvalidInputException.
	 */
	@Override
	public Address loadAddressById(Long addressId) {
		if (addressId != null) {
			Optional<Address> oAddress = addressDao.findById(addressId);
			return oAddress.isPresent() ? oAddress.get() : null;
		} else {
			throw new InvalidInputException("null address ID");
		}
	}

	/**
	 * Loads a phone by its ID.
	 * 
	 * @param phoneId	the phone ID.
	 * @return	the phone.
	 * @throws	InvalidInputException.
	 */
	@Override
	public Phone loadPhoneById(Long phoneId) {
		if (phoneId != null) {
			Optional<Phone> oPhone = phoneDao.findById(phoneId);
			return oPhone.isPresent() ? oPhone.get() : null;
		} else {
			throw new InvalidInputException("null phone ID");
		}
	}

	/**
	 * Adds a new contact.
	 * 
	 * @param contact	the new contact.
	 * @return the contact inserted.
	 * @throws InvalidInputException.
	 */
	@Override
	public Contact addContact(Contact contact) {
		if (contact != null) {
			if (contact.getId() == null) {
				validateContact(contact);
				return contactDao.save(contact);
			} else {
				throw new InvalidInputException("contact ID must not be supplied for new records");
			}
		} else {
			throw new InvalidInputException("contact record was not supplied");
		}
	}

	/**
	 * Adds a new address.
	 * 
	 * @param address	the new address.
	 * @return	the new address added.
	 * @throws	InvalidInputException.
	 */
	@Override
	public Address addAddress(Address address) {
		if (address != null) {
			if (address.getId() == null) {
				validateAddress(address);
				return addressDao.save(address);
			} else {
				throw new InvalidInputException("address ID must not be supplied for new records");
			}
		} else {
			throw new InvalidInputException(msgAddressNotSupplied);
		}
	}

	/**
	 * Adds a new phone.
	 * 
	 * @param phone	the new phone.
	 * @return	the new phone added.
	 * @throws	InvalidInputException.
	 */
	@Override
	public Phone addPhone(Phone phone) {
		if (phone != null) {
			if (phone.getId() == null) {
				validatePhone(phone);
				return phoneDao.save(phone);
			} else {
				throw new InvalidInputException("phone ID must not be supplied for new records");
			}
		} else {
			throw new InvalidInputException(msgPhoneNotSupplied);
		}
	}

	/**
	 * Updates a contact.
	 * 
	 * @param contact	the contact.
	 * @return	the updated contact.
	 * @throws	InvalidInputException.
	 * @throws	ResourceNotFoundException.
	 */
	@Override
	public Contact updateContact(Contact contact) {
		if (contact != null) {
			var oldContact = loadContactById(contact.getId());
			
			if (oldContact != null) {
				validateContact(contact);
				return contactDao.save(contact);
			} else {
				throw new ResourceNotFoundException(Contact.class, contact.getId());
			}
		} else {
			throw new InvalidInputException("contact record was not supplied");
		}
	}

	/**
	 * Updates an address.
	 * 
	 * @param address	the address.
	 * @return	the updated address.
	 * @throws	InvalidInputException.
	 * @throws	ResourceNotFoundException.
	 */
	@Override
	public Address updateAddress(Address address) {
		if (address != null) {
			var oldAddress = loadAddressById(address.getId());
			
			if (oldAddress != null) {
				validateAddress(address);
				return addressDao.save(address);
			} else {
				throw new ResourceNotFoundException(Address.class, address.getId());
			}
		} else {
			throw new InvalidInputException(msgAddressNotSupplied);
		}
	}

	/**
	 * Updates an address if it belongs to a contact.
	 *
	 * @param contactId	the contact ID.
	 * @param address	the address.
	 * @return	the updated address.
	 * @throws	InvalidInputException.
	 * @throws	ResourceNotFoundException.
	 */
	@Override
	public Address updateAddress(Long contactId, Address address) {
		if (address != null) {
			var contact = loadContactById(contactId);
			
			if (contact != null) {
				var oldAddress = loadAddressById(address.getId());
				
				if (oldAddress != null) {
					if (oldAddress.getContact().getId().equals(contactId)) {
						validateAddress(address);
						return addressDao.save(address);
					} else {
						throw new InvalidInputException("supplied address does not belong to supplied contact ID");
					}
				} else {
					throw new ResourceNotFoundException(Address.class, address.getId());
				}
			} else {
				throw new ResourceNotFoundException(Contact.class, contactId);
			}
		} else {
			throw new InvalidInputException(msgAddressNotSupplied);
		}
	}

	/**
	 * Updates a phone.
	 * 
	 * @param phone	the phone.
	 * @return	the updated phone.
	 * @throws	InvalidInputException.
	 * @throws	ResourceNotFoundException.
	 */
	@Override
	public Phone updatePhone(Phone phone) {
		if (phone != null) {
			var oldPhone = loadPhoneById(phone.getId());
			
			if (oldPhone != null) {
				validatePhone(phone);
				return phoneDao.save(phone);
			} else {
				throw new ResourceNotFoundException(Phone.class, phone.getId());
			}
		} else {
			throw new InvalidInputException(msgPhoneNotSupplied);
		}
	}

	/**
	 * Updates a phone if it belongs to a contact.
	 *
	 * @param contactId	the contact ID.
	 * @param phone	the phone.
	 * @return	the updated phone.
	 * @throws	InvalidInputException.
	 * @throws	ResourceNotFoundException.
	 */
	@Override
	public Phone updatePhone(Long contactId, Phone phone) {
		if (phone != null) {
			var contact = loadContactById(contactId);
			
			if (contact != null) {
				var oldPhone = loadPhoneById(phone.getId());
				
				if (oldPhone != null) {
					if (oldPhone.getContact().getId().equals(contactId)) {
						validatePhone(phone);
						return phoneDao.save(phone);
					} else {
						throw new InvalidInputException("supplied phone does not belong to supplied contact ID");
					}
				} else {
					throw new ResourceNotFoundException(Phone.class, phone.getId());
				}
			} else {
				throw new ResourceNotFoundException(Contact.class, contactId);
			}
		} else {
			throw new InvalidInputException(msgPhoneNotSupplied);
		}
	}

	/**
	 * Deletes a contact.
	 * 
	 * @param contactId	the contact ID.
	 * 
	 * @throws	ResourceNotFoundException.
	 */
	@Override
	public void deleteContact(Long contactId) {
		var contact = loadContactById(contactId);
		
		if (contact != null) {
			contactDao.deleteById(contactId);
		} else {
			throw new ResourceNotFoundException(Contact.class, contactId);
		}
	}

	/**
	 * Deletes an address.
	 * 
	 * @param addressId	the address ID.
	 * 
	 * @throws	ResourceNotFoundException.
	 */
	@Override
	public void deleteAddress(Long addressId) {
		var address = loadAddressById(addressId);
		
		if (address != null) {
			addressDao.deleteById(addressId);
		} else {
			throw new ResourceNotFoundException(Address.class, addressId);
		}
	}

	/**
	 * Deletes an address if it belongs to a contact.
	 *
	 * @param contactId the contact ID.
	 * @param addressId	the address ID.
	 * @throws	ResourceNotFoundException.
	 * @throws	InvalidInputException.
	 */
	@Override
	public void deleteAddress(Long contactId, Long addressId) {
		var contact = loadContactById(contactId);
		
		if (contact != null) {
			var address = loadAddressById(addressId);
			
			if (address != null) {
				if (address.getContact().getId().equals(contactId)) {
					addressDao.deleteById(addressId);
				} else {
					throw new InvalidInputException("supplied address ID does not belong to supplied contact ID");
				}
			} else {
				throw new ResourceNotFoundException(Address.class, addressId);
			}
		} else {
			throw new ResourceNotFoundException(Contact.class, contactId);
		}
	}

	/**
	 * Deletes a phone.
	 * 
	 * @param phoneId	the phone ID.
	 * 
	 * @throws	ResourceNotFoundException.
	 */
	@Override
	public void deletePhone(Long phoneId) {
		var phone = loadPhoneById(phoneId);
		
		if (phone != null) {
			phoneDao.deleteById(phoneId);
		} else {
			throw new ResourceNotFoundException(Phone.class, phoneId);
		}
	}

	/**
	 * Deletes a phone if it belongs to a contact.
	 *
	 * @param contactId the contact ID.
	 * @param phoneId	the phone ID.
	 * @throws	ResourceNotFoundException.
	 * @throws	InvalidInputException.
	 */
	@Override
	public void deletePhone(Long contactId, Long phoneId) {
		var contact = loadContactById(contactId);
		
		if (contact != null) {
			var phone = loadPhoneById(phoneId);
			
			if (phone != null) {
				if (phone.getContact().getId().equals(phoneId)) {
					phoneDao.deleteById(phoneId);
				} else {
					throw new InvalidInputException("supplied phone ID does not belong to supplied contact ID");
				}
			} else {
				throw new ResourceNotFoundException(Phone.class, phoneId);
			}
		} else {
			throw new ResourceNotFoundException(Contact.class, contactId);
		}
	}
}
