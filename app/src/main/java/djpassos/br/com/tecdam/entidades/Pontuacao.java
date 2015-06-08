package djpassos.br.com.tecdam.entidades;

import java.io.Serializable;

/**
 * Created by djalma on 07/06/2015.
 */
public class Pontuacao implements Serializable{

    private String jogador;
    private String tempo;
    private int minutos;
    private int segundos;

    public String getJogador() {
        return jogador;
    }

    public void setJogador(String jogador) {
        this.jogador = jogador;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public int getSegundos() {
        return segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }

    @Override
    public String toString() {
        return  "Nome:" + jogador + "\ntempo:" + tempo;
    }
}
