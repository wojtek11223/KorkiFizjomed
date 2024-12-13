import React, { useEffect, useState, useCallback } from 'react';
import { View, Text, FlatList, StyleSheet, ActivityIndicator, TouchableOpacity, Alert } from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { REACT_APP_API_URL } from '@env';
import { useFocusEffect } from '@react-navigation/native'; // Importuj useFocusEffect
import LoadingComponent from '../compoments/LoadingComponent';
import { Picker } from '@react-native-picker/picker';
import apiClient from '../utils/apiClient';

const DoctorAppointmentsScreen = ({ navigation }) => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filteredAppointments, setFilteredAppointments] = useState([]);
  const [statusFilter, setStatusFilter] = useState(''); // Nowy stan filtra

  const fetchAppointments = async () => {
    try {
      const response = await apiClient.get(`/api/appointments/doctor`);
      setAppointments(response.data);
      setFilteredAppointments(response.data);
    } catch (error) {
      console.error('Error fetching appointments:', error);
      Alert.alert('Error', error.response?.data || error.message);
    } finally {
      setLoading(false);
    }
  };

  useFocusEffect(
    useCallback(() => {
      fetchAppointments();
    }, [])
  );

  const filterAppointmentsByStatus = (status) => {
    setStatusFilter(status);
    if (status === '') {
      setFilteredAppointments(appointments);
    } else {
      setFilteredAppointments(appointments.filter((appt) => appt.status === status));
    }
  };

  const renderAppointment = ({ item }) => {
    const statusColor = item.status === 'Zrealizowana' ? '#28a745' : item.status === 'Potwierdzona' ? '#ffc107' : '#dc3545';

    return (
      <TouchableOpacity
        style={styles.appointmentCard}
        onPress={() => navigation.navigate('AppointmentDetail', { appointment: item })}
      >
        <View style={styles.cardHeader}>
          <Text style={styles.appointmentTitle}>{item.firstName} {item.lastName}</Text>
          <Text style={[styles.statusBadge, { backgroundColor: statusColor }]}>
            {item.status}
          </Text>
        </View>
        <Text style={styles.cardDetail}>📅 Data: {new Date(item.appointmentDateTime).toLocaleString()}</Text>
        <Text style={styles.cardDetail}>💼 Rodzaj usługi: {item.serviceName}</Text>
        <Text style={styles.cardDetail}>📝 Opis: {item.appointmentDescription}</Text>
        <Text style={styles.cardDetail}>💲 Cena: {item.price}</Text>
      </TouchableOpacity>
    );
  };

  if (loading) {
    return <LoadingComponent />;
  }

  return (
    <View style={styles.container}>
      <Picker
        selectedValue={statusFilter}
        style={styles.picker}
        onValueChange={(itemValue) => filterAppointmentsByStatus(itemValue)}
      >
        <Picker.Item label="Wszystkie" value="" />
        <Picker.Item label="Zrealizowana" value="Zrealizowana" />
        <Picker.Item label="Potwierdzona" value="Potwierdzona" />
        <Picker.Item label="Anulowana" value="Anulowana" />
        <Picker.Item label="Niezatwierdzona" value="Niezatwierdzona" />
      </Picker>
      {filteredAppointments.length === 0 ? (
        <Text style={styles.noAppointmentsText}>
          Nie ma żadnych rejestracji aktualnie. Poczekaj na nowych pacjentów.
        </Text>
      ) : (
        <FlatList
          data={filteredAppointments}
          renderItem={renderAppointment}
          keyExtractor={(item) => item.id}
          contentContainerStyle={{ paddingBottom: 20 }}
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
  },
  appointmentCard: {
    backgroundColor: '#ffffff',
    padding: 20,
    marginVertical: 10,
    borderRadius: 12,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 10,
  },
  appointmentTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#343a40',
  },
  statusBadge: {
    color: '#fff',
    fontSize: 14,
    fontWeight: '600',
    paddingVertical: 4,
    paddingHorizontal: 10,
    borderRadius: 20,
    overflow: 'hidden',
    textTransform: 'capitalize',
  },
  cardDetail: {
    fontSize: 16,
    color: '#495057',
    marginTop: 5,
  },
  noAppointmentsText: {
    fontSize: 18,
    textAlign: 'center',
    marginTop: 20,
    color: '#6c757d',
  },
});