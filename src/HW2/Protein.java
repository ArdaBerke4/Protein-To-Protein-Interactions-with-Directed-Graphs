package HW2;

    public class Protein {
        private String id;
        private String name;


        public Protein(String id , String name){
            this.id= id;
            this.name = name;
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