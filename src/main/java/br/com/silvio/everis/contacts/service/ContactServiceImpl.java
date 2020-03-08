package br.com.silvio.everis.contacts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.silvio.everis.contacts.dao.AddressDao;
import br.com.silvio.everis.contacts.dao.ContactDao;
import br.com.silvio.everis.contacts.dao.PhoneDao;
import br.com.silvio.everis.contacts.enums.PhoneType;
import br.com.silvio.everis.contacts.exceptions.IdSuppliedForNew;
import br.com.silvio.everis.contacts.exceptions.Invalid;
import br.com.silvio.everis.contacts.exceptions.Mandatory;
import br.com.silvio.everis.contacts.exceptions.RecordNotSupplied;
import br.com.silvio.everis.contacts.exceptions.ResourceNotFound;
import br.com.silvio.everis.contacts.exceptions.SuppliedDoesNotBelongTo;
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
	
	@Value("${contacts.regex.contact.name}")
	private String regexContactName;
	
	@Value("${contacts.regex.contact.cpf}")
	private String regexContactCpf;
	
	@Value("${contacts.regex.address.zipcode}")
	private String regexAddressZipCode;
	
	@Value("${contacts.regex.phone.fix}")
	private String regexPhoneFix;
	
	@Value("${contacts.regex.phone.mobile}")
	private String regexPhoneMobile;
	
	@Value("${contacts.regex.phone.ddi}")
	private String regexPhoneDdi;
	
	@Value("${contacts.regex.phone.ddd}")
	private String regexPhoneDdd;
	
	/**
	 * Validates contact record against rules (REGEX).
	 * 
	 * @param contact	the contact to be validated.
	 * @throws	Invalid.
	 * @throws	Mandatory.
	 */
	private void validateContact(Contact contact) {
		var name = contact.getName();
		var cpf = contact.getCpf();
		
		if ((name != null) && (!name.isBlank())) {
			if (!name.matches(regexContactName)) {
				throw new Invalid("contact name");
			}
		} else {
			throw new Mandatory("contact name");
		}
		
		if ((cpf != null) && (!cpf.matches(regexContactCpf))) {
			throw new Invalid("contact CPF");
		}
	}
	
	/**
	 * Validates address record against rules (REGEX).
	 * 
	 * @param address	the address to be validated.	
	 * @throws	Invalid.
	 * @throws	Mandatory.
	 */
	private void validateAddress(Address address) {
		var street = address.getStreet();
		var city = address.getCity();
		var zipCode = address.getZipCode();
		
		if ((street == null) || (street.isBlank())) {
			throw new Mandatory("address street");
		}
		
		if ((city == null) || (city.isBlank())) {
			throw new Mandatory("address city");
		}
		
		if ((zipCode != null) && (!zipCode.matches(regexAddressZipCode))) {
				throw new Invalid("address zip code");
		}
	}
	
	/**
	 * Validates phone record against rules (REGEX).
	 * 
	 * @param phone	the phone to be validated.	
	 * @throws	Invalid.
	 * @throws	Mandatory.
	 */
	private void validatePhone(Phone phone) {
		var phoneType = phone.getPhoneType();
		var ddi = phone.getDdi();
		var ddd = phone.getDdd();
		var number = phone.getNumber();
		var regexPhone = (phoneType == PhoneType.FIX) ? regexPhoneFix : regexPhoneMobile;

		if (ddi == null) {
			throw new Mandatory("phone ddi");
		}
		
		if (ddd == null) {
			throw new Mandatory("phone ddd");
		}
		
		if (number == null) {
			throw new Mandatory("phone number");
		}
		
		if (!ddi.matches(regexPhoneDdi)) {
			throw new Invalid("phone ddi");
		}
		
		if (!ddd.matches(regexPhoneDdd)) {
			throw new Invalid("phone ddd");
		}

		if (!number.matches(regexPhone)) {
			throw new Invalid("phone number");
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
			throw new ResourceNotFound(Contact.class, contactId);
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
			throw new ResourceNotFound(Contact.class, contactId);
		}
	}

	/**
	 * Loads a contact, given its ID.
	 * 
	 * @param contactId	the contact ID.
	 * @return	the contact.
	 * @throws	Invalid.
	 */
	@Override
	public Contact loadContactById(Long contactId) {
		if (contactId != null) {
			Optional<Contact> oContact = contactDao.findById(contactId);
			return oContact.isPresent() ? oContact.get() : null;
		} else {
			throw new Invalid("null contact ID");
		}
	}

	/**
	 * Loads an address by its ID.
	 * 
	 * @param addressId	the address ID.
	 * @return	the address.
	 * @throws	Invalid.
	 */
	@Override
	public Address loadAddressById(Long addressId) {
		if (addressId != null) {
			Optional<Address> oAddress = addressDao.findById(addressId);
			return oAddress.isPresent() ? oAddress.get() : null;
		} else {
			throw new Invalid("null address ID");
		}
	}

	/**
	 * Loads a phone by its ID.
	 * 
	 * @param phoneId	the phone ID.
	 * @return	the phone.
	 * @throws	Invalid.
	 */
	@Override
	public Phone loadPhoneById(Long phoneId) {
		if (phoneId != null) {
			Optional<Phone> oPhone = phoneDao.findById(phoneId);
			return oPhone.isPresent() ? oPhone.get() : null;
		} else {
			throw new Invalid("null phone ID");
		}
	}

	/**
	 * Adds a new contact.
	 * 
	 * @param contact	the new contact.
	 * @return the contact inserted.
	 * @throws IdSuppliedForNew.
	 * @throws RecordNotSupplied.
	 */
	@Override
	public Contact addContact(Contact contact) {
		if (contact != null) {
			if (contact.getId() == null) {
				validateContact(contact);
				return contactDao.save(contact);
			} else {
				throw new IdSuppliedForNew("contact");
			}
		} else {
			throw new RecordNotSupplied("contact");
		}
	}

	/**
	 * Adds a new address.
	 * 
	 * @param address	the new address.
	 * @return	the new address added.
	 * @throws IdSuppliedForNew.
	 * @throws RecordNotSupplied.
	 */
	@Override
	public Address addAddress(Address address) {
		if (address != null) {
			if (address.getId() == null) {
				validateAddress(address);
				return addressDao.save(address);
			} else {
				throw new IdSuppliedForNew("address");
			}
		} else {
			throw new RecordNotSupplied("address");
		}
	}

	/**
	 * Adds a new phone.
	 * 
	 * @param phone	the new phone.
	 * @return	the new phone added.
	 * @throws IdSuppliedForNew.
	 * @throws RecordNotSupplied.
	 */
	@Override
	public Phone addPhone(Phone phone) {
		if (phone != null) {
			if (phone.getId() == null) {
				validatePhone(phone);
				return phoneDao.save(phone);
			} else {
				throw new IdSuppliedForNew("phone");
			}
		} else {
			throw new RecordNotSupplied("phone");
		}
	}

	/**
	 * Updates a contact.
	 * 
	 * @param contact	the contact.
	 * @return	the updated contact.
	 * @throws	RecordNotSupplied.
	 * @throws	ResourceNotFound.
	 */
	@Override
	public Contact updateContact(Contact contact) {
		if (contact != null) {
			var oldContact = loadContactById(contact.getId());
			
			if (oldContact != null) {
				validateContact(contact);
				return contactDao.save(contact);
			} else {
				throw new ResourceNotFound(Contact.class, contact.getId());
			}
		} else {
			throw new RecordNotSupplied("contact");
		}
	}

	/**
	 * Updates an address.
	 * 
	 * @param address	the address.
	 * @return	the updated address.
	 * @throws	RecordNotSupplied.
	 * @throws	ResourceNotFound.
	 */
	@Override
	public Address updateAddress(Address address) {
		if (address != null) {
			var oldAddress = loadAddressById(address.getId());
			
			if (oldAddress != null) {
				validateAddress(address);
				return addressDao.save(address);
			} else {
				throw new ResourceNotFound(Address.class, address.getId());
			}
		} else {
			throw new RecordNotSupplied("address");
		}
	}

	/**
	 * Updates an address if it belongs to a contact.
	 *
	 * @param contactId	the contact ID.
	 * @param address	the address.
	 * @return	the updated address.
	 * @throws SuppliedDoesNotBelongTo.
	 * @throws RecordNotSupplied.
	 * @throws	ResourceNotFound.
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
						throw new SuppliedDoesNotBelongTo("address", "contact ID");
					}
				} else {
					throw new ResourceNotFound(Address.class, address.getId());
				}
			} else {
				throw new ResourceNotFound(Contact.class, contactId);
			}
		} else {
			throw new RecordNotSupplied("address");
		}
	}

	/**
	 * Updates a phone.
	 * 
	 * @param phone	the phone.
	 * @return	the updated phone.
	 * @throws	RecordNotSupplied.
	 * @throws	ResourceNotFound.
	 */
	@Override
	public Phone updatePhone(Phone phone) {
		if (phone != null) {
			var oldPhone = loadPhoneById(phone.getId());
			
			if (oldPhone != null) {
				validatePhone(phone);
				return phoneDao.save(phone);
			} else {
				throw new ResourceNotFound(Phone.class, phone.getId());
			}
		} else {
			throw new RecordNotSupplied("phone");
		}
	}

	/**
	 * Updates a phone if it belongs to a contact.
	 *
	 * @param contactId	the contact ID.
	 * @param phone	the phone.
	 * @return	the updated phone.
	 * @throws	RecordNotSupplied.
	 * @throws	SuppliedDoesNotBelongTo.
	 * @throws	ResourceNotFound.
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
						throw new SuppliedDoesNotBelongTo("phone", "contact ID");
					}
				} else {
					throw new ResourceNotFound(Phone.class, phone.getId());
				}
			} else {
				throw new ResourceNotFound(Contact.class, contactId);
			}
		} else {
			throw new RecordNotSupplied("phone");
		}
	}

	/**
	 * Deletes a contact.
	 * 
	 * @param contactId	the contact ID.
	 * 
	 * @throws	ResourceNotFound.
	 */
	@Override
	public void deleteContact(Long contactId) {
		var contact = loadContactById(contactId);
		
		if (contact != null) {
			contactDao.deleteById(contactId);
		} else {
			throw new ResourceNotFound(Contact.class, contactId);
		}
	}

	/**
	 * Deletes an address.
	 * 
	 * @param addressId	the address ID.
	 * 
	 * @throws	ResourceNotFound.
	 */
	@Override
	public void deleteAddress(Long addressId) {
		var address = loadAddressById(addressId);
		
		if (address != null) {
			addressDao.deleteById(addressId);
		} else {
			throw new ResourceNotFound(Address.class, addressId);
		}
	}

	/**
	 * Deletes an address if it belongs to a contact.
	 *
	 * @param contactId the contact ID.
	 * @param addressId	the address ID.
	 * @throws	ResourceNotFound.
	 * @throws	SuppliedDoesNotBelongTo.
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
					throw new SuppliedDoesNotBelongTo("address", "contact ID");
				}
			} else {
				throw new ResourceNotFound(Address.class, addressId);
			}
		} else {
			throw new ResourceNotFound(Contact.class, contactId);
		}
	}

	/**
	 * Deletes a phone.
	 * 
	 * @param phoneId	the phone ID.
	 * 
	 * @throws	ResourceNotFound.
	 */
	@Override
	public void deletePhone(Long phoneId) {
		var phone = loadPhoneById(phoneId);
		
		if (phone != null) {
			phoneDao.deleteById(phoneId);
		} else {
			throw new ResourceNotFound(Phone.class, phoneId);
		}
	}

	/**
	 * Deletes a phone if it belongs to a contact.
	 *
	 * @param contactId the contact ID.
	 * @param phoneId	the phone ID.
	 * @throws	ResourceNotFound.
	 * @throws	SuppliedDoesNotBelongTo.
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
					throw new SuppliedDoesNotBelongTo("phone", "contact ID");
				}
			} else {
				throw new ResourceNotFound(Phone.class, phoneId);
			}
		} else {
			throw new ResourceNotFound(Contact.class, contactId);
		}
	}
}
