package com.foodbe.DTO;

import com.foodbe.DTO.response.MediaResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ProductDTO {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal priceAfterPromotion;
    private BigDecimal promotionDiscount;
    private Integer stock;
    private String unit;
    private String sku;
    private Boolean active;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String primaryImage;
    private List<MediaResponse> images;
    private List<String> tags;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getPriceAfterPromotion() { return priceAfterPromotion; }
    public void setPriceAfterPromotion(BigDecimal priceAfterPromotion) { this.priceAfterPromotion = priceAfterPromotion; }
    public BigDecimal getPromotionDiscount() { return promotionDiscount; }
    public void setPromotionDiscount(BigDecimal promotionDiscount) { this.promotionDiscount = promotionDiscount; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreateDate() { return createDate; }
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }
    public LocalDateTime getUpdateDate() { return updateDate; }
    public void setUpdateDate(LocalDateTime updateDate) { this.updateDate = updateDate; }
    public String getPrimaryImage() { return primaryImage; }
    public void setPrimaryImage(String primaryImage) { this.primaryImage = primaryImage; }
    public List<MediaResponse> getImages() { return images; }
    public void setImages(List<MediaResponse> images) { this.images = images; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}
