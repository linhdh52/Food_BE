package com.foodbe.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "number")
    private Long number;

    @Column(name = "password")
    private String password;

    @Column(name = "sex")
    private boolean sex;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "time_create_user")
    private String timeCreateUser;

    @Column(name = "address")
    private String address;

    @Column(name = "accumulate_points")
    private Long accumulatePoints;

    @Column(name = "level")
    private Long level;

    @Column(name = "purchase_history")
    private Date purchaseHistory;

    public AccountEntity() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getTimeCreateUser() {
        return timeCreateUser;
    }

    public void setTimeCreateUser(String timeCreateUser) {
        this.timeCreateUser = timeCreateUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getAccumulatePoints() {
        return accumulatePoints;
    }

    public void setAccumulatePoints(Long accumulatePoints) {
        this.accumulatePoints = accumulatePoints;
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
