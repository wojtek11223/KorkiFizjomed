import React, { useEffect, useState, useCallback } from 'react';
import { View, Text, Button, Alert, StyleSheet, TextInput } from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { REACT_APP_API_URL } from '@env';
import { loadUserInfo } from '../utils/functions';
import { useFocusEffect } from '@react-navigation/native';

const AppointmentDetailScreen = ({ route, navigation }) => {
  const { appointment } = route.params;
  const [loading, setLoading] = useState(false);
  const [userInfo, setUserInfo] = useState(null);
  const [doctorNotes, setDoctorNotes] = useState(appointment.appointmentDescription); // New state for doctor notes/results
  const defaultNote = appointment.appointmentDescription;
  const today = new Date();
  const appointmentDate = new Date(appointment.appointmentDateTime);
  const fetchUserInfo = async () => {
    const user = await loadUserInfo();
    if (user) {
      setUserInfo(user);
    }
  };

  useFocusEffect(
    useCallback(() => {
      fetchUserInfo();
    }, [])
  );

  const handleCancelAppointment = async () => {
    try {
      setLoading(true);
      const token = await AsyncStorage.getItem('token');
      await axios.post(`${REACT_APP_API_URL}/api/appointments/${appointment.id}/cancel`, {}, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      Alert.alert('Sukces', 'Wizyta została anulowana');
      navigation.goBack();
    } catch (error) {
      Alert.alert('Błąd', error.response?.data || error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleStatusAppointment = async (status) => {
    try {
      setLoading(true);
      const token = await AsyncStorage.getItem('token');
      await axios.post(`${REACT_APP_API_URL}/api/appointments/${appointment.id}/setStatus`, {
        "status": status,
        "isDoctor": appointment.doctorAppointment ? 1 : 0
      }, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      Alert.alert('Sukces', 'Wizyta zmieniła status na ' + status);
      navigation.goBack();
    } catch (error) {
      Alert.alert('Błąd', error.response?.data || error.message);
    } finally {
      setLoading(false);
    }
  };

  // New function to handle saving doctor's notes/results
  const handleSaveDoctorNotes = async () => {
    try {
      setLoading(true);
      const token = await AsyncStorage.getItem('token');
      await axios.post(`${REACT_APP_API_URL}/api/appointments/${appointment.id}/add-notes`, {
        "notes": doctorNotes,
      }, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      Alert.alert('Sukces', 'Notatki zostały zapisane');
      setDoctorNotes('');
      navigation.goBack();
    } catch (error) {
      Alert.alert('Błąd', error.response?.data || error.message);
    } finally {
      setLoading(false);
    }
  };

  const getStatusStyle = () => {
    switch (appointment.status) {
      case 'Zrealizowana':
      case 'Potwierdzona':
        return styles.statusConfirmed;
      case 'Anulowana':
        return styles.statusCancelled;
      case 'Niezatwierdzona' || 'Niepotwierdzona':
      default:
        return styles.statusPending;
    }
  };
  
    return (
      <View style={styles.container}>
        <Text style={styles.title}>Szczegóły wizyty</Text>
        
        <View style={styles.card}>
          <Text style={styles.label}>
            {appointment.doctorAppointment ? 'Pacjent:' : 'Doktor:'}{' '}
            <Text style={styles.value}>
              {appointment.firstName} {appointment.lastName}
            </Text>
          </Text>
          <Text style={styles.label}>
            Data:{' '}
            <Text style={styles.value}>
              {appointmentDate.toLocaleString()}
            </Text>
          </Text>
          <Text style={styles.label}>
            Rodzaj usługi:{' '}
            <Text style={styles.value}>
              {appointment.serviceName}
            </Text>
          </Text>
          <Text style={styles.label}>
            Opis:{' '}
            <Text style={styles.value}>
              {appointment.appointmentDescription}
            </Text>
          </Text>
          <Text style={styles.label}>
            Cena:{' '}
            <Text style={styles.value}>
              {appointment.price} zł
            </Text>
          </Text>
          <Text style={[styles.label, getStatusStyle()]}>
            Status: {appointment.status}
          </Text>
        </View>
  
        <View style={styles.buttonContainer}>
          {(appointment.status === 'Niezatwierdzona' || appointment.status === 'Potwierdzona') && (
            <Button
              title="Anuluj wizytę"
              color="#dc3545"
              onPress={() => handleStatusAppointment('Anulowana')}
              disabled={loading}
            />
          )}
          {(appointment.status === 'Niezatwierdzona' || appointment.status === 'Niepotwierdzony') &&
            appointment.doctorAppointment && (
              <Button
                title="Potwierdź wizytę"
                color="#28a745"
                onPress={() => handleStatusAppointment('Potwierdzona')}
                disabled={loading}
              />
            )}
          {appointment.status === 'Potwierdzona' && appointment.doctorAppointment && today > appointmentDate && (
            <Button
              title="Potwierdź realizację wizyty"
              color="#007bff"
              onPress={() => handleStatusAppointment('Zrealizowana')}
              disabled={loading}
            />
          )}
  
          {appointment.doctorAppointment && appointment.status === 'Zrealizowana' && (
            <>
              <TextInput
                style={styles.input}
                placeholder="Dodaj notatki lub wyniki badań"
                value={doctorNotes}
                onChangeText={setDoctorNotes}
                multiline
              />
              <Button
                title="Zapisz notatki"
                color="#6c757d"
                onPress={handleSaveDoctorNotes}
                disabled={loading || doctorNotes.trim() === '' || defaultNote === doctorNotes}
              />
            </>
          )}
        </View>
      </View>
    );
  };

  export default AppointmentDetailScreen;
  
  const styles = StyleSheet.create({
    container: {
      flex: 1,
      padding: 20,
      backgroundColor: '#f8f9fa',
    },
    title: {
      fontSize: 26,
      fontWeight: 'bold',
      color: '#343a40',
      marginBottom: 20,
      textAlign: 'center',
    },
    card: {
      backgroundColor: '#ffffff',
      borderRadius: 10,
      padding: 15,
      shadowColor: '#000',
      shadowOffset: { width: 0, height: 2 },
      shadowOpacity: 0.1,
      shadowRadius: 4,
      elevation: 3,
      marginBottom: 20,
    },
    label: {
      fontSize: 16,
      fontWeight: '600',
      color: '#495057',
      marginBottom: 5,
    },
    value: {
      fontWeight: '400',
      color: '#6c757d',
    },
    buttonContainer: {
      marginTop: 20,
    },
    input: {
      borderWidth: 1,
      borderColor: '#dee2e6',
      padding: 10,
      borderRadius: 5,
      backgroundColor: '#f1f3f5',
      marginBottom: 10,
      height: 100,
      textAlignVertical: 'top',
    },
    statusConfirmed: {
      color: '#28a745',
      fontWeight: 'bold',
    },
    statusCancelled: {
      color: '#dc3545',
      fontWeight: 'bold',
    },
    statusPending: {
      color: '#6c757d',
      fontWeight: 'bold',
    },
  });
