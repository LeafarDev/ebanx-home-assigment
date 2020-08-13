# Take Home assignment from EBANX

The API consists of two endpoints, GET /balance, and POST /event. Using your favorite programming language, build a system that can handle those requests, publish it on the internet, and test it using our automated test suite.
### Running localhost
`lein ring server`

### Running jar
```
lein do clean, ring uberjar
java -jar target/server.jar
```
### Generating war
`lein ring uberwar`

## License
Eclipse Public License - v 2.0