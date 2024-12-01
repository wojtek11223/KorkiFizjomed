import * as Notifications from 'expo-notifications';
import { Platform } from 'react-native';
import * as Device from 'expo-device';
import Constants from 'expo-constants';
import AsyncStorage from '@react-native-async-storage/async-storage';
import axios from 'axios';
import { REACT_APP_API_URL } from '@env';

export async function registerForPushNotificationsAsync() {
  if (Platform.OS === 'android') {
    await Notifications.setNotificationChannelAsync('default', {
      name: 'default',
      importance: Notifications.AndroidImportance.MAX,
    });
  }

  if (Device.isDevice) {
    const { status: existingStatus } = await Notifications.getPermissionsAsync();
    let finalStatus = existingStatus;
    if (existingStatus !== 'granted') {
      const { status } = await Notifications.requestPermissionsAsync();
      finalStatus = status;
    }
    if (finalStatus !== 'granted') {
      alert('Failed to get push token for push notification!');
      return;
    }
    const projectId = Constants?.expoConfig?.extra?.eas?.projectId ?? Constants?.easConfig?.projectId;
    const expoPushToken = (await Notifications.getExpoPushTokenAsync({ projectId })).data;
    const tokenJWT = await AsyncStorage.getItem('token');
    console.log('FCM Token:', expoPushToken);

    response = await axios.post(`${REACT_APP_API_URL}/users/fcm-token`, {
      fcmToken: expoPushToken,
    },{
      headers: {
        'Authorization': `Bearer ${tokenJWT}`,
      }}
    );
    console.log('Token FCM został pomyślnie wysłany na serwer');
  }
}