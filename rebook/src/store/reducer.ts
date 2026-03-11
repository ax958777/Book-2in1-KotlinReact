
import type { BooksState } from '../types/book.types';
import type { BooksAction } from './actions';
import * as types from './actionTypes';

export const initialState: BooksState = {
    books: [],
    favorites: [],
    loading: false,
    error: null,
};

export function booksReducer(
    state = initialState,
    action: BooksAction
): BooksState {
    switch (action.type) {
        case types.LOAD_BOOKS_REQUEST:
            return {
                ...state,
                loading: true,
                error: null,
            };

        case types.LOAD_BOOKS_SUCCESS:
            return {
                ...state,
                loading: false,
                books: action.payload,
            };

        case types.LOAD_BOOKS_FAILURE:
            return {
                ...state,
                loading: false,
                error: action.payload,
            };

        case types.ADD_FAVORITE:
            return {
                ...state,
                favorites: [...state.favorites, action.payload],
            };

        case types.REMOVE_FAVORITE:
            return {
                ...state,
                favorites: state.favorites.filter(id => id !== action.payload),
            };

        default:
            return state;
    }
}