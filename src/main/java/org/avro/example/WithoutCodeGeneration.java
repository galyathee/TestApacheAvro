package org.avro.example;

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

/**
 * This class uses the Parser object to create an in-memory representation of the src/main/avro/user.asvc definition:
 * - create User instances
 * - serialize User
 * - Deserialize User
 * - Print user definition
 */
public class WithoutCodeGeneration {

    public void showApacheAvroUseWithoutCodeGeneration(){
        // use a Parser to read schema definition and generate a Shema object
        Schema schema = null;
        try {
            schema = new Schema.Parser().parse(new File("./src/main/avro/user.avsc"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // use the schema to create users
        GenericRecord user1 = new GenericData.Record(schema);
        user1.put("name", "Alan");
        user1.put("favorite_number", 1940);
        // Leave favorite color null

        GenericRecord user2 = new GenericData.Record(schema);
        user2.put("name", "Marie");
        user2.put("favorite_number", 1867);
        user2.put("favorite_color", "gray");

        // Serialize user1, user2 to disk
        File file = new File("users_w.avro");
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter);
        try {
            dataFileWriter.create(schema, file);
            dataFileWriter.append(user1);
            dataFileWriter.append(user2);
            dataFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialize users from disk
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        DataFileReader<GenericRecord> dataFileReader = null;
        try {
            dataFileReader = new DataFileReader<>(file, datumReader);
            GenericRecord user = null;
            while (dataFileReader.hasNext()) {
                // Reuse user object by passing it to next(). This saves us from
                // allocating and garbage collecting many objects for files with
                // many items.
                user = dataFileReader.next(user);
                System.out.println(user);
            }
            dataFileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
