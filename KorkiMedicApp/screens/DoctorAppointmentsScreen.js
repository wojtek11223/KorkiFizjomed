import React, { useEffect, useState, useCallback } from 'react';
import { View, Text, FlatList, StyleSheet, ActivityIndicator, TouchableOpacity, Alert } from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { REACT_APP_API_URL } from '@env';
import { useFocusEffect } from '@react-navigation/native'; // Importuj useFocusEffect
import LoadingComponent from '../compoments/LoadingComponent';

const DoctorAppointmentsScreen = ({ navigation }) => {

  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchAppointments = async () => {
    try {
      const token = await AsyncStorage.getItem('token');
      const response = await axios.get(`${REACT_APP_API_URL}/api/appointments/doctor`,{
        headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      }});
      setAppointments(response.data);
    } catch (error) {
      console.error('Error fetching appointments:', error);
      Alert.alert('Error', error.response?.data || error.message)
    } finally {
      setLoading(false);
    }
  };

  useFocusEffect(
    useCallback(() => {
      fetchAppointments();
    }, [])
  );

  const renderAppointment = ({ item }) => {
    return (
      <TouchableOpacity
        style={styles.appointmentCard}
        onPress={() => navigation.navigate('AppointmentDetail', { appointment: item })}
      >
        <Text style={styles.appointmentTitle}>{item.firstName} {item.lastName}</Text>
        <Text>Data: {new Date(item.appointmentDateTime).toLocaleString()}</Text>
        <Text>Rodzaj usługi: {item.serviceName}</Text>
        <Text>Opis: {item.serviceDescription}</Text>
        <Text>Cena: {item.price}</Text>
        <Text>Status: {item.status}</Text>
      </TouchableOpacity>
    );
  };

  if (loading) {
    return (
      LoadingComponent()
    );
  }

  return (
    <View style={styles.container}>
      {appointments.length === 0 ? (
        <Text style={styles.noAppointmentsText}>Nie ma żadnych rejestracji aktualnie. Poczekaj na nowych pacjentów</Text>
      ) : (
        <FlatList
          data={appointments}
          renderItem={renderAppointment}
          keyExtractor={(item) => item.id}
        />
      )}

    </View>
  );
};

export default DoctorAppointmentsScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
    padding: 16,
    position: 'relative',  // Relative positioning for absolute button
  },
  appointmentCard: {
    backgroundColor: '#ffffff',
    padding: 20,
    marginVertical: 8,
    borderRadius: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 1,
  },
  appointmentTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  noAppointmentsText: {
    fontSize: 18,
    textAlign: 'center',
    marginTop: 20,
    color: '#888',
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  // Styl dla przycisku
  bookButton: {
    position: 'absolute',
    bottom: 20,  // 20 px od dołu ekranu
    left: 20,
    right: 20,
    backgroundColor: '#007bff',
    padding: 15,
    borderRadius: 30,
    alignItems: 'center',
  },
  bookButtonText: {
    color: '#fff',
    fontSize: 18,
    fontWeight: 'bold',
  },
});
