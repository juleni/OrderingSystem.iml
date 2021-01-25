package sk.juleni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.juleni.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
      @Query("from Product where product_id=?1")
      Product findOneById(Long productId);

      @Query("from Product where product_code=?1")
      List<Product> findByCode(String code);

      @Query("from Product where user_id=?1")
      List<Product> findByUserId(String userId);

      @Query("from Product where product_id in :userIDs")
      List<Product> findByProductIDs(List<Long> userIDs);

      @Query("delete from Product where order_id=?1")
      List<Product> deleteByOrderId(String orderId);
}
