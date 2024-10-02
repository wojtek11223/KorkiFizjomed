import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Alert } from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function SettingsScreen({ navigation }) {
  const handleLogout = async () => {
    try {
      await AsyncStorage.removeItem('token');
      // Po pomyślnym wylogowaniu
      Alert.alert('Wylogowano pomyślnie');
      navigation.navigate('Login');
      // Tutaj możesz dodać nawigację do ekranu logowania lub inny ekran
    } catch (error) {
      console.error('Błąd podczas wylogowywania:', error);
      Alert.alert('Error creating appointment:', error.response?.data || error.message);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Ustawienia</Text>
      <TouchableOpacity style={styles.logoutButton} onPress={handleLogout}>
        <Text style={styles.logoutButtonText}>Wyloguj się</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 16,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  logoutButton: {
    position: 'absolute',
    bottom: 30,
    width: '90%',
    backgroundColor: '#FF5733',
    padding: 15,
    borderRadius: 10,
    alignItems: 'center',
  },
  logoutButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
});
