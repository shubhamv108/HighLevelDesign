package SystemDesign.ConsistentHashing;

import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashing {
    private final SortedMap<Integer, String> circle;  // Hash ring (virtual nodes)
    private final int numberOfReplicas;               // Number of virtual nodes per physical node
    private final int numberOfNodes;                  // Number of physical nodes

    // Constructor
    public ConsistentHashing(int numberOfReplicas, int numberOfNodes) {
        this.numberOfReplicas = numberOfReplicas;
        this.numberOfNodes = numberOfNodes;
        this.circle = new TreeMap<>();  // Sorted map to store virtual nodes
        buildRing();
    }

    // Build the hash ring with virtual nodes for each physical node
    private void buildRing() {
        for (int i = 0; i < numberOfNodes; ++i) {
            String node = "Node" + i;
            addNode(node);
        }
    }

    // Add virtual nodes to the ring for a physical node
    private void addNode(String node) {
        for (int i = 0; i < numberOfReplicas; ++i) {
            // We append the replica index to ensure each virtual node has a unique hash
            int hash = getHash(node + "-" + i);
            circle.put(hash, node);  // Add the virtual node to the ring
        }
    }

    // Remove a physical node from the ring (all its replicas will be removed)
    public void removeNode(String node) {
        for (int i = 0; i < numberOfReplicas; ++i) {
            int hash = getHash(node + "-" + i);
            circle.remove(hash);
        }
    }

    // Get the node for a given key
    public String getNode(String key) {
        if (circle.isEmpty()) {
            return null;  // No nodes available
        }

        // Hash the key and find the closest virtual node
        int hash = getHash(key);
        SortedMap<Integer, String> tailMap = circle.tailMap(hash);

        // If the tail map is empty, we need to wrap around and use the first node in the ring
        if (tailMap.isEmpty()) {
            return circle.get(circle.firstKey());
        } else {
            return tailMap.get(tailMap.firstKey());
        }
    }

    // Hash function to map a string to an integer
    private int getHash(String key) {
        return key.hashCode();  // You can replace this with a better hash function (e.g., MurmurHash)
    }

    // For debugging purposes, print the hash ring
    public void printRing() {
        System.out.println("Hash Ring: " + circle);
    }

    public static void main(String[] args) {
        // Example usage
        ConsistentHashing consistentHashing = new ConsistentHashing(3, 5);  // 3 replicas per node, 5 physical nodes

        // Adding new nodes
        consistentHashing.addNode("Node5");

        // Get the node for a key
        System.out.println("Node for key 'user123': " + consistentHashing.getNode("user123"));

        // Remove a node
        consistentHashing.removeNode("Node2");

        // Print the current ring
        consistentHashing.printRing();
    }
}
