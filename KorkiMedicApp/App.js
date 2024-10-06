import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { enableScreens } from 'react-native-screens';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Ionicons } from '@expo/vector-icons';

import LoginScreen from './screens/LoginScreen';
import ForgotPasswordScreen from './screens/ForgotPasswordScreen';
import RegisterScreen from './screens/RegisterScreen';
import HomeScreen from './screens/HomeScreen';
import SettingsScreen from './screens/SettingsScreen';
import AppointmentsScreen from './screens/AppointmentsScreen';
import BookAppointmentScreen from './screens/BookAppointmentScreen';
import HomeTab from './screens/HomeTab';
import DoctorAppointmentsScreen from './screens/DoctorAppointmentsScreen';

// Enable screens for better performance
enableScreens();

const Stack = createNativeStackNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator
        initialRouteName="Login"
        screenOptions={{
          headerShown: false,
          animation: 'slide_from_right',
        }}
      >
        <Stack.Screen name="Login" component={LoginScreen} />
        <Stack.Screen name="ForgotPassword" component={ForgotPasswordScreen} />
        <Stack.Screen name="Register" component={RegisterScreen} />
        <Stack.Screen name="HomeTabs" component={HomeTab} />
        <Stack.Screen name="BookAppointment" component={BookAppointmentScreen} />
        <Stack.Screen name="DoctorBookAppointment" component={DoctorAppointmentsScreen} />
        <Stack.Screen name="AppointmentDetail" component={AppointmentDetailScreen} options={{ title: 'Szczegóły Wizyty' }} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
