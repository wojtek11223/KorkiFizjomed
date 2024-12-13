import React, { useEffect, useRef, useState } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { enableScreens } from 'react-native-screens';
import { navigationRef } from './utils/NavigationService'; // Import NavigationService
import AsyncStorage from '@react-native-async-storage/async-storage';
import LoginScreen from './screens/LoginScreen';
import ForgotPasswordScreen from './screens/ForgotPasswordScreen';
import RegisterScreen from './screens/RegisterScreen';
import BookAppointmentScreen from './screens/BookAppointmentScreen';
import HomeTab from './screens/HomeTab';
import DoctorAppointmentsScreen from './screens/DoctorAppointmentsScreen';
import AppointmentDetailScreen from './screens/AppointmentDetailScreen';
import * as Notifications from 'expo-notifications';
import ProfileEditScreen from './screens/ProfileEditScreen';
import AppointmentsScreen from './screens/AppointmentsScreen';
import PointActionsScreen from './screens/PointActionsScreen';
import ManageServicesScreen from './screens/ManageServicesScreen';
import AdDetailsScreen from './screens/AdDetailsScreen';
import LoadingComponent from './compoments/LoadingComponent';
// Enable screens for better performance
enableScreens();

const Stack = createNativeStackNavigator();

Notifications.setNotificationHandler({
  handleNotification: async () => ({
    shouldShowAlert: true,
    shouldPlaySound: false,
    shouldSetBadge: false,
  }),
});

export default function App() {
  const notificationListener = useRef();
  const responseListener = useRef();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkToken = async () => {
      try {
        const token = await AsyncStorage.getItem('token');
        if (token) {
          // Możesz tu dodać dodatkową weryfikację tokena, np. wysłanie zapytania do API
          setIsAuthenticated(true);
        }
      } catch (error) {
        console.error('Failed to retrieve token:', error);
      } finally {
        setLoading(false);
      }
    };

    checkToken();

    notificationListener.current = Notifications.addNotificationReceivedListener(notification => {
      console.log('Notification Received!', notification);
    });

    responseListener.current = Notifications.addNotificationResponseReceivedListener(response => {
      console.log('Notification Response!', response);
    });

    return () => {
      Notifications.removeNotificationSubscription(notificationListener.current);
      Notifications.removeNotificationSubscription(responseListener.current);
    };
  }, []);

  if (loading) {
    return LoadingComponent();
  }

  return (
    <NavigationContainer ref={navigationRef}>
      <Stack.Navigator
        initialRouteName={isAuthenticated ? 'HomeTabs' : 'Login'}
        screenOptions={{
          headerShown: false,
          animation: 'slide_from_right',
        }}
      >
        <Stack.Screen name="Login" component={LoginScreen} />
        <Stack.Screen name="ProfileEdit" component={ProfileEditScreen} />
        <Stack.Screen name="ForgotPassword" component={ForgotPasswordScreen} />
        <Stack.Screen name="Register" component={RegisterScreen} />
        <Stack.Screen name="HomeTabs" component={HomeTab} />
        <Stack.Screen name="BookAppointment" component={BookAppointmentScreen} />
        <Stack.Screen name="DoctorBookAppointment" component={DoctorAppointmentsScreen} />
        <Stack.Screen name="Appointments" component={AppointmentsScreen} />
        <Stack.Screen name="AppointmentDetail" component={AppointmentDetailScreen} options={{ title: 'Szczegóły Wizyty' }} />
        <Stack.Screen name="PointActions" component={PointActionsScreen} />
        <Stack.Screen name="ManageServices" component={ManageServicesScreen} />
        <Stack.Screen name="AdDetails" component={AdDetailsScreen} options={{ title: 'Szczegóły Reklamy' }} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
