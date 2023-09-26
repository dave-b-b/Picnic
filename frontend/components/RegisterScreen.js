import React, { useContext, useState } from 'react';
import { SafeAreaView, View, Text, TextInput, TouchableOpacity } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

const RegisterScreen = ({ navigation }) => {

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState([]);

  const handleSignUp = async (event) => {
    event.preventDefault();

    const ipAddress = await AsyncStorage.getItem("ip-address");

    try{
    const response = await fetch(`http://192.168.0.142:8080/signup`, {
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
      const { jwt_token } = await response.json();
      navigation.navigate('Login');
    } else if (response.status === 403) {
      setErrors(["Signup failed."]);
    } else {
      setErrors(["Password requirement is 8 characters long: containting numbers, characters, and special characters."]);
    }
      
    } catch (e) {
      console.log(e)
    }
  }


  return (
    <SafeAreaView style={{ flex: 1, justifyContent: 'center' }}>
      <View style={{ paddingHorizontal: 25 }}>
        <View style={{ alignItems: 'center' }}>
        </View>
        <Text style={{ fontSize: 28, fontWeight: '500', color: '#333', marginBottom: 10 }}>Create An Account</Text>
        <View style={{ flexDirection: 'row', borderBottomColor: '#ccc', borderBottomWidth: 1, paddingBottom: 8, marginBottom: 2, }}>
          <TextInput
            placeholder='Username'
            style={{ flex: 1, paddingVertical: 6 }}
            value={username}
            onChangeText={setUsername}
          />
        </View>
        <View style={{ flexDirection: 'row', borderBottomColor: '#ccc', borderBottomWidth: 1, paddingBottom: 4, marginBottom: 6, }}>
          <TextInput
            placeholder='Password'
            style={{ flex: 1, paddingVertical: 6 }}
            secureTextEntry={true}
            value={password}
            onChangeText={setPassword}
          />
        </View>
        <TouchableOpacity onPress={handleSignUp} style={{ backgroundColor: '#3944BC', padding: 20, borderRadius: 10, marginBottom: 10, marginTop: 20, }}>
          <Text style={{ textAlign: 'center', fontWeight: '700', fontSize: 16, color: '#fff' }}>Register</Text>
        </TouchableOpacity>

        {errors.length > 0 && (
          <Text style={{ textAlign: 'center', color: 'red', marginBottom: 10, }}>
            {errors}
          </Text>
        )}

        <View style={{ flexDirection: 'row', justifyContent: 'center', marginBottom: 30 }}>
          <Text>Already Registered?</Text>
          <TouchableOpacity onPress={() => navigation.navigate('Login')}>
            <Text style={{ color: '#3944BC', fontWeight: '700' }}> Login Here!</Text>
          </TouchableOpacity>
        </View>

      </View>
    </SafeAreaView>
  );
};

export default RegisterScreen;
