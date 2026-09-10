ENTRYPOINT [
    "java",
    "-Xms64M",
    "-Xmx128M",
    "-Dcom.sun.management.jmxremote=true",
    "-Dcom.sun.management.jmxremote.port=8009",
    "-Dcom.sun.management.jmxremote.ssl=false",
    "-Dcom.sun.management.jmxremote.authenticate=false",
    "-Djava.rmi.server.hostname=localhost"
    "-jar",
    "app.jar"
]

"-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"