
import type { Book } from '../types/book.types';

const API_URL = 'https://openlibrary.org/search.json?q=oliver+sacks&limit=5';

export const bookService = {
    async fetchBooks(): Promise<Book[]> {
        const response = await fetch(API_URL);
        const data = await response.json();
        return data.docs || [];
    }
};