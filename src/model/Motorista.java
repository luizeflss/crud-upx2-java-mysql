package model;

public class Motorista {
    private int id;
    private String nome;
    private String cnh;
    private String telefone;
    private Van van;

    public Motorista() {
    }

    public Motorista(String nome, String cnh, String telefone, Van van) {
        this.nome = nome;
        this.cnh = cnh;
        this.telefone = telefone;
        this.van = van;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Van getVan() {
        return van;
    }

    public void setVan(Van van) {
        this.van = van;
    }
    
    
    
    
}
