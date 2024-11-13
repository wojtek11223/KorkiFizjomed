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
        "status": status
      }, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      Alert.alert('Sukces', 'Wizyta zmieniła status na' + status);
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
      {appointment.doctorAppointment ?
        <Text>Pacjent: {appointment.firstName} {appointment.lastName}</Text> :
        <Text>Doktor: {appointment.firstName} {appointment.lastName}</Text>
      }
      
      <Text>Data: {new Date(appointment.appointmentDateTime).toLocaleString()}</Text>
      <Text>Rodzaj usługi: {appointment.serviceName}</Text>
      <Text>Opis: {appointment.appointmentDescription}</Text>
      <Text>Cena: {appointment.price}</Text>
      <Text style={getStatusStyle()}>Status: {appointment.status}</Text>

      <View style={styles.buttonContainer}>
        {(appointment.status === 'Niezatwierdzona' || appointment.status === 'Potwierdzona') &&
          <Button
            title="Anuluj wizytę"
            color="red"
            onPress={() =>{handleStatusAppointment("Anulowana")}}
            disabled={loading}
          />
        }
        {(appointment.status === 'Niezatwierdzona' || appointment.status === 'Niepotwierdzony') && appointment.doctorAppointment &&
          <Button
            title="Potwierdź wizytę"
            onPress={() =>{handleStatusAppointment("Potwierdzona")}}
            disabled={loading}
          />
        }
        {appointment.status === 'Potwierdzona' && appointment.doctorAppointment && today > appointmentDate &&
          <Button
            title="Potwierdź realizacje wizyty"
            onPress={() =>{handleStatusAppointment("Zrealizowana")}}
            disabled={loading}
          />
        }

        {/* Show input for doctors only */}
        {appointment.doctorAppointment &&  appointment.status === "Zrealizowana" &&(
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
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  buttonContainer: {
    marginTop: 20,
  },
  input: {
    borderWidth: 1,
    borderColor: '#ccc',
    padding: 10,
    marginVertical: 10,
    height: 100,
    textAlignVertical: 'top', // ensures text starts from top
  },
  statusConfirmed: {
    color: 'green',
    fontSize: 18,
    fontWeight: 'bold',
  },
  statusCancelled: {
    color: 'red',
    fontSize: 18,
    fontWeight: 'bold',
  },
  statusPending: {
    color: 'gray',
    fontSize: 18,
    fontWeight: 'bold',
  },
});
