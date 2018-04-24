package com.db.awmd.challenge.web;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.helper.TransferAmountHelper;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InvalidAccountIdException;
import com.db.awmd.challenge.exception.OverDraftsException;
import com.db.awmd.challenge.service.AccountsService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;


  @Autowired
  public AccountsController(AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @PostMapping(path = "/createAccount",consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
    log.info("Creating account {}", account);

    try {
    this.accountsService.createAccount(account);
      return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
    } catch (DuplicateAccountIdException daie) {
      return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(path = "/{accountId}")
  public Account getAccount(@PathVariable String accountId) {
    log.info("Retrieving account for id {}", accountId);
    return this.accountsService.getAccount(accountId);
  }

  @PostMapping(path = "/transferAmount", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> transferAmount( @RequestBody @Valid TransferAmountHelper trnsfrAmntHldr) {
      log.info("Transferring amount {} from {} to {}", trnsfrAmntHldr.getAmountToTransfer(), trnsfrAmntHldr.getAccountFromId(), trnsfrAmntHldr.getAccountToId());

    try {
      this.accountsService.transferAmount(trnsfrAmntHldr.getAccountFromId(), trnsfrAmntHldr.getAccountToId(), trnsfrAmntHldr.getAmountToTransfer());
        return new ResponseEntity<>("Amount transferred successfully", HttpStatus.OK);
    } catch (InvalidAccountIdException iaie) {
      return new ResponseEntity<>(iaie.getMessage(), HttpStatus.BAD_REQUEST);
    }
    catch (OverDraftsException ode) {
        return new ResponseEntity<>(ode.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
