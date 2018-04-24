package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountsService implements AccountsBasicService{

  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }

  /**
   *
   * @param accountFromId ""
   * @param accountToId   ""
   * @param amountToTransfer ""
   */
  public void transferAmount(String accountFromId,  String accountToId, BigDecimal amountToTransfer) {
    this.accountsRepository.transferAmount(accountFromId, accountToId, amountToTransfer);
  }

   }
