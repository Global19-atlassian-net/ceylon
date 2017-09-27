import ceylon.html {
    Html,
    html5,
    Head,
    Body,
    P
}
import ceylon.html.serializer {
    NodeSerializer
}
import ceylon.net.http.server {
    newServer_bogus,
    Endpoint,
    startsWith,
    Response,
    Request
}

Html html(Request request) {
    return Html {
        doctype = html5; 
        Head { title = "Hello"; }; 
        Body {
            P("Hello!")
        };
    };
}

shared void run() {
    newServer_bogus {
        Endpoint { 
            path = startsWith("/"); 
            void service(Request request, Response response) {
                NodeSerializer(response.writeString).serialize(html(request));
            }
        }
    }.start();
}
