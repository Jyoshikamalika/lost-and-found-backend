package com.example.lostandfound;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    // Spring Data JPA automatically creates a method to find all items
    // and sort them by the 'createdAt' field in descending order (newest first).
    List<Item> findAllByOrderByCreatedAtDesc();

}
