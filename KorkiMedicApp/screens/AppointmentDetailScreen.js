import React, { useState } from 'react';
import { View, Text, Button, Alert, StyleSheet } from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { REACT_APP_API_URL } from '@env';

const AppointmentDetailScreen = ({ route, navigation }) => {
  const { appointment } = route.params; // Odbierz dane przekazane w nawigacji
  const [loading, setLoading] = useState(false);

  const handleCancelAppointment = async () => {
    try {
      setLoading(true);
      const token = await AsyncStorage.getItem('token');
      await axios.post(`${REACT_APP_API_URL}/api/appointments/${appointment.id}/cancel`, {
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

  const handleConfirmAppointment = async () => {
    try {
      setLoading(true);
      const token = await AsyncStorage.getItem('token');
      await axios.post(`${REACT_APP_API_URL}/api/appointments/${appointment.id}/confirm`, {}, {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      });
      Alert.alert('Sukces', 'Wizyta została potwierdzona');
      navigation.goBack();
    } catch (error) {
      Alert.alert('Błąd', error.response?.data || error.message);
    } finally {
      setLoading(false);
    }
  };

  // Funkcja do dynamicznego ustawiania stylu w zależności od statusu wizyty
  const getStatusStyle = () => {
    switch (appointment.status) {
      case 'Zrealizowany':
      case 'Potwierdzony':
        return styles.statusConfirmed;
      case 'Anulowany':
        return styles.statusCancelled;
      case 'Niezatwierdzony':
      default:
        return styles.statusPending;
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Szczegóły wizyty</Text>
      <Text>Doktor: {appointment.doctorFirstName} {appointment.doctorLastName}</Text>
      <Text>Data: {new Date(appointment.appointmentDateTime).toLocaleString()}</Text>
      <Text>Rodzaj usługi: {appointment.serviceName}</Text>
      <Text>Opis: {appointment.serviceDescription}</Text>
      <Text>Cena: {appointment.price}</Text>
      {/* Dynamicznie przypisany kolor dla statusu */}
      <Text style={getStatusStyle()}>Status: {appointment.status}</Text>

      <View style={styles.buttonContainer}>
        <Button
          title="Anuluj wizytę"
          color="red"
          onPress={handleCancelAppointment}
          disabled={loading}
        />
        <Button
          title="Potwierdź wizytę"
          onPress={handleConfirmAppointment}
          disabled={loading}
        />
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
  // Style dla statusów
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
