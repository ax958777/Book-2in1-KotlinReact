import { Provider } from 'react-redux';
import { store } from './store/store';
import { BooksList } from './components/BooksList';

export default function App() {
  return (
    <Provider store={store}>
      <BooksList />
    </Provider>
  );
}

