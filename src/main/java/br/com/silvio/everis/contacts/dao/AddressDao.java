package br.com.silvio.everis.contacts.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.silvio.everis.contacts.model.Address;
import br.com.silvio.everis.contacts.model.Contact;

public interface AddressDao extends JpaRepository<Address, Long> {
	public List<Address> findAllByContact(Contact contact);
}
