import com.google.gson.Gson;


import javax.print.DocFlavor;
import java.io.*;
import java.util.*;


public class Graph {
    @Override
    public String toString() {
        return "Graph{" +
                "nodes=" + nodes +
                ", links=" + links +
                '}';
    }

    private LinkedList<Node>  nodes;
    private LinkedList<Link>  links;


    Graph(){
        nodes=new LinkedList<>();
        links=new LinkedList<>();
    }
    private class Node{
        String id;
        int group;


        public Node(String id, int group) {
            this.id = id;
            this.group = group;
        }
    }
    private class Link{
        String source,target;
        double value;

        @Override
        public String toString() {
            return "Link{" +
                    "source='" + source + '\'' +
                    ", target='" + target + '\'' +
                    ", value=" + value +
                    '}';
        }

        public Link(String source, String target, double value) {
            this.source = source;
            this.target = target;
            this.value=value;
        }
    }
    void addEdge(String source, String destination,double value, boolean bidirectional){
        if(getNode(source)==null || getNode(destination)==null){
            if(getNode(source)==null )addVertex(source,1);
            if (getNode(destination)==null)addVertex(destination,1);
        }
        if(!links.contains(new Link(source,destination,value))){
            links.add(new Link(source,destination,value));
            if (bidirectional)addEdge(destination,source,value,!bidirectional);
        }
    }


    void addVertex(String id,int group) {
        if(!nodes.contains(new Node(id, group)))nodes.add(new Node(id, group));
    }


    int countVert(){
        return nodes.size();
    }


    public int countEdges(){
        return links.size();
    }



    static Graph deserialize(String path){
        Gson gson=new Gson();
        StringBuilder str= new StringBuilder();
        try {
            BufferedReader in=new BufferedReader(new FileReader(path));
            String str2=in.readLine();
            while (str2!=null){
                str.append(str2);
                str2=in.readLine();
            }
        }catch (Exception e){


        }
        return gson.fromJson(str.toString(),Graph.class);
    }
    void serialize(String path) throws IOException {
        Gson gson = new Gson();
        String str=gson.toJson(this);


        try (
                BufferedWriter out=new BufferedWriter(new FileWriter(path))) {
            out.write(str);
        }
    }
    boolean isEmpty(){
        return nodes.isEmpty();
    }


    private LinkedList<Node> getVertices() {
        return nodes;
    }
    Node getNode(String node){
        for (Node n:nodes) {
            if (n.id.equals(node))return n;
        }
        return null;
    }



    LinkedList<Node> getNeighbor(Node n){
        LinkedList<Node>ls=new LinkedList<>();
        for(Link l:links){
            if(l.source.equals(n.id))ls.add(getNode(l.target));
        }
        return ls;
    }
    Link getLink(Node s,Node t){
        for(Link l:links){
            if(l.source.equals(s.id) && l.target.equals(t.id))return l;
        }
        return null;
    }

    void setBidirectionalGraph(){
        LinkedList l=new LinkedList<>();
        for(Link link : links)
            l.add(new Link(link.target,link.source,link.value));
        links.addAll(l);
    }

}

