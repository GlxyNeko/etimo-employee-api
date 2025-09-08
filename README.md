# About
Etimo Employee API is an API based on a code case from Etimo. It's able to perform 4 operations:
* Add an employee
* Get an employee *- (This was added as an extra operation. You may disregard this in your assessment.)*
* Get all employees
* Delete an employee

The API is fully HAL JSON and HATEOAS compliant, and allows you to "discover" the API from the root endpoint.

# Getting Started
## How to start the API
To use the API, all you have to do is run the `EtimoEmployeeApiApplication` in your IDE. The API should now be accessible on your [localhost](http://localhost:8080/api/v1) domain.

## Using the API
### Documentation Summary
There are 3 endpoints available:
* `/api/v1`
  * This is the root endpoint. `GET` will return what endpoints you can access.
* `/api/v1/employees`
  * This endpoint handles the employees.
    * `GET` lets you retrieve all employees.
    * `POST` lets you add a new employee. The body takes JSON with the following format:
      ```json
      { "firstName": <first name>, "lastName": <last name>, "email": <email> }
* `/api/v1/employees/{id}`
  * This endpoint lets you access a specific employee. `{id}` is a UUID that's generated for each added employee to mask their email from the URL for security reasons.
    * `GET` lets you retrieve the employee.
    * `DELETE` lets you remove an employee. Returns `204 No Content` with no body if successful, as the resource URI is no longer valid.

For the full documentation, see the `oas.json` file. It contains the entire API documentation in JSON, using the OpenAPI Specification.

### Interacting
For `GET` requests, it's possible to simply use a web browser. With that said, the best way to interact with the API is to use a tool like *cURL* or *Postman*.

#### cURL Commands
The following commands will let you interact with the API, and shows both the response header and body:
* `GET` the API root:
    ```bash
    curl -i "http://localhost:8080/api/v1"
* `GET` all employees:
    ```bash
    curl -i "http://localhost:8080/api/v1/employees"
* `POST` to add a new employee (HTTP Code 409 if duplicate email exists):
    ```bash
    curl -i -X POST -H "Content-Type: application/json" -d "{\"firstName\": \"Gandalf\", \"lastName\": \"the Grey\", \"email\": \"gandalf@istari.org\"}" "http://localhost:8080/api/v1/employees"
* `GET` an employee (replace ID with the value returned from getting all employees, or adding a new employee)(HTTP Code 404 if employee doesn't exist):
    ```bash
    curl -i "http://localhost:8080/api/v1/employees/<ID>"
* `DELETE` an employee (replace ID with the value returned from getting all employees, or adding a new employee)(HTTP Code 404 if employee doesn't exist):
    ```bash
    curl -i -X DELETE "http://localhost:8080/api/v1/employees/<ID>"
