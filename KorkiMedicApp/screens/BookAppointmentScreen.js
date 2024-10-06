import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, Button, Alert, ScrollView } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import { Calendar } from 'react-native-calendars';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { REACT_APP_API_URL } from '@env';
import LoadingComponent from '../compoments/LoadingComponent';
import { loadUserInfo } from '../utils/functions';


const BookAppointmentScreen = () => {
  const [selectedDoctor, setSelectedDoctor] = useState(null);
  const [selectedDate, setSelectedDate] = useState('');
  const [selectedTime, setSelectedTime] = useState(null);
  const [currentMonth, setCurrentMonth] = useState(new Date().toISOString().split('T')[0].slice(0, 7));
  const [appointments, setAppointments] = useState([]);
  const [doctors, setDoctors] = useState([]); 
  const [selectedService, setSelectedService] = useState(null);
  const [rewards, setRewards] = useState([]);
  const [userInfo, setUserInfo] = useState({});
  const [selectedReward, setSelectedReward] = useState(null);
  const [loading, setLoading] = useState(false);

  const allTimes = [
    '08:00', '08:30', '09:00', '09:30', '10:00', '10:30', '11:00', '11:30',
    '12:00', '12:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30',
    '16:00', '16:30', '17:00', '17:30', '18:00'
  ];

  const today = new Date();
  const formattedToday = today.toISOString().split('T')[0];
  const currentYearMonth = formattedToday.slice(0, 7);

  if (loading) {
    return (
      LoadingComponent()
    );
  }

  useEffect(() => {
    setSelectedTime(null);
    setSelectedDate('');
    setSelectedService(null);

  }, [selectedDoctor]);

  useEffect(() => {
    setLoading(true);
    fetchDoctors();
    fetchUserInfo();
    setLoading(false);
  }, []);


  const fetchUserInfo = async () => {
    const user = await loadUserInfo();
    if (user) {
      setUserInfo(user);
    }
  };

  const onConfirmAppointment = async () => {
    if (selectedDoctor && selectedDate && selectedTime && selectedService) {
      const appointmentData = {
        doctorId: selectedDoctor,
        serviceName: selectedService,
        date: `${selectedDate}T${selectedTime}`,
        description: 'Checkup appointment', 
        rewardId: selectedReward
      }
      try {
        setLoading(true);
        const token = await AsyncStorage.getItem('token');
        // Wysłanie zapytania POST do API
        const response = await axios.post(`${REACT_APP_API_URL}/api/appointments/create`, appointmentData, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
        });
        Alert.alert(
          'Wizyta zarezerwowana',
          `Zarezerwowałeś wizytę u ${doctors.find(doctor => doctor.id === selectedDoctor)?.firstName} ${doctors.find(doctor => doctor.id === selectedDoctor)?.lastName} na dzień ${selectedDate} o godzinie ${selectedTime}.`
        );
        setSelectedDoctor(null);
        setSelectedDate('');
        setSelectedTime(null);
        console.log('Appointment created successfully:', response.data);
        return response.data;
      } catch (error) {
        Alert.alert('Error creating appointment:', error.response?.data || error.message);
      } 
      finally {
        setLoading(false);
      }
    }
    else {
      Alert.alert('Błąd', 'Proszę wybrać lekarza, datę, godzinę oraz typ konsultacji.');
    }
  };

  const markedDates = {
    [selectedDate]: { selected: true, marked: true, selectedColor: 'blue' },
  };

  const handleMonthChange = (month) => {
    const selectedYearMonth = month.dateString.slice(0, 7);
    if (selectedYearMonth < currentYearMonth) {
      setCurrentMonth(currentYearMonth);
    } else {
      setCurrentMonth(selectedYearMonth);
    }
  };

  const getAllReservedTimesForDoctor = (date) => {
    if (!appointments) return [];
    
    return appointments.filter(item => item.date.split('T')[0] === date).map((item) => new Date(item.date)
    .toLocaleTimeString('en-GB', { hour: '2-digit', minute: '2-digit' }));
  };

  const getDisabledDates = () => {
    const disabledDates = {};
    const date = new Date();

    for (let i = 0; i < 365; i++) {
      const currentDate = new Date(date);
      currentDate.setDate(date.getDate() + i);
      const dayOfWeek = currentDate.getDay();
      if (dayOfWeek === 0 || dayOfWeek === 6) {
        const dateString = currentDate.toISOString().split('T')[0];
        disabledDates[dateString] = { disabled: true, disableTouchEvent: true, color: 'gray' };
      }
    }

    return disabledDates;
  };

  const fetchDoctors = async () => {
    try {
      const token = await AsyncStorage.getItem('token');
      const response = await axios.get(`${REACT_APP_API_URL}/api/doctors/info`,{headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      }});
      setDoctors(response.data);
    } catch (error) {
      console.error('Error fetching doctors:', error);
    }
  };

  const fetchAppointments = async (doctorId) => {
    try {
      setLoading(true);
      const token = await AsyncStorage.getItem('token');
      const response = await axios.get(`${REACT_APP_API_URL}/api/doctors/${doctorId}/appointments`,{headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      }});
      setAppointments(response.data.appointments);
    } catch (error) {
      console.error('Error fetching appointments:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDoctorChange = (doctorId) => {
    if (doctorId) {
      fetchAppointments(doctorId);
      setSelectedDoctor(doctorId);
    } else {
      setAppointments([]);
    }
  };
  
  const fetchRewards = async (service) => {
    try {
      const response = await axios.get(`${REACT_APP_API_URL}/api/rewards/${service}`);
      setRewards(response.data);
    } catch (error) {
      console.error('Error fetching rewards:', error);
    }
  };
  
  const handleServiceChange = (service) => {
    if(service) {
      fetchRewards(service);
      setSelectedService(service);
    }
  }

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.title}>Wybierz lekarza:</Text>
      <Picker
        selectedValue={selectedDoctor}
        style={styles.picker}
        onValueChange={(itemValue) => handleDoctorChange(itemValue)}
      >
        <Picker.Item label="Wybierz lekarza..." value={null} />
        {doctors.map((doctor) => (
          <Picker.Item key={doctor.id} label={`${doctor.firstName} ${doctor.lastName} - ${[...doctor.specializations].join(', ')}`} value={doctor.id} />
        ))}
      </Picker>
      {selectedDoctor && doctors.find(doctor => doctor.id === selectedDoctor)?.services.length > 0 && (
        <>
          <Text style={styles.title}>Wybierz rodzaj wizyty:</Text>
          <Picker
            selectedValue={selectedService}
            style={styles.picker}
            onValueChange={(itemValue) => handleServiceChange(itemValue)}
          >
            <Picker.Item label="Wybierz rodzaj wizyty..." value={null} />
            {doctors.find(doctor => doctor.id === selectedDoctor).services.map((service, index) => (
              <Picker.Item key={index} label={service.name} value={service.id} />
            ))}
          </Picker>
        </>
      )}
      {selectedService && rewards.length > 0 && (
        <>
          <Text style={styles.title}>Wykorzystaj punkty:</Text>
          <Picker
            selectedValue={selectedReward}
            style={styles.picker}
            onValueChange={(itemValue) => setSelectedReward(itemValue)} // Ustawia poprawnie "null"
          >
            <Picker.Item label="Brak nagrody.." value={null} />
            {rewards.map((reward) => {
              const isAvailable = userInfo.loyaltyPoints >= reward.pointsRequired;
              return (
                <Picker.Item
                  key={reward.rewardId}
                  label={`${reward.description} - ${reward.pointsRequired} pkt`}
                  value={reward.rewardId}
                  style={{
                    opacity: isAvailable ? 1 : 0.5,
                  }}
                  enabled={isAvailable}
                />
              );
            })}
          </Picker>
        </>
      )}


      {selectedDoctor && (
        <>
          <Text style={styles.title}>Wybierz datę wizyty:</Text>
          <Calendar
            current={currentMonth}
            minDate={formattedToday}
            onDayPress={(day) => {
              setSelectedDate(day.dateString);
              setSelectedTime(null);
            }}
            onMonthChange={handleMonthChange}
            markedDates={{ 
              ...getDisabledDates(),
              ...markedDates,
            }}
            theme={{
              todayTextColor: 'blue',
              arrowColor: 'blue',
              disabledArrowColor: 'gray',
              monthTextColor: 'black',
              textSectionTitleColor: 'black',
              dayTextColor: 'black',
              textDisabledColor: 'gray',
              textMonthFontWeight: 'bold',
              textDayHeaderFontWeight: 'bold',
              textDayFontWeight: 'normal',
              textDayStyle: { fontSize: 16 },
              textMonthFontSize: 18,
              textDayHeaderFontSize: 16,
            }}
            firstDay={1} // Set the first day of the week to Monday
            monthFormat={'yyyy-MM'}
          />

          {selectedDate ? (
            <>
              <Text style={styles.title}>Wybierz godzinę wizyty:</Text>
              <Picker
                selectedValue={selectedTime}
                style={styles.picker}
                onValueChange={(itemValue) => setSelectedTime(itemValue)}
              >
                <Picker.Item label="Wybierz godzinę..." value={null} />
                {allTimes.map((time, index) => {
                  const isReserved = getAllReservedTimesForDoctor(selectedDate).includes(time);
                  return (
                    <Picker.Item
                      key={index}
                      label={time}
                      value={time}
                      color={isReserved ? 'gray' : 'black'}
                      enabled={!isReserved}
                    />
                  );
                })}
              </Picker>
            </>
          ) : null}
        </>
      )}

      <Button title="Zarezerwuj wizytę" onPress={onConfirmAppointment} />
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flexGrow: 1,
    padding: 20,
    backgroundColor: '#fff',
  },
  title: {
    fontSize: 18,
    marginVertical: 10,
    fontWeight: 'bold',
  },
  picker: {
    height: 50,
    marginBottom: 20,
  },
});

export default BookAppointmentScreen;
