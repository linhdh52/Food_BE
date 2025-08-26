package com.foodbe.controller;

import com.foodbe.DTO.AccountDTO;
import com.foodbe.DTO.CategoriesDTO;
import com.foodbe.DTO.response.ApiResponse;
import com.foodbe.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account/")
@CrossOrigin("*")
@Tag(name = "Account API", description = "Tài khoản")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    @Operation(summary = "Tạo mới tài khoản")
    public ApiResponse<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        return accountService.createAccount(accountDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật tài khoản theo id")
    public ApiResponse<AccountDTO> updateAccount(@PathVariable Long id, @RequestBody AccountDTO accountDTO) {
        return accountService.updateAccount(id, accountDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá tài khoản theo id")
    public ApiResponse<String> deleteAccount(@PathVariable Long id) {
        return accountService.deleteAccount(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy chi tiết tài khoản theo id")
    public ApiResponse<AccountDTO> getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @GetMapping
    @Operation(summary = "Lấy toàn bộ danh sách tài khoản")
    public ApiResponse<List<AccountDTO>> getAllAccount() {
        return accountService.getAllAccount();
    }
}
