
import { loadBooksRequest, loadBooksSuccess, loadBooksFailure } from './actions';

export const loadBooks = () => {
    return async (dispatch: any) => {
        dispatch(loadBooksRequest());
        try {
            const response = await fetch(
                'https://openlibrary.org/search.json?q=oliver+sacks&limit=5'
            );
            const data = await response.json();
            dispatch(loadBooksSuccess(data.docs || []));
        } catch (error) {
            dispatch(loadBooksFailure('Failed to load books. Please try again.'));
        }
    };
};