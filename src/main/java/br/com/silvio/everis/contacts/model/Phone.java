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

import br.com.silvio.everis.contacts.common.PhoneType;

@Entity
@Table(name="PHONE")
public class Phone extends RepresentationModel<Phone> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_phones")
	@SequenceGenerator(name="seq_phones", sequenceName="seqphones", allocationSize=1)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="owner", nullable = false, updatable = true, insertable = true)
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
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public PhoneType getPhoneType() {
		return phoneType;
	}
	
	public void setPhoneType(PhoneType phoneType) {
		this.phoneType = phoneType;
	}
	
	public String getDdi() {
		return ddi;
	}
	
	public void setDdi(String ddi) {
		this.ddi = ddi;
	}
	
	public String getDdd() {
		return ddd;
	}
	
	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public void setExtension(String extension) {
		this.extension = extension;
	}
}
