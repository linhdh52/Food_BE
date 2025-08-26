package com.foodbe.service;

import com.foodbe.DTO.AccountDTO;
import com.foodbe.DTO.CategoriesDTO;
import com.foodbe.DTO.response.ApiResponse;

import java.util.List;

public interface AccountService {

    ApiResponse<AccountDTO> createAccount(AccountDTO accountDTO);
    ApiResponse<AccountDTO> updateAccount(Long id, AccountDTO accountDTO);
    ApiResponse<String> deleteAccount(Long id);
    ApiResponse<AccountDTO> getAccountById(Long id);
    ApiResponse<List<AccountDTO>> getAllAccount();
}
