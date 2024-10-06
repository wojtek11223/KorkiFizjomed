import React, { useEffect, useState, useCallback } from 'react';
import { View, Text } from 'react-native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Ionicons } from '@expo/vector-icons';

import HomeScreen from '../screens/HomeScreen';
import AppointmentsScreen from '../screens/AppointmentsScreen';
import SettingsScreen from '../screens/SettingsScreen';
import { loadUserInfo } from '../utils/functions';
import { useFocusEffect } from '@react-navigation/native';
import LoadingComponent from '../compoments/LoadingComponent';

const Tab = createBottomTabNavigator();

export default function HomeTab() {
  const [userInfo, setUserInfo] = useState(null);

  const fetchUserInfo = async () => {
    try {
      const user = await loadUserInfo();  // Pobierz dane użytkownika
      if (user) {
        setUserInfo(user);  // Zaktualizuj stan userInfo, gdy dane zostaną pobrane
      }
    } catch (error) {
      console.error("Error loading user info:", error);  // Obsługa błędów
    }
  };

  useFocusEffect(
    useCallback(() => {
      fetchUserInfo();  // Wywołanie funkcji fetchUserInfo
    }, [])
  );

  if (!userInfo) {
    return (
      LoadingComponent()
    );
  }

  return (
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
          } else if (route.name === 'Pacjenci') {  // Nowa zakładka dla lekarza
            iconName = focused ? 'people' : 'people-outline';
          }
          return <Ionicons name={iconName} size={size} color={color} />;
        },
        tabBarActiveTintColor: 'tomato',
        tabBarInactiveTintColor: 'gray',
        headerShown: true,
        headerStyle: { backgroundColor: 'tomato' },
        headerTintColor: '#fff',
        headerTitleStyle: { fontWeight: 'bold' },
      })}
    >
      <Tab.Screen 
        name="Główna strona" 
        component={HomeScreen} 
        options={{ title: 'Główna Strona' }}  
      />
      <Tab.Screen 
        name="Wizyty" 
        component={AppointmentsScreen} 
        options={{ title: 'Twoje Wizyty' }}  
      />
      {userInfo.doctor && (
        <Tab.Screen 
          name="Pacjenci" 
          component={AppointmentsScreen} 
          options={{ title: 'Twoi Pacjenci' }}  
        />
      )}
      <Tab.Screen 
        name="Ustawienia" 
        component={SettingsScreen} 
        options={{ title: 'Ustawienia' }}  
      />
      
    </Tab.Navigator>
  );
}
