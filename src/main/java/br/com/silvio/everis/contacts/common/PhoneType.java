package br.com.silvio.everis.contacts.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PhoneType {
	FIX("Fixo"),
	MOBILE("Móvel");
	
	private String description;
	
	PhoneType(String description) {
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
