import React, { useState } from 'react';
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
import axios from 'axios';


export default function RegisterScreen({ navigation }) {
  const [firstName, setFirstName] = useState('');
  const [lastName, setFirstLast] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [birthDate, setBirthDate] = useState(new Date());
  const [show, setShow] = useState(false);
  const [errors, setErrors] = useState({});

  const onChange = (event, selectedDate) => {
    const currentDate = selectedDate || birthDate;
    setShow(Platform.OS === 'ios');
    setBirthDate(currentDate);
  };

  const showDatepicker = ({ navigation }) => {
    setShow(true);
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
    if (!password) {
      formErrors.password = 'Hasło jest wymagane';
    } else if (password !== confirmPassword) {
      formErrors.confirmPassword = 'Hasła muszą się zgadzać';
    }
    return formErrors;
  };

  const handleSubmit = async () => {
    const formErrors = validateForm();
    if (Object.keys(formErrors).length === 0) {
      // Submit form
      try {
        const response = await axios.post(`${REACT_APP_API_URL}/auth/signup`, {
          email: email,
          password: password,
          firstName: firstName,
          lastName: lastName,
          phoneNumber: phoneNumber,
          dateOfBirth: birthDate
        });
        Alert.alert(response?.data || "ok");
        navigation.navigate('Login');
        } catch (error) {
          Alert.alert('Błąd podczas rejestracji:', error.response?.data || error.message);
        }
    } else {
      setErrors(formErrors);
    }
  };

  return (
    <View style={globalStyles.container}>
      <Text style={globalStyles.title}>Zarejestruj się</Text>
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
      <TextInput
        style={globalStyles.input}
        placeholder="Hasło"
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
      <TouchableOpacity style={globalStyles.button} onPress={handleSubmit}>
        <Text style={globalStyles.buttonText}>Zarejestruj się</Text>
      </TouchableOpacity>
    </View>
  );
}


