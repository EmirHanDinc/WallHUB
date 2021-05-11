var firebaseConfig = {
    apiKey: "AIzaSyDGJ19XRk3GP9YzjPe9vADWGDabxL94Gjs",
    authDomain: "wallhub-5f036.firebaseapp.com",
    databaseURL: "https://wallhub-5f036-default-rtdb.firebaseio.com",
    projectId: "wallhub-5f036",
    storageBucket: "wallhub-5f036.appspot.com",
    messagingSenderId: "44144871370",
    appId: "1:44144871370:web:21be2164f63c5ebf86b133",
    measurementId: "G-Z2PCY98127"
  };
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);
  firebase.analytics();

  firebase.auth.Auth.Persistence.LOCAL; 

  $("#btn-login").click(function(){
      
      var email = $("#email").val();
      var password = $("#password").val(); 

      var result = firebase.auth().signInWithEmailAndPassword(email, password);
  
      result.catch(function(error){
          var errorCode = error.code; 
          var errorMessage = error.message; 

          console.log(errorCode);
          console.log(errorMessage);
      });

  });

  $("#btn-logout").click(function(){
      firebase.auth().signOut();
  });

  function switchView(view){
      $.get({
          url:view,
          cache: false,  
      }).then(function(data){
          $("#container").html(data);
      });
  }