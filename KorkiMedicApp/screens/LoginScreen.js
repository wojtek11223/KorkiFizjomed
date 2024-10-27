import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  ScrollView,
  RefreshControl,
  StyleSheet,
  ActivityIndicator
} from 'react-native';
import { globalStyles, errorStyles } from './styles';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { REACT_APP_API_URL } from '@env';
import LoadingComponent from '../compoments/LoadingComponent';
import * as Notifications from 'expo-notifications';
import Constants from 'expo-constants';
import { registerForPushNotificationsAsync } from '../utils/triggerNotification';

export default function LoginScreen({ navigation }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState({});
  const [refreshing, setRefreshing] = useState(false);
  const [loading, setLoading] = useState(false);

  const onRefresh = () => {
    setRefreshing(true);
    setTimeout(() => {
      setRefreshing(false);
    }, 2000);
  };

  const validateForm = () => {
    let formErrors = {};
    if (!email) {
      formErrors.email = 'Email jest wymagany';
    } else if (!/\S+@\S+\.\S+/.test(email)) {
      formErrors.email = 'Nieprawidłowy format email';
    }
    if (!password) {
      formErrors.password = 'Hasło jest wymagane';
    }
    return formErrors;
  };

  async function getAndSendFCMToken(expoPushToken) {
    try {
      const token = await AsyncStorage.getItem('token');
      console.log('FCM Token:', expoPushToken);
  
      // Wyślij token FCM na serwer za pomocą axios
      await axios.post(`${REACT_APP_API_URL}/api/fcm-token`, {
        fcmToken: expoPushToken,
      },{
        headers: {
          'Authorization': `Bearer ${token}`,
        }}
      );
      console.log('Token FCM został pomyślnie wysłany na serwer');
    } catch (error) {
      console.error('Błąd podczas wysyłania tokena FCM:', error);
    }
  }

  const handleLogin = async () => {
    setLoading(true);
    const formErrors = validateForm();
    if (Object.keys(formErrors).length === 0) {
      try {
        console.log(REACT_APP_API_URL);
        const response = await axios.post(`http://192.168.0.101:8005/auth/login`, {
          email: email,
          password: password,
        });
  
        if (response.status === 200) {
          await AsyncStorage.setItem('token', response.data.token);
          //getAndSendFCMToken(expoPushToken);
          await registerForPushNotificationsAsync();
          navigation.navigate('HomeTabs');
          setErrors({});
        } else {
          setErrors({ form: response.data.message || 'Błąd logowania' });
        }
      } catch (error) {
        console.error(error);
        setErrors({ form: error.response.data || 'Wystąpił błąd podczas logowania' });
      }finally {
        setLoading(false);
      }
    } else {
      setErrors(formErrors);
      setLoading(false);
    }
  };
  
  if (loading) {
    return (
      LoadingComponent()
    );
  }

  return (
    <ScrollView
      contentContainerStyle={globalStyles.container}
      refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} />}
    >
      <Text style={globalStyles.title}>Logowanie</Text>
      <TextInput
        style={globalStyles.input}
        placeholder="Email"
        keyboardType="email-address"
        autoCapitalize="none"
        value={email}
        onChangeText={setEmail}
      />
      {errors.email && <Text style={errorStyles.errorText}>{errors.email}</Text>}
      <TextInput
        style={globalStyles.input}
        placeholder="Hasło"
        secureTextEntry
        autoCapitalize="none"
        value={password}
        onChangeText={setPassword}
      />
      {errors.password && <Text style={errorStyles.errorText}>{errors.password}</Text>}
      {errors.form && <Text style={errorStyles.errorText}>{errors.form}</Text>}
      <TouchableOpacity style={globalStyles.button} onPress={handleLogin}>
        <Text style={globalStyles.buttonText}>Zaloguj się</Text>
      </TouchableOpacity>
      <View style={globalStyles.footer}>
        <TouchableOpacity style={globalStyles.footerButton} onPress={() => navigation.navigate('ForgotPassword')}>
          <Text style={globalStyles.footerButtonText}>Zapomniałem hasła</Text>
        </TouchableOpacity>
        <TouchableOpacity style={globalStyles.footerButton} onPress={() => navigation.navigate('Register')}>
          <Text style={globalStyles.footerButtonText}>Zarejestruj się</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
}
