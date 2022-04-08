Latency:
Digitally recolor all flowers with white color to purple.

Recolor in single thread.
Recolor in multiple threads.
Calculate duration. 

Throughput:
Build HTTP Server which we will send a flow http requests as input.
HTTP Server first load a very large book from the disk.
The application is act as very basic search engine.
The client send us a word, any word in http uri and application will search for this word in book and count 
how many times this word appears.
http://127.0.0.1:8000/search?word=talk

As response the application will send count to the user.
status:200, body:3443