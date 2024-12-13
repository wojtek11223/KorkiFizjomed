import React, { useEffect, useState, useCallback } from 'react';
import { View, Text, Button, Alert, StyleSheet, TextInput, Linking, TouchableOpacity } from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { REACT_APP_API_URL } from '@env';
import { loadUserInfo } from '../utils/functions';
import { useFocusEffect } from '@react-navigation/native';
import apiClient from '../utils/apiClient';

const AppointmentDetailScreen = ({ route, navigation }) => {
  const { appointment } = route.params;
  const [loading, setLoading] = useState(false);
  const [extraInfo, setExtraInfo] = useState(null);
  const [doctorNotes, setDoctorNotes] = useState(appointment.appointmentDescription); // New state for doctor notes/results
  const defaultNote = appointment.appointmentDescription;
  const today = new Date();
  const appointmentDate = new Date(appointment.appointmentDateTime);
  const fetchUserInfo = async () => {
    try {
      setLoading(true);
      const response = await apiClient.post(`/api/appointments/${appointment.id}/info`, {
        "isDoctor": appointment.doctorAppointment
      });
      setExtraInfo(response.data);
    } catch (error) {
      Alert.alert('Błąd', error.response?.data || error.message);
      navigation.goBack();
    } finally {
      setLoading(false);
    }
};

  useEffect(() => {
    fetchUserInfo();
  }, []);

  const handleStatusAppointment = async (status) => {
    try {
      setLoading(true);
      await await apiClient.post(`/api/appointments/${appointment.id}/setStatus`, {
        "status": status,
        "isDoctor": appointment.doctorAppointment
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
      await apiClient.post(`/api/appointments/${appointment.id}/add-notes`, {
        "notes": doctorNotes,
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
          {extraInfo?.specializations && <Text style={styles.label}>Specjalizacje doktora:</Text>}
          {extraInfo?.specializations?.length > 0 ? (
            extraInfo.specializations.map((specialization, index) => (
              <Text key={index} style={styles.specializationText}>
                {specialization}
              </Text>
            ))
          ) : (
            <></>
          )}
          <Text style={styles.label}>Email:</Text>
          <Text style={styles.value}>{extraInfo?.email}</Text>
            <TouchableOpacity 
              style={styles.button} 
              onPress={() => extraInfo?.phoneNumber && Linking.openURL(`mailto:${extraInfo.email}`)}
            >
              <Text style={styles.buttonText}>Wyślij maila</Text>
            </TouchableOpacity>

          <Text style={styles.label}>Numer telefonu:</Text>
          <Text style={styles.value}>{extraInfo?.phoneNumber}</Text>
            <TouchableOpacity 
              style={styles.button} 
              onPress={() => extraInfo?.phoneNumber && Linking.openURL(`tel:${extraInfo.phoneNumber}`)}
            >
              <Text style={styles.buttonText}>Zadzwoń</Text>
            </TouchableOpacity>

            <TouchableOpacity 
              style={styles.button} 
              onPress={() => extraInfo?.phoneNumber && Linking.openURL(`sms:${extraInfo.phoneNumber}`)}
            >
              <Text style={styles.buttonText}>Napisz SMS</Text>
            </TouchableOpacity>
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
            Opis usługi:{' '}
            <Text style={styles.value}>
              {extraInfo?.serviceDescription}
            </Text>
          </Text>
          {extraInfo?.rewardName && <Text style={styles.label}>
            Wykorzystana nagroda:{' '}
            <Text style={styles.value}>
              {extraInfo?.rewardName}
            </Text>
          </Text>
          }
          <Text style={styles.label}>
            Notatka:{' '}
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
  
          {appointment.doctorAppointment && (appointment.status === 'Zrealizowana' || appointment.status === 'Potwierdzona') && (
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
      marginVertical: 8,
    },
    title: {
      fontSize: 26,
      fontWeight: 'bold',
      color: '#343a40',
      marginBottom: 20,
      textAlign: 'center',
    },
    specializationText: {
      fontSize: 16,
      marginVertical: 4,
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
    button: {
      backgroundColor: '#007AFF',
      padding: 12,
      borderRadius: 8,
      marginVertical: 8,
      alignItems: 'center',
    }
  });
