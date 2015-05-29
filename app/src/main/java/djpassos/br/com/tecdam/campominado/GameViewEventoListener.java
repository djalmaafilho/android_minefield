package djpassos.br.com.tecdam.campominado;

public interface GameViewEventoListener extends java.util.EventListener {

	void partidaAcabou(GameViewEvento e);
	
	void JogoComecou(GameViewEvento e);
	
	void jogoReiniciou(GameViewEvento e);
	
	void jogodaInicialOcorreu(GameViewEvento e);

	void mudouQtdBandeira(GameViewEvento e);
	
	void vitoria(GameViewEvento e);
}

