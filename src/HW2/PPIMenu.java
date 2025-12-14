package HW2;

import java.util.Scanner;

public class PPIMenu {

    private Scanner sc;
    private PPIGraph graph;

    public PPIMenu() {
        sc = new Scanner(System.in);
        graph = new PPIGraph();
    }


    public void start() {
        while (true) {
            printMenu();

            int choice = sc.nextInt();
            sc.nextLine(); // Enter'ı temizle

            switch (choice) {
                case 1:
                    loadGraph();
                    break;
                case 2:
                    searchProtein();
                    break;
                case 3:
                    checkInteraction();
                    break;
                case 4:

                    break;
                case 5:
                    showMetrics();
                    break;
                case 6:
                    bfsTraverse();

                    break;
                case 7:
                    dfsTraverse();
                    break;
                default:

                    System.out.println("Seçtiğin: " + choice + " (henüz bağlanmadı)");
            }

        }
    }
    private void bfsTraverse() {
        System.out.print("Baslangic protein ID: ");
        String start = sc.nextLine();

        graph.bfsTraverse(start);
    }

    private void dfsTraverse() {
        System.out.print("Baslangic protein ID: ");
        String start = sc.nextLine();

        graph.dfsTraverse(start);
    }

    private void loadGraph() {
        graph.clear();

        System.out.print("Confidence threshold (0-1): ");
        String input = sc.nextLine();
        input = input.replace(',', '.');

        double threshold = Double.parseDouble(input);


        graph.loadProteins("C:\\Users\\ARDA-PC\\IdeaProjects\\HW2\\src\\HW2\\9606.protein.info.v12.0.txt");
        graph.loadInteractions("C:\\Users\\ARDA-PC\\IdeaProjects\\HW2\\src\\HW2\\9606.protein.links.v12.0.txt", threshold);

        System.out.println("Graph başarıyla yüklendi.");
    }



//
    private void printMenu() {
        System.out.println("\n==== PPI GRAPH MENU ====");
        System.out.println("1. Load graph");
        System.out.println("2. Search protein by ID");
        System.out.println("3. Check interaction");
        System.out.println("4. Most confident path");
        System.out.println("5. Graph metrics");
        System.out.println("6. BFS Traverse");
        System.out.println("7. DFS Traverse");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    private void searchProtein(){
        System.out.print("Protein ID gir: ");
        String id = sc.nextLine();
        Protein p = graph.getProtein(id);

        if(graph.hasProtein(id)){
            System.out.println("Protein bulundu:");
            System.out.println("ID        : " + p.getId());
            System.out.println("Name      : " + p.getName());
            System.out.println("Size      : " + p.getSize());
            System.out.println("Annotation: " + p.getAnnotation());

        }
    }
    private void checkInteraction() {
        System.out.print("Protein 1 ID: ");
        String p1 = sc.nextLine();

        System.out.print("Protein 2 ID: ");
        String p2 = sc.nextLine();

        if (!graph.hasProtein(p1) || !graph.hasProtein(p2)) {
            System.out.println("Girilen proteinlerden biri bulunamadi.");
            return;
        }

        if (graph.hasInteraction(p1, p2)) {
            System.out.println("Bu iki protein arasinda INTERACTION VAR.");
        } else {
            System.out.println("Bu iki protein arasinda interaction yok.");
        }
    }

        private void showMetrics() {
            System.out.println("=== GRAPH METRICS ===");
            System.out.println("Vertex count   : " + graph.getVertexCount());
            System.out.println("Edge count     : " + graph.getEdgeCount());
            System.out.println("Average degree : " + graph.getAverageDegree());
            System.out.println("Diameter       : " + graph.getDiameter());
            System.out.println("Reciprocity    : " + graph.getReciprocity());

            System.out.println("\nDevam etmek icin Enter'a basin...");
            sc.nextLine();
        }

    }



