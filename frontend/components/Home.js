import React from "react";
import {
  View,
  Text,
  FlatList,
  StyleSheet,
  TouchableOpacity,
  Image,
} from "react-native";
import { useEffect, useState } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";

const Home = ({ navigation }) => {
  const [story, setStory] = useState([]);
  const [comments, setComments] = useState([]);

  const loadTopStory = async () => {
        const ipAddress = await AsyncStorage.getItem("ip-address");

        fetch(`http://192.168.0.142:8080/api/stories`)
            .then((response) => response.json())
            .then((payload) => {
                const topStory = payload[0];
                //TODO logic for top story needs to go here.

                setStory([topStory]);
            })
          .catch(e => {
            console.log(`Some kind of error happened in the home screen: ${e}`)
          });
    };

    useEffect(() => {
        loadTopStory()
    }, []);

  return (
    <View style={styles.container}>
      <View style={styles.center}>
        <Image style={styles.logo} source={require("./images/logo.png")} />
      </View>
      <Text style={styles.heading}>Top Stories</Text>
      <FlatList
        data={story}
        keyExtractor={(item) => item.storyId.toString()}
        renderItem={({ item }) => (
          <TouchableOpacity
            onPress={() => {
              navigation.navigate("Story", { storyId: item.storyId });
            }}
            style={styles.postContainer}
          >
            <View style={styles.postDetails}>
              <Text style={styles.boldTextUserName}>
                {item.appUser.username}
              </Text>
              <Text>{item.title}</Text>
            </View>
            <View style={styles.postDetails}>
              <View style={styles.boxForLogo}>
                <Image
                  style={styles.tinyTinyLogo}
                  source={require("./images/greencheckmark.png")}
                />
                <Text> {item.votes.length}</Text>
              </View>
            </View>
          </TouchableOpacity>
        )}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 10,
    backgroundColor: "#fff",
  },
  center: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
  },
  centerHeading: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 10,
  },
  heading: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 10,
  },
  noContainer: {
    flexDirection: "row",
    marginBottom: 20,
    borderBottomWidth: 1,
    borderBottomColor: "#ccc",
    paddingBottom: 10,
  },
  postDetails: {
    flex: 1,
    flexDirection: "column",
    marginHorizontal: 10,
  },
  boldText: {
    fontWeight: "bold",
  },
  postContainer: {
    borderWidth: 1,
    borderColor: "#ccc",
    padding: 10,
    marginBottom: 10,
  },
  boldTextUserName: {
    fontWeight: "bold",
    fontSize: 8,
  },
  tinyLogo: {
    width: 50,
    height: 50,
  },
  logo: {
    resizeMode: "contain",
    width: 300,
    height: 300,
  },
  boxForLogo: {
    flexDirection: "row",
    alignItems: "center",
  },
  tinyTinyLogo: {
    width: 12.5,
    height: 12.5,
  },
});

export default Home;
