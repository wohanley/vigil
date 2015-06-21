(ns vigil.data.util)

(defn filter-keys [item keys]
  "Select a subset of keys from item, or all if keys is not supplied."
  (if keys
    (select-keys item keys)
    item))

(defn replace-rename-keys [search replace item]
  "Replace search in all the keys of item with replace."
  (clojure.set/rename-keys
   item
   (reduce
    #(assoc %1 (first %2) (keyword (clojure.string/replace (name (first %2)) search replace)))
    {} item)))
