import java.io.*;
import java.util.*;

// Day 12 - only part 1 is implemented.
public class Ex12 {
  static Set<String> visitedPaths;

  public static void main(String[] args) throws Exception {
    File f = new File("input.txt");
    Scanner in = new Scanner(f);

    Map<String, Node> map = new HashMap<>();
    Node start = new Node("e");

    int pathCount = 0;

    while (in.hasNext()) {
      String line = in.nextLine();
      String[] delim = line.split("-");
      Node node1;
      Node node2;

      if (!map.containsKey(delim[0])) {
        node1 = new Node(delim[0]);
        map.put(delim[0], node1);

        if (node1.isStart)
          start = node1;
      }
      else {
        node1 = map.get(delim[0]);
      }

      if (!map.containsKey(delim[1])) {
        node2 = new Node(delim[1]);
        map.put(delim[1], node2);

        if (node2.isStart)
          start = node2;
      }
      else {
        node2 = map.get(delim[1]);
      }

      node1.connectNode(node2);
      node2.connectNode(node1);
    }

    map.forEach((k, v) -> System.out.println(k + " is connected to: " + v.getLabels()));
    ArrayList<Node> nextNodes = start.getConnection();
    visitedPaths = new HashSet<>();

    for (int i = 0 ; i < nextNodes.size(); i++) {
      String path = start.getLabel() + " ";
      pathCount = countPaths(nextNodes.get(i), pathCount, path);
    }

    System.out.println("Count: " + pathCount);
  }

  private static int countPaths(Node node, int count, String path) {
    int paths = count;
    Node thisNode = node;

    if (node.getLabel().equals("end")) {
      path += node.getLabel();

      if (visitedPaths.contains(path)) {
        return paths;
      }
      else {
        paths++;
        visitedPaths.add(path);
        System.out.println(path);

        return paths;
      }
    }
    else if (node.getLabel().equals("start")) {
      return paths;
    }
    else if (node.isSmallCave && countInstances(node.getLabel(), path) == 1) {
      System.out.println("Adding: " + node.getLabel() + " Not including: " + path);
      return paths;
    }
    else {
      String newPath = path;
      newPath += node.getLabel();
      newPath += " ";

      for (int i = 0; i < thisNode.getConnection().size(); i++) {
        paths = countPaths(thisNode.getConnection().get(i), paths, newPath);
      }
    }

    return paths;
  }

  private static int countInstances(String label, String path) {
    String[] arr = path.split(" ");
    int count = 0;

    for (String str : arr) {
      if (str.equals(label))
        count++;
    }

    return count;
  }
}

class Node {
  public boolean isSmallCave;
  public boolean isStart;
  public boolean isEnd;
  private ArrayList<Node> connectedNodes;
  private ArrayList<String> labels;
  private String label;

  public Node(String str) {
    this.label = str;
    this.connectedNodes = new ArrayList<>();
    this.labels = new ArrayList<String>();

    if (str.equals("start")) {
      this.isStart = true;
      this.isSmallCave = false;
      this.isEnd = false;
    }
    else if (str.equals("end")) {
      this.isEnd = true;
      this.isStart = false;
      this.isSmallCave = false;
    }
    else {
      this.isStart = false;
      this.isEnd = false;


      if (Character.isLowerCase(str.charAt(0)))
        this.isSmallCave = true;
      else
        this.isSmallCave = false;
    }
  }

  public String getLabel() {
    return this.label;
  }

  public void connectNode(Node node) {
    this.connectedNodes.add(node);
    this.labels.add(node.getLabel());
  }

  public ArrayList<Node> getConnection() {
    return this.connectedNodes;
  }

  public ArrayList<String> getLabels() {
    return this.labels;
  }
}
