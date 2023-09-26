import React from "react";
import { createNativeStackNavigator } from "@react-navigation/native-stack";

import HomeScreen from "../HomeScreen";
import RegisterScreen from "../RegisterScreen";
import LoginScreen from "../LoginScreen";
import StoryScreen from "../StoryScreen";
import DrawerNavigator from "./DrawerNavigator";
import UserContext from "../../context/UserContext";

const Stack = createNativeStackNavigator();

const AuthStack = () => {
  return (
    <>
    
    <UserContext.Provider value={auth}>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        <Stack.Screen name="DrawerNavigator" component={DrawerNavigator} />
      </Stack.Navigator>
    </UserContext.Provider>
    </>
  );
};

export default AuthStack;
