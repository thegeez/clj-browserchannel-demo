(ns bc.core
  (:require
   [bc.dom-helpers :as dom]
   [goog.net.BrowserChannel :as goog-browserchannel]
   [goog.events :as events]
   [goog.events.KeyCodes :as key-codes]
   [goog.events.KeyHandler :as key-handler]

   ;;debug
   [goog.debug.FancyWindow :as debug-window]
   [goog.net.ChannelDebug :as goog-chandebug]
   ))

(defn handler []
  (let [h (goog.net.BrowserChannel.Handler.)]
    (set! (.-channelOpened h)
          (fn [channel]
            (enable-chat)))
    (set! (.-channelHandleArray h)
          (fn [x data]
            (let [msg (aget data "msg")]
              (dom/append (dom/get-element "room") (dom/element :div (str "MSG:" msg))))))
    h))

(defn say [text]
  (.sendMap channel (doto (js-obj)
                      (aset "msg" text)) ))

(defn enable-chat []
  (let [msg-input (dom/get-element "msg-input")
        send-button (dom/get-element "send-button")
        handler (fn [e]
                  (say (dom/value msg-input))
                  (dom/set-value msg-input ""))]
    (dom/set-disabled msg-input false)
    (dom/set-disabled send-button false)
    (events/listen (goog.events.KeyHandler. msg-input)
                   "key"
                   (fn [e]
                     (when (= (.-keyCode e) key-codes/ENTER)
                       (handler e))))
    (events/listen send-button
                  "click"
                  handler)))

(def channel (goog.net.BrowserChannel.))

(def debug-window (doto (goog.debug.FancyWindow. "main")
                    (.setEnabled true)
                    (.init ())))

;; this makes the second stage /channel/test continue after
;; receiving 11111 instead of waiting the whole 2 seconds
;; this feature is the default from at least GClosure rev 1698
;; cljs currently includes rev 790
;; this does not work with advanced compilation
;; Wait for CLJS-88 or use more recent GClosure lib (ie wait for CLJS-35)
(set! goog.net.BrowserTestChannel.ALLOW_EARLY_NON_BUFFERED_DETECTION true)

(defn ^:export run []
  (events/listen js/window "unload" #(.disconnect channel ()))
  (doto channel
    (.setChannelDebug (goog.net.ChannelDebug.))
    (.setHandler (handler))
    ;;(.setAllowChunkedMode false)
    (.connect "/channel/test" "/channel/bind") ;;
    )
  
  )
