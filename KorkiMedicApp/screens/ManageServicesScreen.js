import React, { useEffect, useState } from 'react';
import { View, Text, Button, StyleSheet, FlatList, Switch, Alert } from 'react-native';
import apiClient from '../utils/apiClient';

export default function ManageServicesScreen() {
    const [services, setServices] = useState([]);
    const [selectedServices, setSelectedServices] = useState({});
    const [initialSelectedServices, setInitialSelectedServices] = useState([]);


    useEffect(() => {
        fetchServices();
    }, []);

    const fetchServices = async () => {
        try {
            const response = await apiClient.get('/api/services/all');
            setServices(response.data);
            let initialSelectedServices = {};
            response.data.forEach((service) => {
                initialSelectedServices[service.id] = service.assigned;
            });
            setInitialSelectedServices(initialSelectedServices);
            setSelectedServices(initialSelectedServices);
        } catch (error) {
            console.error('Error fetching services:', error);
        }
    };

    const toggleService = (id) => {
        setSelectedServices((prev) => ({
            ...prev,
            [id]: !prev[id],
        }));
    };

    const updateServices = async () => {
        const serviceIds = Object.keys(selectedServices).filter((id) => selectedServices[id]);
        try {
            const response = await apiClient.post('/api/services/update', {
                serviceId: serviceIds, // Przesyłamy obiekt zgodny z ServiceIdsDTO
            });
            Alert.alert(response.data);
        } catch (error) {
            console.error('Error updating services:', error);
        }
    };

    return (
        <View style={styles.container}>
            <FlatList
                data={services}
                keyExtractor={(item) => item.id.toString()}
                renderItem={({ item }) => (
                    <View style={styles.serviceItem}>
                        <Text style={styles.serviceName}>{item.name}</Text>
                        <Switch
                            value={selectedServices[item.id]}
                            onValueChange={() => toggleService(item.id)}
                        />
                    </View>
                )}
            />
            {initialSelectedServices != selectedServices &&
            <Button title="Update Services" onPress={updateServices} />

            }
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 16,
    },
    serviceItem: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginVertical: 8,
    },
    serviceName: {
        fontSize: 16,
    },
});
