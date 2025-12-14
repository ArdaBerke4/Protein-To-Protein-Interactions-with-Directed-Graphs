package HW2;

public class Protein {
    private String id;
    private String name;
    private int size;
    private String annotation;
//
    public Protein(String id , String name,int size ,String annotation){
        this.id= id;
        this.name = name;
        this.size = size;
        this.annotation= annotation;
    }
    public int getSize() {
        return size;
    }

    public String getAnnotation() {
        return annotation;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + " (" + name + ")";
    }



}
