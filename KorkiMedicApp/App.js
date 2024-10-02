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


// Enable screens for better performance
enableScreens();

const Stack = createNativeStackNavigator();
const Tab = createBottomTabNavigator();

const HomeTab = () => (
  <Tab.Navigator
    screenOptions={({ route }) => ({
      tabBarIcon: ({ focused, color, size }) => {
        let iconName;
        if (route.name === 'Główna strona') {
          iconName = focused ? 'home' : 'home-outline';
        } else if (route.name === 'Wizyty') {
          iconName = focused ? 'calendar' : 'calendar-outline';
        } else if (route.name === 'Ustawienia') {
          iconName = focused ? 'settings' : 'settings-outline';
        }
        return <Ionicons name={iconName} size={size} color={color} />;
      },
      tabBarActiveTintColor: 'tomato',
      tabBarInactiveTintColor: 'gray',
    })}
  >
    <Tab.Screen name="Główna strona" component={HomeScreen} />
    <Tab.Screen name="Wizyty" component={AppointmentsScreen} />
    <Tab.Screen name="Ustawienia" component={SettingsScreen} />
  </Tab.Navigator>
);

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator
        initialRouteName="Login"
        screenOptions={{
          headerShown: false,
          animation: 'slide_from_right', // Możesz zmienić na inne style, np. 'fade_from_bottom'
        }}
      >
        <Stack.Screen name="Login" component={LoginScreen} />
        <Stack.Screen name="ForgotPassword" component={ForgotPasswordScreen} />
        <Stack.Screen name="Register" component={RegisterScreen} />
        <Stack.Screen name="HomeTabs" component={HomeTab} />
        <Stack.Screen name="BookAppointment" component={BookAppointmentScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
