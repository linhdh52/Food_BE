package com.foodbe.DTO;

import java.util.ArrayList;
import java.util.List;

public class CategoriesDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Long parentId;
    private boolean active;
    private List<CategoriesDTO> children = new ArrayList<>();

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

    public List<CategoriesDTO> getChildren() {
        return children;
    }

    public void setChildren(List<CategoriesDTO> children) {
        this.children = children;
    }
}
