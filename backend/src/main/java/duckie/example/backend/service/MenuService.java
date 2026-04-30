



package duckie.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import duckie.example.backend.dto.MenuItemRequest;
import duckie.example.backend.dto.MenuItemResponse;
import duckie.example.backend.entity.Category;
import duckie.example.backend.entity.MenuItem;
import duckie.example.backend.entity.Restaurant;
import duckie.example.backend.mapper.MenuItemMapper;
import duckie.example.backend.repository.CategoryRepository;
import duckie.example.backend.repository.MenuItemRepository;
import duckie.example.backend.repository.RestaurantRepository;
import duckie.example.backend.repository.UserRepository;
import io.jsonwebtoken.io.IOException;

@Service
public class MenuService {

    
    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemMapper menuItemMapper; 
    private final CloudinaryService cloudinaryService;
    private final RestaurantRepository restaurantRepository;

    public MenuService(MenuItemRepository menuItemRepository, CategoryRepository categoryRepository, MenuItemMapper menuItemMapper, CloudinaryService cloudinaryService, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
        this.menuItemMapper = menuItemMapper;
        this.cloudinaryService = cloudinaryService;
        this.restaurantRepository=restaurantRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getMenuItemsByOwnerUsername(String username) {
        return menuItemRepository.findByOwnerUsername(username);
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<MenuItem> getMenuItemsByCategory(Long categoryId) {
        return menuItemRepository.findByCategoryIdAndIsAvailableTrue(categoryId);
    }

  

    @Transactional
    public MenuItem createMenuItem(MenuItemRequest request, String imageUrl) {
        Category category = categoryRepository.findByName(request.categoryName())
            .orElseGet(() -> {
                Category newCat = new Category();
                newCat.setName(request.categoryName());

                Restaurant res = new Restaurant();
                // res.setId(currentRestaurantId);
                res.setId(1L);
                newCat.setRestaurant(res);
                
                return categoryRepository.save(newCat);
            });

        MenuItem newItem = MenuItem.builder()
                .category(category)
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .imageUrl(imageUrl)
                .isAvailable(true)
                .build();

        return menuItemRepository.save(newItem);
    }

   @Transactional
    public MenuItemResponse updateMenuItem(Long itemId, MenuItemRequest incomingData, MultipartFile image) {
        MenuItem existingItem = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Not found MenuItem"));

        existingItem.setName(incomingData.name());
        existingItem.setDescription(incomingData.description());
        existingItem.setPrice(incomingData.price());
        existingItem.setIsAvailable(incomingData.isAvailable());

        if (incomingData.categoryName() != null) {
            Category category = categoryRepository.findByName(incomingData.categoryName())
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(incomingData.categoryName());
                        return categoryRepository.save(newCategory);
                    });
            existingItem.setCategory(category);
        }
        if (image != null && !image.isEmpty()) {
            try {
                String newImageUrl = cloudinaryService.uploadFile(image);
                existingItem.setImageUrl(newImageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Upload image failed", e);
            }
        }
        MenuItem updatedItem = menuItemRepository.save(existingItem);
        return menuItemMapper.toResponse(updatedItem);
    }


    @Transactional
    public void deleteMenuItem(Long itemId) {
        MenuItem existingItem = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Not found MenuItem"));
        existingItem.setIsAvailable(false);
        menuItemRepository.save(existingItem);
    }

    @Transactional
    public MenuItem toggleAvailability(Long itemId) {
        MenuItem item = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setIsAvailable(!item.getIsAvailable()); 
        return menuItemRepository.save(item);
    }   
}