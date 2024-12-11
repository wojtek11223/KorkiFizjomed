import React from 'react';
import { Text, StyleSheet, Image, ScrollView } from 'react-native';

const AdDetailsScreen = ({ route }) => {
  const { ad } = route.params;

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Image source={{ uri: `data:image/jpeg;base64,${ad.image}` }} style={styles.image} />
      <Text style={styles.title}>{ad.title}</Text>
      <Text style={styles.description}>{ad.description}</Text>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flexGrow: 1,
    backgroundColor: '#fff',
    padding: 20,
    paddingTop: 40
  },
  image: {
    width: '100%',
    height: 250,  // Zwiększenie wysokości obrazka
    borderRadius: 10,
    marginBottom: 20,
  },
  title: {
    fontSize: 32,  // Zwiększenie rozmiaru czcionki tytułu
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 15,
  },
  description: {
    fontSize: 18,  // Zwiększenie rozmiaru czcionki opisu
    color: '#666',
    lineHeight: 22,  // Ulepszenie czytelności przez zwiększenie odstępów między liniami
  },
});

export default AdDetailsScreen;
