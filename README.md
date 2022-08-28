
<h1 align="center">Grocery Shopping Application</h3>
 
  <p align="center">
   <img width="260" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Home%20Screen.png"> 
</p>

 - This project aims to help with buying and selling of groceries for both customers and shop owners to avoid crowded areas such as malls, supermarkets, and wet markets. 

 - With the resurgence of increasing COVID-19 cases, it is important to have a means in which a customer can find all home necessities in one application for ease of use and for the safety of the family.

 
<details open="open">
  <h2>Table of Contents</h2>
  <ol>
    <li>
      <a href="#intro">Introduction</a>
      <ul>
        <li><a href="#reqs">Requirements and Dependencies</a></li>
      </ul>
    </li>
    <li>
      <a href="#setup">Setup</a>
    </li>
    <li><a href="#revisions">Revisions</a></li>
  </ol>
</details>

<h2 id="intro">Introduction</h2>

This android application is an ecommerce online shopping application that aims to provide a service that allows people to purchase their necessities across various different stores and order their goods to their specified location with the least amount of physical contact . 


**Project Structure**

    ├── .idea
    ├── app
    │   └── src
    │        └── build.gradle.........contains gradle
    │        └── main
    │             └── java............contains the classes of the project 
    │             └── res.............contains the xml files
    ├── assets
    └── gradle/wrapper
  

For the organization of the project structure, the classes are to be stored inside of the java folder and the layouts of the activities, recycler views, and fragments are to be  stored inside of the res folder. 

  
<h2 id="reqs">Requirements and Dependencies</h2>

The software requires at minimum a phone bearing an android OS that is at a minimum SDK of API 14 (Android 4.0) or 4.4(Kitkat). 


 - Firebase
 - Glide
 - Material Ui
 - androidx

`Firebase` is used as the primary database of this application, storing the personal information of the users as well as it is being used for the authentication of the users login. `Glide` is used as the primary image loading library for this project as it can directly load images from firebase and place them into their respective ImageView. `Material Ui` is used not only for the look of the application but also for the components that it brings that are not available in the vanilla android studio. 



<h2 id="setup">Setup</h2>

Download the project above and open android studio. Choose any of the available virtual devices and click run (The application will be an `apk` after the development). 

<p align="center">
   <img width="600" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/step1.png"> 
  </p>
  
Once the application is up and running the first screen that would be shown is the login screen. If the user is new to the application they can press register and sign up to the application; otherwise, if they are a old user then they can simply login to the application

<p align="center">
   <img width="200" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Login%20Screen.png"> 
&nbsp;&nbsp;&nbsp;&nbsp;
 <img width="200" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Register%20Screen.png"> 
  </p>
  
Upon registering to the application the user will be prompted with a profile creation screen and will be asked to input his/her personal information (if the user's application crashed or was closed during the profile creation screen, upon next login the profile creation screen will be displayed instead of directly going to the home page).

<p align="center">
   <img width="200" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Profile%20Creation%20Screen.png"> 
  </p>
  
The user is greeted with the home screen and can choose among the various tabs or directly go to one of the popular stores or buy from the hot items (based on ratings)
  
  <p align="center">
   <img width="200" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Home%20Screen.png"> 
&nbsp;&nbsp;&nbsp;&nbsp;
 <img width="200" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Item%20View.png"> 
  </p>
  <p>
  
Users can checkout items in their cart if there are enough stock from the seller's store
 
  <p align="center">
    <p align="center">
   <img width="200" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Cart.png"> 
&nbsp;&nbsp;&nbsp;&nbsp;
 <img width="200" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Checkout.png"> 

  <p>
  
Lastly, users can check the status of each item, confirm whether they received the item, and review items in the status tab
 
  <p align="center">
    <p align="center">
   <img width="200" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Processing.png"> 
&nbsp;&nbsp;&nbsp;&nbsp;
 <img width="200" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Delivered.png"> 
 &nbsp;&nbsp;&nbsp;&nbsp;
 <img width="200" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Rating.png"> 
  
  <p>
Sellers would instead go to a seller home page where they would see all the items that have recently been ordered and their total sales, purchases, and rating.
  <p align="center">
        <p align="center">
    <p align="center">
   <img width="200" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Seller%20Page.png"> 
  </p>
  <p>
  
Sellers can adjust the status of the item from the Orders Tab and look at the ratings from the ratings tab
 
  <p align="center">
    <p align="center">
   <img width="200" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Orders.png"> 
  <p>
  
Sellers can add categories and add items on each category using the add items tab.
 
  <p align="center">
    <p align="center">
   <img width="200" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Categories.png"> 
&nbsp;&nbsp;&nbsp;&nbsp;
 <img width="200" src="https://raw.githubusercontent.com/tyrone890123/Shopping-App/main/assets/Items%20in%20Category.png"> 


<h2 id="revisions">Revisions</h2>
 - April 5, 2021 Initial prototype(Creation of the layouts for each pages and initial development of the homepage)
 - April 6, 2021 Addition of new scrolling for the homepage
 - April 10, 2021 Firebase integration (Addition of a working login, register and personalized homepage for the user that logged in)
 - April 18, 2021 Initial work on shopping cart (Added new child nodes to the firebase database for items and worked on the retrieval of those items in the application)

