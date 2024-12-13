import React, { useState, useEffect } from 'react';
import { View, Text, FlatList, ActivityIndicator, StyleSheet } from 'react-native';
import apiClient from '../utils/apiClient';
import LoadingComponent from '../compoments/LoadingComponent';
const PointActionsScreen = () => {

  const [loading, setLoading] = useState(true);
  const [pointActions, setPointActions] = useState([]);

  // Pobierz dane z API
  useEffect(() => {
    const fetchPointActions = async () => {
      try {
        const response = await apiClient.get(`/api/user-point-actions`);
        setPointActions(response.data);
      } catch (error) {
        console.error("Error fetching point actions:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchPointActions();
  }, []);

  const renderItem = ({ item }) => {
    // Sprawdzenie, czy punkty są ujemne i usunięcie minusa
    const pointsStyle = item.points < 0 ? styles.negativePoints : styles.points;
    const points = item.points < 0 ? Math.abs(item.points) : item.points;

    return (
      <View style={styles.card}>
        <Text style={styles.cardTitle}>{item.pointActionName}</Text>
        <Text style={styles.cardDate}>{new Date(item.actionDate).toLocaleString()}</Text>
        <Text style={pointsStyle}>{item.points < 0 ? 'Odjęto:' : "Dodano:"} {points}</Text>
      </View>
    );
  };

  return (
    <View style={styles.container}>
      {loading ? (
        <LoadingComponent />
      ) : (
        <FlatList
          data={pointActions}
          renderItem={renderItem}
          keyExtractor={(item) => item.actionDate.toString()}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#f7f7f7',
  },
  card: {
    backgroundColor: '#fff',
    marginBottom: 16,
    padding: 16,
    borderRadius: 8,
    shadowColor: '#000',
    shadowOpacity: 0.1,
    shadowRadius: 5,
    elevation: 3,
  },
  cardTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 8,
  },
  cardDate: {
    fontSize: 14,
    color: '#888',
    marginBottom: 8,
  },
  points: {
    fontSize: 16,
    fontWeight: '600',
    color: '#4caf50', // Green color for positive points
  },
  negativePoints: {
    fontSize: 16,
    fontWeight: '600',
    color: '#FF0000', // Red color for negative points
  },
});

export default PointActionsScreen;