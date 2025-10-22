package com.example.salonmanagement.repository;

import com.example.salonmanagement.entity.PromotionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface cho PromotionEntity
 * Cung cấp các phương thức truy vấn database cho bảng promotion
 * 
 * FEATURE 1: Lấy danh sách khuyến mãi
 * - Các query cơ bản để lấy danh sách khuyến mãi
 */
@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity, Integer> {
    
    /**
     * Lấy danh sách khuyến mãi theo trạng thái
     * FEATURE 1: Lấy danh sách khuyến mãi theo trạng thái
     */
    List<PromotionEntity> findByStatus(PromotionEntity.PromotionStatus status);
    
    /**
     * Lấy danh sách khuyến mãi đang hoạt động
     * FEATURE 1: Lấy danh sách khuyến mãi đang hoạt động
     */
    @Query("SELECT p FROM PromotionEntity p WHERE p.status = 'ACTIVE' AND " +
           "p.startDate <= :currentDate AND p.endDate >= :currentDate")
    List<PromotionEntity> findActivePromotions(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Lấy danh sách khuyến mãi sắp hết hạn (trong 7 ngày tới)
     * FEATURE 1: Lấy danh sách khuyến mãi sắp hết hạn
     */
    @Query("SELECT p FROM PromotionEntity p WHERE p.status = 'ACTIVE' AND " +
           "p.endDate BETWEEN :currentDate AND :futureDate")
    List<PromotionEntity> findPromotionsExpiringSoon(@Param("currentDate") LocalDate currentDate, 
                                                    @Param("futureDate") LocalDate futureDate);
    
    // ========================================
    // FEATURE 3: Sắp xếp danh sách khuyến mãi
    // ========================================
    
    /**
     * Lấy danh sách khuyến mãi sắp xếp theo tên
     * FEATURE 3: Sắp xếp danh sách khuyến mãi theo tên
     */
    List<PromotionEntity> findAllByOrderByNameAsc();
    
    /**
     * Lấy danh sách khuyến mãi sắp xếp theo ngày bắt đầu
     * FEATURE 3: Sắp xếp danh sách khuyến mãi theo ngày bắt đầu
     */
    List<PromotionEntity> findAllByOrderByStartDateAsc();
    
    /**
     * Lấy danh sách khuyến mãi sắp xếp theo ngày kết thúc
     * FEATURE 3: Sắp xếp danh sách khuyến mãi theo ngày kết thúc
     */
    List<PromotionEntity> findAllByOrderByEndDateAsc();
    
    // ========================================
    // FEATURE 4: Phân trang danh sách khuyến mãi
    // ========================================
    
    /**
     * Lấy danh sách khuyến mãi theo trạng thái với phân trang
     * FEATURE 4: Phân trang danh sách khuyến mãi
     */
    Page<PromotionEntity> findByStatus(PromotionEntity.PromotionStatus status, Pageable pageable);
    
    // ========================================
    // FEATURE 5: Thêm khuyến mãi mới
    // ========================================
    
    /**
     * Kiểm tra xem có khuyến mãi nào trùng tên không
     * FEATURE 5: Validation thêm khuyến mãi mới
     */
    boolean existsByName(String name);
    
    // ========================================
    // FEATURE 7: Chỉnh sửa khuyến mãi
    // ========================================
    
    /**
     * Kiểm tra xem có khuyến mãi nào trùng tên (trừ khuyến mãi hiện tại)
     * FEATURE 7: Validation chỉnh sửa khuyến mãi
     */
    boolean existsByNameAndIdPromotionNot(String name, Integer idPromotion);
    
    // ========================================
    // FEATURE 11: Tìm kiếm khuyến mãi
    // ========================================
    
    /**
     * Tìm kiếm khuyến mãi theo tên hoặc mô tả
     * FEATURE 11: Tìm kiếm khuyến mãi
     */
    @Query("SELECT p FROM PromotionEntity p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<PromotionEntity> searchPromotions(@Param("keyword") String keyword);
    
    /**
     * Tìm kiếm khuyến mãi với phân trang
     * FEATURE 11: Tìm kiếm khuyến mãi với phân trang
     */
    @Query("SELECT p FROM PromotionEntity p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<PromotionEntity> searchPromotionsWithPagination(@Param("keyword") String keyword, Pageable pageable);
}