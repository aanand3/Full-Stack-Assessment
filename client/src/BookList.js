import React from 'react'
import PropTypes from 'prop-types'
import Book from './Book'

const BookList = props =>
{
    const bookTableContent = props.books.map(book =>
    {
        return <Book key={book.id} book={book} fetchAllItems={props.fetchAllItems} />;
    })

    const tableHeader = [<th key={1}>{"TITLE"}</th>, 
                         <th key={2}>{"AUTHOR"}</th> ];

    return (
        <table>
            <tbody>
                <tr>{tableHeader}</tr>
                {bookTableContent}
            </tbody>
        </table>
    )
}

BookList.propTypes = {
    fetchAllItems: PropTypes.func,
    books: PropTypes.array,
}

export default BookList

