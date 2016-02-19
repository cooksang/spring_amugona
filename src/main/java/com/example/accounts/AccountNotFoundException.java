package com.example.accounts;

public class AccountNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -689099574239232175L;

	Long id;
	
	public AccountNotFoundException(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

}
