package edu.odu.cs.cs361;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Encyclopedia implements Iterable<Topic> {

    /**
     * A map from each topic P in allTopics to those topics
     * that are an immediate requirement of P.
     */
    private Map<Topic, Set<Topic>> reqts;

    /**
     * A map from each topic P in allTopics to those topics
     * that P is an immediate requirement of.
     */
    private Map<Topic, Set<Topic>> isReqtFor;

    /**
     * Check to see if a topic already exists with this
     * name. If not, create one. Return the created topic or
     * existing one that was found.
     */
    private void addIfMissing(Topic topic) {
        Set<Topic> required = reqts.get(topic);
        if (required == null) {
            reqts.put(topic, new HashSet<>());
            isReqtFor.put(topic, new HashSet<>());
        }
    }

    /**
     * Create a new encyclopedia.
     */
    public Encyclopedia() {
        reqts = new HashMap<>();
        isReqtFor = new HashMap<>();
    }

    /**
     * Provide access to the topics in the encyclopedia.
     */
    public Iterator<Topic> iterator() {
        return reqts.keySet().iterator();
    }

    /**
     * @return number of topics in the encyclopedia.
     */
    public int size() {
        return reqts.size();
    }

    /**
     * Adds a plan requirement to the encyclopedia. This is a pair of topics,
     * possibly never seen before, such that one topic must have been researched
     * before research can begin on the other.
     * 
     * Adding the same requirement more than once may have unpredictable results.
     *
     * @param topic1             a topic, possibly never seen before
     * @param requiredPriorTopic another topic that must be researched
     *                           before topic1. Might not have been
     *                           seen before.
     */
    public void addPlanRequirement(Topic topic1, Topic requiredPriorTopic) {
        addIfMissing(requiredPriorTopic);
        addIfMissing(topic1);
        //*** Add your code below

        // Edge : requiredPriorTopic -> topic1

        // Update 'reqts': topic1 requires requiredPriorTopic
        reqts.get(topic1).add(requiredPriorTopic);

        // Update 'isReqtFor': requiredPriorTopic is required FOR topic1
        isReqtFor.get(requiredPriorTopic).add(topic1);
    }

    /**
     * Remove a topic from the encyclopedia, including any
     * plan requirements in which it participates.
     */
    public void removeTopic(Topic topic) {
        //*** Add your code below
        //Fisrt Find 'enabled' topics (those for which 'topic' was a prerequisite)
        Set<Topic> enabledTopics = isReqtFor.get(topic);

        if (enabledTopics != null) {
            // For each unlocked topic, remove 'topic' from its prerequisite list.
            for (Topic enabled : enabledTopics) {
                // We are updating the list of prerequisites (reqts) for the 'enabled' topic
                Set<Topic> requirementsOfEnabled = reqts.get(enabled);
                if (requirementsOfEnabled != null) {
                    requirementsOfEnabled.remove(topic);
                }
                // Note: We do not remove 'enabled' from isReqtFor.get(topic),
                // because 'topic' will be removed immediately (after step 3).
            }
        }

        // Remove the subject itself from both cards (which completes the edge removal)
        reqts.remove(topic);
        isReqtFor.remove(topic);
    }

    /**
     * See if a topic is already in the catalog.
     *
     * @param topic a topic whose name is to be searched for
     * @return true iff topic is in the catalog
     */
    boolean containsTopic(Topic topic) {
        return reqts.get(topic) != null;
    }

    /**
     * Gets the set of topics that
     * are immediate requirements ofThisTopic.
     *
     * Important: This operation must be faster than
     * O(size()). Sequential searches are not
     * acceptable.
     */
    Set<Topic> getReqts(Topic ofThisTopic) {
        Set<Topic> result = reqts.get(ofThisTopic);
        return (result == null) ? new HashSet<>() : result;
    }

    /**
     * Gets all requirements, direct and indirect, that must be
     * researched before ofThisTopic can be researched.
     *
     * Important: This operation must be no slower than
     * O(size()). Sequential searches are not
     * acceptable.
     */
    Set<Topic> getAllRequirements(Topic ofThisTopic) {
        Set<Topic> result = new HashSet<>();
        //*** Add your code below
        // Use a Queue for Breadth First Search (BFS)
        Queue<Topic> queue = new LinkedList<>();

        // Initialize the queue with immediate prerequisites
        Set<Topic> immediateReqts = reqts.get(ofThisTopic);

        if (immediateReqts != null) {
            for (Topic reqt : immediateReqts) {
                if (!result.contains(reqt)) {
                    result.add(reqt);
                    queue.offer(reqt);
                }
            }
        }

        // Iteratively traverse dependencies (BFS)
        while (!queue.isEmpty()) {
            Topic current = queue.poll();

            // We find the prerequisites of the current topic
            Set<Topic> nextReqts = reqts.get(current);

            if (nextReqts != null) {
                for (Topic nextReqt : nextReqts) {
                    // If this prerequisite has not been explored
                    if (!result.contains(nextReqt)) {
                        result.add(nextReqt);
                        queue.offer(nextReqt); // Add it to explore your own prerequisites
                    }
                }
            }
        }
        
        return result;
    }

    /**
     * Gets the topics that byThisTopic is an immediate requirement for.
     **
     * Important: This operation must be no slower than
     * O(size()).
     */
    Set<Topic> getEnabled(Topic byThisTopic) {
        return isReqtFor.get(byThisTopic);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Encyclopedia) {
            Encyclopedia enc = (Encyclopedia) obj;
            return reqts.equals(enc.reqts);
        } else
            return false;
    }

}
