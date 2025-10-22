package com.example.salonmanagement.controller;

import com.example.salonmanagement.dto.PromotionDTO;
import com.example.salonmanagement.entity.PromotionEntity;
import com.example.salonmanagement.service.impl.PromotionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller cho Promotion
 * Cung cấp các REST endpoints cho quản lý khuyến mãi
 * 
 * FEATURE 1: Lấy danh sách khuyến mãi
 * - Tất cả GET endpoints
 */
@RestController
@RequestMapping("/api/promotions")
@CrossOrigin(origins = "*")
public class PromotionController {
    
    @Autowired
    private PromotionService promotionService;
    
    /**
     * Lấy danh sách tất cả khuyến mãi
     * FEATURE 1: Lấy danh sách khuyến mãi
     * Endpoint: GET /api/promotions
     */
    @GetMapping
    public ResponseEntity<List<PromotionDTO>> getAllPromotions() {
        try {
            List<PromotionDTO> promotions = promotionService.getAllPromotions();
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Lấy khuyến mãi theo ID
     * FEATURE 1: Lấy danh sách khuyến mãi
     * Endpoint: GET /api/promotions/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PromotionDTO> getPromotionById(@PathVariable Integer id) {
        try {
            Optional<PromotionDTO> promotion = promotionService.getPromotionById(id);
            if (promotion.isPresent()) {
                return ResponseEntity.ok(promotion.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Lấy danh sách khuyến mãi theo trạng thái
     * FEATURE 1: Lấy danh sách khuyến mãi
     * Endpoint: GET /api/promotions/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PromotionDTO>> getPromotionsByStatus(@PathVariable String status) {
        try {
            PromotionEntity.PromotionStatus promotionStatus = PromotionEntity.PromotionStatus.valueOf(status.toUpperCase());
            List<PromotionDTO> promotions = promotionService.getPromotionsByStatus(promotionStatus);
            return ResponseEntity.ok(promotions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Lấy danh sách khuyến mãi đang hoạt động
     * FEATURE 1: Lấy danh sách khuyến mãi
     * Endpoint: GET /api/promotions/active
     */
    @GetMapping("/active")
    public ResponseEntity<List<PromotionDTO>> getActivePromotions() {
        try {
            List<PromotionDTO> promotions = promotionService.getActivePromotions();
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Lấy danh sách khuyến mãi sắp hết hạn
     * FEATURE 1: Lấy danh sách khuyến mãi
     * Endpoint: GET /api/promotions/expiring-soon
     */
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<PromotionDTO>> getPromotionsExpiringSoon() {
        try {
            List<PromotionDTO> promotions = promotionService.getPromotionsExpiringSoon();
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // ========================================
    // FEATURE 3: Sắp xếp danh sách khuyến mãi
    // ========================================
    
    /**
     * Sắp xếp danh sách khuyến mãi theo tên
     * FEATURE 3: Sắp xếp danh sách khuyến mãi
     * Endpoint: GET /api/promotions/sorted/name
     */
    @GetMapping("/sorted/name")
    public ResponseEntity<List<PromotionDTO>> getPromotionsSortedByName() {
        try {
            List<PromotionDTO> promotions = promotionService.getPromotionsSortedByName();
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Sắp xếp danh sách khuyến mãi theo ngày bắt đầu
     * FEATURE 3: Sắp xếp danh sách khuyến mãi
     * Endpoint: GET /api/promotions/sorted/start-date
     */
    @GetMapping("/sorted/start-date")
    public ResponseEntity<List<PromotionDTO>> getPromotionsSortedByStartDate() {
        try {
            List<PromotionDTO> promotions = promotionService.getPromotionsSortedByStartDate();
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Sắp xếp danh sách khuyến mãi theo ngày kết thúc
     * FEATURE 3: Sắp xếp danh sách khuyến mãi
     * Endpoint: GET /api/promotions/sorted/end-date
     */
    @GetMapping("/sorted/end-date")
    public ResponseEntity<List<PromotionDTO>> getPromotionsSortedByEndDate() {
        try {
            List<PromotionDTO> promotions = promotionService.getPromotionsSortedByEndDate();
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // ========================================
    // FEATURE 4: Phân trang danh sách khuyến mãi
    // ========================================
    
    /**
     * Lấy danh sách khuyến mãi với phân trang
     * FEATURE 4: Phân trang danh sách khuyến mãi
     * Endpoint: GET /api/promotions/paginated?page=0&size=10
     */
    @GetMapping("/paginated")
    public ResponseEntity<?> getAllPromotionsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // Kiểm tra page hợp lệ
            if (page < 0) {
                return ResponseEntity.badRequest().body("Số trang phải >= 0");
            }
            
            // Kiểm tra size hợp lệ
            if (size <= 0) {
                return ResponseEntity.badRequest().body("Kích thước trang phải > 0");
            }
            
            // Giới hạn size tối đa để tránh load quá nhiều dữ liệu
            if (size > 100) {
                return ResponseEntity.badRequest().body("Kích thước trang không được vượt quá 100");
            }
            
            Page<PromotionDTO> promotions = promotionService.getAllPromotionsWithPagination(page, size);
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
        }
    }
    
    /**
     * Lấy danh sách khuyến mãi theo trạng thái với phân trang
     * FEATURE 4: Phân trang danh sách khuyến mãi
     * Endpoint: GET /api/promotions/status/{status}/paginated?page=0&size=10
     */
    @GetMapping("/status/{status}/paginated")
    public ResponseEntity<Page<PromotionDTO>> getPromotionsByStatusWithPagination(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PromotionEntity.PromotionStatus promotionStatus = PromotionEntity.PromotionStatus.valueOf(status.toUpperCase());
            Page<PromotionDTO> promotions = promotionService.getPromotionsByStatusWithPagination(promotionStatus, page, size);
            return ResponseEntity.ok(promotions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // ========================================
    // FEATURE 5: Thêm khuyến mãi mới
    // ========================================
    
    /**
     * Thêm khuyến mãi mới
     * FEATURE 5: Thêm khuyến mãi mới
     * Endpoint: POST /api/promotions
     */
    @PostMapping
    public ResponseEntity<?> addPromotion(@Valid @RequestBody PromotionDTO promotionDTO) {
        try {
            // Kiểm tra null request body
            if (promotionDTO == null) {
                return ResponseEntity.badRequest().body("Dữ liệu khuyến mãi không được để trống");
            }
            
            // Kiểm tra business logic validation
            if (!promotionDTO.isValidBusinessLogic()) {
                return ResponseEntity.badRequest().body("Dữ liệu khuyến mãi không hợp lệ theo quy tắc nghiệp vụ");
            }
            
            PromotionDTO savedPromotion = promotionService.addPromotion(promotionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPromotion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
        }
    }
    
    // ========================================
    // FEATURE 7: Chỉnh sửa khuyến mãi
    // ========================================
    
    /**
     * Cập nhật khuyến mãi
     * FEATURE 7: Chỉnh sửa khuyến mãi
     * Endpoint: PUT /api/promotions/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePromotion(@PathVariable Integer id, @Valid @RequestBody PromotionDTO promotionDTO) {
        try {
            // Kiểm tra ID hợp lệ
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body("ID khuyến mãi không hợp lệ");
            }
            
            // Kiểm tra null request body
            if (promotionDTO == null) {
                return ResponseEntity.badRequest().body("Dữ liệu khuyến mãi không được để trống");
            }
            
            // Kiểm tra business logic validation
            if (!promotionDTO.isValidBusinessLogic()) {
                return ResponseEntity.badRequest().body("Dữ liệu khuyến mãi không hợp lệ theo quy tắc nghiệp vụ");
            }
            
            PromotionDTO updatedPromotion = promotionService.updatePromotion(id, promotionDTO);
            return ResponseEntity.ok(updatedPromotion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
        }
    }
    
    // ========================================
    // FEATURE 9: Xóa khuyến mãi
    // ========================================
    
    /**
     * Xóa khuyến mãi
     * FEATURE 9: Xóa khuyến mãi
     * Endpoint: DELETE /api/promotions/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePromotion(@PathVariable Integer id) {
        try {
            // Kiểm tra ID hợp lệ
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body("ID khuyến mãi không hợp lệ");
            }
            
            boolean deleted = promotionService.deletePromotion(id);
            if (deleted) {
                return ResponseEntity.ok().body("Xóa khuyến mãi thành công");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
        }
    }
    
    // ========================================
    // FEATURE 11: Tìm kiếm khuyến mãi
    // ========================================
    
    /**
     * Tìm kiếm khuyến mãi theo từ khóa
     * FEATURE 11: Tìm kiếm khuyến mãi
     * Endpoint: GET /api/promotions/search?keyword=...
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchPromotions(@RequestParam String keyword) {
        try {
            // Kiểm tra keyword hợp lệ
            if (keyword == null || keyword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Từ khóa tìm kiếm không được để trống");
            }
            
            // Kiểm tra keyword quá ngắn
            if (keyword.trim().length() < 2) {
                return ResponseEntity.badRequest().body("Từ khóa tìm kiếm phải có ít nhất 2 ký tự");
            }
            
            // Kiểm tra keyword quá dài
            if (keyword.length() > 100) {
                return ResponseEntity.badRequest().body("Từ khóa tìm kiếm không được vượt quá 100 ký tự");
            }
            
            // Kiểm tra ký tự nguy hiểm
            if (keyword.contains("<") || keyword.contains(">") || keyword.contains("script")) {
                return ResponseEntity.badRequest().body("Từ khóa tìm kiếm chứa ký tự không hợp lệ");
            }
            
            List<PromotionDTO> promotions = promotionService.searchPromotions(keyword.trim());
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
        }
    }
    
    /**
     * Tìm kiếm khuyến mãi với phân trang
     * FEATURE 11: Tìm kiếm khuyến mãi với phân trang
     * Endpoint: GET /api/promotions/search/paginated?keyword=...&page=0&size=10
     */
    @GetMapping("/search/paginated")
    public ResponseEntity<Page<PromotionDTO>> searchPromotionsWithPagination(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<PromotionDTO> promotions = promotionService.searchPromotionsWithPagination(keyword, page, size);
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}