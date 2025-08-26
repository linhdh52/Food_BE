package com.foodbe.DTO;

import java.util.Date;

public class AccountDTO {

    private Long id;
    private Long number;
    private String name;
    private String password;
    private Boolean sex;
    private Date birthday;
    private Date timeCreateUser;
    private String address;
    private Long accumulatePoint;
    private Long level;
    private Date purchaseHistory;

    public AccountDTO() {
    }

    public AccountDTO(Long id, Long number, String name, String password, Boolean sex, Date birthday, Date timeCreateUser, String address, Long accumulatePoint, Long level, Date purchaseHistory) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.birthday = birthday;
        this.timeCreateUser = timeCreateUser;
        this.address = address;
        this.accumulatePoint = accumulatePoint;
        this.level = level;
        this.purchaseHistory = purchaseHistory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getTimeCreateUser() {
        return timeCreateUser;
    }

    public void setTimeCreateUser(Date timeCreateUser) {
        this.timeCreateUser = timeCreateUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getAccumulatePoint() {
        return accumulatePoint;
    }

    public void setAccumulatePoint(Long accumulatePoint) {
        this.accumulatePoint = accumulatePoint;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Date getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(Date purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }
}
