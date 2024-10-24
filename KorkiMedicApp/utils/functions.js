import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';
import { REACT_APP_API_URL } from '@env';


export const loadUserInfo = async () => {
    try {
      console.log(REACT_APP_API_URL);
      const token = await AsyncStorage.getItem('token');
      const response = await axios.get(`http://192.168.0.101:8005/users/me`, {
        headers: {
        'Content-Type': 'application/json',
        // Jeśli API wymaga autoryzacji, można dodać token w nagłówku:
        'Authorization': `Bearer ${token}`,
      }});
      return response.data; // Deserializacja danych
    } catch (e) {
      console.error('Błąd podczas odczytu danych użytkownika:', e);
    }
  };
  