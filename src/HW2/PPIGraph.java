package HW2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class PPIGraph {


    private HashMap<String, Protein> proteins;
    private ArrayList<Edge> edges;


    public PPIGraph() {
        this.proteins = new HashMap<>();
        this.edges = new ArrayList<>();
    }
    public void loadProteins(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader("9606.protein.info.v12.0.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("string_protein_id")) continue;  // header

                String[] parts = line.split("\t");

                String id = parts[0];
                String name = parts[1];

                Protein p = new Protein(id, name);
                proteins.put(id, p);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadInteractions(String filePath, double threshold) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = br.readLine()) != null) {

                if (line.startsWith("protein1")) continue; // header atla

                String[] parts = line.split(" ");

                String p1 = parts[0];
                String p2 = parts[1];
                int rawScore = Integer.parseInt(parts[2]);

                double score = rawScore / 1000.0;

                // threshold'tan düşükse ekleme
                if (score < threshold) continue;

                // Edge ekle
                Edge e = new Edge(p1, p2, score);
                edges.add(e);
            }
        }
        catch (Exception e) {
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

    public ArrayList<String> getNeighbors(String id){
        ArrayList<String> list = new ArrayList<>();

        for (Edge e :edges){
            if(e.getSource().equals(id)){
                list.add(e.getTarget());
            }
        }

        return list;
    }
    public int getVertexCount() {
        return proteins.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }
    public double getAverageDegree() {
        if (proteins.isEmpty()) return 0;
        return (edges.size() * 2.0) / proteins.size();
    }
//ters yönde de bağlantı var mı diye bakıyor
    public double getReciprocity(){
        int reciprocitycount =0;
        for (Edge e : edges){
            String source = e.getSource();
            String target = e.getTarget();
            if(hasInteraction(target,source)){
                reciprocitycount++;
            }
        }
        if (edges.isEmpty()) return 0.0;
        return (double) reciprocitycount / edges.size();
    }

    private int bfsDistance(String start){
        Map<String, Integer> distance = new HashMap<>();
        Queue<String> queue = new LinkedList<>();

        distance.put(start,0);
        queue.add(start);

        while(!queue.isEmpty()){
            String current = queue.poll();//queuenin başını alıyoruz

            for (String neighbor : getNeighbors(current) ){
                if(!distance.containsKey(neighbor)){
                    distance.put(neighbor,distance.get(current)+1); //current 0 uzaklıktaysa komşu 1
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

}
