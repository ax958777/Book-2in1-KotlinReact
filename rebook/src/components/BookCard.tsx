import { useState } from 'react';
import { BookOpen, Heart } from 'lucide-react';
import type { Book } from '../types/book.types';

interface BookCardProps {
    book: Book;
    isFavorite: boolean;
    onToggleFavorite: () => void;
}

export function BookCard({ book, isFavorite, onToggleFavorite }: BookCardProps) {
    const [imageError, setImageError] = useState(false);
    const thumbnail = book.cover_i && !imageError ? `https://covers.openlibrary.org/b/id/${book.cover_i}-M.jpg` : '';
    const authors = book.author_name?.join(', ') || 'Unknown Author';

    return (
        <div className="bg-white rounded-lg shadow-md p-4 flex gap-4 hover:shadow-lg transition-shadow">
            <div className="flex-shrink-0">
                {thumbnail ? (
                    <img
                        src={thumbnail}
                        alt={book.title}
                        className="w-24 h-32 object-cover rounded"
                        onError={() => setImageError(true)}
                    />
                ) : (
                    <div className="w-24 h-32 bg-gray-200 rounded flex items-center justify-center">
                        <BookOpen className="w-8 h-8 text-gray-400" />
                    </div>
                )}
            </div>
            <div className="flex-1 min-w-0">
                <div className="flex items-start justify-between gap-2">
                    <h3 className="font-bold text-lg text-gray-900 line-clamp-2">
                        {book.title}
                    </h3>
                    <button
                        onClick={onToggleFavorite}
                        className="flex-shrink-0 p-2 rounded-full hover:bg-gray-100 transition-colors"
                        aria-label={isFavorite ? 'Remove from favorites' : 'Add to favorites'}
                    >
                        <Heart
                            className={`w-5 h-5 ${isFavorite ? 'fill-red-500 text-red-500' : 'text-gray-400'
                                }`}
                        />
                    </button>
                </div>
                <p className="text-sm text-gray-600 mt-1">{authors}</p>
                {book.first_publish_year && (
                    <p className="text-xs text-gray-500 mt-1">
                        First Published: {book.first_publish_year}
                    </p>
                )}
            </div>
        </div>
    );
}