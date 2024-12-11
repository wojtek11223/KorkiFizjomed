import React, { useEffect, useState, useCallback } from 'react';
import { View, Text, StyleSheet, FlatList, Image } from 'react-native';
import { loadUserInfo } from '../utils/functions';
import { useFocusEffect } from '@react-navigation/native';
import apiClient from '../utils/apiClient';
import LoadingComponent from '../compoments/LoadingComponent';

const HomeScreen = ({ navigation }) => {
  const [userInfo, setUserInfo] = useState(null);
  const [ads, setAds] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchAds = async () => {
    try {
      setLoading(true);
      const response = await apiClient.get(`/api/ads/top`);
      setAds(response.data);
    } catch (error) {
      console.error('Error fetching ads:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchUserInfo = async () => {
    const user = await loadUserInfo();
    if (user) {
      setUserInfo(user);
    }
  };

  useFocusEffect(
    useCallback(() => {
      fetchUserInfo();
      fetchAds();
    }, [])
  );

  if (loading) {
    return <LoadingComponent />;
  }

  const renderAdItem = ({ item }) => (
    <View style={styles.adCard} onTouchEnd={() => navigateToDetails(item)}>
      <Image source={{ uri: `data:image/jpeg;base64,${item.image}` }} style={styles.adImage} />
      <Text style={styles.adTitle}>{item.title}</Text>
    </View>
  );
  
  const navigateToDetails = (ad) => {
    navigation.navigate('AdDetails', { ad });
  };
  

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.greeting}>
          Witaj, {userInfo?.firstName} {userInfo?.lastName}!
        </Text>
        <Text style={styles.points}>Masz {userInfo?.loyaltyPoints || '0'} punktów</Text>
        <Text style={styles.advert}>
          Zyskaj więcej punktów i skorzystaj z naszych fantastycznych ofert!
        </Text>
      </View>

      <FlatList
        data={ads}
        keyExtractor={(item) => item.id}
        renderItem={renderAdItem}
        contentContainerStyle={styles.adList}
        showsVerticalScrollIndicator={false}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f0f0f0',
  },
  header: {
    padding: 20,
    backgroundColor: '#ffffff',
    borderBottomWidth: 1,
    borderBottomColor: '#ddd',
    marginBottom: 10,
  },
  greeting: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 10,
  },
  points: {
    fontSize: 24,
    color: '#666',
    marginBottom: 10,
  },
  advert: {
    fontSize: 18,
    color: '#777',
    textAlign: 'center',
  },
  adList: {
    paddingHorizontal: 10,
  },
  adCard: {
    backgroundColor: '#fff',
    borderRadius: 8,
    padding: 15,
    marginVertical: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  adImage: {
    width: '100%',
    height: 150,
    borderRadius: 8,
    marginBottom: 10,
  },
  adTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 5,
  },
  adDescription: {
    fontSize: 16,
    color: '#666',
  },
});

export default HomeScreen;
