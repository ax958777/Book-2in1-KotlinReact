
// ==========================================
// FILE: src/types/book.types.ts
// ==========================================
export interface Book {
    key: string;
    title: string;
    author_name?: string[];
    cover_i?: number;
    first_publish_year?: number;
}

export interface BooksState {
    books: Book[];
    favorites: string[];
    loading: boolean;
    error: string | null;
}