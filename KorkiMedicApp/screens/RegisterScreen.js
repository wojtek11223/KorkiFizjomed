import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity } from 'react-native';
import { globalStyles } from './styles';
import { Button, Platform } from 'react-native';
import DateTimePicker from '@react-native-community/datetimepicker';

export default function RegisterScreen({ navigation }) {
  const [firstName, setFirstName] = useState('');
  const [lastName, setFirstLast] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [birthDate, setBirthDate] = useState('');
  const [show, setShow] = useState(false);

  const onChange = (event, selectedDate) => {
    const currentDate = selectedDate || date;
   // setShow(Platform.OS === 'ios');
    setBirthDate(currentDate);
  };

  const showDatepicker = () => {
    setShow(true);
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
      <Button onPress={showDatepicker} title="Show date picker!" />
      <Text style={{ marginTop: 20 }}>{date.toDateString()}</Text>
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
        onChangeText={setPassword}
        secureTextEntry
        value={password }
        autoCapitalize="none"
      />
      <TextInput
        style={globalStyles.input}
        placeholder="Potwierdź hasło"
        secureTextEntry
        autoCapitalize="none"
      />
      <TouchableOpacity style={globalStyles.button}>
        <Text style={globalStyles.buttonText}>Zarejestruj się</Text>
      </TouchableOpacity>
    </View>
  );
}
