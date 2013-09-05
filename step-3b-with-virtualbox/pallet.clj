;;; Pallet project configuration file
(require '[pallet.crate.java :as java]
         '[pallet.crate.runit :as runit]
         '[pallet.crate.app-deploy :as app-deploy])

(def webserver
  (group-spec "webserver"
    :extends [(java/server-spec {})
              (runit/server-spec {})
              (app-deploy/server-spec
                {:app-root "/opt/my-webapp"
                 :artifacts
                 {:from-lein
                  [{:project-path "target/webapp-%s-standalone.jar"
                    :path "webapp.jar"}]}
                 :run-command "java -jar /opt/my-webapp/webapp.jar"}
                :instance-id :my-webapp)]))

(defproject webapp
  :provider {:vmfest
             {:node-spec
              {:image {:os-family :ubuntu
                       :os-version-matches "13.04"
                       :os-64-bit? true}
               :network {:incoming-ports [22 3000]}}}}
  :groups [webserver])
