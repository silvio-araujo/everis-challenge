package br.com.silvio.everis.contacts.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.silvio.everis.contacts.enums.PhoneType;

/**
 * Phone class.
 * 
 * @author silvio.araujo
 *
 */
@Entity
@Table(name="PHONE")
public class Phone extends RepresentationModel<Phone> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_phones")
	@SequenceGenerator(name="seq_phones", sequenceName="seqphones", allocationSize=1)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="owner", nullable = false, updatable = true, insertable = true)
	@JsonIgnore
	private Contact contact;
	
	@Column
	private PhoneType phoneType;
	
	@Column
	private String ddi;
	
	@Column
	private String ddd;
	
	@Column
	private String number;
	
	@Column
	private String extension;
	
	/**
	 * Gets phone ID.
	 * 
	 * @return	the phone ID.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets phone ID.
	 * 
	 * @param id	the phone ID.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Gets the contact owner of this phone.
	 * 
	 * @return	the contact owner.
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * Sets the contact owner of this phone.
	 * 
	 * @param contact	the contact owner.
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * Gets the phone type.
	 * 
	 * @return	the phone type.
	 */
	public PhoneType getPhoneType() {
		return phoneType;
	}
	
	/**
	 * Sets the phone type.
	 * 
	 * @param phoneType	the phone type.
	 */
	public void setPhoneType(PhoneType phoneType) {
		this.phoneType = phoneType;
	}
	
	/**
	 * Gets the phone DDI.
	 * 
	 * @return	the phone DDI.
	 */
	public String getDdi() {
		return ddi;
	}
	
	/**
	 * Sets the phone DDI.
	 * 
	 * @param ddi	the phone DDI.
	 */
	public void setDdi(String ddi) {
		this.ddi = ddi;
	}
	
	/**
	 * Gets the phone DDD.
	 * 
	 * @return	the phone DDD.
	 */
	public String getDdd() {
		return ddd;
	}
	
	/**
	 * Sets the phone DDD.
	 * 
	 * @param ddd	the phone DDD.
	 */
	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	
	/**
	 * Gets the phone number.
	 * 
	 * @return	the phone number.
	 */
	public String getNumber() {
		return number;
	}
	
	/**
	 * Sets the phone number.
	 * 
	 * @param number	the phone number.
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	
	/**
	 * Gets the phone extension.
	 * 
	 * @return	the phone extension.
	 */
	public String getExtension() {
		return extension;
	}
	
	/**
	 * Sets the phone extension.
	 * 
	 * @param extension	the phone extension.
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}
}
