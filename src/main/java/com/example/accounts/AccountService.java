package com.example.accounts;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.accounts.AccountDto.Update;

@Service
@Transactional
@Slf4j
public class AccountService {
	
	@Autowired
	private AccountRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ModelMapper modelMapper;

	public Account createAccount(AccountDto.Create dto) {
		
		Account account = modelMapper.map(dto, Account.class);
		//TODO 유효한 username인지 판단
		String userName = dto.getUserName();
		if(repository.findByUserName(userName) != null){
			log.error("user duplicated exception. {}", userName);
			throw new UserDuplicatedException(userName);
		}
		
//		Account account = new Account();
//		BeanUtils.copyProperties(dto, account);
		
		Date now = new Date();
		account.setJoined(now);
		account.setUpdated(now);
		
		Account save = repository.save(account);
		
		return save;
	}

	public Account updateAccount(Long id, Update updateDto) {
		Account account = getAccount(id);
		account.setPassword(updateDto.getPassword());
		account.setFullName(updateDto.getFullName());
		return repository.save(account);
	}

	public Account getAccount(Long id) {
		Account account = repository.findOne(id);
		if(account == null){
			throw new AccountNotFoundException(id);
		}
		return account;
	}

	public void deleteAccount(Long id) {
		repository.delete(getAccount(id));
	}
	
}
