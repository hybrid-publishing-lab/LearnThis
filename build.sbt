name := """leuphana-hybrides-publizieren"""

version := "1.0-SNAPSHOT"

playJavaSettings

ebeanEnabled := false

libraryDependencies ++= Seq(
    javaCore,
    javaJpa,
    "org.springframework" % "spring-context" % "4.1.6.RELEASE",
    "javax.inject" % "javax.inject" % "1",
    "org.springframework.data" % "spring-data-jpa" % "1.8.0.RELEASE",
    "org.springframework" % "spring-expression" % "4.1.6.RELEASE",
    "org.hibernate" % "hibernate-entitymanager" % "3.6.10.Final",
    "mysql" % "mysql-connector-java" % "5.1.18",
    "commons-io" % "commons-io" % "2.3",
    "org.mockito" % "mockito-core" % "1.9.5" % "test"
)