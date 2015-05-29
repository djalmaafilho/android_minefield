package djpassos.br.com.tecdam.fachada;

import java.util.List;

import android.graphics.drawable.Drawable;

import djpassos.br.com.tecdam.entidades.Casa;
import djpassos.br.com.tecdam.entidades.ControleCasa;
import djpassos.br.com.tecdam.entidades.ControleRelogio;
import djpassos.br.com.tecdam.entidades.Relogio;

/**
 * Esta classe Guarda o estado do jogo Utiliza padroes de projeto facade e
 * singleton, e abstrai detalhes do controle.
 * 
 * @author djalma
 * 
 */
public class Fachada {

	private static Fachada fachada;
	private ControleCasa controleCasa;
	private ControleRelogio controleRelogio;

	private Fachada() {
		iniciarControles();
	}

	private void iniciarControles() {
		this.controleCasa = new ControleCasa();
		this.controleRelogio = new ControleRelogio();
	}

	public static Fachada getInstancia() {
		if (fachada == null) {
			fachada = new Fachada();
		}
		return fachada;
	}

	// Metodos de Controle casas
	public List<Casa> recuperarCasas() {

		return this.controleCasa.getCasas();
	}

	public int getTamMatriz() {
		return controleCasa.getTamMatriz();
	}

	public int getQTDBomba() {
		return this.controleCasa.getQTDBomba();
	}

	public int getQtdBandeira() {
		return this.controleCasa.getQtdBandeira();
	}

	/**
	 * Atualiza o estado do jogo
	 * 
	 * @param casas
	 */
	public void atualizarCasas(List<Casa> casas) {
		this.controleCasa.setCasas(casas);
	}

	/**
	 * Carrega as imagens iniciais das casas do jogo
	 * 
	 * @param drawable
	 */
	public void carregarImagemInicio(Drawable[] drawable) {
		if (controleCasa.getCasas().get(0).getImagem() == null) {
			this.controleCasa.carregarImagemInicio(drawable);
		}
	}

	public int[] casasVizinhas(int indice) {
		return this.controleCasa.getCasasVizinhas(indice);
	}

	//indica se ocorreu fim do jogo com vitoria
	public boolean fimJogoVitoria() {
		return this.controleCasa.isFimJogo();
	}

	// Metodos de Controle Relogio

	public void reiniciarRelogio() {
		this.controleRelogio.reiniciarRelogio();
	}

	public void incrementarRelogio() {
		this.controleRelogio.incrementarRelogio();
	}

	public void pararRelogio() {
		this.controleRelogio.pararRelogio();
	}

	public Relogio recuperarRelogio() {
		return this.controleRelogio.getRelogio();
	}

	public boolean isParadoRelogio() {
		return this.controleRelogio.isParado();
	}

	public void liberarRelogio() {
		this.controleRelogio.setParado(false);
	}

	public String recuperarTempo() {
		return this.controleRelogio.getTempo();
	}

	/**
	 * Metodo rsponsavel por tirar a instancia atual da Fachada da memoria.
	 */
	public static void finalizarFachada() {
		if (fachada != null) {
			fachada = null;
		}
	}
}