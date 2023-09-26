import React from "react";
import {
  View,
  Text,
  ScrollView,
  FlatList,
  StyleSheet,
  TextInput,
  TouchableOpacity,
  Image,
} from "react-native";
import { useEffect, useState } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";
import AuthContext from "../context/UserContext";

const StoryScreen = (props) => {
  const route = props.route;
  const navigation = props.navigation;
  const storyId = route.params.storyId;
  const [username, setUsername] = useState("");

  const [votes, setVotes] = useState(0);
  const [errors, setErrors] = useState([]);
  const [story, setStory] = useState([]);
  const [comments, setComments] = useState([]);
  const [body, setBody] = useState("");

  const refreshStory = () => {
    const ipAddress = AsyncStorage.getItem("ip-address");

    fetch(`http://192.168.0.142:8080/api/stories/${storyId}`)
      .then((response) => response.json())
      .then((payload) => {
        // Reverse the comments in the list
        setComments([...payload.comments.reverse()]);
        setStory([payload]);
      })
      .catch((e) => {
        console.log(e);
      });
  };

  const loadStory = async () => {
    const ipAddress = await AsyncStorage.getItem("ip-address");
    const storedUsername = await AsyncStorage.getItem("username");
    setUsername(storedUsername);

    await fetch(`http://192.168.0.142:8080/api/stories/${storyId}`)
      .then((response) => response.json())
      .then((payload) => {
        setVotes(payload.votes);
        setComments(payload.comments.reverse());
        setStory([payload]);
      })
      .catch((e) => {
        console.log(e);
      });
  };

  useEffect(() => {
    loadStory();
  }, [comments, votes]);

  const handleSubmit = async (evt) => {
    evt.preventDefault();

    const ipAddress = await AsyncStorage.getItem("ip-address");

    const token = await AsyncStorage.getItem("token");
    const newComment = {
      body: body,
      parentId: props.route.params.storyId,
      timeDate: "",
    };

    let url = `http://192.168.0.142:8080/api/comments/addComment`;
    let method = "POST";

    fetch(url, {
      method,
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",

        Authorization: "Bearer " + token,
      },
      body: JSON.stringify(newComment),
    }).then((response) => {
      if (response.ok) {
        response.json().then((answer) => {
          // refreshStory();
          setBody("");
          setComments([newComment, ...comments]);
        });
      } else {
        response.json().then((error) => {
          if (Array.isArray(error)) {
            setErrors(error);
          } else {
            setErrors([error]);
          }
        });
      }
    })
    .catch((e) => {
      console.log(e);
    });
  };

  const handleUpVote = async (evt) => {
    evt.preventDefault();

    const ipAddress = await AsyncStorage.getItem("ip-address");

    const token = await AsyncStorage.getItem("token");
    const newVote = {
      vote: 1,
      parentId: props.route.params.storyId,
      timeDate: "",
    };

    let url = `http://192.168.0.142:8080/api/votes/addVote`;
    let method = "POST";

    fetch(url, {
      method,
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",

        Authorization: "Bearer " + token,
      },
      body: JSON.stringify(newVote),
    })
      .then((response) => {
        if (response.ok) {
          response.json().then((_answer) => {
            setVotes([...votes, newVote]);
          });
        } else {
          response.json().then((errors) => {
            if (Array.isArray(errors)) {
              setErrors(errors);
            } else {
              setErrors([errors]);
            }
          });
        }
      })
      .catch((e) => {
        console.log(e);
      });
  };

  const handleDelete = async (evt) => {
    evt.preventDefault();

    const ipAddress = await AsyncStorage.getItem("ip-address");
    const token = await AsyncStorage.getItem("token");

    const url = `http://192.168.0.142:8080/api/stories/delete/${storyId}`;
    const method = "DELETE";

    fetch(url, {
      method,
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization: "Bearer " + token,
      },
    }).then((response) => {
      if (response.ok) {
        navigation.navigate("Stories");
      }
    })
    .catch((e) => {
      console.log(e);
    });
  };

  return (
    <View style={styles.container}>
      {story.map((item) => (
        <View key={item.storyId} style={styles.postContainer}>
          <View style={styles.username}>
            <Text style={styles.username}>
              User {item.appUser.username} wrote:
            </Text>
            <Text></Text>
          </View>
          <View style={styles.postHeader}>
            <Text style={styles.title}>Title: {item.title}</Text>
          </View>
          <Text style={styles.body}>Body: {item.body}</Text>
          {item.votes.some(vote => vote.username === username) ? (
            <Text>You've already voted</Text>
          ) : (
            <TouchableOpacity onPress={handleUpVote}>
            <View style={styles.boxForLogo}>
              <Image
                style={styles.tinyLogo}
                source={require("./images/greencheckmark.png")}
              />
              <Text style={styles.upVoteText}> {item.votes.length}</Text>
            </View>
          </TouchableOpacity>
          )}
        </View>
      ))}

      <TextInput
        style={styles.commentInput}
        placeholder="Leave a comment..."
        value={body}
        onChangeText={setBody}
      />

      <View style={{ flexDirection: "row", justifyContent: "space-between" }}>
        <TouchableOpacity
          onPress={handleDelete}
          style={{
            backgroundColor: "#fff",
          }}
        >
          <Text
            style={{
              textAlign: "left",
              fontWeight: "700",
              fontSize: 16,
              color: "#FF0000",
            }}
          >
            Delete Story
          </Text>
        </TouchableOpacity>

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
      </View>

      <Text> </Text>
      <Text style={styles.commentHeader}>Comments</Text>
      <FlatList
        data={comments}
        keyExtractor={(item) => {
          return item.commentId;
        }}
        renderItem={({ item }) => (
          <TouchableOpacity
            onPress={() =>
              navigation.navigate("Comment", { commentId: item.commentId })
            }
            style={{ backgroundColor: "#fff" }}
          >
            <View key={item.commentId} style={styles.commentContainer}>
              <Text style={styles.commentUsername}>{item.username}</Text>
              <Text style={styles.commentBody}>{item.body}</Text>
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
  postContainer: {
    marginBottom: 20,
    borderBottomWidth: 1,
    borderBottomColor: "#ccc",
    paddingBottom: 10,
  },
  postHeader: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 10,
  },
  username: {
    marginRight: 0,
    fontSize: 12,
  },
  title: {
    fontWeight: "bold",
  },
  body: {
    marginBottom: 10,
  },
  commentInput: {
    borderWidth: 1,
    borderColor: "#ccc",
    padding: 10,
    marginBottom: 10,
  },
  commentHeader: {
    fontWeight: "bold",
    marginBottom: 5,
  },
  commentContainer: {
    borderWidth: 1,
    borderColor: "#ccc",
    padding: 10,
    marginBottom: 10,
  },
  commentUsername: {
    fontWeight: "bold",
    marginBottom: 5,
  },
  commentBody: {},
  tinyLogo: {
    width: 18,
    height: 18,
  },
  boxForLogo: {
    flexDirection: "row",
    alignItems: "center",
  },
  upVoteText: {
    fontSize: 18,
  },
});

export default StoryScreen;
