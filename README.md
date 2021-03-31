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

[2] `src/main/java/org/avro/example/Main` is the main class\

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

## Example without code generation
This example makes use of the `Parser` class to first generate a `Schema` object.\
The previously generated `User` class is replaced by a `GenericRecord` class.\

## Basic benchmark 
### Benchmark with code generation
Class `org.avro.BenchmarkWithCodeGeneration`\
That benchmark measures for 10 ... 10 000 000 users:
- object creation
- object serialization
- objects deserialization

Here are the results on a Macbook Pro 2019 (time in ms):
```
Generation + Serialization time of 10 objects: 1
[with alternate constructor] Generation + Serialization time of 10 objects: 1
[via Builder] Generation + Serialization time of 10 objects: 1
Deserialization time of 10 objects: 4

Generation + Serialization time of 100 objects: 3
[with alternate constructor] Generation + Serialization time of 100 objects: 2
[via Builder] Generation + Serialization time of 100 objects: 3
Deserialization time of 100 objects: 5

Generation + Serialization time of 1000 objects: 5
[with alternate constructor] Generation + Serialization time of 1000 objects: 2
[via Builder] Generation + Serialization time of 1000 objects: 13
Deserialization time of 1000 objects: 13

Generation + Serialization time of 10000 objects: 22
[with alternate constructor] Generation + Serialization time of 10000 objects: 20
[via Builder] Generation + Serialization time of 10000 objects: 51
Deserialization time of 10000 objects: 14

Generation + Serialization time of 100000 objects: 140
[with alternate constructor] Generation + Serialization time of 100000 objects: 133
[via Builder] Generation + Serialization time of 100000 objects: 512
Deserialization time of 100000 objects: 68

Generation + Serialization time of 1000000 objects: 475
[with alternate constructor] Generation + Serialization time of 1000000 objects: 375
[via Builder] Generation + Serialization time of 1000000 objects: 1293
Deserialization time of 1000000 objects: 193

Generation + Serialization time of 10000000 objects: 3511
[with alternate constructor] Generation + Serialization time of 10000000 objects: 3413
[via Builder] Generation + Serialization time of 10000000 objects: 13028
Deserialization time of 10000000 objects: 1456
```
### Benchmark without code generation
Class `org.avro.BenchmarkWithoutCodeGeneration`\
That benchmark measures for 10 ... 10 000 000 users:    
- object creation                                       
- object serialization                                  
- objects deserialization                             
                                                        
Here are the results on a Macbook Pro 2019 (time in ms):
```
[Without code generation] User creation + Serialization time of 10 objects: 1
Deserialization time of 10 objects: 3

[Without code generation] User creation + Serialization time of 100 objects: 2
Deserialization time of 100 objects: 3

[Without code generation] User creation + Serialization time of 1000 objects: 4
Deserialization time of 1000 objects: 6

[Without code generation] User creation + Serialization time of 10000 objects: 31
Deserialization time of 10000 objects: 24

[Without code generation] User creation + Serialization time of 100000 objects: 361
Deserialization time of 100000 objects: 249

[Without code generation] User creation + Serialization time of 1000000 objects: 521
Deserialization time of 1000000 objects: 133

[Without code generation] User creation + Serialization time of 10000000 objects: 3297
Deserialization time of 10000000 objects: 1381
```