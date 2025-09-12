package com.foodbe.entity;

import com.foodbe.constants.PrefixedCode;
import com.foodbe.constants.PrefixedCodeListener;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "categories")
@PrefixedCode(field = "categoriesCode", prefix = "CATEGORIES")
@EntityListeners(PrefixedCodeListener.class)
public class CategoriesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categories_id")
    private Long id;

    @Column(name = "categories_code", unique = true, nullable = true)
    private String categoriesCode;

    @Column(nullable = false, unique = true, name = "name")
    private String name;
 
    @Column(name = "slug", unique = true)
    private String slug;

    @Column(name = "description")
    private String description;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "level")
    private Long level;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "update_date")
    private ZonedDateTime updateDate;

    public CategoriesEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoriesCode() {
        return categoriesCode;
    }

    public void setCategoriesCode(String categoriesCode) {
        this.categoriesCode = categoriesCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(ZonedDateTime updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "CategoriesEntity{" +
                "id=" + id +
                ", categoriesCode='" + categoriesCode + '\'' +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", description='" + description + '\'' +
                ", parentId=" + parentId +
                ", active=" + active +
                ", level=" + level +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
