package ro.msg.learning.shop.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.msg.learning.shop.entity.Stock;

import java.util.List;
import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s JOIN FETCH s.product JOIN FETCH s.location WHERE s.product.id IN :productIds")
    List<Stock> findByProductIdIn(@Param("productIds") List<UUID> productIds);
}
