package duckie.example.backend.controller;

import java.util.List;

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

import duckie.example.backend.entity.MenuItem;
import duckie.example.backend.service.MenuService;


@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(@PathVariable Long categoryId) {
        List<MenuItem> items = menuService.getMenuItemsByCategory(categoryId);
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<MenuItem> createMenu(@RequestBody MenuItem menuItem) {
        if (menuItem.getCategory() == null || menuItem.getCategory().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Long categoryId = menuItem.getCategory().getId();
        MenuItem createdItem = menuService.createMenuItem(categoryId, menuItem);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<MenuItem> updateMenu(@PathVariable Long itemId, @RequestBody MenuItem menuItem) {
        MenuItem updatedItem = menuService.updateMenuItem(itemId, menuItem);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long itemId) {
        menuService.deleteMenuItem(itemId);
        return ResponseEntity.noContent().build();
    }
    
}
