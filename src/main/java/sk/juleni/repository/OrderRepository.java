package sk.juleni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.juleni.model.Order;

import java.util.List;

public interface OrderRepository  extends JpaRepository<Order, Integer> {
    @Query("from Order where order_id=?1")
    Order findOneById(Long orderId);

    @Query("from Order where order_no=?1 and user_id=?2")
    Order findByNoAndUserId(String order_no, Long user_id);

    @Query("from Order where user_id=?1")
    List<Order> findByUserId(String user_id);
}
