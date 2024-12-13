import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  Platform,
  Alert,
} from 'react-native';
import DateTimePicker from '@react-native-community/datetimepicker';
import { globalStyles, datePicker, errorStyles } from './styles';
import { REACT_APP_API_URL } from '@env';
import { loadUserInfo } from '../utils/functions';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import apiClient from '../utils/apiClient';


export default function ProfileEditScreen ({ navigation }) {
  const [firstName, setFirstName] = useState('');
  const [lastName, setFirstLast] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [password, setPassword] = useState('');
  const [oldPassword, setOldPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [birthDate, setBirthDate] = useState(new Date());
  const [show, setShow] = useState(false);
  const [errors, setErrors] = useState({});

  const onChange = (event, selectedDate) => {
    const currentDate = selectedDate || birthDate;
    setShow(Platform.OS === 'ios');
    setBirthDate(currentDate);
  };

  const showDatepicker = () => {
    setShow(true);
  };
  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      const response = await loadUserInfo();
      setFirstName(response.firstName);
      setFirstLast(response.lastName);
      setEmail(response.email);
      setPhoneNumber(response.phoneNumber);
      setBirthDate(new Date(response.dateOfBirth));

    } catch (error) {
      console.error(error);
      Alert.alert("Failed to load user data.");
    }
  };
  const formatDate = (date) => {
    return date.toLocaleDateString('pl-PL', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  };
  
  const validateForm = () => {
    let formErrors = {};
    if (!email) {
      formErrors.email = 'Email jest wymagany';
    } else if (!/\S+@\S+\.\S+/.test(email)) {
      formErrors.email = 'Nieprawidłowy format email';
    }
  
    return formErrors;
  };
  const validatePasswordForm = () => {
    let formErrors = {};
    if (!password) {
      formErrors.password = 'Hasło jest wymagane';
    } else if (password !== confirmPassword) {
      formErrors.confirmPassword = 'Hasła muszą się zgadzać';
    } else if(password === oldPassword) {
      formErrors.password = 'Nowe hasło nie może być takie samo jak stare';
    }
    return formErrors;
  }
  const handleSubmit = async () => {
    const formErrors = validateForm();
    if (Object.keys(formErrors).length === 0) {
      // Submit form
      try {
        const updatedUserDTO = {
          firstName: firstName,
          lastName: lastName,
          email: email,
          phoneNumber: phoneNumber,
          dateOfBirth: birthDate
        };
        const token = await AsyncStorage.getItem('token');
  
        await apiClient.put(`/users/updated`, updatedUserDTO);
        Alert.alert("Pomyślnie zaktualizowano dane osobowe");
        await fetchProfile();
      } catch (error) {
        console.error(error);
        Alert.alert('Błąd', error.response?.data || error.message);
      }
    } else {
      setErrors(formErrors);
    }
  };

  const changePassword = async () => {
    const formErrors = validatePasswordForm();
    if (Object.keys(formErrors).length === 0) {
      // Submit form
      try {
        const changePasswordDTO = {
          newPassword: password,
          oldPassword: oldPassword
        };
        const token = await AsyncStorage.getItem('token');
  
        await axios.put(`${REACT_APP_API_URL}/users/change-password`, changePasswordDTO, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
        });
        Alert.alert("Pomyślnie zmieniono hasło");
        setOldPassword('');
        setPassword('');
        setConfirmPassword('');
        await fetchProfile();
      } catch (error) {
        console.error(error);
        Alert.alert('Błąd', error.response?.data || error.message);
      }
    } else {
      setErrors(formErrors);
    }
  };
  return (
    <View style={globalStyles.container}>
      <Text style={globalStyles.title}>Dane osobowe</Text>
      <TextInput
        style={globalStyles.input}
        placeholder="Imię"
        autoCapitalize="none"
        value={firstName}
        onChangeText={setFirstName}
      />
      <TextInput
        style={globalStyles.input}
        placeholder="Nazwisko"
        value={lastName}
        autoCapitalize="none"
        onChangeText={setFirstLast}
      />
      <TextInput
        style={globalStyles.input}
        placeholder="Email"
        value={email}
        keyboardType="email-address"
        autoCapitalize="none"
        onChangeText={setEmail}
      />
      <TextInput
        style={globalStyles.input}
        placeholder="Telefon"
        value={phoneNumber}
        keyboardType="name-phone-pad"
        autoCapitalize="none"
        onChangeText={setPhoneNumber}
      />
      {errors.email && <Text style={errorStyles.errorText}>{errors.email}</Text>}
      <Text style={datePicker.label}>Data urodzenia</Text>
      <TouchableOpacity onPress={showDatepicker} style={datePicker.datePickerButton}>
        <Text style={datePicker.datePickerButtonText}>{formatDate(birthDate)}</Text>
      </TouchableOpacity>
      {show && (
        <DateTimePicker
          testID="dateTimePicker"
          value={birthDate}
          mode="date"
          display="default"
          onChange={onChange}
        />
      )}
      <TouchableOpacity style={globalStyles.button} onPress={handleSubmit}>
        <Text style={globalStyles.buttonText}>Potwierdź zmiany</Text>
      </TouchableOpacity>
      <TextInput
        style={globalStyles.input}
        placeholder="Stare hasło"
        secureTextEntry
        value={oldPassword}
        onChangeText={setOldPassword}
        autoCapitalize="none"
      />
      <TextInput
        style={globalStyles.input}
        placeholder="Nowe hasło"
        secureTextEntry
        value={password}
        onChangeText={setPassword}
        autoCapitalize="none"
      />
      {errors.password && <Text style={e.errorText}>{errors.password}</Text>}
      <TextInput
        style={globalStyles.input}
        placeholder="Potwierdź hasło"
        secureTextEntry
        value={confirmPassword}
        onChangeText={setConfirmPassword}
        autoCapitalize="none"
      />
      {errors.confirmPassword && <Text style={e.errorText}>{errors.confirmPassword}</Text>}
      <TouchableOpacity style={globalStyles.button} onPress={changePassword}>
        <Text style={globalStyles.buttonText}>Zmień hasło</Text>
      </TouchableOpacity>
    </View>
  );
}


