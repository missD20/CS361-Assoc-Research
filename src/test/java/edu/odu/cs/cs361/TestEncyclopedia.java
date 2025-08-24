/**
 * 
 */
package edu.odu.cs.cs361;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author zeil
 *
 */
public class TestEncyclopedia {

    Topic alphabet;
    Topic commerce;
    Topic sailing;
    Topic mapping;
    Topic trading;

    @BeforeEach
    public void setup() {
        alphabet = new Topic("Alphabet", 15);
        commerce = new Topic("Commerce", 25);
        sailing = new Topic("Sailing", 36);
        mapping = new Topic("Mapping", 30);
        trading = new Topic("Trading", 35);
    }

    @Test
    public void testEncyclopediaConstructor() {
        Encyclopedia enc = new Encyclopedia();
        assertThat(enc.iterator().hasNext(), equalTo(false));

        Topic topic = new Topic("anything", 1);
        Set<Topic> requirements = enc.getReqts(topic);
        assertThat(requirements.size(), is(0));
        requirements = enc.getAllRequirements(topic);
        assertThat(requirements.size(), is(0));

    }

    @Test
    public void testEncyclopediaAddOneRequirement() {
        Encyclopedia enc = new Encyclopedia();
        enc.addPlanRequirement(commerce, alphabet);
        assertThat(enc.size(), equalTo(2));
        assertTrue(enc.containsTopic(alphabet));
        assertTrue(enc.containsTopic(commerce));
        assertFalse(enc.containsTopic(trading));

        assertThat(enc, containsInAnyOrder(alphabet, commerce));

        Set<Topic> requirements = enc.getReqts(commerce);
        assertThat(requirements.size(), is(1));
        assertThat(requirements.contains(alphabet), is(true));

        requirements = enc.getAllRequirements(commerce);
        assertThat(requirements.size(), is(1));
        assertThat(requirements.contains(alphabet), is(true));

        Set<Topic> enabled = enc.getEnabled(alphabet);
        assertThat(enabled.size(), is(1));
        assertThat(enabled.contains(commerce), is(true));
    }

    @Test
    public void testEncyclopediaAddRequirementChain() {
        Encyclopedia enc = new Encyclopedia();
        enc.addPlanRequirement(commerce, alphabet);
        enc.addPlanRequirement(trading, commerce);

        assertThat(enc.size(), equalTo(3));
        assertTrue(enc.containsTopic(alphabet));
        assertTrue(enc.containsTopic(commerce));
        assertTrue(enc.containsTopic(trading));

        assertThat(enc, containsInAnyOrder(alphabet, commerce, trading));

        Set<Topic> requirements = enc.getReqts(commerce);
        assertThat(requirements.size(), is(1));
        assertThat(requirements.contains(alphabet), is(true));

        requirements = enc.getReqts(trading);
        assertThat(requirements.size(), is(1));
        assertThat(requirements.contains(commerce), is(true));

        requirements = enc.getAllRequirements(trading);
        assertThat(requirements.size(), is(2));
        assertThat(requirements, containsInAnyOrder(alphabet, commerce));

        Set<Topic> enabled = enc.getEnabled(alphabet);
        assertThat(enabled.size(), is(1));
        assertThat(enabled.contains(commerce), is(true));

        enabled = enc.getEnabled(commerce);
        assertThat(enabled.size(), is(1));
        assertThat(enabled.contains(trading), is(true));

        enabled = enc.getEnabled(trading);
        assertThat(enabled.size(), is(0));
    }

    @Test
    public void EncyclopediaRemoveCourse() {

        Encyclopedia enc = new Encyclopedia();
        enc.addPlanRequirement(commerce, alphabet);
        enc.addPlanRequirement(trading, sailing);
        enc.addPlanRequirement(trading, mapping);
        enc.addPlanRequirement(mapping, commerce);
        enc.addPlanRequirement(sailing, commerce);

        enc.removeTopic(mapping);

        assertThat(enc.size(), equalTo(4));
        assertTrue(enc.containsTopic(alphabet));
        assertTrue(enc.containsTopic(commerce));
        assertTrue(enc.containsTopic(sailing));
        assertFalse(enc.containsTopic(mapping));
        assertTrue(enc.containsTopic(trading));

        Set<Topic> requirements = enc.getReqts(trading);
        assertThat(requirements.size(), is(1));

        Set<Topic> enabled = enc.getEnabled(commerce);
        assertThat(enabled.size(), is(1));
        assertThat(enabled.contains(sailing), is(true));
    }

    @Test
    public void EncyclopediaRead() throws IOException {
        Topic agriculture = new Topic("Agriculture", 1);
        Topic alphabet = new Topic("Alphabet", 1);
        Topic printing = new Topic("Printing", 2);
        Topic publishing = new Topic("Publishing", 3);

        String testIn = 
                "Printing2 Alphabet1\n" +
                "Publishing3 Printing2 Agriculture1\n"
                + "---";
        BufferedReader in = new BufferedReader(new StringReader(testIn));
        Encyclopedia enc = Planner.read(in);

        assertTrue(enc.containsTopic(agriculture));
        assertTrue(enc.containsTopic(printing));
        assertTrue(enc.containsTopic(publishing));
        assertTrue(enc.containsTopic(alphabet));

        assertThat(enc.size(), equalTo(4));

        assertThat(enc.getReqts(agriculture).size(), equalTo(0));
        assertThat(enc.getReqts(printing).size(), equalTo(1));
        assertThat(enc.getReqts(publishing).size(), equalTo(2));

    }

}
