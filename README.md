# Application is building... 🛠️

run docker-compose up to create MySQL container.

if you don't want to install something like MySQL Workbench to create the
schema of database, you can run: 

1 - Use docker exec to ger inside the MySQL container's shell:
`docker exec -it CONTAINER_ID /bin/bash` 

2 - Once inside the container, run your MySQL command:
`mysql -uroot -pYOURPASSWORD < /script.sql`

