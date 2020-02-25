package br.com.silvio.everis.contacts.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.silvio.everis.contacts.model.Phone;

public interface PhoneDao extends JpaRepository<Phone, Long> {

}
