package com.example.accounts;

public class UserDuplicatedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7959731952496034706L;
	String userName;
	
	public UserDuplicatedException(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

}
