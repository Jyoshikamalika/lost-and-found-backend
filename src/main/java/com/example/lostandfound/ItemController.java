    package com.example.lostandfound;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDateTime;
    import java.util.List;

    // THIS IS THE FINAL LINE YOU ADDED!
    // It tells your server to trust your Netlify frontend.
    @CrossOrigin(origins = "https://melodious-horse-f4f9a7.netlify.app")
    @RestController
    @RequestMapping("/api/items")
    public class ItemController {

        @Autowired
        private ItemRepository itemRepository;

        // GET /api/items - Get all items
        @GetMapping
        public List<Item> getAllItems() {
            // We fetch all items and sort them by date, newest first
            return itemRepository.findAllByOrderByReportedAtDesc();
        }

        // POST /api/items - Create a new item
        @PostMapping
        public Item createItem(@RequestBody Item item) {
            item.setReportedAt(LocalDateTime.now());
            item.setClaimed(false); // New items are never claimed
            return itemRepository.save(item);
        }

        // PUT /api/items/{id}/claim - Mark an item as claimed
        @PutMapping("/{id}/claim")
        public ResponseEntity<Item> toggleClaimItem(@PathVariable Long id) {
            // Find the item in the database by its ID
            return itemRepository.findById(id)
                    .map(item -> {
                        // Toggle the claimed status
                        item.setClaimed(!item.isClaimed());
                        // Save the updated item back to the database
                        Item updatedItem = itemRepository.save(item);
                        // Return the updated item
                        return ResponseEntity.ok(updatedItem);
                    })
                    // If the item with that ID wasn't found, return a 404 (Not Found) error
                    .orElse(ResponseEntity.notFound().build());
        }

        // DELETE /api/items/{id} - Delete an item
        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteItem(@PathVariable Long id) {
            // Find the item by its ID to make sure it exists
            return itemRepository.findById(id)
                    .map(item -> {
                        // If it exists, delete it
                        itemRepository.delete(item);
                        // Return an "OK" response with no content
                        return ResponseEntity.ok().build();
                    })
                    // If it wasn't found, return a 404 (Not Found) error
                    .orElse(ResponseEntity.notFound().build());
        }
    }
    

