import React, { useContext, useEffect, useState } from 'react';
import { createDrawerNavigator } from '@react-navigation/drawer';
import Logout from '../Logout';
import Home from '../Home';
import LoginScreen from '../LoginScreen';
import StoryStack from './StoryStack'
import StoryScreen from '../StoryScreen';
import AsyncStorage from '@react-native-async-storage/async-storage';
import RegisterScreen from '../RegisterScreen';
import UserContext from '../../context/UserContext';

const Drawer = createDrawerNavigator();

const DrawerNavigator = () => {

  const [username, setUsername] = useState(null);

  useEffect(() => {
    (async () => {
      const username = await AsyncStorage.getItem("username");
      if(username != null && username != undefined)
      setUsername(username);
    })

  }, []);

  return (

    <Drawer.Navigator initialRouteName={username ? "Home" : "Login"} >
      <Drawer.Screen name="Home" component={Home} />
      <Drawer.Screen name="Story" component={StoryScreen} options={{ drawerItemStyle: { height: 0 } }} />
      <Drawer.Screen name="List of Stories" component={StoryStack} />
      <Drawer.Screen name="Login" component={LoginScreen} options={{ headerShown: false, drawerItemStyle: { height: 0 } }} />
      <Drawer.Screen name="Register" component={RegisterScreen} options={{ drawerItemStyle: { height: 0 } }} />
      <Drawer.Screen name="Logout" component={Logout} />


    </Drawer.Navigator>

  );
};

export default DrawerNavigator;