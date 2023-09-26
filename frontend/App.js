import React, { useEffect, useState } from 'react';
import { NavigationContainer } from '@react-navigation/native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { createStackNavigator } from '@react-navigation/stack';
import DrawerNavigator from './components/navigation/DrawerNavigator';
import UserContext from './context/UserContext';

const Stack = createStackNavigator();

function App() {

  useEffect(() => {
    (async () => {

      if (process.env.IP_ADDRESS != undefined || process.env.IP_ADDRESS != null) {
        await AsyncStorage.setItem("ip-address", process.env.IP_ADDRESS);
      }

    })();
  }, []);



  return (

    <NavigationContainer>
      <DrawerNavigator />
    </NavigationContainer>


  );
}

export default App;