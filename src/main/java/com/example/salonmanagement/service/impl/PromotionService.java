package com.example.salonmanagement.service.impl;

import com.example.salonmanagement.dto.PromotionDTO;
import com.example.salonmanagement.entity.PromotionEntity;
import com.example.salonmanagement.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class cho Promotion
 * Cung cấp các phương thức xử lý business logic cho khuyến mãi
 * 
 * FEATURE 1: Lấy danh sách khuyến mãi
 * - Tất cả methods getAll*, getPromotionsBy*
 */
@Service
@Transactional
public class PromotionService {
    
    @Autowired
    private PromotionRepository promotionRepository;
    
    /**
     * Lấy danh sách tất cả khuyến mãi
     * FEATURE 1: Lấy danh sách khuyến mãi
     */
    public List<PromotionDTO> getAllPromotions() {
        List<PromotionEntity> entities = promotionRepository.findAll();
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lấy khuyến mãi theo ID
     * FEATURE 1: Lấy danh sách khuyến mãi
     */
    public Optional<PromotionDTO> getPromotionById(Integer id) {
        Optional<PromotionEntity> entity = promotionRepository.findById(id);
        return entity.map(this::convertToDTO);
    }
    
    /**
     * Lấy danh sách khuyến mãi theo trạng thái
     * FEATURE 1: Lấy danh sách khuyến mãi
     */
    public List<PromotionDTO> getPromotionsByStatus(PromotionEntity.PromotionStatus status) {
        List<PromotionEntity> entities = promotionRepository.findByStatus(status);
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lấy danh sách khuyến mãi đang hoạt động
     * FEATURE 1: Lấy danh sách khuyến mãi
     */
    public List<PromotionDTO> getActivePromotions() {
        LocalDate currentDate = LocalDate.now();
        List<PromotionEntity> entities = promotionRepository.findActivePromotions(currentDate);
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lấy danh sách khuyến mãi sắp hết hạn
     * FEATURE 1: Lấy danh sách khuyến mãi
     */
    public List<PromotionDTO> getPromotionsExpiringSoon() {
        LocalDate currentDate = LocalDate.now();
        LocalDate futureDate = currentDate.plusDays(7);
        List<PromotionEntity> entities = promotionRepository.findPromotionsExpiringSoon(currentDate, futureDate);
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // ========================================
    // FEATURE 3: Sắp xếp danh sách khuyến mãi
    // ========================================
    
    /**
     * Sắp xếp danh sách khuyến mãi theo tên
     * FEATURE 3: Sắp xếp danh sách khuyến mãi
     */
    public List<PromotionDTO> getPromotionsSortedByName() {
        List<PromotionEntity> entities = promotionRepository.findAllByOrderByNameAsc();
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Sắp xếp danh sách khuyến mãi theo ngày bắt đầu
     * FEATURE 3: Sắp xếp danh sách khuyến mãi
     */
    public List<PromotionDTO> getPromotionsSortedByStartDate() {
        List<PromotionEntity> entities = promotionRepository.findAllByOrderByStartDateAsc();
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Sắp xếp danh sách khuyến mãi theo ngày kết thúc
     * FEATURE 3: Sắp xếp danh sách khuyến mãi
     */
    public List<PromotionDTO> getPromotionsSortedByEndDate() {
        List<PromotionEntity> entities = promotionRepository.findAllByOrderByEndDateAsc();
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // ========================================
    // FEATURE 4: Phân trang danh sách khuyến mãi
    // ========================================
    
    /**
     * Lấy danh sách khuyến mãi với phân trang
     * FEATURE 4: Phân trang danh sách khuyến mãi
     */
    public Page<PromotionDTO> getAllPromotionsWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PromotionEntity> entityPage = promotionRepository.findAll(pageable);
        return entityPage.map(this::convertToDTO);
    }
    
    /**
     * Lấy danh sách khuyến mãi theo trạng thái với phân trang
     * FEATURE 4: Phân trang danh sách khuyến mãi
     */
    public Page<PromotionDTO> getPromotionsByStatusWithPagination(PromotionEntity.PromotionStatus status, 
                                                                 int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PromotionEntity> entityPage = promotionRepository.findByStatus(status, pageable);
        return entityPage.map(this::convertToDTO);
    }
    
    // ========================================
    // FEATURE 5: Thêm khuyến mãi mới
    // ========================================
    
    /**
     * Thêm khuyến mãi mới
     * FEATURE 5: Thêm khuyến mãi mới
     */
    public PromotionDTO addPromotion(PromotionDTO promotionDTO) {
        // Validation
        validatePromotionData(promotionDTO);
        
        // Kiểm tra trùng tên
        if (promotionRepository.existsByName(promotionDTO.getName())) {
            throw new IllegalArgumentException("Tên khuyến mãi đã tồn tại");
        }
        
        // Kiểm tra ngày hợp lệ
        if (promotionDTO.getEndDate().isBefore(promotionDTO.getStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
        
        PromotionEntity entity = convertToEntity(promotionDTO);
        PromotionEntity savedEntity = promotionRepository.save(entity);
        return convertToDTO(savedEntity);
    }
    
    /**
     * Validation dữ liệu khuyến mãi
     * FEATURE 5: Validation cho thêm khuyến mãi mới
     */
    private void validatePromotionData(PromotionDTO promotionDTO) {
        // Kiểm tra null pointer
        if (promotionDTO == null) {
            throw new IllegalArgumentException("Dữ liệu khuyến mãi không được null");
        }
        
        // Validation tên khuyến mãi
        if (promotionDTO.getName() == null || promotionDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khuyến mãi không được để trống");
        }
        
        // Kiểm tra tên chỉ chứa khoảng trắng
        if (promotionDTO.getName().trim().length() == 0) {
            throw new IllegalArgumentException("Tên khuyến mãi không được chỉ chứa khoảng trắng");
        }
        
        // Kiểm tra tên quá ngắn (ít nhất 2 ký tự)
        if (promotionDTO.getName().trim().length() < 2) {
            throw new IllegalArgumentException("Tên khuyến mãi phải có ít nhất 2 ký tự");
        }
        
        // Validation phần trăm giảm giá
        if (promotionDTO.getDiscountPercent() == null) {
            throw new IllegalArgumentException("Phần trăm giảm giá không được để trống");
        }
        
        if (promotionDTO.getDiscountPercent().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Phần trăm giảm giá phải lớn hơn 0");
        }
        
        if (promotionDTO.getDiscountPercent().compareTo(new java.math.BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Phần trăm giảm giá không được vượt quá 100%");
        }
        
        // Validation ngày tháng
        if (promotionDTO.getStartDate() == null) {
            throw new IllegalArgumentException("Ngày bắt đầu không được để trống");
        }
        
        if (promotionDTO.getEndDate() == null) {
            throw new IllegalArgumentException("Ngày kết thúc không được để trống");
        }
        
        // Kiểm tra ngày kết thúc phải sau ngày bắt đầu
        if (promotionDTO.getEndDate().isBefore(promotionDTO.getStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
        
        // Validation trạng thái
        if (promotionDTO.getStatus() == null) {
            throw new IllegalArgumentException("Trạng thái không được để trống");
        }
    }
    
    /**
     * Chuyển đổi từ Entity sang DTO
     * FEATURE 1, 3, 4, 5: Hỗ trợ lấy danh sách khuyến mãi, sắp xếp, phân trang và thêm mới
     */
    private PromotionDTO convertToDTO(PromotionEntity entity) {
        return new PromotionDTO(
                entity.getIdPromotion(),
                entity.getName(),
                entity.getDiscountPercent(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getDescription(),
                entity.getStatus()
        );
    }
    
    /**
     * Chuyển đổi từ DTO sang Entity
     * FEATURE 5: Hỗ trợ thêm khuyến mãi mới
     */
    private PromotionEntity convertToEntity(PromotionDTO dto) {
        return new PromotionEntity(
                dto.getName(),
                dto.getDiscountPercent(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getDescription(),
                dto.getStatus()
        );
    }
    
    // ========================================
    // FEATURE 7: Chỉnh sửa khuyến mãi
    // ========================================
    
    /**
     * Cập nhật khuyến mãi
     * FEATURE 7: Chỉnh sửa khuyến mãi
     */
    public PromotionDTO updatePromotion(Integer id, PromotionDTO promotionDTO) {
        // Kiểm tra khuyến mãi có tồn tại không
        Optional<PromotionEntity> existingEntity = promotionRepository.findById(id);
        if (existingEntity.isEmpty()) {
            throw new IllegalArgumentException("Khuyến mãi không tồn tại");
        }
        
        // Validation
        validatePromotionData(promotionDTO);
        
        // Kiểm tra trùng tên (trừ khuyến mãi hiện tại)
        if (promotionRepository.existsByNameAndIdPromotionNot(promotionDTO.getName(), id)) {
            throw new IllegalArgumentException("Tên khuyến mãi đã tồn tại");
        }
        
        // Kiểm tra ngày hợp lệ
        if (promotionDTO.getEndDate().isBefore(promotionDTO.getStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
        
        PromotionEntity entity = convertToEntity(promotionDTO);
        entity.setIdPromotion(id);
        PromotionEntity savedEntity = promotionRepository.save(entity);
        return convertToDTO(savedEntity);
    }
    
    // ========================================
    // FEATURE 9: Xóa khuyến mãi
    // ========================================
    
    /**
     * Xóa khuyến mãi
     * FEATURE 9: Xóa khuyến mãi
     */
    public boolean deletePromotion(Integer id) {
        if (promotionRepository.existsById(id)) {
            promotionRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // ========================================
    // FEATURE 11: Tìm kiếm khuyến mãi
    // ========================================
    
    /**
     * Tìm kiếm khuyến mãi theo từ khóa
     * FEATURE 11: Tìm kiếm khuyến mãi
     */
    public List<PromotionDTO> searchPromotions(String keyword) {
        List<PromotionEntity> entities = promotionRepository.searchPromotions(keyword);
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Tìm kiếm khuyến mãi với phân trang
     * FEATURE 11: Tìm kiếm khuyến mãi với phân trang
     */
    public Page<PromotionDTO> searchPromotionsWithPagination(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PromotionEntity> entityPage = promotionRepository.searchPromotionsWithPagination(keyword, pageable);
        return entityPage.map(this::convertToDTO);
    }
}