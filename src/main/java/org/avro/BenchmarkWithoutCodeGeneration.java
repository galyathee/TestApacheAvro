package org.avro;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class uses the Parser object to create an in-memory representation of the src/main/avro/user.asvc definition:
 * - create User instances
 * - serialize User
 * - Deserialize User
 * - Print user definition
 */
public class BenchmarkWithoutCodeGeneration {

    public void benchmarkAvroUseWithoutCodeGeneration(int nbUsers){

        removeFile("users_w.avro");

        long cTime = System.currentTimeMillis();

        // use a Parser to read schema definition and generate a Shema object
        Schema schema = null;
        try {
            schema = new Schema.Parser().parse(new File("./src/main/avro/user.avsc"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Serialize user1, user2 to disk
        File file = new File("users_w.avro");
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        try (DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter)){
            dataFileWriter.create(schema, file);
            GenericRecord user = new GenericData.Record(schema);
            for (int i = 1; i <= nbUsers; i++) {
                user.put("name", "Alan_"+i);
                user.put("favorite_number", i);
                user.put("favorite_color", "blue");
                dataFileWriter.append(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long cTime2 = System.currentTimeMillis();
        System.out.println("[Without code generation] User creation + Serialization time of " + nbUsers + " objects: " + Long.toString(cTime2 - cTime));

        // Deserialize users from disk
        cTime = System.currentTimeMillis();
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        try (DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, datumReader)){

            GenericRecord user = null;
            while (dataFileReader.hasNext()) {
                // Reuse user object by passing it to next(). This saves us from
                // allocating and garbage collecting many objects for files with
                // many items.
                user = dataFileReader.next(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cTime2 = System.currentTimeMillis();
        System.out.println("Deserialization time of " + nbUsers + " objects: " + Long.toString(cTime2 - cTime));
    }

    /**
     * Remove a file
     * @param filePath
     */
    private void removeFile(String filePath){
        Path p = Paths.get(filePath);
        if(Files.exists(p)) {
            try {
                Files.delete(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
