(defproject clj-browserchannel-demo "0.0.1"
  :description "BrowserChannel"
  :url ""
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [ring/ring-core "1.1.0-SNAPSHOT" :exclusions [javax.servlet/servlet-api]]
                 [ring/ring-servlet "1.1.0-SNAPSHOT" :exclusions [javax.servlet/servlet-api]]
                 [org.eclipse.jetty/jetty-server "8.1.2.v20120308"];; includes ssl
                 [org.clojure/data.json "0.1.3"]
                 #_[org.clojure/clojurescript "0.0-927"]
                 [org.clojure/clojurescript "0.0-1006"]]
  )
