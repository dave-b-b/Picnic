import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import StoriesScreen from '../StoriesScreen';
import StoryScreen from '../StoryScreen';
import CommentScreen from '../CommentScreen';
import { Button } from 'react-native';

const Stack = createNativeStackNavigator();

const StoryStack = () => {
    return (
        <Stack.Navigator initialRouteName='Stories'>
            <Stack.Screen name="Stories" component={StoriesScreen} options={{headerShown: false}}/>
            <Stack.Screen name='Story' component={StoryScreen} options={{ headerShown: false }} />
            <Stack.Screen name="Comment" component={CommentScreen} />
        </Stack.Navigator>
    )
}

export default StoryStack;

//test