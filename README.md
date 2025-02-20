# Application is building... 🛠️

run docker-compose up to create MySQL container.

if you don't want to install something like MySQL Workbench to create the
schema of database, you can run: 

1 - Copy script.sql into the Docker Container: 
`docker cp /path/to/your/project/script.sql CONTAINER_ID:/script.sql`

2 - Use docker exec to get inside the MySQL container's shell:
`docker exec -it CONTAINER_ID /bin/bash` 

3 - Once inside the container, run your MySQL command:
`mysql -uroot -pYOURPASSWORD < /script.sql`

