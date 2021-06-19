<h1 align="center">Grocery Shopping Application</h3>
 
  <p align="center">
   <img width="260" src="https://raw.githubusercontent.com/dmarkdenzel/LBYCPD2/master/assets/Home%20Screen.png"> 
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
    <li><a href="#contributors">Contributors</a></li>
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
   <img width="600" src="https://raw.githubusercontent.com/dmarkdenzel/LBYCPD2/master/assets/step1.png"> 
  </p>
  
Once the application is up and running the first screen that would be shown is the login screen. If the user is new to the application they can press register and sign up to the application; otherwise, if they are a old user then they can simply login to the application

<p align="center">
   <img width="200" src="https://raw.githubusercontent.com/dmarkdenzel/LBYCPD2/master/assets/Login%20Screen.png"> 
&nbsp;&nbsp;&nbsp;&nbsp;
 <img width="200" src="https://raw.githubusercontent.com/dmarkdenzel/LBYCPD2/master/assets/Register%20Screen.png"> 
  </p>
  
Upon registering to the application the user will be prompted with a profile creation screen and will be asked to input his/her personal information (if the user's application crashed or was closed during the profile creation screen, upon next login the profile creation screen will be displayed instead of directly going to the home page).

<p align="center">
   <img width="200" src="https://raw.githubusercontent.com/dmarkdenzel/LBYCPD2/master/assets/Profile%20Creation%20Screen.png"> 
  </p>
  
  Finally, the user is able to access the home page where they can explore the various shops and place items into their cart for purchase.
  
  <p align="center">
   <img width="200" src="https://raw.githubusercontent.com/dmarkdenzel/LBYCPD2/master/assets/Home%20Screen.png"> 
&nbsp;&nbsp;&nbsp;&nbsp;
 <img width="200" src="https://raw.githubusercontent.com/dmarkdenzel/LBYCPD2/master/assets/Shopping%20Cart%20Screen.png"> 
  </p>

<h2 id="revisions">Revisions</h2>

 - April 5, 2021 Initial prototype(Creation of the layouts for each pages and initial development of the homepage)
 - April 6, 2021 Addition of new scrolling for the homepage
 - April 10, 2021 Firebase integration (Addition of a working login, register and personalized homepage for the user that logged in)
 - April 18, 2021 Initial work on shopping cart (Added new child nodes to the firebase database for items and worked on the retrieval of those items in the application)

<h2 id="contributors">Contributors</h2>
<table>
  <tr>
    <th>
     <img width="100" src="https://scontent.fmnl17-3.fna.fbcdn.net/v/t1.6435-9/33579872_10209565952465269_236511908981637120_n.jpg?_nc_cat=110&ccb=1-3&_nc_sid=09cbfe&_nc_eui2=AeE9mQBM54JKCL6TveHVNjbke9j5FsOyudt72PkWw7K52_roZLJdZYYROiVruxHc2lGb1hRg16bkRVi9CR11sq-V&_nc_ohc=h6GNKY9y3BoAX-Z47FS&_nc_ht=scontent.fmnl17-3.fna&oh=cbb986cf5e49f93b6c0d9c75b2cc171b&oe=60A417EF">
     <p align="center">
     Jose Antonio Cadavillo
     </p>
          <p align="center">
                 <a href="https://github.com/headphoneJac">     jose_antonio_cadavillo@dlsu.edu.ph</a>
     </p>
	</th>
    <th>
    <img width="100" src="https://scontent.fcrk1-1.fna.fbcdn.net/v/t1.6435-9/135517362_10217887485649279_8394049210403086330_n.jpg?_nc_cat=103&ccb=1-3&_nc_sid=09cbfe&_nc_eui2=AeEa4TVvi6P99i929kTWEQqbZjaWfVdlVd5mNpZ9V2VV3rsu4KGiaC5rsfWMynBmEPsqAVQ-Bh25aqnCYdtZOgyR&_nc_ohc=FvbPtC9Y9KsAX9r0pKo&_nc_ht=scontent.fcrk1-1.fna&oh=4dfb7a2c255c0d89a11711fa148ae84f&oe=60A46DF1">
         <p align="center">
     Mark Denzel Delgado
     </p>
               <p align="center">
       <a href="https://github.com/dmarkdenzel">mark_denzel_delgado@dlsu.edu.ph</a>
     </p>
    </th> 
    <th> <img width="100" src="https://scontent.fmnl17-3.fna.fbcdn.net/v/t1.6435-1/p240x240/52522601_2540839149263674_602915654902743040_n.jpg?_nc_cat=110&ccb=1-3&_nc_sid=7206a8&_nc_eui2=AeGLryPaE_qAesHeoG7z7oIb1SeXW3CsXzPVJ5dbcKxfMxD3NVSi9IVqSoF2CsyWbX_IkGL4mLxnSnnMWp--54g0&_nc_ohc=_CzK-LB2zX4AX-e7tup&_nc_ht=scontent.fmnl17-3.fna&tp=6&oh=c9332ac42b13a0bd0fb152c03ef1718a&oe=60A5D5FB">
             <p align="center">
     Tyrone Ashley Go
     </p>
     <p align="center">
     <a href="https://github.com/tyrone890123">tyrone_go@dlsu.edu.ph</a>
     </p>
    </th>
  </tr>
</table>
