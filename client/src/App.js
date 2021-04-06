import logo from './logo.svg';
import './App.css';
import BookList from './BookList'

import React, { Component } from 'react'
import BookCreator from './BookCreator';

export class App extends Component
{
  constructor(props)
  {
    super(props)

    this.state = {
      books: [],
    }
  }

  componentDidMount()
  {
    this.fetchAllItems();
  }

  // basic async fetcher fn
  async fetchAllItems()
  {
    try
    {
      const url = "http://localhost:8080/books/";
      const response = await fetch(url);
      const json = await response.json();
      console.log(json);

      this.setState({ books: json })
    }
    catch (error)
    {
      console.error(error);
    }
  }

  render()
  {
    return (
      <div className="App">
        <header className="App-header">
          <h1>My Library</h1> 
          <BookList books={this.state.books} fetchAllItems={this.fetchAllItems.bind(this)}/>
          <BookCreator fetchAllItems={this.fetchAllItems.bind(this)}/>
        </header>
      </div>
    )
  }
}

export default App;
