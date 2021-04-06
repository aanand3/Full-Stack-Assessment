package com.example.server;

import com.example.server.book.Book;
import com.example.server.book.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookControllerTests
{
    @Autowired
    MockMvc mvc;

    @Autowired
    private BookRepository repo;

    @Test
    @Transactional
    @Rollback
    public void testGetMapping() throws Exception
    {
        // create a book and save it
        Book newBook = new Book();
        newBook.setAuthor("paolini");
        newBook.setTitle("eragon");
        repo.save(newBook);

        // check that it's in the list
        MockHttpServletRequestBuilder getRequest = get("/books")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(getRequest)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].title", is(newBook.getTitle())));
    }

    @Test
    @Transactional
    @Rollback
    public void testPostMapping() throws Exception
    {
        // create and send a book as post body
        Book newBook = new Book();
        newBook.setAuthor("paolini");
        newBook.setTitle("eragon");

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder postRequest = post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newBook));

        this.mvc.perform(postRequest)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title", is(newBook.getTitle())));

        // check that its in the list
        MockHttpServletRequestBuilder getRequest = get("/books")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(getRequest)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].title", is(newBook.getTitle())));
    }

    @Test
    @Transactional
    @Rollback
    public void testReadMapping() throws Exception
    {
        // create and save books
        Book newBook = new Book();
        newBook.setAuthor("paolini");
        newBook.setTitle("eragon");

        Book newBook2 = new Book();
        newBook2.setAuthor("paolini");
        newBook2.setTitle("eldest");

        repo.save(newBook); repo.save(newBook2);

        // check a specific one
        MockHttpServletRequestBuilder getRequest = get("/books/2")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(getRequest)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title", is(newBook2.getTitle())));
    }

    @Test
    @Transactional
    @Rollback
    public void testPatchMapping() throws Exception
    {
        // create and save books
        Book newBook = new Book();
        newBook.setAuthor("paolini");
        newBook.setTitle("eragon");

        Book newBook2 = new Book();
        newBook2.setAuthor("paolini");
        newBook2.setTitle("eldest");

        repo.save(newBook); repo.save(newBook2);

        // update the title locally
        newBook2.setTitle("brisingr");

        // patch a specific one
        ObjectMapper mapper = new ObjectMapper();
        MockHttpServletRequestBuilder patchRequest = patch("/books/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newBook2));

        this.mvc.perform(patchRequest)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title", is(newBook2.getTitle())));

        // double check with a get request
        MockHttpServletRequestBuilder getRequest = get("/books/2")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(getRequest)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title", is(newBook2.getTitle())));
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteMapping() throws Exception
    {
        // create and save books
        Book newBook = new Book();
        newBook.setAuthor("paolini");
        newBook.setTitle("eragon");

        Book newBook2 = new Book();
        newBook2.setAuthor("paolini");
        newBook2.setTitle("eldest");

        repo.save(newBook); repo.save(newBook2);

        // delete a specific one
        MockHttpServletRequestBuilder deleteRequest = delete("/books/2")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(deleteRequest)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("Item deleted"));

        // double check with a get request
        MockHttpServletRequestBuilder getRequest = get("/books/2")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(getRequest)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("This item does not exist"));
    }

}
