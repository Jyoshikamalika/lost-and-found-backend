package com.example.lostandfound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// --- THIS IS THE MISSING LINE! ---
import java.time.LocalDateTime; 
// --- END OF FIX ---

import java.util.List; // This import was also needed for List

@CrossOrigin(origins = "https://melodious-horse-f4f9a7.netlify.app")
@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    // GET /api/items - Get all items
    @GetMapping
    public List<Item> getAllItems() {
        return itemRepository.findAllByOrderByReportedAtDesc();
    }

    // POST /api/items - Create a new item
    @PostMapping
    public Item createItem(@RequestBody Item item) {
        item.setReportedAt(LocalDateTime.now()); // This line will now work
        item.setClaimed(false);
        return itemRepository.save(item);
    }

    // PUT /api/items/{id}/claim - Mark an item as claimed
    @PutMapping("/{id}/claim")
    public ResponseEntity<Item> toggleClaimItem(@PathVariable Long id) {
        return itemRepository.findById(id)
                .map(item -> {
                    item.setClaimed(!item.isClaimed());
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

