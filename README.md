# webapp

Web Application Development:

RESTful API Requirements:

- All API request/response payloads should be in JSON.
- No UI should be implemented for the application.
- As a user, I expect all API calls to return with a proper HTTP status code.
- As a user, I expect the code quality of the application to be maintained to the     highest standards using the unit and/or integration tests.

Bootstrapping Database
- The application is expected to automatically bootstrap the database at startup.
- Bootstrapping creates the schema, tables, indexes, sequences, etc., or updates them if their definition has changed.
- The database cannot be set up manually by running SQL scripts.
- It is highly recommended that you use ORM frameworks such as Hibernate (for java), SQLAlchemy (for python), and Sequelize (for Node.js).

Users & User Account
- The web application will load account information from a CSV file from well known location /opt/user.csv.
- The application should load the file at startup and create users based on the information provided in the CSV file.
- The application should create new user account if one does not exist.
- If the account already exists, no action is required i.e. no updates.
Deletion is not supported.
- The user's password must be hashed using BCrypt before it is stored in the database.
- Users should not be able to set values for account_created and account_updated. Any value provided for these fields must be ignored.

Authentication Requirements
- The user must provide a basic authentication token when making an API call to the authenticated endpoint.
- The web application must only support Token-Based authentication and not Session Authentication.

API Implementation
- In this assignment we are going to implement REST API to allow users to create assignments. The API specifications can be written as below.

GET /v1/assignments

POST /v1/assignments

GET /v1/assignments/{id}

DELETE /v1/assignments/{id}

PUT /v1/assignments/{id}

GET /healthz

The authenticated user should be able to do the following:
- Create Assignment
- Any user can add a assignment.
- Assignment points must be between 1 and 10.
Update Assignment
- Only the user who created the assignment can update the assignment.
- Users can use either the PUT API for updates.
Delete Assignment
- Only the user who created the assignment can delete the assignment.
- Users should not be able to set values for assignment_created and assignment_updated. 
Any value provided for these fields must be ignored.   
