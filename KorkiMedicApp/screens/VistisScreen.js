import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, Button, Alert, ScrollView } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import { Calendar } from 'react-native-calendars';
import axios from 'axios';

const AppointmentScreen = () => {
  const [selectedDoctor, setSelectedDoctor] = useState(null);
  const [selectedDate, setSelectedDate] = useState('');
  const [selectedTime, setSelectedTime] = useState(null);
  const [currentMonth, setCurrentMonth] = useState(new Date().toISOString().split('T')[0].slice(0, 7));
  const [appointments, setAppointments] = useState([]);
  const [doctors, setDoctors] = useState([]); // Lista lekarzy

  // Zakres wszystkich możliwych godzin
  const allTimes = [
    '08:00', '08:30', '09:00', '09:30', '10:00', '10:30', '11:00', '11:30',
    '12:00', '12:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30',
    '16:00', '16:30', '17:00', '17:30', '18:00'
  ];


  const today = new Date();
  const formattedToday = today.toISOString().split('T')[0];
  const currentYearMonth = formattedToday.slice(0, 7);

  useEffect(() => {
    setSelectedTime(null); // Resetuj wybraną godzinę po zmianie lekarza
    setSelectedDate(''); // Resetuj datę po zmianie lekarza
  }, [selectedDoctor]);

  const onConfirmAppointment = () => {
    if (selectedDoctor && selectedDate && selectedTime) {
      Alert.alert(
        'Wizyta zarezerwowana',
        `Zarezerwowałeś wizytę u ${selectedDoctor} na dzień ${selectedDate} o godzinie ${selectedTime}.`
      );
      setSelectedDoctor(null);
      setSelectedDate('');
      setSelectedTime(null);
    } else {
      Alert.alert('Błąd', 'Proszę wybrać lekarza, datę oraz godzinę.');
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

  const getReservedTimesForDate = (date) => {
    return doctors.flatMap(doctor => 
      doctor.reservedTimes.filter(item => item.date === date).map(item => item.time)
    );
  };

  const getReservedTimesForDoctorAndDate = (doctor, date) => {
    if (!doctor) return [];
    return doctor.reservedTimes.filter(item => item.date === date).map(item => item.time);
  };

  const getAvailableTimes = (doctor, selectedDate) => {
    if (!doctor) return [];
    const reservedTimesForDate = getReservedTimesForDate(selectedDate);
    const reservedTimesForDoctor = getReservedTimesForDoctorAndDate(doctor, selectedDate);
    return allTimes.filter(time => !reservedTimesForDate.includes(time) && !reservedTimesForDoctor.includes(time));
  };

  const getAllReservedTimesForDoctor = (appointments, date) => {
    if (!appointments) return [];
    return appointments.filter(item => item.date.toISOString().split('T')[0] === date).map(item => item.date.toISOString().split('T')[1]);
  };

  // Funkcja do oznaczania dni tygodnia jako niedostępnych (soboty i niedziele)
  const getDisabledDates = () => {
    let disabledDates = {};
    for (let i = 0; i < 7; i++) {
      const day = new Date();
      day.setDate(day.getDate() + i);
      const dateString = day.toISOString().split('T')[0];
      const dayOfWeek = day.getDay(); // 0 = niedziela, 6 = sobota
      if (dayOfWeek === 0 || dayOfWeek === 6) {
        disabledDates[dateString] = { disabled: true, color: 'gray' };
      }
    }
    return disabledDates;
  };
  useEffect(() => {
    fetchDoctors();
  }, []);

  // Fetch doctors from API
  const fetchDoctors = async () => {
    try {
      const response = await axios.get('http://192.168.0.101:8005/api/doctors/info');
      setDoctors(response.data);
    } catch (error) {
      console.error('Error fetching doctors:', error);
    }
  };

  const fetchAppointments = async (doctorId) => {
    try {
      const response = await axios.get(`http://192.168.0.101:8005/api/doctors/${doctorId}/appointments`);
      setAppointments(response.data.appointments);
    } catch (error) {
      console.error('Error fetching appointments:', error);
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

      {selectedDoctor && (
        <>
          <Text style={styles.title}>Wybierz datę wizyty:</Text>
          <Calendar
            current={currentMonth}
            minDate={formattedToday}
            onDayPress={(day) => {
              setSelectedDate(day.dateString);
              setSelectedTime(null); // Resetuj godzinę przy wyborze nowej daty
            }}
            onMonthChange={handleMonthChange}
            markedDates={{ 
              ...markedDates,
              ...getDisabledDates(), // Dodaj niedostępne dni do zaznaczenia
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
            firstDay={1} // Ustaw pierwszy dzień tygodnia na poniedziałek
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
                  const isReserved = getAllReservedTimesForDoctor(appointments, selectedDate).includes(time);
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

export default AppointmentScreen;
