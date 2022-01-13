
/*
* author: UGUR AKYEL
* std id: 20190808020
* */

import java.util.*;
import java.util.Map.Entry;

public class hw2_20190808020 {
    public static void main(String[] args){
        int[] WN = create_WN(Integer.parseInt(args[0]));
        int[][] WE = create_WE(Integer.parseInt(args[0]));
        Tree tree = new Tree(WN, WE);

        greedy_min_weight(tree, "0", tree.getRoot().getWeight());

        HashMap<String, Integer> paths = new HashMap<>();
        int minw = recursive_min_weight_allpaths(tree.root, "0", tree.root.getWeight(), paths);
        recursive_get_min_path(paths);

        dynamic_min_path(tree);


        System.out.println("\n\n-----------FOR DIFFERENT 3 INPUT SIZES SOLUTION------------");

        long[][] time_comp = new long[2][3];
        for (int i = 100, j = 0; i <= 10000; i=i*10, j++) {

            int[] WN1 = create_WN(i);
            int[][] WE1 = create_WE(i);
            Tree tree1 = new Tree(WN1, WE1);

            HashMap<String, Integer> paths1 = new HashMap<String, Integer>();
            long start_i = System.nanoTime();
            int minw_i = recursive_min_weight_allpaths(tree1.root, "0", tree1.root.getWeight(), paths1);
            recursive_get_min_path(paths1);
            long finish_i = System.nanoTime();
            long elapsedTimei = finish_i - start_i;
            time_comp[0][j] = elapsedTimei;

            start_i = System.nanoTime();
            dynamic_min_path(tree1);
            finish_i = System.nanoTime();
            elapsedTimei = finish_i - start_i;
            time_comp[1][j] = elapsedTimei;
        }

        System.out.println("--------------------------------------------------------------");
        System.out.printf("Input Size | %11d    | %11d    | %11d    | \n", 100, 1000, 10000);
        System.out.println("--------------------------------------------------------------");
        System.out.printf("Recursive  | %11d ns | %11d ns | %11d ns | \n", time_comp[0][0], time_comp[0][1],time_comp[0][2]);
        System.out.println("--------------------------------------------------------------");
        System.out.printf("Dynamic    | %11d ns | %11d ns | %11d ns | \n", time_comp[1][0], time_comp[1][1],time_comp[1][2]);;
        System.out.println("--------------------------------------------------------------");

    }

    public static int[] create_WN(int n){
        int[] result = new int[n];
        for(int i = 0; i < n; i++){
            result[i] = (int) (Math.random()*20);
            if(result[i] == 0)
                result[i] += 2;
        }
        return result;
    }

    public static int[][] create_WE(int n){
        int[][] result = new int[n][n];
        for (int[] row: result)
            Arrays.fill(row, 0);
        for (int i = 0; i < n; i++){
            int left = i * 2 + 1;
            int right = i * 2 + 2;
            if(left >= n)
                break;
            result[i][left] = (int) (Math.random() * 20);
            if (result[i][left] == 0) result[i][left] += 3;
            if (right >= n)
                break;
            result[i][right] = (int) (Math.random() * 20);
            if (result[i][right] == 0) result[i][right] += 3;
        }
        return result;
    }

    public static void print_1d_arr(int[] arr){
        for (int j : arr) System.out.print(j + " ");
        System.out.println();
    }

