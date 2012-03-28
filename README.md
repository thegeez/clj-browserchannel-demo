# clj-browserchannel-demo

Cross-browser compatible, real-time, bi-directional
communication between ClojureScript and Clojure using Google Closure
BrowserChannel.

## goog.net.BrowserChannel

From the Google Closure API: "A [BrowserChannel][1] simulates a
bidirectional socket over HTTP. It is the basis of the Gmail Chat IM
connections to the server." 
The javascript api of BrowserChannel is open-source and part of the
Google Closure library. The server component is not, as is noted in
the Google Closure book ("Closure: The Definitive Guide by Michael Bolin").

[1]: http://closure-library.googlecode.com/svn-history/r144/docs/closure_goog_net_browserchannel.js.html

## Demo

clj-browserchannel-demo is an example chat application using a server
side implementation for BrowserChannel written in Clojure. The server
component is for BrowserChannel version 8.

This enables client->server and server->client communication in
ClojureScript and Closure web apps, without any javascript
dependencies other than the Google Closure [library][2].

[2]: https://developers.google.com/closure/library/

The example runs in at least:

* Chrome
* Firefox
* Internet Explorer 5.5+ (!!)
* Android browser

## Jetty Async

When there are long lasting connections between a client and a
webserver it is desirable to not have a thread per
connection. Therefore this demo runs with with an asynchronous Jetty
adapter. This adapter is compatible with Ring.

The adapter is based on [ring-jetty-async-adapter][3] by Mark McGranaghan.

[3]: https://github.com/mmcgrana/ring/tree/jetty-async

An implementation on top of Netty, through [Aleph][4] is in
development.

[4]: https://github.com/ztellman/aleph

## Related and alternative frameworks

* Websockets - Websockets solve the same problems as BrowserChannel,
  however BrowserChannel works on almost all existing clients.
* socket.io - [socket.io][5] provides a similar api as BrowserChannel on
top of many transport protocols, including websockets. BrowserChannel
only has two transport protocols: XHR and forever frames (for IE) in
streaming and non-streaming mode.

[5]: http://socket.io

## Run 
    lein run -m tasks.build-dev-js ;; compile cljs
    lein run -m tasks.build-advanced-js ;; compile cljs in advanced mode
    lein run -m chat.core

Open the app on [http://localhost:8080/index.html][http://localhost:8080/index.html] (Advanced compiled)
or [http://localhost:8080/index-dev.html][http://localhost:8080/index-dev.html]

## Run on Heroku
Use this [buildpack][6], which runs the two lein run tasks to compile
the ClojureScript during deployment.

[6]: https://github.com/thegeez/heroku-buildpack-clojure

### Note on disconnections on Heroku
I have found that Heroku does not immediately report when a connection to a client
is broken. If the client is able to reconnect this is not a problem,
as this is supported by the BrowserChannel API. However when you
unplug the internet cable the client cannot reconnect and the server
must timeout the session. Ussually this happens when trying to send the next
heartbeat to the client. On Heruko this does not report an error, even
though there is no connection to the client. So instead of the
connection timeing out on a heartbeat (after seconds/a minute) the
connection will only timeout after the connection is timeout by the
server (4 minutes by default). The Netty implementation has the same
problem on Heroku. Deployments on Amazon Web Services do not have this
problem. 

## Configuration:
See default-options in src/net/thegeez/browserchannel.clj
And the :response-timeout option in src/net/thegeez/jetty_async_adapter.clj

### Debug / Play around
BrowserChannel has a helpful debug window. Uncomment the debug-window
and .setChannelDebug lines in cljs/bc/core.cljs to enable the logging window.

## Todo
- Handling acknowledgements by client and callbacks on queued arrays
- Host prefixes
- Heroku disconnection
- Replace session listeners, possibly with lamina
- Explore other event based Java webservers, such as Netty and Webbit

## Other BrowserChannel implementations
Many thanks to these authors, their work is the only open-source
documentation on the BrowserChannel protocol.
* [libevent-browserchannel-server][http://code.google.com/p/libevent-browserchannel-server]
in C++ by Andy Hochhaus - Has the most extensive [documentation][http://code.google.com/p/libevent-browserchannel-server/wiki/BrowserChannelProtocol] on the BrowserChannel protocol
* [browserchannel][https://github.com/dturnbull/browserchannel] in Ruby by David Turnbull
* [node-browserchannel][https://github.com/josephg/node-browserchannel]
in Node.js/Javascript by Joseph Gentle

## About

Written by:
Gijs Stuurman / [@thegeez][http://twitter.com/thegeez] / [Blog][http://thegeez.github.com] / [Github][https://github.com/thegeez]

License

Copyright (c) 2012 Gijs Stuurman and released under an MIT license.
