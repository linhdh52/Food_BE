package com.foodbe.service;

import com.foodbe.entity.AccountEntity;
import com.foodbe.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;

    public List<AccountEntity> getAllAccount() {
        return accountRepo.findAll();
    }

    public AccountEntity getAccount(AccountEntity accountEntity1) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity = accountRepo.getAccountByID(accountEntity1.getId());
        return accountEntity;
    }

    public AccountEntity createAccount(AccountEntity accountEntity1) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(accountEntity1.getId());
        accountEntity.setLevel(accountEntity1.getLevel());
        accountEntity.setSex(accountEntity1.isSex());
        accountEntity.setAddress(accountEntity.getAddress());
        accountEntity.setNumber(accountEntity1.getNumber());
        accountEntity.setBirthday(accountEntity1.getBirthday());
        return accountEntity;
    }
}
