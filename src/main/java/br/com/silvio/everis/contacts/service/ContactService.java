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
	public Contact addContact(Contact contact);
	public Address addAddress(Address address);
	public Phone addPhone(Phone phone);
	public Contact updateContact(Contact contact);
	public Address updateAddress(Address address);
	public Address updateAddress(Long contactId, Address address);
	public Phone updatePhone(Phone phone);
	public Phone updatePhone(Long contactId, Phone phone);
	public void deleteContact(Long contactId);
	public void deleteAddress(Long addressId);
	public void deleteAddress(Long contactId, Long addressId);
	public void deletePhone(Long phoneId);
	public void deletePhone(Long contactId, Long phoneId);
}
