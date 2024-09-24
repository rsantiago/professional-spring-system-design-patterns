# How to run this service

## Setting up a dev mongodb instance

First, you will need to install mongodb

- Download it from the mongodb website `https://www.mongodb.com/try/download/community`
- If you want to keep it simple, download the tarball and extract in a local directory.

If you intend to create a test database for development purposes, feel free to use your own home directory. For instance, you can download your tarball straight to your `~/mongodb` database.

Extract the latest version of your mongodb to your test directory, then create another directory for your actual database files.

For instance, I created the following directory to host any test databases I need:

`~/mongodb/mongodbs`

Inside the mongodbs database, I have one directory for hosting the proposal service database, which I called proposaldb:

`~/mongodb/mongodbs/proposaldb`

Inside this database, create the following directories:

``` 
cd ~/mondodb/mongodbs/proposaldb
mkdir -p data/db
mkdir log
```

With those two directories, it is possible to start your mongodb instance, with the following command:

```
cd ~/mongodb
./mongodb-linux-x86_64-ubuntu2204-7.0.14/bin/mongod --dbpath mongodbs/proposaldb/data/db --logpath mongodbs/proposaldb/log/mongodb.log --fork
```
That command will work as long as you have downloaded and extracted the mongodb to your home directory.

This command runs mongod, which is the mongo server daemon.

To make sure it is running, just use this command now:

```
ps aux | grep mongod
```

If you get an output like the following, it means your instance is running correctly:

```
rodrigo@rodrigo-desktop:~/mongodb$ ps aux | grep mongod
rodrigo    15992  0.6  0.7 559832 126904 ?       Sl   20:30   0:11 mongodb-linux-x86_64-ubuntu2204-7.0.14/bin/mongod --dbpath mongodbs/proposaldb/data/db --logpath mongodbs/proposaldb/log/mongodb.log --fork
```

Now, you need a client app to connect to your mongodb. Make sure to download Mongodb Shell, from the official mondodb website:
```
    https://www.mongodb.com/try/download/shell
```

Once you download and extract it to a separate directory on your home dir, you will be able to use it with the following command:
```
cd ~/mongodb
./mongosh-2.3.1-linux-x64/bin/mongosh
```

This command will automatically connect mongo shell to your local mongo daemon.

Here are some important and essential commands for you to know how to navigate your database:
```
show dbs # show your databases
use <dbname> # use or create a new database
show collections # show your document collections inside a database (your tables, basically)
db.myCollection.find() # show all documents in a collection named myCollection 

To shutdown your mongodb server, try the following commands:
use admin # this will use your admin database
db.shutdownServer() # this will kill your mongodb process
```

If you manually shut down your mongodb server and restart it with the same command line stated before, the same database will be used again - therefore, keeping your saved data intact.

## Setting up a dev Kafka instance

This service use Kafka as the event infrastructure. To use Kafka, you need to set up a dev instance, using the following steps:

1 - Download Kafka from the website: https://kafka.apache.org/downloads

You can use the latest version. Just download the binary package and extract it to a separate home directory, like `~/kafka`.

2 - Run the dev instance

To run your kafka dev instance, you will need to run these two commands:

- Zookeper: 
```
cd ~/kafka/<your-kafka-extracted-directory>
./bin/zookeeper-server-start.sh config/zookeeper.properties`
```

- Kafka: 
````
cd ~/kafka/<your-kafka-extracted-directory>
./bin/kafka-server-start.sh config/server.properties 
```

Running these two commands will get you a kafka dev instance online, which is enough so that this Spring service can connect and create the topic.

3 - Connect a console consumer group so that you can see the produced events

Run the following command in order to create a separate consumer in your terminal:

```
cd ~/kafka/<your-kafka-extracted-directory>
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic proposal-topic --group console-consumer-group --from-beginning
```

This will ensure every event published to the 'proposal-topic' can be subscribed in the console. It makes it easier for you to see the events being produced in real time.
