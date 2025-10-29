package com.example.lostandfound;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// This is an interface that Spring Boot will automatically implement for us
public interface ItemRepository extends JpaRepository<Item, Long> {

    // THIS IS THE NEW LINE YOU ARE ADDING!
    // This custom method tells Spring to find all items and sort them by the
    // "reportedAt" field in descending order (newest first).
    List<Item> findAllByOrderByReportedAtDesc();

}

