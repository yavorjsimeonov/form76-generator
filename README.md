Form76 generator App

Running the application

1. Make sure u have docker desktop installed
2. Create `.env` file in project root directory and add following env variables with the corresponding values
   MYSQL_ROOT_PASSWORD=<password_for_myslq_root_account>

   //When running locally the Form76 generator App will use mylinkmock app instead of a real MyLink system 
   MY_LINK_API_TOKEN=<random_string> 

   //Configs for mail server to be used for sending emails. I used google service `smtp.gmail.com`
   SPRING_MAIL_HOST=smtp.gmail.com
   SPRING_MAIL_PORT=587
   SPRING_EMAIL_USERNAME=<username>
   SPRING_EMAIL_PASSWORD=<password>

   //Configs for google cloud bucket where files are stored
   GOOGLE_CLOUD_PROJECT_ID=<project_id>
   GOOGLE_CLOUD_BUCKET_NAME=<bucket_name>    
3. Add bucket authorisation configs as json file in `server/src/main/resources`
4. Run mylinkmock (https://github.com/yavorjsimeonov/mylinkmock) also locally
5. Run `docker compose --env-file ./.env up -d`
6. To re-run all containers use `./rerun-dc.sh` file 