    public static void print_2d_arr(int[][] arr){
        for (int[] ints : arr) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.printf("%3d ", ints[j]);
            }
            System.out.println();
        }
    }

    public static void greedy_min_weight(Tree t, String s, int min_weight){
        int i = 0;
        StringBuilder sBuilder = new StringBuilder(s);
        while (t.getList(i).getRight_node() != null && t.getList(i).getLeft_node() != null){
            int l_weight = t.getList(i).getLeft_node().getWeight()
                    + t.getList(i).getLeft_edge().getWeight();
            int r_weight = t.getList(i).getRight_node().getWeight()
                    + t.getList(i).getRight_edge().getWeight();
            if (l_weight <= r_weight){
                min_weight += l_weight;
                sBuilder.append("-").append(t.getList(i).getLeft_node().getId());
                 i = i*2+1;
            }else {
                min_weight += r_weight;
                sBuilder.append("-").append(t.getList(i).getRight_node().getId());
                i = i*2+2;
            }
        }
        s = sBuilder.toString();
        System.out.println("GREEDY: Min total weight path includes nodes " + s + " with total weight " + min_weight);
    }

    public static int recursive_min_weight_allpaths(Node root, String s, int weight, HashMap<String, Integer> paths){

        if (root.getRight_node() == null && root.getLeft_node() == null){
            paths.put(s, weight);

            return weight;
        }else {
            if (root.getRight_node() != null){
                Node right_node = root.getRight_node();

                return Math.min(recursive_min_weight_allpaths(root.getLeft_node(), s + "-" + root.getLeft_node().getId(),
                        weight + root.getLeft_node().getWeight()
                                + root.getLeft_edge().getWeight(), paths),
                        recursive_min_weight_allpaths(root.getRight_node(), s + "-" + root.getRight_node().getId(),
                                weight + root.getRight_node().getWeight() + root.getRight_edge().getWeight(), paths));
            }
            return recursive_min_weight_allpaths(root.getLeft_node(), s + "-" + root.getLeft_node().getId(),
                    weight + root.getLeft_node().getWeight()
                            + root.getLeft_edge().getWeight(), paths);
        }
    }

    public static void recursive_get_min_path(HashMap<String, Integer> paths){
        int minValueInMap=(Collections.min(paths.values()));
        for (Entry<String, Integer> entry : paths.entrySet()) {
            if (entry.getValue()==minValueInMap) {
                System.out.println("RECURSIVE: Min total weight path includes nodes "
                        + entry.getKey() + " with total weight " + minValueInMap);
                break;
            }
        }
    }

    public static void dynamic_min_path(Tree t){
        int n = t.getList().length;
        int min = Integer.MAX_VALUE;
        String nodes = "";
        int[][] c = new int[n][n];

        for (int i = n-1; i >= n/2; i--) {
            int j = i;
            StringBuilder s = new StringBuilder("" + t.getList(j).getId());
            int weight = t.getList(j).getWeight();
            do{
                weight += t.getList(j).getParent().getWeight() + t.getList(j).getParent_edge().getWeight();
                c[i][t.getList(j).getParent().getId()] = weight;
                j = (j-1)/2;
                s.append("-").append(j);
            }while (t.getList(j).getParent() != null);

            min = Math.min(min, weight);
            if (min == c[i][0]){
                nodes = s.toString();
            }
        }
        System.out.println("DYNAMIC: Min total weight path includes nodes " + reverse(nodes) + " with total weight " + min);
    }

    public static String reverse(String in){
        String[] splits = in.split("-");
        StringBuilder s = new StringBuilder();
        for (int i = splits.length-1; i >= 0; i--) {
            s.append(splits[i]).append("-");
        }
        return s.substring(0, s.length()-1);
    }
}

class Tree{
    Node[] list;
    Node root;

    public Tree(int[] wn, int[][] we){
        this.list = node_list(wn);
        this.root = list[0];
        create_tree(getList(), we);
    }

    public void create_tree(Node[] nodes, int[][] edges){
        int n = nodes.length;

        for(int i = 0; i < n; i++){
            if (left(i) < n){
                Edge l_e = new Edge(edges[i][left(i)], nodes[i], nodes[left(i)]);
                nodes[i].setLeft_edge(l_e);
                nodes[i].setLeft_node(nodes[left(i)]);
                nodes[left(i)].setParent(nodes[i]);
                nodes[left(i)].setParent_edge(l_e);
            }
            if (right(i) < n){
                Edge r_e = new Edge(edges[i][right(i)], nodes[i], nodes[right(i)]);
                nodes[i].setRight_edge(r_e);
                nodes[i].setRight_node(nodes[right(i)]);
                nodes[right(i)].setParent(nodes[i]);
                nodes[right(i)].setParent_edge(r_e);
            }
        }

    }

    public Node[] node_list(int[] nodes){
        Node[] list = new Node[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            list[i] = new Node(i, nodes[i]);
        }
        return list;
    }

    public Node[] getList() {
        return list;
    }

    public Node getList(int a) {
        return list[a];
    }

    public void setList(Node[] list) {
        this.list = list;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public int left(int a){
        return a*2+1;
    }

    public int right(int a){
        return a*2+2;
    }

    public int parent(int a){
        return (a-1)/2;
    }
}

class Node{
    int id;
    int weight;
    Node right_node = null;
    Node left_node = null;
    Node parent = null;
    Edge parent_edge = null;
    Edge right_edge = null;
    Edge left_edge = null;

    public Node(int id, int weight) {
        this.id = id;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Node getRight_node() {
        return right_node;
    }

    public void setRight_node(Node right_node) {
        this.right_node = right_node;
    }

    public Node getLeft_node() {
        return left_node;
    }

    public void setLeft_node(Node left_node) {
        this.left_node = left_node;
    }

    public Edge getRight_edge() {
        return right_edge;
    }

    public void setRight_edge(Edge right_edge) {
        this.right_edge = right_edge;
    }

    public Edge getLeft_edge() {
        return left_edge;
    }

    public void setLeft_edge(Edge left_edge) {
        this.left_edge = left_edge;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Edge getParent_edge() {
        return parent_edge;
    }

    public void setParent_edge(Edge parent_edge) {
        this.parent_edge = parent_edge;
    }
}

class Edge{
    int weight;
    Node parent;
    Node child;

    public Edge(int weight, Node parent, Node child){
        this.weight = weight;
        this.parent = parent;
        this.child = child;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getChild() {
        return child;
    }

    public void setChild(Node child) {
        this.child = child;
    }
}
