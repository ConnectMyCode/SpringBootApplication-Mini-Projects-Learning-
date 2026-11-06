To connect with backend EC2 :->
Note: Chekout the actual ".sql" file name it is definitely renamed 
ssh -i "C:\Amazon_WorkSPace\Spring-14-Fitclub_pem-key-pair\FitClub-Backend.pem" ubuntu@13.204.85.167
    I have installed Mysql Client in EC2 to access it use cmmd :-> 
             mysql -h spring14-fitclub-database-mysql.cv2ya4wwg8bd.ap-south-1.rds.amazonaws.com -P 3306 -u fitclub_admin -p

    Transferring the "Mysql._sql" file into the Backend instance Ec2 go to dir where backend.pem key is and execute cmmds
    1.To check if both files exists in respective loacations: dir dir "C:\SpringBoot_And_Microservices_Workspace\Spring14-fitclub-mvc\src\Spring14_Mysql_Queries.sql"
    2.TO copy it from Windows to EC2 instance :    scp -i "C:\Amazon_WorkSPace\Spring-14-Fitclub_pem-key-pair\FitClub-Backend.pem" "C:\SpringBoot_And_Microservices_Workspace\Spring14-fitclub-mvc\src\Spring14_Mysql_Queries.sql" ubuntu@13.204.85.167:/home/ubuntu/
    3.Connect to EC2 inside run it to confirm: ls -lh /home/ubuntu/Spring14_Mysql_Queries.sql
    4.Inspecting the file :-> powershell -NoProfile -Command "Get-Content 'C:\SpringBoot_And_Microservices_Workspace\Spring14-fitclub-mvc\src\spring14_backup.sql' -TotalCount 50"
    5.Open mysql client inside EC2 instance: mysql -h spring14-fitclub-database-mysql.cv2ya4wwg8bd.ap-south-1.rds.amazonaws.com \ -P 3306 \ -u fitclub_admin \ -p
    6.Created Database with smae name then exit the mysql client -> exit; 
    7. mysql -h spring14-fitclub-database-mysql.cv2ya4wwg8bd.ap-south-1.rds.amazonaws.com \ -P 3306 \ -u fitclub_admin \ -p \
    8. After execute abot 7. cmmd enter password of RDS 
spring14 < /home/ubuntu/Spring14_Latest_Dump.sql
    
To connect with frontend EC2 :->
ssh -i "C:\Amazon_WorkSPace\Spring-14-Fitclub_pem-key-pair\Spring14-Fitclub-Frontend.pem" ubuntu@15.207.14.172






