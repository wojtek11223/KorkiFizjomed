import React, { useState } from 'react';
import { View, TextInput, Button, Alert } from 'react-native';

const ResetPasswordScreen = () => {
    const [email, setEmail] = useState('');
    const [resetToken, setResetToken] = useState('');
    const [newPassword, setNewPassword] = useState('');

    const requestResetPassword = async () => {
        try {
            const response = await fetch('http://your-api-url/api/password/reset-request', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email }),
            });
            const data = await response.json();
            Alert.alert('Reset Link Sent', data.message);
        } catch (error) {
            Alert.alert('Error', 'Failed to send reset link');
        }
    };

    const resetPassword = async () => {
        try {
            const response = await fetch('http://your-api-url/api/password/reset', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ token: resetToken, newPassword }),
            });
            const data = await response.json();
            Alert.alert('Password Reset', data.message);
        } catch (error) {
            Alert.alert('Error', 'Failed to reset password');
        }
    };

    return (
        <View>
            <TextInput
                placeholder="Enter your email"
                value={email}
                onChangeText={setEmail}
            />
            <Button title="Send Reset Link" onPress={requestResetPassword} />

            <TextInput
                placeholder="Enter reset token"
                value={resetToken}
                onChangeText={setResetToken}
            />
            <TextInput
                placeholder="Enter new password"
                value={newPassword}
                onChangeText={setNewPassword}
            />
            <Button title="Reset Password" onPress={resetPassword} />
        </View>
    );
};

export default ResetPasswordScreen;
