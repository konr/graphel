(ns graphel.utils)


(defn find-first [pred coll]
  (first (filter pred coll)))

(defn filter-keys [fun coll]
  (->> coll
       (filter (fn [[k v]] (fun k)))
       (into {})))

(defn filter-vals [fun coll]
  (->> coll
       (filter (fn [[k v]] (fun v)))
       (into {})))

(defn map-keys [fun coll]
  (->> coll
       (map (fn [[k v]] [(fun k) v]))
       (into {})))

(defn map-vals [fun coll]
  (->> coll
       (map (fn [[k v]] [k (fun v)]))
       (into {})))

(defn partial< [fun & old-args]
  (fn [& new-args] (apply fun (concat new-args old-args))))

(def p> partial)
(def p< partial<)

(def keep-first (comp first keep))

