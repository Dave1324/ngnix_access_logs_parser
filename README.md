#Smart logs API

This project converts unstructured nginx log file data, into actionable structured data within a MySql database. The project is based on a Java spring boot + JPA (native MySql queries only :) stack, and is set up for local deployment using docker-compose.

###Project structure
Smart logs API utilizes a standard 3 tier architectural approach. There are 4 packages in total:
1. `controller`: Web layer
	- `Api`: Exposes all web endpoints.
2. `dao`: Data access layer
	- `DatabaseApiInterface`: Handles the direct interaction with the MySql DB.
3. `model`: Business entities
	- `NginxLogfile`: Represents a single log file
	- `NginxLogfileRow`: Represents a single line within such a file
	- `Request`: Represents the `$request` token in nginx logging.

4. `service`: Most of the business logic goes here
	- `Detokenizer`: Is the core of this system; takes in a single log file line, and outputs a map of the detokenized content.
	- `LogfileResolver`: Log files are stored in the cloud. Log file resolver takes in a "key" or uri, and fetches it. In this demo example it's just fetching a few static files from the target classpath, but the rest of the application doesn't know or care.
	- `NginxLogfileParser`: Parses an nginx log file and saves its data into the database.
	- `Token`: Represents a single expression in the collective token stream that makes up a line within a log file.

In addition to the above, you'll find the `init.sql` in the root directory.

### Running the project
Clone this repo,  `cd` into the project root directory, and then execute the `docker-compose up` command.