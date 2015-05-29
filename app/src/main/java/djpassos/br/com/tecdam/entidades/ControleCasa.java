package djpassos.br.com.tecdam.entidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.graphics.drawable.Drawable;

import djpassos.br.com.tecdam.campominado.GameView;
import djpassos.br.com.tecdam.campominado.R;

/**
 * Classe responsavel por aplicar regras do jogo
 * 
 * @author djalma
 * 
 */
public class ControleCasa {

	private List<Casa> casas;
	private final int QTD_BOMBAS = 10;
	private final int TAM = 10; // resulta em TAM x TAM casas

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ControleCasa() {
		this.casas = new ArrayList();
		initCasas(TAM, QTD_BOMBAS);
	}

	/*
	 * incializa todas as casas do jogo colocando aleatoriamente as bombas em
	 * determinado numero de casas que esta predefinido
	 * 
	 * @param tamanho
	 * 
	 * @param qtdBombas
	 */
	private void initCasas(int tamanho, int qtdBombas) {
		Casa casa;
		boolean statusBomba = false;
		int[] bombas = criarBombas(tamanho, qtdBombas);
		int indice = 0;
		for (int linha = 1; linha <= tamanho; linha++) {
			for (int coluna = 1; coluna <= tamanho; coluna++) {

				for (int i = 0; i < qtdBombas; i++) {
					statusBomba = (indice == bombas[i]);
					if (statusBomba)
						break;
				}
				casa = new Casa(linha, coluna, false, false, statusBomba);
				inserirCasa(casa);
				indice++;
			}
		}

		setarBombasProximo();
	}

	/*
	 * Cria um array com quantidade de posicoes = qtdBombas onde casa posicao
	 * possui um inteiro que indica o indice de uma casa que sera bomba em
	 * ArrayList casas
	 */
	private int[] criarBombas(int tamMatriz, int qtdBombas) {

		int[] bombas = new int[qtdBombas];
		int bombasCriadas = 0;
		int idCasaBomba = 0;
		final int ID_CASA_MAX = (tamMatriz * tamMatriz);
		// preencher array com numeros de casas aleatorios
		while (bombasCriadas < qtdBombas) {
			idCasaBomba = (Math.round((int) (ID_CASA_MAX * (Math.random()))));
			for (int i = 0; i < qtdBombas; i++) {
				if (bombas[i] == idCasaBomba) {//nao deixa inserir 2 vezes o mesmo valor
					break;
				} else if (bombas[i] == 0) {
					bombas[i] = idCasaBomba;
					bombasCriadas++;
					break;
				}
			}
		}

		ordenaVetorBombas(bombas); // retorna o vetor ordenado

		return bombas;
	}

	// Ordena o vetor de casas que serão bomba por linha coluna
	private void ordenaVetorBombas(int[] bombas) {
		boolean ordenado = false;
		boolean trocou = false;
		int aux = 0;
		int i = 0;
		while (!ordenado) {
			if (i + 1 < bombas.length) {
				if (bombas[i] > bombas[i + 1]) {
					aux = bombas[i];
					bombas[i] = bombas[i + 1];
					bombas[i + 1] = aux;
					trocou = true;
				}
				i++;
			}
			if (i + 1 == bombas.length && trocou != false) {
				i = 0;
				trocou = false;
			} else if (i + 1 == bombas.length) {
				ordenado = true;
			}
		}
	}

