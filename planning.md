PICNIC - Problem in Chair; Not in Computer (realpicnic.com)

Problem we are solving

Giving senior developers a place where they can vent about junior developers (and a place where junior developers can rag on each other).


Classes/Schema

1. Data that is accessed together, should be stored together.
2. Upvotes will be stored on Users by HashMap<votable, votableId>. This is so that the data can be kept in a Context and travel with the user, so that the database doesn't have to be queried each time a user goes to a question???????
3. 


Models in Java

Story (What happened)
	int storyId
	String title
	String body
	LocalDate dateTime
	ArrayList<Vote> votes
	AppUser appUser
	boolean visibleToAll

Comment 
	int commentId
	String body
	LocalDate dateTime
	AppUser appUser
	int storyId
	ArrayList<Vote> votes /// or HashMap<String, Integer>  <votable, commentid/storyid>
	int storyId
	
	int totalCountVotes(){
		return votes.size();
		}
		

Vote
	int voteId
	int appUserId
	boolean upVote
	ArrayList<String> votable = new ArrayList<String> (“Comment”, “Story”)
	votableId <This is the story or comment id>

AppUser
	appUserId
	username
	password
	ArrayList<String> position = new ArrayList<String> (“Senior Dev”, “Junior Dev”, “other”)
	ArrayList<String> appUserRole = new ArrayList<String> (“USER”, “MOD”, “ADMIN”)
		User (CRUD own posts, Create account)
		Moderator (CRUD own post, + RUD other people’s posts)
		Admin (CRUD other posts and AppUsers, CRUD moderators)
	HashMap<String, Integer> votes (this is a list of <votable, votableId>) (each vote they posted on a story or comment)

ENUMs

Job/Position
	SENIOR_DEV, JUNIOR_DEV, OTHER

AppRole
	USER, MOD, ADMIN



-----------------------

                                          Schema for mongodb /// NEED TO FINISH

Story (What happened)
	int storyId
	String title
	String body
	LocalDate dateTime
	ArrayList<Comments> comments  ((( This is an array of objects )))
	ArrayList<Vote(UUIDs)> votes (each vote they posted on a story or comment)
	AppUser appUser
	boolean visibleToAll

Comment (subcomments would be a good stretch goal!)
	int commentId
	String body
	LocalDate dateTime
	String username (as a reference id to the rest of the user's information)
	int storyId
	ArrayList<Vote(UUIDs)> votes (each vote they posted on a story or comment)
	subcomments (of type comment that is an array of comments)
	
	int totalCountVotes(){
		return votes.size();
		}

AppUser
	appUserId
	username
	password
	ArrayList<String> position = new ArrayList<String> (“Senior Dev”, “Junior Dev”, “other”)
	ArrayList<String> appUserRole = new ArrayList<String> (“USER”, “MOD”, “ADMIN”)
		User (CRUD own posts, Create account)
		Moderator (CRUD own post, + RUD other people’s posts)
		Admin (CRUD other posts and AppUsers, CRUD moderators)
	ArrayList<Vote(UUIDs)> votes (each vote they posted on a story or comment)
		
Vote
	UUID
	int voteId
	int appUserId
	boolean upVote
	ArrayList<String> votable = new ArrayList<String> (“Comment”, “Story”)
	votableId <This is the story or comment id>

-----------------------

                                                              Data

StoryRepo (Interface)
StoryMongodbRepository

CommentRepo (Interface)
CommentMongodbRepository

AppUserRepo (Interface)
AppUserMongodbRepository

															  Domain Layer

															  Service Layer

															  Controller Layer

@RequestMapping("/api/stories")
@RequestMapping("/api/authenticate")

-----------------------

Requirements
Manage 4-7 database tables (entities) that are independent concepts. A simple bridge table doesn't count.
Relational database for data management
Spring Boot, MVC (@RestController), JdbcTemplate, Testing
An HTML and CSS UI built with React
Sensible layering and pattern choices
At least one UI secured by role
A full test suite that covers the domain and data layers.

Tech Stack
Java + Maven + SpringBoot, SpringSecurity, JWT
Frontend
Website?
Vue.js instead of React
Phone app
React Native
MongoDB (is it like Firestore?)
Incorporate memes




Informal List of User Stories

The idea behind the app is that senior devs (and others) can go in and write about the dumb things that junior devs do.


Context
Action
User not logged in
Can see front page and can see partial posts that senior devs have posted. Best post for the day (with comments) will be on front page
User not logged in
User can create account with username, password, and job/position
Senior Dev logged in
Can post stories and make comments about junior devs
Junior Dev logged in
Can see posts and comments. Cannot see posts marked as “not visible to junior devs”


