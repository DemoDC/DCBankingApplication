package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;

import java.math.BigDecimal;

public interface AccountsBasicService {

    void createAccount(Account account);

    Account getAccount(String accountId);

    void transferAmount(String accountFromId,  String accountToId, BigDecimal amountToTransfer);

}
