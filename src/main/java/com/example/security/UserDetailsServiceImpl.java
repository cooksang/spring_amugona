package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.accounts.Account;
import com.example.accounts.AccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	AccountRepository accountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		Account account = accountRepository.findByUserName(userName);
		if(account == null){
			throw new UsernameNotFoundException(userName);
		}
		return new UserDetailsImpl(account);
	}

}
