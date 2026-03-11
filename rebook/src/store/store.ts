
import { createStore, applyMiddleware } from 'redux';
import { booksReducer } from './reducer';
import { thunkMiddleware } from './middleware';

export const store = createStore(booksReducer, applyMiddleware(thunkMiddleware));

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;