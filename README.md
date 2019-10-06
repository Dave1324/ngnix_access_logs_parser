# Smart logs API

This project converts unstructured nginx log file data, into actionable structured data within a MySql database. The project is based on a Java spring boot + JPA (native MySql queries only :) stack, and is set up for local deployment using docker-compose.

### Project structure
Smart logs API utilizes a standard 3 tier architectural approach. There are 4 packages in total:
1. `controller`: Web layer
	- `Api`: Exposes all web endpoints.
2. `dao`: Data access layer
	- `DatabaseApiInterface`: Handles the direct interaction with the MySql DB.
3. `model`: Business entities
	- `NginxLogfile`: Represents a single log file
	- `NginxLogfileRow`: Represents a single line within such a file

4. `service`: Most of the business logic goes here
	- `Detokenizer`: Is the core of this system; takes in a single log file line, and outputs a map of the detokenized content.
	- `LogfileResolver`: Log files are stored in the cloud. Log file resolver takes in a "key" or uri, and fetches it. In this demo example it's just fetching a few static files from the target classpath, but the rest of the application doesn't know or care.
	- `NginxLogfileParser`: Parses an nginx log file and saves its data into the database.
	- `Token`: Represents a single expression in the collective token stream that makes up a line within a log file.

5. `dto`: The objects that are actually returned from the API endpoints
	- `Top5Tuple`: The return type of the `/top5` endpoint; `{url, hits}`
	- `RangeTuple`: The return type of the `/rate` endpoint; `{url, dateTime, hits}`

In addition to the above, you'll find the `init.sql` in the root directory. You do not need to execute it, as docker-compose will 
launch and initialize the database automatically.

### How it works
1. A `POST` request is made to the `/enqueue-log-files` API endpoint, containing 
an array of object keys as a request parameter. 

2. These keys are placed on a thread pool based queue, which uses the `LogfileResolver` 
service to fetch the actual file associated with a given key (from s3, etc.).

3. The parsing process consists of 2 primary stages:
    - parsing the "$log-format" config parameter to 
    compose an exact list of expected tokens.
    - parsing an actual line / token stream, and matching the actual
    content with the token templates compiled in the previous phase.
4. The newly structured data is persisted into the database.

Since all of the above parsing process in taking place on a separate thread, the
 `/top5` and `/rate` API endpoints are available from launch time.
 
 **Note:** In this demo stage number 1 is simulated by passing in the locations of 
 static classpath resources.

### Running the project
Clone this repo,  `cd` into the project root directory, and then execute the `docker-compose up` command.
The API is exposed on port 8080, the database is exposed on port 3306 (make sure that in your environment they're freed up).
