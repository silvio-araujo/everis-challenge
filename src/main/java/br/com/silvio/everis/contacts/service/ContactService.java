package br.com.silvio.everis.contacts.service;

import java.util.List;

import br.com.silvio.everis.contacts.model.Address;
import br.com.silvio.everis.contacts.model.Contact;
import br.com.silvio.everis.contacts.model.Phone;

public interface ContactService {
	public List<Contact> loadContacts();
	public List<Address> loadContactAddresses(Long contactId);
	public List<Phone> loadContactPhones(Long contactId);
	public Contact loadContactById(Long contactId);
	public Address loadAddressById(Long addressId);
	public Phone loadPhoneById(Long phoneId);
}
