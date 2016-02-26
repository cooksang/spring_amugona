package com.example.accounts;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.accounts.exception.AccountNotFoundException;
import com.example.accounts.exception.ErrorResponse;
import com.example.accounts.exception.UserDuplicatedException;

@RestController
public class AccountController {

	@Autowired
	private AccountService service;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AccountRepository repository;
	
	@RequestMapping(value = "/accounts", method = POST)
	@ResponseBody
	@ResponseStatus(value=HttpStatus.CREATED)
	public AccountDto.Response createAccount(@RequestBody @Valid AccountDto.Create create,
										BindingResult result){
		checkValidation(result);
		Account newAccount = service.createAccount(create);
		
//		return new ResponseEntity<>(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
		return modelMapper.map(newAccount, AccountDto.Response.class);
	}


	@RequestMapping(value = "/accounts",method = GET)
	public ResponseEntity<?> getAccounts(Pageable pageable){
		Page<Account> page = repository.findAll(pageable);
		List<AccountDto.Response> content = page.getContent().parallelStream()
		.map(account -> modelMapper.map(account, AccountDto.Response.class))
		.collect(Collectors.toList());
		
		PageImpl<AccountDto.Response> result = new PageImpl<>(content, pageable, page.getTotalElements());
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/accounts/{id}", method = GET)
	public ResponseEntity<?> getAccount(@PathVariable Long id) {
		Account account = service.getAccount(id);
		AccountDto.Response result =  modelMapper.map(account, AccountDto.Response.class);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/accounts/{id}", method = PUT)
	public ResponseEntity<?> updateAccount(@PathVariable Long id, 
											@RequestBody @Valid AccountDto.Update updateDto,
											BindingResult result) {
		if(result.hasErrors()){
			validationRejected(result);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		
		Account updatedAccount = service.updateAccount(id, updateDto);
		return new ResponseEntity<>(modelMapper.map(updatedAccount, AccountDto.Response.class), HttpStatus.OK);
	}

	private void checkValidation(BindingResult result) {
		if( result.hasErrors() ){
			validationRejected(result);
		}
	}
	
	private void validationRejected(BindingResult result) {
		StringBuilder message = new StringBuilder();
		for(ObjectError error : result.getAllErrors()) {
			message.append("code" + error.getCode()+ ", message:" + error.getDefaultMessage());
		}
		
		throw new ValidationException(message.toString());
		
	}

	@RequestMapping(value = "/accounts/{id}", method = DELETE)
	public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
		service.deleteAccount(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@ExceptionHandler(UserDuplicatedException.class)
	public ResponseEntity<?> handleUserDuplicatedException(UserDuplicatedException e) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(e.getUserName() + " 중복된 유저입니다.");
		errorResponse.setCode("duplicated.username.exception");
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AccountNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleAccountNotFoundException(AccountNotFoundException e) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage("[" + e.getId() + "]에 해당하는 계정이 없습니다.");
		return errorResponse;
	}
	
	
}

