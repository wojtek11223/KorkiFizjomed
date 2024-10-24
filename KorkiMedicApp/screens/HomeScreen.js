import React, { useEffect, useState, useCallback } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { loadUserInfo } from '../utils/functions';
import { useFocusEffect } from '@react-navigation/native'; // Importuj useFocusEffect

const HomeScreen = () => {
  const [userInfo, setUserInfo] = useState(null);

  const fetchUserInfo = async () => {
    const user = await loadUserInfo();
    if (user) {
      setUserInfo(user);
    }
  };

  useFocusEffect(
    useCallback(() => {
      fetchUserInfo();
    }, [])
  );

  return (
    <View style={styles.container}>
      <Text style={styles.greeting}>Witaj, {userInfo?.firstName} {userInfo?.lastName}!</Text>
      <Text style={styles.points}>Masz {userInfo?.loyaltyPoints || "0"} punktów</Text>
      <Text style={styles.advert}>Zyskaj więcej punktów i skorzystaj z naszych fantastycznych ofert!</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f0f0f0',
    padding: 20,
  },
  greeting: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 20,
  },
  points: {
    fontSize: 24,
    color: '#666',
    marginBottom: 40,
  },
  advert: {
    fontSize: 18,
    color: '#777',
    textAlign: 'center',
    paddingHorizontal: 10,
  },
});

export default HomeScreen;
