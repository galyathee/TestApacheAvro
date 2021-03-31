# TestApacheAvro
Test of [Apache Avro](https://avro.apache.org/docs/current/gettingstartedjava.html) examples.

## Technologies
* IDEA
* Java 8
* Maven 3.1
* Apache Avro 1.10.2

## Notes on IDEA
IDEA will not start the avro-maven-plugin automatically. To workaround this:
#### Set `mvn compile` to be run before build / rebuild
Open **View -> Tool windows -> Maven**\
Right click on *compile* and check *Execute before build* and *Execute before rebuild*.\
That way, the action "Build module" and "Rebuild module" will always execute the avro maven plugin.

## Project elements
[1] `pom.xml` contains the avro plugin definition:
```
<plugin>
  <groupId>org.apache.avro</groupId>
  <artifactId>avro-maven-plugin</artifactId>
  <version>1.10.2</version>
  <executions>
    <execution>
      <phase>generate-sources</phase>
      <goals>
        <goal>schema</goal>
      </goals>
      <configuration>
        <sourceDirectory>${project.basedir}/src/main/avro/</sourceDirectory>
        <outputDirectory>${project.basedir}/src/main/java/</outputDirectory>
      </configuration>
    </execution>
  </executions>
</plugin>
```
With that declaration `mvn compile` looks for **avsc** files under *${project.basedir}/src/main/avro/* and generates classes under *${project.basedir}/src/main/java/*

[2] `src/main/java/org/avro/example/Main` is the main class

## Example with code generation
#### Avro *.avsc* file
`src/main/avro/user.avsc` contains the JSON structure we want to work with as object.\

```
{"namespace": "pkg.where.to.generate.files",
 "type": "record",
 "name": "User",
 "fields": [
     {"name": "name", "type": "string"},
     {"name": "favorite_number",  "type": ["int", "null"]},
     {"name": "favorite_color", "type": ["string", "null"]}
 ]
}
```
The generated objects will be placed under the *outputDirectory* specified in the pom.xml, in the Java package given in the *namespace* attribute of the avsc file\

`src/main/java/org/avro/example/codegen/WithCodeGeneration.java` shows a possible use of the generated objects:
- Instanciation of the generated objects
- Object serialization
- Object de-serialization
