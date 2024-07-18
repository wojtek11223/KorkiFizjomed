import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, ScrollView, RefreshControl } from 'react-native';
import { globalStyles } from './styles';

export default function LoginScreen({ navigation }) {
  const [refreshing, setRefreshing] = useState(false);

  const onRefresh = () => {
    setRefreshing(true);
    // Tu można dodać logikę odświeżania, np. pobieranie nowych danych
    setTimeout(() => {
      setRefreshing(false);
    }, 2000); // Symulujemy odświeżanie przez 2 sekundy
  };

  return (
    <ScrollView
      contentContainerStyle={globalStyles.container}
      refreshControl={
        <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
      }
    >
      <Text style={globalStyles.title}>Logowanie</Text>
      <TextInput
        style={globalStyles.input}
        placeholder="Email"
        keyboardType="email-address"
        autoCapitalize="none"
      />
      <TextInput
        style={globalStyles.input}
        placeholder="Hasło"
        secureTextEntry
        autoCapitalize="none"
      />
      <TouchableOpacity style={globalStyles.button}>
        <Text style={globalStyles.buttonText}>Zaloguj się</Text>
      </TouchableOpacity>
      <View style={globalStyles.footer}>
        <TouchableOpacity
          style={globalStyles.footerButton}
          onPress={() => navigation.navigate('ForgotPassword')}
        >
          <Text style={globalStyles.footerButtonText}>Zapomniałem hasła</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={globalStyles.footerButton}
          onPress={() => navigation.navigate('Register')}
        >
          <Text style={globalStyles.footerButtonText}>Zarejestruj się</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );
}
