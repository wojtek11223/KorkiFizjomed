import React, { useEffect, useRef } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { enableScreens } from 'react-native-screens';
import LoginScreen from './screens/LoginScreen';
import ForgotPasswordScreen from './screens/ForgotPasswordScreen';
import RegisterScreen from './screens/RegisterScreen';
import BookAppointmentScreen from './screens/BookAppointmentScreen';
import HomeTab from './screens/HomeTab';
import DoctorAppointmentsScreen from './screens/DoctorAppointmentsScreen';
import AppointmentDetailScreen from  './screens/AppointmentDetailScreen';
import * as Notifications from 'expo-notifications';
import ProfileEditScreen from './screens/ProfileEditScreen';
// Enable screens for better performance
enableScreens();

const Stack = createNativeStackNavigator();

// Ustawienie handlera powiadomień, aby wyświetlały się, gdy są otrzymane
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

  useEffect(() => {
    //registerForPushNotificationsAsync();

    // Listener for foreground notifications
    notificationListener.current = Notifications.addNotificationReceivedListener(notification => {
      console.log('Notification Received!', notification);
    });

    // Listener for user's response to notifications
    responseListener.current = Notifications.addNotificationResponseReceivedListener(response => {
      console.log('Notification Response!', response);
    });

    return () => {
      Notifications.removeNotificationSubscription(notificationListener.current);
      Notifications.removeNotificationSubscription(responseListener.current);
    };
  }, []);

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
        <Stack.Screen name="ProfileEdit" component={ProfileEditScreen} />
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
