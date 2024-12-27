import React, { useEffect, useState, useCallback } from 'react';
import { View, Text, FlatList, StyleSheet, TextInput, TouchableOpacity, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Picker } from '@react-native-picker/picker';
import { useFocusEffect } from '@react-navigation/native';
import LoadingComponent from '../compoments/LoadingComponent';
import apiClient from '../utils/apiClient';

const AppointmentsScreen = ({ navigation }) => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filteredAppointments, setFilteredAppointments] = useState([]);
  const [statusFilter, setStatusFilter] = useState(''); // Stan filtra statusu
  const [sortBy, setSortBy] = useState(''); // Stan sortowania
  const [nameFilter, setNameFilter] = useState(''); // Stan filtra imienia/nazwiska
  const [isFilterPanelVisible, setIsFilterPanelVisible] = useState(false); // Widoczność panelu filtra

  const fetchAppointments = async () => {
    try {
      setLoading(true);
      const token = await AsyncStorage.getItem('token');
      const response = await apiClient.get(`/api/appointments/patient`);
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

  const applyFilters = () => {
    let updatedAppointments = [...appointments];

    // Filtrowanie po statusie
    if (statusFilter !== '') {
      updatedAppointments = updatedAppointments.filter((appt) => appt.status === statusFilter);
    }

    // Filtrowanie po imieniu/nazwisku
    if (nameFilter.trim() !== '') {
      updatedAppointments = updatedAppointments.filter((appt) =>
        `${appt.firstName} ${appt.lastName}`.toLowerCase().includes(nameFilter.toLowerCase())
      );
    }

    // Sortowanie po dacie
    if (sortBy === 'dateAsc') {
      updatedAppointments.sort((a, b) => new Date(a.appointmentDateTime) - new Date(b.appointmentDateTime));
    } else if (sortBy === 'dateDesc') {
      updatedAppointments.sort((a, b) => new Date(b.appointmentDateTime) - new Date(a.appointmentDateTime));
    }

    setFilteredAppointments(updatedAppointments);
  };

  useEffect(() => {
    applyFilters();
  }, [statusFilter, sortBy, nameFilter, appointments]);

  const renderAppointment = ({ item }) => {
    const statusColor = item.status === 'Zrealizowana' ? '#28a745' : item.status === 'Potwierdzona' ? '#ffc107' : '#dc3545';

    return (
      <TouchableOpacity
        style={styles.appointmentCard}
        onPress={() => navigation.navigate('AppointmentDetail', { appointment: item })}
      >
        <View style={styles.cardHeader}>
          <Text style={styles.appointmentTitle}>{item.firstName} {item.lastName}</Text>
          <Text style={[styles.statusBadge, { backgroundColor: statusColor }]}> {item.status} </Text>
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
      <TouchableOpacity
        style={styles.filterButton}
        onPress={() => setIsFilterPanelVisible(!isFilterPanelVisible)}
      >
        <Text style={styles.filterButtonText}>Filtry</Text>
      </TouchableOpacity>

      {isFilterPanelVisible && (
        <View style={styles.filterPanel}>
          <Picker
            selectedValue={statusFilter}
            style={styles.smallPicker}
            onValueChange={(itemValue) => setStatusFilter(itemValue)}
          >
            <Picker.Item label="Wszystkie" value="" />
            <Picker.Item label="Zrealizowana" value="Zrealizowana" />
            <Picker.Item label="Potwierdzona" value="Potwierdzona" />
            <Picker.Item label="Anulowana" value="Anulowana" />
            <Picker.Item label="Niezatwierdzona" value="Niezatwierdzona" />
          </Picker>

          <Picker
            selectedValue={sortBy}
            style={styles.smallPicker}
            onValueChange={(itemValue) => setSortBy(itemValue)}
          >
            <Picker.Item label="Brak sortowania" value="" />
            <Picker.Item label="Data rosnąco" value="dateAsc" />
            <Picker.Item label="Data malejąco" value="dateDesc" />
          </Picker>

          <TextInput
            style={styles.searchInput}
            placeholder="Wyszukaj po imieniu lub nazwisku"
            value={nameFilter}
            onChangeText={(text) => setNameFilter(text)}
          />
        </View>
      )}

      {filteredAppointments.length === 0 ? (
        <Text style={styles.noAppointmentsText}>
          Nie ma żadnych rejestracji aktualnie. Umów się już teraz!
        </Text>
      ) : (
        <FlatList
          data={filteredAppointments}
          renderItem={renderAppointment}
          keyExtractor={(item) => item.id}
          contentContainerStyle={{ paddingBottom: 80 }}
        />
      )}
      <TouchableOpacity
        style={styles.bookButton}
        onPress={() => navigation.navigate('BookAppointment')}
      >
        <Text style={styles.bookButtonText}>Zarejestruj wizytę</Text>
      </TouchableOpacity>
    </View>
  );
};

export default AppointmentsScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
    padding: 16,
  },
  filterButton: {
    backgroundColor: '#007bff',
    padding: 10,
    borderRadius: 20,
    alignItems: 'center',
    marginBottom: 10,
  },
  filterButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
  filterPanel: {
    backgroundColor: '#ffffff',
    padding: 10,
    paddingBottom: 35,
    borderRadius: 10,
    marginBottom: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
  },
  smallPicker: {
    backgroundColor: '#fff',
    borderRadius: 8,
    marginVertical: 5,
    height: 40,
  },
  searchInput: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 8,
    marginVertical: 5,
    fontSize: 14,
    borderColor: '#ccc',
    borderWidth: 1,
    width: '100%',
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
  bookButton: {
    position: 'absolute',
    bottom: 20,
    left: 20,
    right: 20,
    backgroundColor: '#007bff',
    padding: 15,
    borderRadius: 30,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 3 },
    shadowOpacity: 0.2,
    shadowRadius: 4,
    elevation: 3,
  },
  bookButtonText: {
    color: '#fff',
    fontSize: 18,
    fontWeight: 'bold',
  },
});