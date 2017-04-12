package models;

public class Casa {

    private Peca peca;

    public Casa(){
        peca = new Peca();
    }

    public Peca getPeca() {
        return peca;
    }
}
