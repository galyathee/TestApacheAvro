package org.avro;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class uses the generated User and Builder objects from pkg.where.to.generate.files to:
 * - create 10, 100, 1000, 10 000, 100 000 User instances with different constructors
 * - serialize User + measure serialization time
 * - Deserialize User + measure deserialization time
 */
public class BenchmarkWithCodeGeneration {

    public void benchmarkAvroWithCodeGeneration(int nbUsers) {

        removeFile("users.avro");

        // Serialize users to disk
        long cTime = System.currentTimeMillis();
        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.class);
        try (DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userDatumWriter)){
            User user = new User();
            user.setName("Alyssa_0");
            user.setFavoriteNumber(0);
            user.setFavoriteColor("blue");
            dataFileWriter.create(user.getSchema(), new File("users.avro"));
            for (int i = 1; i <= nbUsers; i++) {
                // basic constructor
                user = new User();
                user.setName("Alyssa_"+i);
                user.setFavoriteNumber(i);
                user.setFavoriteColor("blue");
                dataFileWriter.append(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long cTime2 = System.currentTimeMillis();
        System.out.println("Generation + Serialization time of " + nbUsers + " objects: " + Long.toString(cTime2 - cTime));

        // Serialize users to disk
        // creation with alternate constructor
        removeFile("users.avro");
        cTime = System.currentTimeMillis();
        userDatumWriter = new SpecificDatumWriter<>(User.class);
        try (DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userDatumWriter)){
            User user = new User("Alyssa_0", 0, "blue");
            dataFileWriter.create(user.getSchema(), new File("users.avro"));
            for (int i = 1; i <= nbUsers; i++) {
                // classical constructor
                user = new User("Alyssa_"+i, i, "blue");
                dataFileWriter.append(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cTime2 = System.currentTimeMillis();
        System.out.println("[with alternate constructor] Generation + Serialization time of " + nbUsers + " objects: " + Long.toString(cTime2 - cTime));

        // Serialize users to disk
        // create User objects via builder
        removeFile("users.avro");
        cTime = System.currentTimeMillis();
        userDatumWriter = new SpecificDatumWriter<>(User.class);
        try (DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userDatumWriter)){
            User user = User.newBuilder()
                    .setName("Alyssa_0")
                    .setFavoriteColor("blue")
                    .setFavoriteNumber(0)
                    .build();

            dataFileWriter.create(user.getSchema(), new File("users.avro"));
            for (int i = 1; i <= nbUsers; i++) {
                // classical constructor
                user = User.newBuilder()
                        .setName("Alyssa_"+i)
                        .setFavoriteColor("blue")
                        .setFavoriteNumber(i)
                        .build();
                dataFileWriter.append(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        cTime2 = System.currentTimeMillis();
        System.out.println("[via Builder] Generation + Serialization time of " + nbUsers + " objects: " + Long.toString(cTime2 - cTime));

        // Deserialize Users from disk
        cTime = System.currentTimeMillis();
        DatumReader<User> userDatumReader = new SpecificDatumReader<>(User.class);
        try(DataFileReader<User> dataFileReader = new DataFileReader<>(new SeekableFileInput(new File("users.avro")) , userDatumReader)) {
            User user = null;
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