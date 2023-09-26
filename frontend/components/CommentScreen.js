import React, { useCallback } from "react";
import {
  View,
  Text,
  ScrollView,
  FlatList,
  StyleSheet,
  TextInput,
  TouchableOpacity,
} from "react-native";
import { useEffect, useState } from "react";
import AsyncStorage from "@react-native-async-storage/async-storage";

const CommentScreen = (props) => {
  const route = props.route;
  const navigation = props.navigation;
  const commentId = route.params.commentId;
  const [username, setUsername] = useState("");
  const wait = (timeout) => {
    return new Promise((resolve) => setTimeout(resolve, timeout));
  };
  const [isRefreshing, setIsRefreshing] = useState(false);

  const [errors, setErrors] = useState([]);
  const [originalComment, setOriginalComment] = useState([]);
  const [comments, setComments] = useState([]);
  const [body, setBody] = useState("");
  const [votes, setVotes] = useState(0);

  const loadOriginalComment =  async () => {

    fetch(`http://192.168.0.142:8080/api/comments/${commentId}`)
      .then((response) => response.json())
      .then((payload) => {
        // Set the comments on the comments
        setComments(payload.comments.reverse());
        setVotes(payload.votes);
        setOriginalComment([payload]);
      })
      .catch((e) => {
        console.log(`Some kind of error happened on the comment screen: ${e}`);
      });
  };

  const onRefresh = useCallback( async () => {
    setIsRefreshing(true);
    wait(2000).then(() => setIsRefreshing(false));
    fetch(`http://192.168.0.142:8080/api/stories`)
      .then((response) => response.json())
      .then((payload) => {
        // Set the comments on the comments
        setComments(payload.comments.reverse());
        setVotes(payload.votes);
        setOriginalComment([payload]);
      })
      .catch((e) => {
        console.log(`Some kind of error happened on the comment screen: ${e}`);
      });
  }, []);

  useEffect(() => {
    loadOriginalComment();
  }, [comments, votes]);

  const handleSubmit = async (evt) => {
    evt.preventDefault();

    const token = await AsyncStorage.getItem("token");
    const newComment = {
      body: body,
      parentId: props.route.params.commentId,
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
    })
      .then((response) => {
        if (response.ok) {
          response.json().then((answer) => {
            setBody("");
            setComments([newComment, ...comments]);
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
        console.log(
          `Error occurred while trying to fetch in the comment's screen. : ${e}`
        );
      });
  };

  const handleDelete = async (evt) => {
    evt.preventDefault();

    const ipAddress = await AsyncStorage.getItem("ip-address");
    const token = await AsyncStorage.getItem("token");

    const url = `http://192.168.0.142:8080/api/comments/delete/${commentId}`;
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
        // Go back to previous screen
        navigation.pop();
      }
    });
  };


  return (
    <View style={styles.container}>
      {originalComment.map((item) => (
        <View key={item.commentId} style={styles.postContainer}>
          <View style={styles.username}>
            <Text style={styles.username}>User {item.username} wrote:</Text>
            <Text></Text>
          </View>
          <Text style={styles.body}>Body: {item.body}</Text>
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
            Delete Comment
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
        refreshing={isRefreshing}
        onRefresh={onRefresh}
        keyExtractor={(item) => {
          return item.commentId;
        }}
        renderItem={({ item }) => (
          <TouchableOpacity
            onPress={() => {
              navigation.navigate("Comment", { commentId: item.commentId });
            }}
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
});

export default CommentScreen;
