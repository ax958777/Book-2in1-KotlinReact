
import type { BooksState } from '../types/book.types';

export const selectBooks = (state: BooksState) => state.books;

export const selectFavorites = (state: BooksState) => state.favorites;

export const selectLoading = (state: BooksState) => state.loading;

export const selectError = (state: BooksState) => state.error;

export const selectFavoriteBooks = (state: BooksState) =>
    state.books.filter(book => state.favorites.includes(book.key));

export const selectIsFavorite = (state: BooksState, bookId: string) =>
    state.favorites.includes(bookId);