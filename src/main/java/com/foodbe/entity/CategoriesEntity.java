package com.foodbe.entity;

import com.foodbe.constants.PrefixedCode;
import com.foodbe.constants.PrefixedCodeListener;

import javax.persistence.*;

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

    public CategoriesEntity() {
    }

    public CategoriesEntity(Long id, String categoriesCode, String name, String slug, String description, Long parentId, boolean active) {
        this.id = id;
        this.categoriesCode = categoriesCode;
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

    @Override
    public String toString() {
        return "CategoriesEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", description='" + description + '\'' +
                ", parentId=" + parentId +
                ", active=" + active +
                '}';
    }
}
