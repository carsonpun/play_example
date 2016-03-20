# Introduction

A play framework (2.5.0) example which does the follows:

(original features on 2016/03/19)

0. upon visiting the webpage, a websocket will be created.
1. users can put in a url (need to prefixed with http://)
2. the url is sent to webserver.
3. webserver will read the content from the url, fix any HTML ill-form, and extract the title string.
4. the extracted title string will be send back to the client through the same websocket
5. the client side will show the resultant title string

(updated on 2016/03/20)

6. whole client side is recoded using angularjs and bootstrap3
7. websocket is handled in the angularjs application

All message communications on the websockets are in json. (using the play-json library)

# To run 

1. `activator dist` which will give you a production version, `./target/universal/play-scala-1.0-SNAPSHOT.zip`
2. `unzip ./target/universal/play-scala-1.0-SNAPSHOT.zip`, you will see `play-scala-1.0-SNAPSHOT/`
3. To deploy the webserver app, run `play-scala-1.0-SNAPSHOT/bin/play-scala`
