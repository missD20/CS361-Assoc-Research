package edu.odu.cs.cs361;

public class Topic implements Comparable<Topic> {

    private String name;
    private int level;

    public Topic() {
        name = "";
        level = 0;
    }

    public Topic(String namePlusLevel) {
        int k = 0;
        while (Character.isAlphabetic(namePlusLevel.charAt(k))) {
            ++k;
        }
        name = namePlusLevel.substring(0, k);
        level = Integer.parseInt(namePlusLevel.substring(k));
    }

    public Topic(String theName, int theLevel) {
        name = theName;
        level = theLevel;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the researchCost
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param researchCost the researchCost to set
     */
    public void setLevel(int researchCost) {
        this.level = researchCost;
    }

    @Override
    public int compareTo(Topic topic) {
        int c = name.compareTo(topic.name);
        if (c != 0)
            return c;
        return level - topic.level;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Topic) {
            Topic topic = (Topic) obj;
            return name.equals(topic.name) && level == topic.level;
        } else
            return false;
    }

    @Override
    public String toString() {
        return name + " " + level;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + 4001 * level;
    }

}
