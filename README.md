# IS3106 Project - Group 22
- Group Leader: Shawn Roshan Pillay, A0167180E, shawn.pillay@u.nus.edu, 83396408
- Group Member 1: Alastair Tan Jin Hui, A0176851W, e0235258@u.nus.edu, 97568302
- Group Member 2: Tan Win Phong, A0170152U, e0191700@u.nus.edu, 86195650
- Group Member 3: Bryan Chee Guang Hao, A0167656N, e0176089@u.nus.edu, 92323154

# Notes
**Internet connection required when viewing the project**  
  
Default username (email) and passwords for customers in (email, password) format:
  - (Steve@gmail.com, password)
  - (peter@gmail.com, password)
  - (bruce@gmail.com, password)
  - (ant@gmail.com, password)
  
Default staff username and password in (username, password) format:
  - (manager, password)

# To Run Angular Project StoreCraftCustomers
1. cd StoreCraftCustomers
2. npm install
3. npm start (not ng serve)

# To Run Netbeans Project StoreCraft
1. Open StoreCraft Project in Netbeans
2. Create local mysql database named "storecraft" : create schema storecraft 
3. Create new JDBC Resource (follow glassfish-resources.xml)
  - JDBC jndi-name: jdbc/storeCraft
  - pool-name: storeCraftConnectionPool
  - jdbc connection pool URL: jdbc:mysql://localhost:3306/storecraft?zeroDateTimeBehavior=CONVERT_TO_NULL
4. Deploy application
5. Proceed to localhost:8080/StoreCraftStaff
