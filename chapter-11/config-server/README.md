# How to access a config profile

This config server loads the files from the /config-files directory in our repository. Those files contain configuration examples for how to customize the property values for each service.
In our example, we have added the following property files for each service:
- {appname}.properties - This is a default property file
- {appname}-{profile}.properties - This is for a specific profile.

For instance, in the rental-proposal-service, we have the following files:

- rental-proposal-service.properties
- rental-proposal-service-dev.properties
- rental-proposal-service-stg.properties
- rental-proposal-service-prod.properties

That means every service has a default remote config and then some different property files for different environments.
Every time you run this service and one of our services query the properties from the service, it will fetch the default properties and it will also fetch the specified profile properties, which will override the default value and the application default values as well (located in the main/resources/application.properties file).

Once you run this service, it will use GIT to download the properties from the git repo. After running the config service, you will be able to query for the properties using a simple browser. Just use this URL template to check the properties hosted by your config service:
http://localhost:8888/{app-name}/{profile}

Here are some examples:

- http://localhost:8888/authprovider/dev
- http://localhost:8888/authprovider/prod
- http://localhost:8888/authprovider/stg
- http://localhost:8888/authprovider/default