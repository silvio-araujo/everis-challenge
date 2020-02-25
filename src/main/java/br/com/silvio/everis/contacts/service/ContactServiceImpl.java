package br.com.silvio.everis.contacts.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.silvio.everis.contacts.dao.AddressDao;
import br.com.silvio.everis.contacts.dao.ContactDao;
import br.com.silvio.everis.contacts.dao.PhoneDao;
import br.com.silvio.everis.contacts.model.Address;
import br.com.silvio.everis.contacts.model.Contact;
import br.com.silvio.everis.contacts.model.Phone;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	ContactDao contactDao;
	
	@Autowired
	AddressDao addressDao;
	
	@Autowired
	PhoneDao phoneDao;
	
	public List<Contact> loadContacts() {
		return contactDao.findAll();
	}

	@Override
	public List<Address> loadContactAddresses(Long contactId) {
		Contact contact = loadContactById(contactId);
		
		return (contact != null) ? contact.getAddresses() : new ArrayList<Address>(); 
	}

	@Override
	public List<Phone> loadContactPhones(Long contactId) {
		Contact contact = loadContactById(contactId);
		
		return (contact != null) ? contact.getPhones() : new ArrayList<Phone>(); 
	}

	@Override
	public Contact loadContactById(Long contactId) {
		Optional<Contact> oContact = contactDao.findById(contactId);
		return oContact.isPresent() ? oContact.get() : null;
	}

	@Override
	public Address loadAddressById(Long addressId) {
		Optional<Address> oAddress = addressDao.findById(addressId);
		return oAddress.isPresent() ? oAddress.get() : null;
	}

	@Override
	public Phone loadPhoneById(Long phoneId) {
		Optional<Phone> oPhone = phoneDao.findById(phoneId);
		return oPhone.isPresent() ? oPhone.get() : null;
	}
}
