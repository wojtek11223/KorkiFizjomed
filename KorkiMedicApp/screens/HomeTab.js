import React from 'react';
import { View, Text } from 'react-native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Ionicons } from '@expo/vector-icons';

import HomeScreen from '../screens/HomeScreen';
import AppointmentsScreen from '../screens/AppointmentsScreen';
import SettingsScreen from '../screens/SettingsScreen';

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
      // Opcje nagłówka dla każdej zakładki
      headerShown: true,
      headerStyle: { backgroundColor: 'tomato' },
      headerTintColor: '#fff',
      headerTitleStyle: { fontWeight: 'bold' },
      /*headerTitle: () => (
        <Text style={{ color: 'white', fontWeight: 'bold', fontSize: 18 }}>
          {userData.firstName} {userData.lastName}
        </Text>
      ), // Wyświetlanie imienia i nazwiska
      headerRight: () => (
        <View style={{ paddingRight: 10 }}>
          <Text style={{ color: 'white', fontSize: 16 }}>
            Punkty: {userData.points}
          </Text>
        </View>
      ), // Wyświetlanie ilości punktów*/
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
    <Tab.Screen 
      name="Ustawienia" 
      component={SettingsScreen} 
      options={{ title: 'Ustawienia' }}  
    />
  </Tab.Navigator>
);

export default HomeTab;
