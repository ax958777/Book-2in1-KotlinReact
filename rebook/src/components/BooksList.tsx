
import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { BookOpen, Heart, Loader2 } from 'lucide-react';
import { addFavorite, removeFavorite } from '../store/actions';
import { loadBooks } from '../store/thunks';
import {
    selectBooks,
    selectFavoriteBooks,
    selectFavorites,
    selectLoading,
    selectError,
} from '../store/selectors';
import { BookCard } from './BookCard';

export function BooksList() {
    const dispatch = useDispatch();
    const [activeTab, setActiveTab] = React.useState<'all' | 'favorites'>('all');

    const books = useSelector(selectBooks);
    const favoriteBooks = useSelector(selectFavoriteBooks);
    const favorites = useSelector(selectFavorites);
    const loading = useSelector(selectLoading);
    const error = useSelector(selectError);

    useEffect(() => {
        dispatch(loadBooks() as any);
    }, [dispatch]);

    const handleToggleFavorite = (bookId: string) => {
        if (favorites.includes(bookId)) {
            dispatch(removeFavorite(bookId));
        } else {
            dispatch(addFavorite(bookId));
        }
    };

    const displayBooks = activeTab === 'all' ? books : favoriteBooks;

    return (
        <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
            <div className="max-w-4xl mx-auto p-6">
                {/* Header */}
                <div className="text-center mb-8">
                    <div className="flex items-center justify-center gap-2 mb-2">
                        <BookOpen className="w-8 h-8 text-indigo-600" />
                        <h1 className="text-3xl font-bold text-gray-900">
                            Oliver Sacks Books
                        </h1>
                    </div>
                    <p className="text-gray-600">
                        Explore and favorite books using Redux
                    </p>
                </div>

                {/* Tabs */}
                <div className="flex gap-2 mb-6 bg-white rounded-lg p-1 shadow-sm">
                    <button
                        onClick={() => setActiveTab('all')}
                        className={`flex-1 py-2 px-4 rounded-md font-medium transition-colors ${activeTab === 'all'
                            ? 'bg-indigo-600 text-white'
                            : 'text-gray-600 hover:bg-gray-100'
                            }`}
                    >
                        All Books ({books.length})
                    </button>
                    <button
                        onClick={() => setActiveTab('favorites')}
                        className={`flex-1 py-2 px-4 rounded-md font-medium transition-colors ${activeTab === 'favorites'
                            ? 'bg-indigo-600 text-white'
                            : 'text-gray-600 hover:bg-gray-100'
                            }`}
                    >
                        <span className="flex items-center justify-center gap-2">
                            <Heart className="w-4 h-4" />
                            Favorites ({favorites.length})
                        </span>
                    </button>
                </div>

                {/* Content */}
                {loading ? (
                    <div className="flex items-center justify-center py-20">
                        <Loader2 className="w-8 h-8 text-indigo-600 animate-spin" />
                        <span className="ml-2 text-gray-600">Loading books...</span>
                    </div>
                ) : error ? (
                    <div className="bg-red-50 border border-red-200 rounded-lg p-4 text-center">
                        <p className="text-red-800">{error}</p>
                    </div>
                ) : displayBooks.length === 0 ? (
                    <div className="bg-white rounded-lg shadow-md p-8 text-center">
                        <Heart className="w-12 h-12 text-gray-300 mx-auto mb-3" />
                        <p className="text-gray-600">
                            {activeTab === 'favorites'
                                ? 'No favorites yet. Add some books to your favorites!'
                                : 'No books found.'}
                        </p>
                    </div>
                ) : (
                    <div className="space-y-4">
                        {displayBooks.map(book => (
                            <BookCard
                                key={book.key}
                                book={book}
                                isFavorite={favorites.includes(book.key)}
                                onToggleFavorite={() => handleToggleFavorite(book.key)}
                            />
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}