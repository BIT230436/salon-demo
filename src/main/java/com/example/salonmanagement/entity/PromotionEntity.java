package com.example.salonmanagement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity class cho bảng Promotion trong database
 * Tương ứng với bảng promotion trong MySQL
 * 
 * FEATURE 1: Lấy danh sách khuyến mãi
 * - Cấu trúc entity cơ bản để lưu trữ thông tin khuyến mãi
 */
@Entity
@Table(name = "promotion")
public class PromotionEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPromotion")
    private Integer idPromotion;
    
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    @Column(name = "discountPercent", precision = 5, scale = 2, nullable = false)
    private BigDecimal discountPercent;
    
    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PromotionStatus status;
    
    // Constructor mặc định
    public PromotionEntity() {}
    
    // Constructor với tham số
    public PromotionEntity(String name, BigDecimal discountPercent, LocalDate startDate, 
                          LocalDate endDate, String description, PromotionStatus status) {
        this.name = name;
        this.discountPercent = discountPercent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.status = status;
    }
    
    // Getters và Setters
    public Integer getIdPromotion() {
        return idPromotion;
    }
    
    public void setIdPromotion(Integer idPromotion) {
        this.idPromotion = idPromotion;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }
    
    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public PromotionStatus getStatus() {
        return status;
    }
    
    public void setStatus(PromotionStatus status) {
        this.status = status;
    }
    
    // Enum cho trạng thái khuyến mãi
    // FEATURE 1: Hỗ trợ lấy danh sách khuyến mãi theo trạng thái
    public enum PromotionStatus {
        ACTIVE,     // Đang hoạt động
        INACTIVE,   // Không hoạt động
        EXPIRED,    // Đã hết hạn
        UPCOMING    // Sắp diễn ra
    }
}