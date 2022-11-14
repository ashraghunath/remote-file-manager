# remote-file-manager

## HTTP Remote File fileManagerServer Implementation

Server command examples: 
httpfs -v -p 8080

httpfs -p 8080

httpfs -v -p 8080 -d TestDirectory



File commands examples:

httpfs GET / http://localhost:8080/get/

httpfs GET /sampleText.txt http://localhost:8080/get/
httpfs GET /sampleText2.txt http://localhost:8080/get/
httpfs GET /yahoo.json http://localhost:8080/get/

httpfs GET /img.png http://localhost:8080/get/

httpfs GET /artemis.html http://localhost:8080/get/

httpfs POST /samplePost.txt http://localhost:8080/post/ -d {Assignment 2 : "Comp 6461"}
httpfs POST /samplePostinTest.txt http://localhost:8080/post/ -d {Assignment 2 : "Comp 6461"}

httpfs POST /samplePost.txt http://localhost:8080/post/ -d {Assignment 2 : "Comp 6477"} overwrite=false
httpfs POST /samplePostinTest.txt http://localhost:8080/post/ -d {Assignment 2 : "Comp 6477"} overwrite=false








httpc commands examples:

httpc get -h Content-Type:application/json -h Testing-Header:TestHeader 'http://localhost:8080/get?course=networking&assignment=1'

httpc post -h Content-Type:application/json -d '{"Assignment": 2, "Course": 6461}' 'http://localhost:8080/post?'

httpc get -v -h Content-Type:application/json 'http://localhost:8080/get?course=networking&assignment=1'

httpc post -v -h Content-Type:application/json -d '{"Assignment": 2, "Course": 6461}' 'http://localhost:8080/post?'






