# The-Handbook
https://docs.google.com/document/d/1QjKJ4-BosoG0bs7pop4WN30Fkk4bwv2kjsA8HzgGMfI/edit?usp=sharing

My capstone project for Udacity Android Nanodegree 

The Handbook
<b>Description</b> 

They Handbook is a tool used to help organize and document your personal tasks. Create step by step directions using pictures and descriptions to show how you complete Your tasks. Share your Handbooks with others so they can help you accomplish these tasks the way you expect them to be done.

App is written solely in Java programming language.
App keeps all strings in a strings.xml file and enables RTL layout switching on all layouts.
Support for accessibility throughout application with Content description and navigation with D-pad.


<b>Intended User</b>

This app is intended for any user looking to organize and document their life and possibly share with others.

Going out of town and have a dog sitter coming over?
Create a Handbook with step by step instructions. Send the sitter your “Feed Poochie” Handbook so they know exactly how to feed your pup without having to write down directions.

Does the wife want you to do the laundry, but you have no clue how?
No Problem!! Have her share her “Laundry” Handbook so you know how she wants it done. Be the hero husband.

Had a water emergency and need to turn off your water, but don’t know how?
Document how the plumber turns off you water, so the next emergency you are prepared. Share the Handbook with you roommates so they know how to deal with this emergency if it arises.

<b>Features</b>

Saves Handbooks internally and in the cloud 
Takes pictures or select images from gallery
Share Handbooks with users
Widget to show a quick view of a Handbook

<b>Data persistence</b>

Bookshelf names are stored in a room database.
Once a book is created, each page is stored in another room database which stores
(Book title, Number of pages, Page title, Page images, Page description, etc..)
When a user shares a shelf it get pushed to Firebase and is stored in realtime database.
Images are stored in Firebase Storage.

All activities follow a flow. When a new activity (EX: When you go to the next step, it finishes/closes the previous step)

<b>libraries used</b>

Glide - glide:4.8.0
Firebase storage - storage:16.0.5
Firebase messaging  - messaging: 17.3.4
Firebase Auth - auth: 4.3.0
Butterknife - 9.0.0-rc1
Room database - persistence.room 1.1.1

<b>Google Play Services</b>

firebase-ui-auth:4.3.0'
firebase-database:16.0.5'
firebase-auth:16.0.5'
firebase-core:16.0.5'
firebase-storage:16.0.5'

