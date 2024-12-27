import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { REACT_APP_API_URL } from '@env';
import { Alert } from 'react-native';
import { navigationRef } from './NavigationService';

const apiClient = axios.create({
  baseURL: REACT_APP_API_URL,
  timeout: 1000000,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
});

apiClient.interceptors.request.use(
  async (config) => {
    const token = await AsyncStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    // Obsługa błędów żądania
    console.error('Request Error:', error);
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const { response } = error;
    if (response) {
      switch (response.status) {
        case 400:
          console.error('Bad Request:', response.data.message || 'Niepoprawne żądanie.');
          Alert.alert('Bad Request:', response.data.message || 'Niepoprawne żądanie.');

          break;
        case 401:
          console.error('Unauthorized:', response.data.message || 'Nieautoryzowany dostęp.');
          await AsyncStorage.removeItem('token');
          Alert.alert('Session Expired', 'Please log in again.');
          navigationRef.navigate('Login'); // Przeniesienie na ekran logowani
          break;
        case 403:
          console.error('Forbidden:', response.data.message || 'Brak dostępu.');
          Alert.alert('Forbidden:', response.data.message || 'Brak dostępu.');
          navigationRef.goBack();
          break;
        case 404:
          console.error('Not Found:', response.data.message || 'Zasób nie został znaleziony.');
          Alert.alert('Not Found:', response.data.message || 'Zasób nie został znaleziony.');

          break;
        case 500:
          console.error('Internal Server Error:', response.data.message || 'Błąd serwera.');
          Alert.alert('Internal Server Error:', response.data.message || 'Błąd serwera.');

          break;
        default:
          console.error(`Error ${response.status}:`, response.data.message || 'Wystąpił błąd.');
          Alert.alert(`Error ${response.status}:`, response.data.message || 'Wystąpił błąd.');

      }
    } else if (error.request) {
      // Brak odpowiedzi z serwera
      console.error('No Response:', 'Serwer nie odpowiedział.');
    } else {
      // Inne błędy
      console.error('Error:', error.message);
    }
    return Promise.reject(error);
  }
);

export default apiClient;
