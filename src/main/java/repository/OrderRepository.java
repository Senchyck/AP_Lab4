package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import entity.PizzaOrder;
import entity.User;

public interface OrderRepository extends JpaRepository<PizzaOrder, Integer> {



    List<PizzaOrder> findAllByUser(User user);
    List<PizzaOrder> findAllByUserOrderByOrderDateDesc(User user);

    List<PizzaOrder> findAllByOrderByQuantityDesc();
    List<PizzaOrder> findAllByOrderByOrderDateDesc();
}

