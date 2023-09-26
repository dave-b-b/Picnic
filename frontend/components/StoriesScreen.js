import React, { useCallback } from "react";
import {
  View,
  Text,
  ScrollView,
  FlatList,
  StyleSheet,
  TouchableOpacity,
  TextInput,
  Image,
} from "react-native";
import { useEffect, useState } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";

const StoriesScreen = ({ navigation }) => {
  
  const wait = (timeout) => {
    return new Promise((resolve) => setTimeout(resolve, timeout));
  };
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [story, setStory] = useState(null);
  const [stories, setStories] = useState([]);
  const [body, setBody] = useState("");
  const [title, setTitle] = useState("");
  const [votes, setVotes] = useState([]);

  const loadStories = async () => {
    const ipAddress = await AsyncStorage.getItem("ip-address");

    fetch(`http://192.168.0.142:8080/api/stories`)
      .then((response) => response.json())
      .then((payload) => setStories([...payload.reverse()]))
      .catch((e) => {
        console.log(e);
      });
  };

  const onRefresh = useCallback( async () => {
    const ipAddress = await AsyncStorage.getItem("ip-address");
    setIsRefreshing(true);
    wait(2000).then(() => setIsRefreshing(false));
    fetch(`http://192.168.0.142:8080/api/stories`)
      .then((response) => response.json())
      .then((payload) => setStories([...payload.reverse()]))
      .catch((e) => {
        console.log(e);
      });
  }, []);


  const refreshStories = () => {
    const ipAddress = AsyncStorage.getItem("ip-address");

    fetch(`http://192.168.0.142:8080/api/stories/`)
      .then((response) => response.json())
      .then((payload) => setStory([payload]))
      .catch((e) => {
        console.log(e);
      });
  };

  const handleSubmit = async (evt) => {
    evt.preventDefault();

    const ipAddress = await AsyncStorage.getItem("ip-address");
    const token = await AsyncStorage.getItem("token");

    const newStory = {
      title: title,
      body: body,
    };

    let url = `http://192.168.0.142:8080/api/stories/addStory`;
    let method = "POST";

    fetch(url, {
      method,
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization: "Bearer " + token,
      },
      body: JSON.stringify(newStory),
    })
      .then((response) => {
        if (response.ok) {
          response.json().then((answer) => {
            setBody("");
            setTitle("");
            //setStories([...stories, newStory]);
            setStory(newStory);
          });
        } else {
          response
            .json()
            .then((errors) => {
              if (Array.isArray(errors)) {
                setErrors(errors);
              } else {
                setErrors([errors]);
              }
            })
            .catch((e) => {
              console.log(e);
            });
        }
      })
      .catch((e) => {
        console.log(e);
      });
  };

  useEffect(() => {
    loadStories();
  }, [story]);

  return (
    <View style={styles.container}>
      <Text style={styles.heading}>Write Your Own Story:</Text>

      <TextInput
        style={styles.titleInput}
        placeholder="Write your title here..."
        value={title}
        onChangeText={setTitle}
        // onSubmitEditing={handleAddComment}
      />

      <TextInput
        style={styles.bodyInput}
        placeholder="Write your post here..."
        value={body}
        onChangeText={setBody}
        // onSubmitEditing={handleAddComment}
      />
      <TouchableOpacity
        onPress={handleSubmit}
        style={{
          backgroundColor: "#fff",
        }}
      >
        <Text
          style={{
            textAlign: "right",
            fontWeight: "700",
            fontSize: 16,
            color: "#3944BC",
          }}
        >
          Post
        </Text>
      </TouchableOpacity>

      <Text style={styles.heading}>Stories</Text>
      <FlatList
        data={stories}
        refreshing={isRefreshing}
        onRefresh={onRefresh}
        keyExtractor={(item) => {
          return item.storyId.toString();
        }}
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
            </View>
            <View style={styles.postDetails}>
              <Text>{item.title}</Text>
              <View style={styles.boxForLogo}>
                <Image
                  style={styles.tinyLogo}
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
  titleInput: {
    borderWidth: 1,
    borderColor: "#ccc",
    padding: 10,
    marginBottom: 10,
  },
  bodyInput: {
    borderWidth: 1,
    borderColor: "#ccc",
    paddingTop: 25,
    paddingBottom: 25,
    paddingLeft: 10,
    paddingRight: 10,
    marginBottom: 10,
  },
  tinyLogo: {
    width: 12.5,
    height: 12.5,
  },
  boxForLogo: {
    flexDirection: "row",
    alignItems: "center",
  },
});

export default StoriesScreen;
