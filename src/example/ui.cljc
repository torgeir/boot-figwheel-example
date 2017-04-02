(ns example.ui
  (:require [rum.core :as rum]))

(defn update-number [state fn]
  #(swap! state update-in [:number] fn))

(rum/defc button
  ([label on-click]
   [:button.btn--large {:on-click on-click} label]))

(rum/defc app < rum/reactive [state]
  [:div
   [:p
    [:span "A number: "]
    [:span.hl (:number (rum/react state))]]
   (button "decrease" (update-number state dec))
   (button "randomize" (update-number state #(rand 42)))
   (button "increase" (update-number state inc))])
