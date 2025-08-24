package edu.odu.cs.cs361;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Planner {

    /**
     * Read an encyclopedia from an input source.
     *
     * Input is repeated lines of
     *
     * topic: prior topic 1, prior topic 2, ... , cost
     *
     * Topic names may be any mixture of alphanumeric characters and
     * blanks, but must have at least one non-blank character, must not begin
     * with a blank, and must not include the characters ',' or ':'.
     *
     * Leading blanks may appear before a topic name in the input, but are
     * ignored.
     *
     * The end of input is signaled by the end of the input stream or by a
     * line consisting solely of the string "---". (Three hyphens.)
     *
     * @param rdr the input source
     * @throws IOException
     */
    public static Encyclopedia read(BufferedReader input) throws IOException {
        Encyclopedia enc = new Encyclopedia();
        String line = input.readLine();
        while (line != null && (!line.equals("---"))) {
            Scanner lineIn = new Scanner(line);
            if (lineIn.hasNext()) {
                String nameToResearch = lineIn.next();
                Topic topicToResearch = new Topic(nameToResearch);
                while (lineIn.hasNext()) {
                    String requiredTopicName = lineIn.next();
                    Topic requiredTopic = new Topic(requiredTopicName);
                    enc.addPlanRequirement(topicToResearch, requiredTopic);
                }
            }
            lineIn.close();

            line = input.readLine();
        }
        return enc;
    }

    // Look for topics that have no requirements. Print and remove them.
    //
    // We will later study this algorithm in connection with graphs.
    // It is called a "partial sort".
    //
    void removeAndListAvailTopics(Encyclopedia encyclopedia,
            Set<Topic> candidates) {
        Set<Topic> researchable = new HashSet<>();
        for (Topic topic : candidates) {
            Set<Topic> required = encyclopedia.getReqts(topic);
            if (required.size() == 0) {
                researchable.add(topic);
            }
        }
        if (researchable.size() == 0) {
            System.out.println("No research plan is possible.");
            System.exit(1);
        }
        for (Topic topic : researchable) {
            encyclopedia.removeTopic(topic);
            candidates.remove(topic);
        }

        List<Topic> names = new ArrayList<>(researchable);
        Collections.sort(names);
        for (Topic name : names) {
            System.out.println(name);
        }
    }

    void planTheResearch(Encyclopedia encyclopedia, Topic goal) {
        Set<Topic> candidates = encyclopedia.getAllRequirements(goal);
        candidates.add(goal);
        while (candidates.size() > 0) {
            removeAndListAvailTopics(encyclopedia, candidates);
        }
    }

    public void run(BufferedReader in) throws IOException {
        String goalName = in.readLine().trim();
        Encyclopedia encyclopedia = read(in);
        in.close();
        Topic goal = new Topic(goalName);
        planTheResearch(encyclopedia, goal);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in;
        if (args.length > 0) {
            in = new BufferedReader(new FileReader(args[0]));
        } else {
            in = new BufferedReader(new InputStreamReader(System.in));
        }
        new Planner().run(in);
        in.close();
    }
}