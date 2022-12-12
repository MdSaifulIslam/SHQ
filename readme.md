## All APIs are tested using Postman</br>
# Problems or may be solved
> Frontend are not included.</br>
> User role solved manually, can be solved by JWT.</br>
> Whether user is active or not, can be solved by JWT.</br>

# API details:

## For login
> **url** : /api/all/auth</br>
> **method** : post </br>
> **body** : Raw, Json, {
    "username" : "user1",
    "password" : "pass"
}</br>
> **response** : {
    "email" : "",
    "accessToken" : ""
}


## For getting all users
> **url** : /api/admin/users</br>
> **method** : get </br>
> **Header** : Authorization : Bearer Toekn </br>
> **response** : List of all useres, including active and not

## For getting approved or disapproved users list</br>
### admin will call /api/admin/users/true for approved users
### admin will call /api/admin/users/fasle for disapproved users
> **url** : /api/admin/users/{approve}</br>
> **method** : get </br>
**Header** : Authorization : Bearer Toekn </br>
> **response** : List of approved or disapproved users

## For getting approved or disapproved posts list</br>
### admin will call /api/admin/posts/true for approved posts
### admin will call /api/admin/posts/fasle for disapproved posts
> **url** : /api/admin/posts/{approve}</br>
> **method** : get </br>
**Header** : Authorization : Bearer Toekn </br>
> **response** : List of approved or disapproved posts

## For approving post</br>
### admin will call /api/admin/posts/{post_id} and reverse the approve status
> **url** : /api/admin/posts/{post_id}</br>
> **method** : get </br>
**Header** : Authorization : Bearer Toekn </br>
> **response** : 'Post Updated'

## For approving users</br>
### admin will call /api/admin/users/{user_id} and reverse the user status
> **url** : /api/admin/users/{user_id}</br>
> **method** : get </br>
**Header** : Authorization : Bearer Toekn </br>
> **response** : 'User Updated'

## For adding new admin
> **url** : /api/admin/admin</br>
> **method** : post </br>
> **body** : Raw, Json, {
    "username" : "user1",
    "password" : "pass"
}</br>
> **response** : 'Admin added'

## For creating new user account
> **url** : /api/users/users</br>
> **method** : post </br>
> **body** : Raw, Json, {
    "username" : "user1",
    "password" : "pass"
}</br>
> **response** : Your account is created, please wait for admin approval

## For creating new post
> **url** : /api/users/posts</br>
> **method** : post </br>
**Header** : Authorization : Bearer Toekn </br>
> **body** : Raw, Json, {
    "title" : "user1",
    "postData" : "pass"
}</br>
> **response** : Your post is created, please wait for admin approval

## For getting all users posts
> **url** : /api/users/posts</br>
> **method** : get </br>
**Header** : Authorization : Bearer Toekn </br>
> **response** : List of users post

## For getting all users approved or disapproved posts
### user will call /api/admin/posts/true for approved posts
### user will call /api/admin/posts/fasle for disapproved posts
> **url** : /api/users/posts/{approve}</br>
> **method** : get </br>
**Header** : Authorization : Bearer Toekn </br>
> **response** : List of users post

## For deleting users posts
> **url** : /api/users/posts/{post_id}</br>
> **method** : delete </br>
**Header** : Authorization : Bearer Toekn </br>
> **response** : Success : Post Deleted, </br>
> error : "User/ Post not found"

## For creating comments to a approved post
> **url** : /api/users/comments/{post_id}</br>
> **method** : post </br>
**Header** : Authorization : Bearer Toekn </br>
> **body** : Raw, Json, {
    "comments" : "user1",
}</br>
> **response** : Your comments is approved

## For getting all approved posts
> **url** : /api/all/posts/{post_id}</br>
> **method** : get </br>
> **response** : List of all users appproved posts

## For a posts with comments
> **url** : /api/users/posts/{post_id}</br>
> **method** : get </br>
> **response** : Blog with comments 
