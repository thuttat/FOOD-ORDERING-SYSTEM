package duckie.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import duckie.example.backend.entity.Category;
import duckie.example.backend.entity.MenuItem;
import duckie.example.backend.repository.CategoryRepository;
import duckie.example.backend.repository.MenuItemRepository;

@Service
public class MenuService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public MenuService(MenuItemRepository menuItemRepository, CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getMenuItemsByCategory(Long categoryId) {
        return menuItemRepository.findByCategoryIdAndIsAvailableTrue(categoryId);
    }

    @Transactional
    public MenuItem createMenuItem(Long categoryId, MenuItem incomingData) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Category"));

        MenuItem newItem = MenuItem.builder()
                .category(category)
                .name(incomingData.getName())
                .description(incomingData.getDescription())
                .price(incomingData.getPrice())
                .imageUrl(incomingData.getImageUrl())
                .isAvailable(true) 
                .build();

        return menuItemRepository.save(newItem);
    }

    @Transactional
    public MenuItem updateMenuItem(Long itemId, MenuItem incomingData) {
        MenuItem existingItem = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy MenuItem"));

        existingItem.setName(incomingData.getName());
        existingItem.setDescription(incomingData.getDescription());
        existingItem.setPrice(incomingData.getPrice());
        existingItem.setImageUrl(incomingData.getImageUrl());
        existingItem.setIsAvailable(incomingData.getIsAvailable());

        return menuItemRepository.save(existingItem);
    }

    @Transactional
    public void deleteMenuItem(Long itemId) {
        MenuItem existingItem = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy MenuItem"));
        existingItem.setIsAvailable(false);
        menuItemRepository.save(existingItem);
    }


    
}