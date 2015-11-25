var mongodb = require('mongodb');

var MongoClient = mongodb.MongoClient;

var url = 'mongodb://kancer:158578@ds057214.mongolab.com:57214/messangerwissen';

MongoClient.connect(url, function(err, db){

    if(err){
       console.log('Unable to connect to mongoDB server. Error: ', err);
    }else{
       console.log('Connection established to', url);

       var collection = db.collection('menu');

       collection.find({"Aramalar": "Arama Bilgisi"}).toArray(function (err, result){

          if(err){
            console.log(err);
          }else if(result.length){
            console.log('Found:', result);
          }else{
            console.log('No document(s) found with defined "find" criteria!');
          }

          db.close();
       });
   }
})
