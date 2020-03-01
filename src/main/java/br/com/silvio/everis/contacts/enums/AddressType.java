package br.com.silvio.everis.contacts.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AddressType {
	BOTH("Residencial ou Comercial"),
	RESIDENTIAL("Residencial"),
	COMMERCIAL("Comercial"),
	OFFICIAL("Endereço Oficial");
	
	private String description;
	
	AddressType(String description) {
		this.description = description;
	}
	
	@JsonValue
	public String getDescription() {
		return this.description;
	}

	@Override
	public String toString() {
		return this.getDescription();
	}
}
