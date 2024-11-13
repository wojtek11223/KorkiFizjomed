import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Alert } from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function SettingsScreen({ navigation }) {
  const handleLogout = async () => {
    try {
      await AsyncStorage.removeItem('token');
      Alert.alert('Wylogowano pomyślnie');
      navigation.navigate('Login');
    } catch (error) {
      console.error('Błąd podczas wylogowywania:', error);
      Alert.alert('Error creating appointment:', error.response?.data || error.message);
    }
  };

  const handleEditProfile = () => {
    navigation.navigate('ProfileEdit');
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Ustawienia</Text>

      {/* Button to navigate to Edit Profile */}
      <TouchableOpacity style={styles.button} onPress={handleEditProfile}>
        <Text style={styles.buttonText}>Edytuj Profil</Text>
      </TouchableOpacity>

      {/* Logout Button */}
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
  button: {
    width: '90%',
    backgroundColor: '#4CAF50',
    padding: 15,
    borderRadius: 10,
    alignItems: 'center',
    marginBottom: 20,
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
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
