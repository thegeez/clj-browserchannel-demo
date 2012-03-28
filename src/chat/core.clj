(ns chat.core
  (:require [net.thegeez.browserchannel :as browserchannel]
            [net.thegeez.jetty-async-adapter :as jetty]
            [ring.middleware.resource :as resource]))

(defn handler [req]
   {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello World"})

(def clients (atom #{}))

(def dev-app
  (-> handler
      (resource/wrap-resource "dev")
      (resource/wrap-resource "public")
      (browserchannel/wrap-browserchannel {:base "/channel"
                                           :on-session
                                           (fn [session-id]
                                             (println "session " session-id "connected")
                                             
                                             (browserchannel/add-listener
                                              session-id
                                              :close
                                              (fn [reason]
                                                (println "session " session-id " disconnected: " reason)
                                                (swap! clients disj session-id)
                                                (doseq [client-id @clients]
                                                  (browserchannel/send-map client-id {"msg" (str "client " session-id " disconnected " reason)}))))
                                             (browserchannel/add-listener
                                              session-id
                                              :map
                                              (fn [map]
                                                (println "session " session-id " sent " map)
                                                (doseq [client-id @clients]
                                                  (browserchannel/send-map client-id map))))
                                             (swap! clients conj session-id)
                                             (doseq [client-id @clients]
                                                  (browserchannel/send-map client-id {"msg" (str "client " session-id " connected")})))})))

(defn -main [& args]
  (jetty/run-jetty-async #'dev-app {:port (Integer.
                                           (or
                                            (System/getenv "PORT")
                                            8080)) :join? false}))

(comment
  (def async-server (-main))
  (.stop async-server)
  (do
    (.stop async-server)
    (def async-server (-main))
    )
  )

