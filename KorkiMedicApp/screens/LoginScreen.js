import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  ScrollView,
  RefreshControl
} from 'react-native';
import { globalStyles, errorStyles } from './styles';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { REACT_APP_API_URL } from '@env';
import LoadingComponent from '../compoments/LoadingComponent';
import { registerForPushNotificationsAsync } from '../utils/triggerNotification';
import apiClient from '../utils/apiClient';

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

  const handleLogin = async () => {
    setLoading(true);
    const formErrors = validateForm();
    if (Object.keys(formErrors).length === 0) {
      try {
        console.log(REACT_APP_API_URL);
        const response = await apiClient.post(`/auth/login`, {
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
        Alert.alert('Błąd', error.response.data);
      }finally {
        setLoading(false);
      }
    } else {
      setErrors(formErrors);
      setLoading(false);
      Alert.alert('Błąd', error.response.data || 'Wystąpił błąd podczas logowania');
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
        <TouchableOpacity style={globalStyles.footerButton} onPress={() => navigation.navigate('Register')}>
          <Text style={globalStyles.footerButtonText}>Zarejestruj się</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
}
