import React from 'react'
import PropTypes from 'prop-types'

const Book = props =>
{
    const bookInfo = buildBookInfo(props.book);
    const favoriteButton = buildFavoriteButton(props.book, props.fetchAllItems);
    const deleteButton = buildDeleteButton(props.book, props.fetchAllItems);

    return (
        <tr>
            {bookInfo}
            <td>{favoriteButton}</td>
            <td>{deleteButton}</td>
        </tr>
    )
}

Book.propTypes = {
    fetchAllItems: PropTypes.func,
    book: PropTypes.object,
}

export default Book

function buildBookInfo(book)
{
    if (book.favorite)
    {
        return [<td> <strong>{book.title}</strong> </td>, <td> <strong>{book.author}</strong> </td>]
    }
    else
    {
        return [<td> {book.title} </td>, <td> {book.author} </td>]
    }
}

function buildFavoriteButton(book, fetchAllItems)
{
    return <button onClick={() => handleFavoriteClick(book, fetchAllItems)}>Favorite</button>
}

async function handleFavoriteClick(book, fetchAllItems)
{
    await patchToBecomeFavorite(book);
    await fetchAllItems();
}

async function patchToBecomeFavorite(book)
{
    try
    {
        const newBook = { "favorite": !book.favorite }
        console.log(newBook)
        const requestOptions = {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(newBook)
        };
        const url = "http://localhost:8080/books/" + book.id;
        const response = await fetch(url, requestOptions);
        const json = await response.json();
        console.log(json);
    }
    catch (error)
    {
        console.error(error);
    }
}

function buildDeleteButton(book, fetchAllItems)
{
    return <button onClick={() => handleDeleteClick(book, fetchAllItems)}>Delete</button>
}

async function handleDeleteClick(book, fetchAllItems)
{
    await deleteBook(book);
    await fetchAllItems();
}

async function deleteBook(book)
{
    try
    {
        const requestOptions = {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' },
        };
        const url = "http://localhost:8080/books/" + book.id;
        const response = await fetch(url, requestOptions);
        console.log(response);
    }
    catch (error)
    {
        console.error(error);
    }
}