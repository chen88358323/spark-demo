name := "spark-app"
version := "0.1"
scalaVersion := "2.12.15"

libraryDependencies += "org.apache.spark" %% "spark-core" % "3.0.3"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "3.0.3"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.0.3"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.0.3"

libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.0.3"

libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.28"
libraryDependencies += "com.alibaba" % "fastjson" % "1.2.80"
