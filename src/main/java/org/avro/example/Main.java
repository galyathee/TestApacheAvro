package org.avro.example;

import org.avro.BenchmarkWithCodeGeneration;
import org.avro.BenchmarkWithoutCodeGeneration;

public class Main {

    public static void main(String[] args) {
        // Use of Apache Avro with code generation
        WithCodeGeneration wcg = new WithCodeGeneration();
        wcg.showApacheAvroUseWithCodeGeneration();

        // Use of Apache Avro withou code generation
        WithoutCodeGeneration wicg = new WithoutCodeGeneration();
        wicg.showApacheAvroUseWithoutCodeGeneration();

        // [based on generate classes] Test User creation + Serialization time
        BenchmarkWithCodeGeneration bwcg = new BenchmarkWithCodeGeneration();
        bwcg.benchmarkAvroWithCodeGeneration(10);
        bwcg.benchmarkAvroWithCodeGeneration(100);
        bwcg.benchmarkAvroWithCodeGeneration(1000);
        bwcg.benchmarkAvroWithCodeGeneration(10000);
        bwcg.benchmarkAvroWithCodeGeneration(100000);
        bwcg.benchmarkAvroWithCodeGeneration(1000000);
        bwcg.benchmarkAvroWithCodeGeneration(10000000);

        // [based on dynamic generation] Test User creation + Serialization time
        BenchmarkWithoutCodeGeneration bwicg = new BenchmarkWithoutCodeGeneration();
        bwicg.benchmarkAvroUseWithoutCodeGeneration(10);
        bwicg.benchmarkAvroUseWithoutCodeGeneration(100);
        bwicg.benchmarkAvroUseWithoutCodeGeneration(1000);
        bwicg.benchmarkAvroUseWithoutCodeGeneration(10000);
        bwicg.benchmarkAvroUseWithoutCodeGeneration(100000);
        bwicg.benchmarkAvroUseWithoutCodeGeneration(1000000);
        bwicg.benchmarkAvroUseWithoutCodeGeneration(10000000);
    }
}
