
import type { Book } from '../types/book.types';
import * as types from './actionTypes';

export const loadBooksRequest = () => ({
    type: types.LOAD_BOOKS_REQUEST as typeof types.LOAD_BOOKS_REQUEST,
});

export const loadBooksSuccess = (books: Book[]) => ({
    type: types.LOAD_BOOKS_SUCCESS as typeof types.LOAD_BOOKS_SUCCESS,
    payload: books,
});

export const loadBooksFailure = (error: string) => ({
    type: types.LOAD_BOOKS_FAILURE as typeof types.LOAD_BOOKS_FAILURE,
    payload: error,
});

export const addFavorite = (bookId: string) => ({
    type: types.ADD_FAVORITE as typeof types.ADD_FAVORITE,
    payload: bookId,
});

export const removeFavorite = (bookId: string) => ({
    type: types.REMOVE_FAVORITE as typeof types.REMOVE_FAVORITE,
    payload: bookId,
});

export type BooksAction =
    | ReturnType<typeof loadBooksRequest>
    | ReturnType<typeof loadBooksSuccess>
    | ReturnType<typeof loadBooksFailure>
    | ReturnType<typeof addFavorite>
    | ReturnType<typeof removeFavorite>;