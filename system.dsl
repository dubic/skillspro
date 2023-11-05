workspace {

    model {
        user = person "User"
        visitor = person "Visitor"
        system = softwareSystem "Skillspro" {
            app = container "Mobile app" "Flutter" {
                tags "mobile"
            }
            backend = container "Backend Service" "Nestjs" "profile and posts management" {
               users = component "Users Component"
               auth = component "Authentication Component"
               notif = component "Notification Component" "emails & SMS"
               firebase = component "Firebase facade"
               cloudStorage = component "Cloud storage facade"
               profile = component "Profile component"
               posts = component "Posts component"
            }
            database = container "Database" "mongo db"{
                tags "db"
            }
        }

        firebaseSystem = softwareSystem "Firebase" {
            tags "external"
        }
        storageSystem = softwareSystem "Cloud Storage" {
            tags "external"
        }

        user -> app "post projects" "http"
        visitor -> app "view posts" "http"
        app -> backend "create and view posts" "REST"
        auth -> users "find user"
        backend -> database "save account, login, posts"
        users -> notif "send verification email"
        notif -> user "verify account mail"
        app -> firebaseSystem "get google account"
        users -> firebase "get google user"
        firebase -> firebaseSystem "get details from token" "Firebase SDK"
        profile -> cloudStorage "store profile picture"
        cloudStorage -> storageSystem "store image" "GC storage SDK"
        profile -> cloudStorage "load, save project image"
    }

    views {
        systemContext system {
            include *
            autolayout lr
        }

        container system {
            include *
            autolayout lr
        }

        component backend {
            include *
            autolayout lr
        }

        styles {
            element "mobile" {
                shape MobileDevicePortrait
            }
            element "db" {
                shape Cylinder
            }
            element "broker" {
                shape Pipe
            }
            element "external" {
                color "#010101"
                background "#d5d5d5"
            }
        }
        theme default
    }

}