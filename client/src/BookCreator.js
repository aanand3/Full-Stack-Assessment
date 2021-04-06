import React, { Component } from 'react'

export class BookCreator extends Component
{
    constructor(props)
    {
        super(props)

        this.state = {
            author: "",
            title: "",
        }

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event)
    {
        this.setState({ [event.target.name] : event.target.value })
    }

    // will call the post and fetch too 
    async handleSubmit(event)
    {
        event.preventDefault();
        console.log('new item submitted')

        await this.postItem(); 
        await this.props.fetchAllItems();
    }

    async postItem()
    {
        try
        {
            const newBook = {"title" : this.state.title, "author" : this.state.author}

            const requestOptions = {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newBook)
            };
            const url = "http://localhost:8080/books";
            const response = await fetch(url, requestOptions);
            const json = await response.json();
            console.log(json);
        }
        catch(error)
        {
            console.error(error);
        }
    }

    render()
    {
        return (
            <form onSubmit={this.handleSubmit}>
                <input type="text"
                    name="title"
                    value={this.state.userInput}
                    onChange={this.handleChange}
                    placeholder = "title"
                    autoFocus
                />
                <input type="text"
                    name="author"
                    value={this.state.userInput}
                    onChange={this.handleChange}
                    placeholder = "author"
                    autoFocus
                />
                <button type="submit">Add Book</button>
            </form>
        )
    }
}

export default BookCreator
