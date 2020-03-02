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

import br.com.silvio.everis.contacts.enums.AddressType;
import br.com.silvio.everis.contacts.enums.StreetType;

/**
 * Address class.
 * 
 * @author silvio.araujo
 *
 */
@Entity
@Table(name="ADDRESS")
public class Address extends RepresentationModel<Address> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_addresses")
	@SequenceGenerator(name="seq_addresses", sequenceName="seqaddresses", allocationSize=1)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="owner", nullable = false, updatable = true, insertable = true)
	@JsonIgnore
	private Contact contact;
	
	@Column
	private AddressType addressType;
	
	@Column
	private StreetType streetType;
	
	@Column
	private String street;
	
	@Column
	private String number;
	
	@Column
	private String complement;
	
	@Column
	private String neighborhood;
	
	@Column
	private String city;
	
	@Column
	private String zipCode;
	
	/**
	 * Gets the address ID.
	 * 
	 * @return	the ID.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the address ID.
	 * 
	 * @param id	the address ID.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the contact owner of this address.
	 * 
	 * @return	the contact owner.
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * Sets the contact owner of this address.
	 * 
	 * @param contact	the contact owner.
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * Gets the address type.
	 * 
	 * @return	the address type.
	 */
	public AddressType getAddressType() {
		return addressType;
	}
	
	/**
	 * Sets the address type.
	 * 
	 * @param addressType	the address type.
	 */
	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}
	
	/**
	 * Gets the street type.
	 * 
	 * @return	the street type.
	 */
	public StreetType getStreetType() {
		return streetType;
	}
	
	/**
	 * Sets the street type.
	 * 
	 * @param streetType	the street type.
	 */
	public void setStreetType(StreetType streetType) {
		this.streetType = streetType;
	}
	
	/**
	 * Gets the street name.
	 * 
	 * @return	the street name.
	 */
	public String getStreet() {
		return street;
	}
	
	/**
	 * Sets the street name.
	 * 
	 * @param street	the street name.
	 */
	public void setStreet(String street) {
		this.street = street;
	}
	
	/**
	 * Gets the number in street.
	 * 
	 * @return	the number in street.
	 */
	public String getNumber() {
		return number;
	}
	
	/**
	 * Sets the number in street.
	 * 
	 * @param number	the number in street.
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	
	/**
	 * Gets the complement of number.
	 * 
	 * @return	the complement of number.
	 */
	public String getComplement() {
		return complement;
	}
	
	/**
	 * Sets the complement of number.
	 * 
	 * @param complement	the complement of number.
	 */
	public void setComplement(String complement) {
		this.complement = complement;
	}
	
	/**
	 * Gets the neighborhood of address.
	 * 
	 * @return	the neighborhood of address.
	 */
	public String getNeighborhood() {
		return neighborhood;
	}
	
	/**
	 * Sets the neighborhood of address.
	 * 
	 * @param neighborhood	the neighborhood of address.
	 */
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}
	
	/**
	 * Gets the city name.
	 * 
	 * @return	the city name.
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * Sets the city name.
	 * 
	 * @param city	the city name.
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * Gets the zip code.
	 * 
	 * @return	the zip code.
	 */
	public String getZipCode() {
		return zipCode;
	}
	
	/** Sets the zip code.
	 * 
	 * @param zipCode	the zip code.
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * Converts all data in this class to a string.
	 * 
	 * @return	all data in this class in a string.
	 */
	@Override
	public String toString() {
		return "Address [addressType=" + addressType + ", streetType=" + streetType + ", street=" + street + ", number="
				+ number + ", complement=" + complement + ", neighborhood=" + neighborhood + ", city=" + city
				+ ", zipCode=" + zipCode + "]";
	}
}
