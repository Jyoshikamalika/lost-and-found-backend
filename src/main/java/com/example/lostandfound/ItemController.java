package com.example.lostandfound;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
// *** VERY IMPORTANT: Allows your HTML file to call this server ***
@CrossOrigin(origins = "*") 
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    // GET /api/items - Get all items
    @GetMapping
    public List<Item> getAllItems() {
        // Find all and sort by newest first
        return itemRepository.findAllByOrderByCreatedAtDesc();
    }

    // POST /api/items - Create a new item
    @PostMapping
    public Item createItem(@Valid @RequestBody Item item) {
        // The 'createdAt' timestamp is added automatically
        return itemRepository.save(item);
    }

    // PUT /api/items/{id}/status - Update an item's status
    @PutMapping("/{id}/status")
    public ResponseEntity<Item> updateItemStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newStatus = body.get("status");
        if (newStatus == null || (!newStatus.equals("active") && !newStatus.equals("claimed"))) {
            return ResponseEntity.badRequest().build();
        }

        return itemRepository.findById(id)
            .map(item -> {
                item.setStatus(newStatus);
                Item updatedItem = itemRepository.save(item);
                return ResponseEntity.ok(updatedItem);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/items/{id} - Delete an item
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        return itemRepository.findById(id)
            .map(item -> {
                itemRepository.delete(item);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
