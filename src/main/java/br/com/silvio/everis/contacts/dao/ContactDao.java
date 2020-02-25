package br.com.silvio.everis.contacts.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.silvio.everis.contacts.model.Contact;

public interface ContactDao extends JpaRepository<Contact, Long> {

}
