package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.OverDraftsException;
import com.db.awmd.challenge.service.EmailNotificationService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.db.awmd.challenge.exception.InvalidAccountIdException;
import lombok.extern.slf4j.Slf4j;
import lombok.Synchronized;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class AccountsRepositoryInMemory implements AccountsRepository {

  private final Map<String, Account> accounts = new ConcurrentHashMap<>();
  private EmailNotificationService emailNotifyService = new EmailNotificationService();

  @Override
  public void createAccount(Account account) throws DuplicateAccountIdException {
    Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
    if (previousAccount != null) {
      throw new DuplicateAccountIdException(
        "Account id " + account.getAccountId() + " already exists!");
    }
  }

    /**
     * Transfers the specified amount from sender to receiver account
     * @param accountFromId "Account Id from which the amount will be transferred"
     * @param accountToId "Account Id to which the amount will be transferred"
     * @param amountToTransfer "Amount to be transferred"
     * @throws InvalidAccountIdException "Alerts, if the account does not exists"
     * @throws OverDraftsException "Alerts, if the amount end up with -ve balance, "
     */
  @Override
  @Synchronized
  public void transferAmount(String accountFromId, String accountToId, BigDecimal amountToTransfer) throws InvalidAccountIdException,OverDraftsException  {

      Account accountFrom = accounts.get(accountFromId);
      Account accountTo = accounts.get(accountToId);

           if (accountFrom != null) {
               if(accountTo != null) {
                      if (accountFrom.getBalance().intValue() > amountToTransfer.intValue()) {
                          accountTo.setBalance(accountTo.getBalance().add(amountToTransfer));
                          accountFrom.setBalance(accountFrom.getBalance().subtract(amountToTransfer));
                          log.info("AccountFrom Balance :  {} , AccountTo Balance:  {}", accountFrom.getBalance(), accountTo.getBalance());
                          String notifySender = "Amount " + amountToTransfer + " transferred successfully to account " + accountTo;
                          String notifyReceiver = "Amount " + amountToTransfer + " received successfully from account " + accountFrom;
                          emailNotifyService.notifyAboutTransfer(accountFrom, notifySender);
                          emailNotifyService.notifyAboutTransfer(accountTo, notifyReceiver);
                      } else
                          throw new OverDraftsException(
                                  "Overdrafts not supported .. please check the balance");
                  }else{
                   throw new InvalidAccountIdException(
                          "Account '" + accountToId + "' does not exist!");
                }
            }else{
               throw new InvalidAccountIdException(
                  "Account '" + accountFromId + "' does not exist!");
            }

  }

  @Override
  public Account getAccount(String accountId) {
    return accounts.get(accountId);
  }

  @Override
  public void clearAccounts() {
    accounts.clear();
  }

}
