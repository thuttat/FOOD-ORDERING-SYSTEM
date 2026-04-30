package duckie.example.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import duckie.example.backend.dto.MenuItemRequest;
import duckie.example.backend.dto.MenuItemResponse;
import duckie.example.backend.entity.MenuItem;
import duckie.example.backend.mapper.MenuItemMapper;
import duckie.example.backend.service.CloudinaryService;
import duckie.example.backend.service.MenuService;

@RestController
@RequestMapping("/api/cloudinary")
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;
    private final MenuService menuService;
    private final MenuItemMapper menuItemMapper;

    public CloudinaryController(CloudinaryService cloudinaryService, MenuService menuService, MenuItemMapper menuItemMapper) {
        this.cloudinaryService = cloudinaryService;
        this.menuService = menuService;
        this.menuItemMapper = menuItemMapper;
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItemResponse> createMenuItem(
            @RequestPart("data") MenuItemRequest request,
            @RequestPart("image") MultipartFile file) {
        
        String imageUrl = cloudinaryService.uploadFile(file);
        
        MenuItem createdItem = menuService.createMenuItem(request, imageUrl);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemMapper.toResponse(createdItem));
    }
    
}
