package br.com.silvio.everis.contacts.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.silvio.everis.contacts.model.Contact;
import br.com.silvio.everis.contacts.model.Phone;

public interface PhoneDao extends JpaRepository<Phone, Long> {
	public List<Phone> findAllByContact(Contact contact);
}
