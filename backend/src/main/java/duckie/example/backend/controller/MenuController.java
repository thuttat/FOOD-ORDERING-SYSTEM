package duckie.example.backend.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("/api/menu-items")
public class MenuController {
    private final MenuService menuService;
    private final MenuItemMapper menuItemMapper;
    private final CloudinaryService cloudinaryService;

    public MenuController(MenuService menuService, MenuItemMapper menuItemMapper, CloudinaryService cloudinaryService) {
        this.menuService = menuService;
        this.menuItemMapper = menuItemMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> getAllMenuItems(Principal principal) {
        String username = principal.getName();
        List<MenuItemResponse> items = menuService.getMenuItemsByOwnerUsername(username)
                .stream()
                .map(menuItemMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByCategory(@PathVariable Long categoryId) {
        List<MenuItemResponse> items = menuService.getMenuItemsByCategory(categoryId)
                .stream().map(menuItemMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItemResponse> createMenu(
        @RequestPart("data") MenuItemRequest request,
        @RequestPart("image") MultipartFile image 
    ) {
        String imageUrl = cloudinaryService.uploadFile(image); 
        MenuItem createdItem = menuService.createMenuItem(request, imageUrl); 
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemMapper.toResponse(createdItem));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MenuItemResponse> updateMenuItem(
        @PathVariable Long id,
        @RequestPart("data") MenuItemRequest request, 
        @RequestPart(value = "image", required = false) MultipartFile image 
    ) {
        MenuItemResponse updatedItem = menuService.updateMenuItem(id, request, image); 
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long itemId) {
        menuService.deleteMenuItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{itemId}/toggle")
    public ResponseEntity<MenuItemResponse> toggleMenuStatus(@PathVariable Long itemId) {
        MenuItem toggledItem = menuService.toggleAvailability(itemId);
        return ResponseEntity.ok(menuItemMapper.toResponse(toggledItem));
    }
}