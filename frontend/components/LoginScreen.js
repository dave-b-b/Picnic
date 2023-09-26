import React, { useState } from 'react';
import { SafeAreaView, View, Text, TextInput, TouchableOpacity, StyleSheet, Image } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import jwtDecode from 'jwt-decode';

const LoginScreen = ({ navigation }) => {

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState([]);


  const login = async (token) => {
    const { sub: username, authorities: authoritiesString } = jwtDecode(token.jwt_token);
    const roles = authoritiesString.split(',');
    const user = {
      username,
      roles,
      token: token.jwt_token,
      hasRole(role) {
        return this.roles.includes(role);
      }
    };

    // This stores each of the properties of the user in AsyncStorage
    const userPropertyKeys = ['username', 'roles', 'token'];
    for (const key of userPropertyKeys) {
      const value = user[key];
      try {
        if (key == 'roles') {
          const serializedValue = JSON.stringify(value);
          AsyncStorage.setItem(`${key}`, serializedValue);
        } else {
          AsyncStorage.setItem(`${key}`, value);
        }
      } catch (error) {
        console.error(`Error saving user property ${key}:`, error);
      }

    }
  }

  const handleLogin = async () => {
    try {
      const ipAddress = await AsyncStorage.getItem("ip-address");

      const response = await fetch(`http://192.168.0.142:8080/authenticate`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username,
          password,
        }),
      });

      if (response.status === 200) {
        const jwt_token = await response.json();

        // Change the login token into user information
        login(jwt_token);

        console.log(username)
        navigation.navigate('Home', { username: username });

      } else if (response.status === 403) {
        setErrors(["Login failed. Incorrect username or password."]);
      } else {
        setErrors(["Unknown error."]);
      }
    } catch (error) {
      console.error("An error occurred during login:", error);
      setErrors(["An error occurred during login."]);
    }
  };


  return (
    <SafeAreaView style={styles.container}>
      
      <View style={styles.content}>
      <View style={styles.logoContainer}>
      <Image style={styles.logo} source={require("./images/logo.png")} />
      </View>
        <Text style={styles.title}>Login</Text>
        <View style={styles.inputContainer}>
          <TextInput
            placeholder='Username'
            style={styles.input}
            value={username}
            onChangeText={setUsername}
          />
        </View>
        <View style={styles.inputContainer}>
          <TextInput
            placeholder='Password'
            style={styles.input}
            secureTextEntry={true}
            value={password}
            onChangeText={setPassword}
          />
        </View>
        <TouchableOpacity onPress={handleLogin} style={styles.loginButton}>
          <Text style={styles.buttonText}>Login</Text>
        </TouchableOpacity>

        {errors.length > 0 && (
          <Text style={styles.errorText}>{errors}</Text>
        )}

        <View style={styles.registerContainer}>
          <Text>Not Registered?</Text>
          <TouchableOpacity onPress={() => navigation.navigate('Register')}>
            <Text style={styles.registerLink}> Register Here!</Text>
          </TouchableOpacity>
        </View>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  content: {
    paddingHorizontal: 25,
    alignItems: 'center',
  },
  title: {
    fontSize: 28,
    fontWeight: '500',
    color: '#333',
    marginBottom: 10,
  },
  inputContainer: {
    flexDirection: 'row',
    borderBottomColor: '#ccc',
    borderBottomWidth: 1,
    paddingBottom: 8,
    marginBottom: 2,
    marginTop: 6,
  },
  input: {
    flex: 1,
    paddingVertical: 6,
  },
  loginButton: {
    backgroundColor: '#3944BC',
    paddingVertical: 20,
    paddingHorizontal: 40,
    borderRadius: 10,
    marginBottom: 10,
    marginTop: 20,
  },
  buttonText: {
    textAlign: 'center',
    fontWeight: '700',
    fontSize: 16,
    color: '#fff',
  },
  errorText: {
    color: 'red',
    textAlign: 'center',
    marginBottom: 10,
  },
  registerContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    marginBottom: 30,
  },
  registerLink: {
    color: '#3944BC',
    fontWeight: '700',
  },
  logoContainer: {
    alignItems: 'center', // Center the image horizontally
    marginBottom: -95,     // Add some space between the image and the "Login" title
  },
  logo: {
    maxWidth: '60%',      // Set maximum width to prevent overflow
    maxHeight: '60%',     // Set maximum height to prevent overflow
  },
});


export default LoginScreen;
