package br.com.silvio.everis.contacts.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Contact class.
 * 
 * @author silvio.araujo
 *
 */
@Entity
@Table(name="CONTACT")
public class Contact extends RepresentationModel<Contact> {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_contacts")
	@SequenceGenerator(name="seq_contacts", sequenceName="seqcontacts", allocationSize=1)
	private Long id;

	@Column
	private String name;

	@Column
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
	private Date birthdate;

	@Column
	private String cpf;

	@JsonIgnore
	@OneToMany(mappedBy="contact", targetEntity=Address.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Address> addresses;

	@JsonIgnore
	@OneToMany(mappedBy="contact", targetEntity=Phone.class, fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Phone> phones;
	
	/**
	 * Gets the contact ID field.
	 * 
	 * @return ID	the ID field.
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets the contact ID field.
	 * 
	 * @param id	the ID field.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Gets the contact name.
	 * 
	 * @return	the contact name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the contact name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the contact birth date.
	 * @return	the birth date.
	 */
	public Date getBirthdate() {
		return birthdate;
	}
	
	/**
	 * Sets the contact birth date.
	 * 
	 * @param birthdate	the birth date.
	 */
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	/**
	 * Gets the contact CPF.
	 * 
	 * @return	the CPF.
	 */
	public String getCpf() {
		return cpf;
	}
	
	/**
	 * Sets the contact CPF.
	 * 
	 * @param cpf	the CPF.
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	/**
	 * Gets the contact addresses.
	 * 
	 * @return	a list of addresses of the contact.
	 */
	public List<Address> getAddresses() {
		return addresses;
	}
	
	/**
	 * Sets the contact addresses.
	 * 
	 * @param addresses	a list of addresses of the contact.
	 */
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	
	/**
	 * Gets the contact phones.
	 * 
	 * @return	a list of phones of the contact.
	 */
	public List<Phone> getPhones() {
		return phones;
	}
	
	/**
	 * Sets the contact phones.
	 * 
	 * @param phones	a list of phones of the contact.
	 */
	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	/**
	 * Converts all data in this class to a string.
	 * 
	 * @return	all data in this class in a string.
	 */
	@Override
	public String toString() {
		return "Contact [name=" + name + ", birthdate=" + 
				String.format("%tF", birthdate) +
				", cpf=" + cpf + "]";
	}
}
