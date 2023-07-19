package org.snpeff.snpEffect.testCases.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.snpeff.SnpEff;
import org.snpeff.interval.Gene;
import org.snpeff.interval.Genome;
import org.snpeff.interval.Transcript;
import org.snpeff.snpEffect.Config;
import org.snpeff.snpEffect.SnpEffectPredictor;
import org.snpeff.snpEffect.factory.SnpEffPredictorFactoryGtf22;

/**
 * Test case
 */
public class TestCasesIntegrationZzz2 extends TestCasesIntegrationBase {


    @Test
    public void test_01_parse_tags_from_gtf() {
        // Test case: Parse GTF with 'tag' and check the number of tags for different transcripts
        String genome = "test_GRCh38.mane.1.0.ensembl.chr21";
        Config config = new Config(genome);
        // Parse the GTF file and create a SnpEffPredictor object
        var spf = new SnpEffPredictorFactoryGtf22(config);
        var sep = spf.create();
        // Count tags parsed directly from GTF file
        Map<String, Integer> tags = new HashMap<>();
        for(Gene g : sep.getGenome().getGenes()) {
            for(Transcript tr: g) {
                String[] trtags = tr.getTags();
                for(String tag : trtags)
                    if(tag!= null) tags.put(tag, tags.getOrDefault(tag, 0) + 1); // Increment tag count
            }
        }
        // Check transcript's tag counts
        assertEquals(213, tags.get("MANE_Select"));
        assertEquals(13, tags.get("alternative_5_UTR"));
        assertEquals(13, tags.get("CAGE_supported_TSS"));
        assertEquals(2, tags.get("NAGNAG_splice_site"));
    }

    @Test
    public void test_02_load() {
        String genome = "test_GRCh38.mane.1.0.ensembl.chr21";
        SnpEffectPredictor sep = loadSnpEffectPredictor(genome, false);

        // Count tags fro the loaded genome
        Map<String, Integer> tags = new HashMap<>();
        for(Gene g : sep.getGenome().getGenes()) {
            for(Transcript tr: g) {
                String[] trtags = tr.getTags();
                for(String tag : trtags)
                    if(tag!= null) tags.put(tag, tags.getOrDefault(tag, 0) + 1); // Increment tag count
            }
        }
        // Check transcript's tag counts
        assertEquals(213, tags.get("MANE_Select"));
        assertEquals(13, tags.get("alternative_5_UTR"));
        assertEquals(13, tags.get("CAGE_supported_TSS"));
        assertEquals(2, tags.get("NAGNAG_splice_site"));
    }

    @Test
    public void test_03_filter_keep_tags() {
        // Command line to filter (i.e. only keep) transcripts having 'MANE_Select'
        List<String> args = new LinkedList<>();
        args.add("-genome");
        args.add("test_GRCh38.mane.1.0.ensembl.chr21");
        args.add("-tag");
        args.add("MANE_Select");
        SnpEff snpeff = runCmd(args);
        Genome genome = snpeff.getConfig().getSnpEffectPredictor().getGenome();
        // Check that all transcripts have 'MANE_Select' tag
        for(Gene g : genome.getGenes()) {
            for(Transcript tr: g) {
                assertTrue(tr.hasTag("MANE_Select"), "Transcript has no MANE_Select tag: " + tr.getId());
            }
        }
    }

    @Test
    public void test_04_filter_out_tags() {
        // TODO: Command line to filter out (i.e. remove) transcripts having 'tag'

    }

}
