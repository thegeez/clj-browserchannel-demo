# clj-browserchannel-demo

Read EuroClj email and copy

Runs on at least:
- Chrome
- Firefox
- IE 5.5+ (!!)
- Android browser

Related and alternative frameworks:
- socket.io
- websockets
- Aleph/Netty work in progress. The lamina channel is also a candidate
  to replace the listeners construction.

Run on heroku
- buildpack compile cljs

Note on disconnections on Heroku:
IN case of a cut internet cable disconnection, where the client cannot
initiate a new connection:
This also happens when using Netty rather than Jetty on
Heroku. Deploying on AWS does propagate the closed connection.
The connection will always be recognized as closed when the
:response-timeout expires. Again this is only a problem when the
client cannot reconnect.

Debug:
in the cljs code are these lines: uncomment them and recompile for a
helpful loggin window

BrowserChannel server side implementation in other languages:
(thank the authors!)
libevent - also has the most extensive documentation on BrowserChannel protocol
Ruby - 
Node.js/Javascript - 


Copyright © 2012 Gijs Stuurman

