package duckie.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import duckie.example.backend.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    // Spring Data JPA đã lo sẵn các hàm cơ bản, 
    // bạn chưa cần viết thêm custom query nào ở đây cho đến khi có nghiệp vụ phát sinh thêm.
}