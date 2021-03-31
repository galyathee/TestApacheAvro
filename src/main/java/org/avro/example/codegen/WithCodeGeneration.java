package org.avro.example.codegen;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.file.SeekableFileInput;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import pkg.where.to.generate.files.User;

import java.io.File;
import java.io.IOException;

/**
 * This class uses the generated User and Builder objects from pkg.where.to.generate.files to:
 * - create User instances
 * - serialize User
 * - Deserialize User
 * - Print user definition
 */
public class WithCodeGeneration {

    public void showApacheAvroUseWithCodeGeneration(){
        // basic constructor
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);

        // alternate constructor
        User user2 = new User("Ben", 7, "red");

        // Construct via builder
        User user3 = User.newBuilder()
                .setName("Charlie")
                .setFavoriteColor("blue")
                .setFavoriteNumber(null)
                .build();

        // Serialize user1, user2 and user3 to disk
        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.class);
        try (DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userDatumWriter)){
            dataFileWriter.create(user1.getSchema(), new File("users.avro"));
            dataFileWriter.append(user1);
            dataFileWriter.append(user2);
            dataFileWriter.append(user3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialize Users from disk
        DatumReader<User> userDatumReader = new SpecificDatumReader<>(User.class);
        try(DataFileReader<User> dataFileReader = new DataFileReader<>(new SeekableFileInput(new File("users.avro")) , userDatumReader)) {
            User user = null;
            while (dataFileReader.hasNext()) {
                // Reuse user object by passing it to next(). This saves us from
                // allocating and garbage collecting many objects for files with
                // many items.
                user = dataFileReader.next(user);
                System.out.println(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
