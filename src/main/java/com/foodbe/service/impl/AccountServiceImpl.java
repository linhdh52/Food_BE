package com.foodbe.service.impl;

import com.foodbe.DTO.AccountDTO;
import com.foodbe.DTO.CategoriesDTO;
import com.foodbe.DTO.response.ApiResponse;
import com.foodbe.entity.AccountEntity;
import com.foodbe.entity.CategoriesEntity;
import com.foodbe.repository.AccountRepository;
import com.foodbe.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepo;

    public AccountServiceImpl(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    private AccountDTO convertToDTO(AccountEntity entity) {
        return new AccountDTO(
                entity.getId(),
                entity.getNumber(),
                entity.getName(),
                entity.getPassword(),
                entity.isSex(),
                entity.getBirthday(),
                entity.getTimeCreateUser(),
                entity.getAddress(),
                entity.getAccumulatePoints(),
                entity.getLevel(),
                entity.getPurchaseHistory()
        );
    }

    private AccountEntity convertToEntity(AccountDTO dto) {
        AccountEntity entity = new AccountEntity();
        entity.setNumber(dto.getNumber());
        entity.setPassword(dto.getPassword());
        entity.setSex(dto.getSex());
        entity.setBirthday(dto.getBirthday());
        entity.setTimeCreateUser(dto.getTimeCreateUser());
        entity.setAddress(dto.getAddress());
        entity.setAccumulatePoints(dto.getAccumulatePoint());
        entity.setLevel(dto.getLevel());
        entity.setPurchaseHistory(dto.getPurchaseHistory());
        return entity;
    }

    @Override
    public ApiResponse<AccountDTO> createAccount(AccountDTO accountDTO) {
        Date  date = new Date();
        if (accountRepo.existsByNumber(accountDTO.getNumber())) {
            return ApiResponse.buildErrorResponse(HttpStatus.BAD_REQUEST, "Số điện thoại đã tồn tại");
        }

        AccountEntity entity = convertToEntity(accountDTO);
        entity.setTimeCreateUser(date);
        AccountEntity saved = accountRepo.save(entity);
        return ApiResponse.buildSuccessResponse(convertToDTO(saved));
    }

    @Override
    public ApiResponse<AccountDTO> updateAccount(Long id, AccountDTO accountDTO) {
        AccountEntity existing = accountRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id " + id));

        existing.setNumber(accountDTO.getNumber());
        existing.setName(accountDTO.getName());
        existing.setPassword(accountDTO.getPassword());
        existing.setSex(accountDTO.getSex());
        existing.setBirthday(accountDTO.getBirthday());
        existing.setTimeCreateUser(accountDTO.getTimeCreateUser());
        existing.setAddress(accountDTO.getAddress());
        existing.setAccumulatePoints(accountDTO.getAccumulatePoint());
        existing.setLevel(accountDTO.getLevel());
        existing.setPurchaseHistory(accountDTO.getPurchaseHistory());

        AccountEntity updated = accountRepo.save(existing);
        return ApiResponse.buildSuccessResponse(convertToDTO(updated));
    }

    @Override
    public ApiResponse<String> deleteAccount(Long id) {
        if (!accountRepo.existsById(id)) {
            return ApiResponse.buildErrorResponse(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản để xóa");
        }
        accountRepo.deleteById(id);
        return ApiResponse.buildSuccessResponse("Xóa danh mục thành công", "ID: " + id);
    }

    @Override
    public ApiResponse<AccountDTO> getAccountById(Long id) {
        AccountEntity category = accountRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id " + id));
        return ApiResponse.buildSuccessResponse(convertToDTO(category));
    }

    @Override
    public ApiResponse<List<AccountDTO>> getAllAccount() {
        List<AccountEntity> entities = accountRepo.findAll();
        List<AccountDTO> dtoList = entities.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ApiResponse.buildSuccessResponse(dtoList);
    }


    public AccountEntity getAccount(AccountEntity accountEntity1) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity = accountRepo.getAccountByID(accountEntity1.getId());
        return accountEntity;
    }

    public AccountEntity createAccount(AccountEntity accountEntity1) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(accountEntity1.getId());
        accountEntity.setNumber(accountEntity1.getNumber());
        accountEntity.setPassword(accountEntity1.getPassword());
        accountEntity.setSex(accountEntity1.isSex());
        accountEntity.setBirthday(accountEntity1.getBirthday());
        accountEntity.setTimeCreateUser(accountEntity1.getTimeCreateUser());
        accountEntity.setAddress(accountEntity1.getAddress());
        accountEntity.setAccumulatePoints(accountEntity1.getAccumulatePoints());
        accountEntity.setLevel(accountEntity1.getLevel());
        accountEntity.setPurchaseHistory(accountEntity1.getPurchaseHistory());
        return accountRepo.save(accountEntity);
    }
}
