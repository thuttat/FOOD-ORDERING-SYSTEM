package duckie.example.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duckie.example.backend.dto.MenuItemRequest;
import duckie.example.backend.dto.MenuItemResponse;
import duckie.example.backend.entity.MenuItem;
import duckie.example.backend.mapper.MenuItemMapper;
import duckie.example.backend.service.MenuService;

@RestController
@RequestMapping("/api/menu-items")
public class MenuController {
    private final MenuService menuService;
    private final MenuItemMapper menuItemMapper;

    public MenuController(MenuService menuService, MenuItemMapper menuItemMapper) {
        this.menuService = menuService;
        this.menuItemMapper = menuItemMapper;
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByCategory(@PathVariable Long categoryId) {
        List<MenuItemResponse> items = menuService.getMenuItemsByCategory(categoryId)
                .stream().map(menuItemMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<MenuItemResponse> createMenu(@RequestBody MenuItemRequest request) {
        MenuItem createdItem = menuService.createMenuItem(request.categoryId(), menuItemMapper.toEntity(request, null));
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemMapper.toResponse(createdItem));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<MenuItemResponse> updateMenu(@PathVariable Long itemId, @RequestBody MenuItemRequest request) {
        MenuItem updatedItem = menuService.updateMenuItem(itemId, menuItemMapper.toEntity(request, null));
        return ResponseEntity.ok(menuItemMapper.toResponse(updatedItem));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long itemId) {
        menuService.deleteMenuItem(itemId);
        return ResponseEntity.noContent().build();
    }
}