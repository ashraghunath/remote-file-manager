# remote-file-manager

## HTTP Remote File Server Implementation

* 1- GET / returns a list of the current files in the data directory. 

* 2- GET /foo returns the content of the file named foo in the data directory. If the content
does not exist, it should return an appropriate status code (e.g. HTTP ERROR 404).
* 3- POST /bar should create or overwrite the file named bar in the data directory with
the content of the body of the request. 



