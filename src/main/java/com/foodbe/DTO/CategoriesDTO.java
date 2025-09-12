package com.foodbe.DTO;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoriesDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Long parentId;
    private boolean active;
    private Long level;
    private ZonedDateTime createDate;
    private ZonedDateTime updateDate;
    private boolean hasChildren;

    public CategoriesDTO() {
    }

    public CategoriesDTO(Long id, String name, String slug, String description, Long parentId, boolean active) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.parentId = parentId;
        this.active = active;
    }

    public CategoriesDTO(Long id, String name, String slug, String description, Long parentId, boolean active, Long level, ZonedDateTime createDate, ZonedDateTime updateDate) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.parentId = parentId;
        this.active = active;
        this.level = level;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public CategoriesDTO(Long id, String name, String slug, String description, Long parentId, boolean active, Long level, ZonedDateTime createDate, ZonedDateTime updateDate, boolean hasChildren) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.parentId = parentId;
        this.active = active;
        this.level = level;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.hasChildren = hasChildren;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}
