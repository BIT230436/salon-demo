package com.example.salonmanagement.dto;

import com.example.salonmanagement.entity.PromotionEntity;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) cho Promotion
 * Sử dụng để truyền dữ liệu giữa các layer
 * 
 * FEATURE 1: Lấy danh sách khuyến mãi
 * - DTO để truyền dữ liệu khuyến mãi
 */
public class PromotionDTO {
    
    private Integer idPromotion;
    
    @NotBlank(message = "Tên khuyến mãi không được để trống")
    @Size(max = 100, message = "Tên khuyến mãi không được vượt quá 100 ký tự")
    private String name;
    
    @NotNull(message = "Phần trăm giảm giá không được để trống")
    @DecimalMin(value = "0.01", message = "Phần trăm giảm giá phải lớn hơn 0")
    @DecimalMax(value = "100.00", message = "Phần trăm giảm giá không được vượt quá 100%")
    private BigDecimal discountPercent;
    
    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;
    
    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;
    
    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;
    
    @NotNull(message = "Trạng thái không được để trống")
    private PromotionEntity.PromotionStatus status;
    
    // Constructor mặc định
    public PromotionDTO() {}
    
    // Constructor với tham số
    public PromotionDTO(Integer idPromotion, String name, BigDecimal discountPercent, 
                       LocalDate startDate, LocalDate endDate, String description, 
                       PromotionEntity.PromotionStatus status) {
        this.idPromotion = idPromotion;
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
    
    public PromotionEntity.PromotionStatus getStatus() {
        return status;
    }
    
    public void setStatus(PromotionEntity.PromotionStatus status) {
        this.status = status;
    }
    
    /**
     * Kiểm tra xem khuyến mãi có còn hiệu lực không
     * @return true nếu khuyến mãi còn hiệu lực
     * FEATURE 1: Hỗ trợ lấy danh sách khuyến mãi có hiệu lực
     */
    public boolean isValid() {
        LocalDate today = LocalDate.now();
        return status == PromotionEntity.PromotionStatus.ACTIVE && 
               !startDate.isAfter(today) && 
               !endDate.isBefore(today);
    }
    
    /**
     * Kiểm tra xem khuyến mãi có sắp hết hạn không (trong 7 ngày tới)
     * @return true nếu khuyến mãi sắp hết hạn
     * FEATURE 1: Hỗ trợ lấy danh sách khuyến mãi sắp hết hạn
     */
    public boolean isExpiringSoon() {
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysFromNow = today.plusDays(7);
        return status == PromotionEntity.PromotionStatus.ACTIVE && 
               endDate.isAfter(today) && 
               endDate.isBefore(sevenDaysFromNow);
    }
    
    // ========================================
    // FEATURE 5: Thêm khuyến mãi mới
    // ========================================
    
    /**
     * Validation business logic cho khuyến mãi
     * @return true nếu dữ liệu hợp lệ
     * FEATURE 5: Validation cho thêm khuyến mãi mới
     */
    public boolean isValidBusinessLogic() {
        // Kiểm tra ngày kết thúc phải sau ngày bắt đầu
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            return false;
        }
        
        // Kiểm tra khuyến mãi không được kéo dài quá 1 năm
        if (startDate != null && endDate != null) {
            LocalDate maxEndDate = startDate.plusYears(1);
            if (endDate.isAfter(maxEndDate)) {
                return false;
            }
        }
        
        // Kiểm tra khuyến mãi không được bắt đầu quá xa trong tương lai (2 năm)
        if (startDate != null) {
            LocalDate maxStartDate = LocalDate.now().plusYears(2);
            if (startDate.isAfter(maxStartDate)) {
                return false;
            }
        }
        
        // Kiểm tra tên không chứa ký tự đặc biệt nguy hiểm
        if (name != null && (name.contains("<") || name.contains(">") || name.contains("script"))) {
            return false;
        }
        
        return true;
    }
}