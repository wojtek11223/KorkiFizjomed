import React from 'react';
import { View, Text, TextInput, TouchableOpacity, styleSheet } from 'react-native';
import { globalStyles } from './styles';

export default function ForgotPasswordScreen({ navigation }) {
  return (
    <View style={globalStyles.container}>
      <Text style={globalStyles.title}>Zapomniałem hasła</Text>
      <TextInput
        style={globalStyles.input}
        placeholder="Email"
        keyboardType="email-address"
        autoCapitalize="none"
      />
      <TouchableOpacity style={globalStyles.button}>
        <Text style={globalStyles.buttonText}>Wyślij</Text>
      </TouchableOpacity>
    </View>
  );
}