package model;

public class Van {
    
    private int id;
    private String placa;
    private String modelo;
    private int capacidade;
    private String condicao;

    public Van() {
    }

    public Van(String placa, String modelo, int capacidade, String condicao) {
        this.placa = placa;
        this.modelo = modelo;
        this.capacidade = capacidade;
        this.condicao = condicao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }
    
    public String toString(){
        return placa + " - " + modelo;
    }
}
