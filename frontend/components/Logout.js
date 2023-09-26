
import React, { useContext, useEffect, useState } from 'react';
import UserContext from "../context/UserContext";
import AsyncStorage from '@react-native-async-storage/async-storage';

const LogoutScreen = ({ navigation }) => {

    const removeUserProperties =  async () => {
        const userPropertyKeys = ['username', 'roles', 'token'];

        for (const key of userPropertyKeys) {
            try {
                 await AsyncStorage.removeItem(`${key}`);
            } catch (error) {
                console.error(`Error removing user property ${key}:`, error);
            }
        }
    };

    useEffect(() => {
        removeUserProperties();
        navigation.navigate("Login")
    }, []);

    return (
        <>
        </>
    );
};

export default LogoutScreen;
