# remote-file-manager

## HTTP Remote File Server Implementation

* 1- GET / returns a list of the current files in the data directory. 

* 2- GET /foo returns the content of the file named foo in the data directory. If the content
does not exist, it should return an appropriate status code (e.g. HTTP ERROR 404).
* 3- POST /bar should create or overwrite the file named bar in the data directory with
the content of the body of the request. 

To test the file transfer command:-

To run the server:-
httpfs -v -p 8080

httpfs -p 8080

httpfs -v -p 8080 -d TestDirectory1

httpfs -v -p 8080 -d D:\MACS\TestDirectory2

To test the file transfer command:-

httpfs GET / http://localhost:8080/get/

httpfs GET /GetTest.txt http://localhost:8080/get/

httpfs POST /PostTest.txt http://localhost:8080/post/ -d {Assignment 2 : "Comp 6461"}

httpfs POST /PostTest.txt http://localhost:8080/post/ -d {Assignment 2 : "Comp 6461"} overwrite=false

httpfs POST /Users/ashwinraghunath/Documents/Fall_2022/COMP_6461_CN/Assignments/PostTest2.txt http://localhost:8080/post/ -d {Assignment 2 : "Comp 6461"}

To test the httpc commands:-

httpc get -h Content-Type:application/json -h Testing-Header:TestHeader 'http://localhost:8080/get?course=networking&assignment=1'

httpc post -h Content-Type:application/json -d '{"Assignment": 2, "Course": 6461}' 'http://localhost:8080/post?'

httpc get -v -h Content-Type:application/json 'http://localhost:8080/get?course=networking&assignment=1'

httpc post -v -h Content-Type:application/json -d '{"Assignment": 2, "Course": 6461}' 'http://localhost:8080/post?'






