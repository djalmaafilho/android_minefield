package djpassos.br.com.tecdam.entidades;

import android.graphics.drawable.Drawable;

/**
 * Classe que representa uma casa do tabuleiro de campo minado
 * 
 * @author djalma
 * 
 */
public class Casa implements Comparable<Casa> {
	
	private Drawable imagem;
	private int linha, coluna;
	private boolean casaAberta, bandeira, bomba;
	private int qtdBombaProximo;

	public Casa(int linha, int coluna, boolean aberta, boolean bandeira,
			boolean bomba) {
		this.linha = linha;
		this.coluna = coluna;
		this.casaAberta = aberta;
		this.bandeira = bandeira;
		this.bomba = bomba;
		this.imagem = null;
		this.qtdBombaProximo = 0;
	}

	public boolean isBomba() {
		return bomba;
	}

	public void setBomba(boolean bomba) {
		this.bomba = bomba;
	}

	public int getQtdBombaProximo() {
		return qtdBombaProximo;
	}

	public void setQtdBombaProximo(int qtdBombaProximo) {
		this.qtdBombaProximo = qtdBombaProximo;
	}

	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	public int getColuna() {
		return coluna;
	}

	public void setColuna(int coluna) {
		this.coluna = coluna;
	}

	public Drawable getImagem() {
		return imagem;
	}

	public boolean isCasaAberta() {
		return casaAberta;
	}

	public void setCasaAberta(boolean casaAberta) {
		this.casaAberta = casaAberta;
	}

	public void setImagem(Drawable imagem) {
		this.imagem = imagem;
	}

	public boolean isBandeira() {
		return bandeira;
	}

	public void setBandeira(boolean bandeira) {
		this.bandeira = bandeira;
	}

	/*
	 * Perimite a ordenacao de um List de Casas atraves de um parametro de
	 * ordenacao predefinido , no caso o escolhido foi linha e coluna
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Casa casa) {

		if (casa.linha == this.linha && casa.coluna == this.coluna) {
			return 0;
		} else if (casa.linha < this.linha) {
			return 1;
		} else if (casa.linha == this.linha && casa.coluna < this.coluna) {
			return 1;
		}
		return -1;
	}
}