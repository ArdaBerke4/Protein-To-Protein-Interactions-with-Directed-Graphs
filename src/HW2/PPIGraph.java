package HW2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class PPIGraph {

    private HashMap<String, ArrayList<String>> adjList;

    private HashMap<String, Protein> proteins;
    private ArrayList<Edge> edges;


    public PPIGraph() {
        this.proteins = new HashMap<>();
        this.edges = new ArrayList<>();
        this.adjList = new HashMap<>();

    }

    public void loadProteins(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) continue;

                String[] parts = line.split("\t");

                String id = parts[0];
                String name = parts[1];
                int size = Integer.parseInt(parts[2]);
                String annotation = parts[3];

                proteins.put(id, new Protein(id, name, size, annotation));
                adjList.put(id, new ArrayList<>());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void clear() {
        proteins.clear();
        edges.clear();
        adjList.clear();
    }





    public void loadInteractions(String filePath, double threshold) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            int count = 0;

            System.out.print("Interaction yukleniyor...");

            while ((line = br.readLine()) != null) {

                if (line.startsWith("protein1")) continue;

                String[] parts = line.split("\\s+");

                String p1 = parts[0];
                String p2 = parts[1];
                int rawScore = Integer.parseInt(parts[2]);

                double score = rawScore / 1000.0;

                if (score < threshold) continue;

                edges.add(new Edge(p1, p2, score));
                edges.add(new Edge(p2, p1, score));
                adjList.get(p1).add(p2);
                adjList.get(p2).add(p1);

                count++;

                if (count % 10000 == 0) {
                    System.out.print("\r" + count + " interaction yüklendi...");
                    System.out.flush();
                }
            }
//
            System.out.println("\nInteraction yükleme tamamlandı.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean hasProtein(String id) {
        return proteins.containsKey(id);
    }

    public boolean hasInteraction(String p1, String p2) {
        for (Edge e : edges) {
            if (e.getSource().equals(p1) && e.getTarget().equals(p2)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getNeighbors(String id) {
        return adjList.getOrDefault(id, new ArrayList<>());
    }


    public int getVertexCount() {
        return proteins.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }

    public double getAverageDegree() {
        if (proteins.isEmpty()) return 0;
        return edges.size() / (double) proteins.size();
    }

    public Protein getProtein(String id) {
        return proteins.get(id); // yoksa null döner
    }



    //ters yönde de bağlantı var mı diye bakıyor
    public double getReciprocity() {
        int reciprocitycount = 0;
        for (Edge e : edges) {
            String source = e.getSource();
            String target = e.getTarget();
            if (hasInteraction(target, source)) {
                reciprocitycount++;
            }
        }
        if (edges.isEmpty()) return 0.0;
        return (double) reciprocitycount / edges.size();
    }

    private int bfsDistance(String start) {
        Map<String, Integer> distance = new HashMap<>();
        Queue<String> queue = new LinkedList<>();

        distance.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();//queuenin başını alıyoruz

            for (String neighbor : getNeighbors(current)) {
                if (!distance.containsKey(neighbor)) {
                    distance.put(neighbor, distance.get(current) + 1); //current 0 uzaklıktaysa komşu 1
                    queue.add(neighbor); //neighbor da kendi komşularına bakacak
                }
            }

        }
        int max = 0;
        for (int d : distance.values()) {
            if (d > max) max = d;
        }

        return max;


    }

    private int bfsMaxDistanceFromStart(String start) {
        Map<String, Integer> distance = new HashMap<>();// uzaklık için
        Queue<String> q = new LinkedList<>();

        distance.put(start, 0);
        q.add(start);//en baştan başlıyoruz

        while (!q.isEmpty()) {//elemanlar bitene akdar
            String current = q.poll();
            for (String nb : getNeighbors(current)) {//currentin tüm komşularına bakıyoruz
                if (!distance.containsKey(nb)) {//zaten gezmemişsek
                    distance.put(nb, distance.get(current) + 1);//uzaklığı +1 yapıyoruz çünkü komşu başlangıçtan 1 uzaktadır
                    q.add(nb);
                }
            }
        }
        int max = 0;
        for (int i : distance.values()) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    public int getDiameter() {
        int diameter = 0;
        int done = 0;
        int total = proteins.size();
        System.out.print("Diameter hesaplaniyor...");
        for (String id : proteins.keySet()) {
            int far = bfsMaxDistanceFromStart(id);
            if (far > diameter) {
                diameter = far;
            }
            done++;
            if (done % 500 == 0) {
                System.out.print("\rDiameter ilerleme: " + done + " / " + total);
                System.out.flush();
            }
        }
        System.out.println("\nDiameter hesaplandi.");
        return diameter;
    }




    private void dfsHelper(String current , Set<String> visited){
        visited.add(current);
        System.out.println(current);
        for (String neighbors : getNeighbors(current)){
            if(!visited.contains(neighbors)){
                dfsHelper(neighbors,visited);
            }
        }
    }

    public void dfsTraverse(String start){
        if(!proteins.containsKey(start)){
            System.out.println("protein bulunamadı");
            return;
        }
        Set<String> visited = new HashSet<>();
        dfsHelper(start,visited);
    }

    public void bfsTraverse(String start){
        if(!proteins.containsKey(start)){
            System.out.println("protein bulunamadı");
            return;
        }

        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        visited.add(start);
        queue.add(start);


        while(!queue.isEmpty()){
            String current = queue.poll();
            System.out.println(current);

            for (String neighbors : getNeighbors(current)){
                if(!visited.contains(neighbors)){
                    visited.add(neighbors);
                    queue.add(neighbors);

                }
            }
        }

    }
}