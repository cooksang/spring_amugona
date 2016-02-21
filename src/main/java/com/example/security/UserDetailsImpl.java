package com.example.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.accounts.Account;

public class UserDetailsImpl extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 468481991105158521L;

	public UserDetailsImpl(Account account) {
		super(account.getUserName(), account.getPassword(), authorities(account));
		// TODO Auto-generated constructor stub
	}

	private static Collection<? extends GrantedAuthority> authorities(Account account) {
		java.util.List<GrantedAuthority> authorities = new ArrayList<>();
		if(account.isAdmin()){
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		else{
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		return authorities;
	}

}