	/*
	 * Inicializa cada casa com a quantidade de bombas que estao ao seu redor
	 */
	private void setarBombasProximo() {
		int bombasProximo = 0;

		for (int i = 0; i < casas.size(); i++) {

			if ((i - TAM) >= 0 && casas.get(i - TAM).isBomba()) {
				bombasProximo++;
			}

			if ((i - (TAM - 1)) >= 0 && casas.get(i - (TAM - 1)).isBomba()
					&& casas.get(i).getColuna() < TAM) {
				bombasProximo++;
			}

			if ((i - (TAM + 1)) >= 0 && casas.get(i - (TAM + 1)).isBomba()
					&& casas.get(i).getColuna() > 1) {
				bombasProximo++;
			}

			if ((i - 1) >= 0 && casas.get(i - 1).isBomba()
					&& casas.get(i).getColuna() > 1) {
				bombasProximo++;
			}

			if ((i + 1) < casas.size() && casas.get(i + 1).isBomba()
					&& casas.get(i).getColuna() < TAM) {
				bombasProximo++;
			}

			if ((i + TAM) < casas.size() && casas.get(i + TAM).isBomba()) {
				bombasProximo++;
			}

			if ((i + (TAM - 1)) < casas.size()
					&& casas.get(i + (TAM - 1)).isBomba()
					&& casas.get(i).getColuna() > 1) {
				bombasProximo++;
			}

			if ((i + TAM + 1) < casas.size()
					&& casas.get(i + TAM + 1).isBomba()
					&& casas.get(i).getColuna() < TAM) {
				bombasProximo++;
			}

			casas.get(i).setQtdBombaProximo(bombasProximo);
			bombasProximo = 0;
		}
	}

	/**
	 * Retorna um array contendo os indices das casas vizinhas de uma
	 * determinada casa que teve seu indice passado com parametro. todas as
	 * posicoes que diferirem de -1 sao indices validos
	 * 
	 * @param indice
	 * @return
	 */
	public int[] getCasasVizinhas(int indice) {

		int[] vizinhos = new int[8];
		for (int x = 0; x < vizinhos.length; x++) {
			vizinhos[x] = -1;
		}
		int i = 0;

		if ((indice - TAM) >= 0) {
			vizinhos[i++] = indice - TAM;
		}

		if ((indice - (TAM - 1)) >= 0 && casas.get(indice).getColuna() < TAM) {
			vizinhos[i++] = indice - (TAM - 1);
		}

		if ((indice - (TAM + 1)) >= 0 && casas.get(indice).getColuna() > 1) {
			vizinhos[i++] = indice - (TAM + 1);
		}

		if ((indice - 1) >= 0 && casas.get(indice).getColuna() > 1) {
			vizinhos[i++] = indice - 1;
		}

		if ((indice + 1) < casas.size() && casas.get(indice).getColuna() < TAM) {
			vizinhos[i++] = indice + 1;
		}

		if ((indice + TAM) < casas.size()
				&& casas.get(indice).getColuna() < TAM) {
			vizinhos[i++] = indice + TAM;
		}

		if ((indice + (TAM - 1)) < casas.size()
				&& casas.get(indice).getColuna() > 1) {
			vizinhos[i++] = indice + (TAM - 1);
		}

		if ((indice + TAM + 1) < casas.size()
				&& casas.get(indice).getColuna() < TAM) {
			vizinhos[i++] = indice + TAM + 1;
		}
		return vizinhos;
	}

	private void inserirCasa(Casa casa) {
		casas.add(casa);
		Collections.sort(casas);
	}

	public List<Casa> getCasas() {
		return casas;
	}

	public void setCasas(List<Casa> casas) {
		this.casas = casas;
	}

	public int getTamMatriz() {
		return this.TAM;
	}

	public int getQTDBomba() {
		return this.QTD_BOMBAS;
	}

	public boolean isFimJogo() {
		int contador = 0;
		int contadorBandeira = 0;
		int contadorBomba = 0;

		for (Casa casa : casas) {
			if (casa.isBandeira())	contadorBandeira++;
			if (casa.isBandeira() && casa.isBomba()) contador++;
			if(casa.isBomba()) contadorBomba++;
		}

		if(contador== contadorBomba && contador == contadorBandeira){
			return true;
		}
		
		return false;
	}

	public int getQtdBandeira() {
		int qtd = 0;
		for (Casa casa : casas) {
			if (casa.isBandeira())
				qtd++;
		}
		return QTD_BOMBAS - qtd;
	}

	public void carregarImagemInicio(Drawable[] drawable) {
		for (int i = 0; i < this.casas.size(); i++) {
			casas.get(i).setImagem(drawable[i]);
		}
	}
}