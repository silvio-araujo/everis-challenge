package br.com.silvio.everis.contacts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.silvio.everis.contacts.dao.AddressDao;
import br.com.silvio.everis.contacts.dao.ContactDao;
import br.com.silvio.everis.contacts.dao.PhoneDao;
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
	 * 
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
	 * 
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
	 * 
	 * @return	the contact.
	 * 
	 * @throws	InvalidInputException.
	 */
	@Override
	public Contact loadContactById(Long contactId) {
		if (contactId != null) {
			Optional<Contact> oContact = contactDao.findById(contactId);
			return oContact.isPresent() ? oContact.get() : null;
		} else {
			throw new InvalidInputException();
		}
	}

	/**
	 * Loads an address by its ID.
	 * 
	 * @param addressId	the address ID.
	 * 
	 * @return	the address.
	 * 
	 * @throws	InvalidInputException.
	 */
	@Override
	public Address loadAddressById(Long addressId) {
		if (addressId != null) {
			Optional<Address> oAddress = addressDao.findById(addressId);
			return oAddress.isPresent() ? oAddress.get() : null;
		} else {
			throw new InvalidInputException();
		}
	}

	/**
	 * Loads a phone by its ID.
	 * 
	 * @param phoneId	the phone ID.
	 * 
	 * @return	the phone.
	 * 
	 * @throws	InvalidInputException.
	 */
	@Override
	public Phone loadPhoneById(Long phoneId) {
		if (phoneId != null) {
			Optional<Phone> oPhone = phoneDao.findById(phoneId);
			return oPhone.isPresent() ? oPhone.get() : null;
		} else {
			throw new InvalidInputException();
		}
	}

	/**
	 * Adds a new contact.
	 * 
	 * @param contact	the new contact.
	 * 
	 * @return the contact inserted.
	 * 
	 * @throws InvalidInputException.
	 */
	@Override
	public Contact addContact(Contact contact) {
		if ((contact != null) && (contact.getId() == null)) {
			return contactDao.save(contact);
		} else {
			throw new InvalidInputException();
		}
	}

	/**
	 * Adds a new address.
	 * 
	 * @param address	the new address.
	 * 
	 * @return	the new address added.
	 * 
	 * @throws	InvalidInputException.
	 */
	@Override
	public Address addAddress(Address address) {
		if ((address != null) && (address.getId() == null)) {
			return addressDao.save(address);
		} else {
			throw new InvalidInputException();
		}
	}

	/**
	 * Adds a new phone.
	 * 
	 * @param phone	the new phone.
	 * 
	 * @return	the new phone added.
	 * 
	 * @throws	InvalidInputException.
	 */
	@Override
	public Phone addPhone(Phone phone) {
		if ((phone != null) && (phone.getId() == null)) {
			return phoneDao.save(phone);
		} else {
			throw new InvalidInputException();
		}
	}

	/**
	 * Updates a contact.
	 * 
	 * @param contact	the contact.
	 * 
	 * @return	the updated contact.
	 * 
	 * @throws	InvalidInputException.
	 * @throws	ResourceNotFoundException.
	 */
	@Override
	public Contact updateContact(Contact contact) {
		if (contact != null) {
			var oldContact = loadContactById(contact.getId());
			
			if (oldContact != null) {
				return contactDao.save(contact);
			} else {
				throw new ResourceNotFoundException(Contact.class, contact.getId());
			}
		} else {
			throw new InvalidInputException();
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
				return addressDao.save(address);
			} else {
				throw new ResourceNotFoundException(Address.class, address.getId());
			}
		} else {
			throw new InvalidInputException();
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
						return addressDao.save(address);
					} else {
						throw new InvalidInputException();
					}
				} else {
					throw new ResourceNotFoundException(Address.class, address.getId());
				}
			} else {
				throw new ResourceNotFoundException(Contact.class, contactId);
			}
		} else {
			throw new InvalidInputException();
		}
	}

	/**
	 * Updates a phone.
	 * 
	 * @param phone	the phone.
	 * 
	 * @return	the updated phone.
	 * 
	 * @throws	InvalidInputException.
	 * @throws	ResourceNotFoundException.
	 */
	@Override
	public Phone updatePhone(Phone phone) {
		if (phone != null) {
			var oldPhone = loadPhoneById(phone.getId());
			
			if (oldPhone != null) {
				return phoneDao.save(phone);
			} else {
				throw new ResourceNotFoundException(Phone.class, phone.getId());
			}
		} else {
			throw new InvalidInputException();
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
						return phoneDao.save(phone);
					} else {
						throw new InvalidInputException();
					}
				} else {
					throw new ResourceNotFoundException(Phone.class, phone.getId());
				}
			} else {
				throw new ResourceNotFoundException(Contact.class, contactId);
			}
		} else {
			throw new InvalidInputException();
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
					throw new InvalidInputException();
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
					throw new InvalidInputException();
				}
			} else {
				throw new ResourceNotFoundException(Phone.class, phoneId);
			}
		} else {
			throw new ResourceNotFoundException(Contact.class, contactId);
		}
	}
}
