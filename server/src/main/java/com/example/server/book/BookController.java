package com.example.server.book;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController
{
    private final BookRepository repo;

    public BookController(BookRepository repo)
    {
        this.repo = repo;
    }

    @GetMapping("")
    public Iterable<Book> listAll() { return this.repo.findAll(); }

    @PostMapping("")
    public Object create(@RequestBody Book newBook) { return this.repo.save(newBook); }

    @GetMapping("/{id}")
    public Object read(@PathVariable Long id)
    {
        return (this.repo.existsById(id) ?
                this.repo.findById(id) :
                "This item does not exist");
    }

    @PatchMapping("/{id}")
    public Book update(@PathVariable Long id, @RequestBody Book newBook)
    {
        // update if exists
        if (repo.existsById(id))
        {
            Book currBook = repo.findById(id).orElse(null);

            if (newBook.getAuthor()!= null)
            {
                currBook.setAuthor(newBook.getAuthor());
            }
            if (newBook.getTitle()!= null)
            {
                currBook.setTitle(newBook.getTitle());
            }
            if (newBook.isFavorite() != currBook.isFavorite())
            {
                currBook.setFavorite(newBook.isFavorite());
            }

            return repo.save(currBook);
        }
        else return repo.save(newBook);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id)
    {
        // delete if it exists
        if (this.repo.existsById(id))
        {
            this.repo.deleteById(id);
            return "Item deleted";
        }
        else return "This item does not exist";
    }
}
