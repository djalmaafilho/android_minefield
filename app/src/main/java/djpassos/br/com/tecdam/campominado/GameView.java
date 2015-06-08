package djpassos.br.com.tecdam.campominado;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import djpassos.br.com.tecdam.entidades.Casa;
import djpassos.br.com.tecdam.entidades.ControleCasa;
import djpassos.br.com.tecdam.fachada.Fachada;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/*
 * Classe responsavel de desenhar na tela da aplicacao
 */
public class GameView extends View {
	
	private static GameView instancia;
	private int x, y;
	private long inicioClik;
	private long fimClick;
	private int alturaCasa;
	private int larguraCasa;
	private int tamMatriz;
	private static Drawable[] casaInicio;
	private static final int JOGO_INICIOU = 0;
	private static final int PARTIDA_ACABOU = 1;
	private static final int JOGO_REINICIOU = 2;
	private static final int START_CRONOMETRO = 3;
	private static final int MUDOU_BANDEIRA = 4;
	private static final int VITORIA = 5;

	private static List<Casa> casas = new ArrayList<Casa>();
	private Collection<GameViewEventoListener> gameViewListeners = new ArrayList<GameViewEventoListener>();

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		instancia = this;

		int qtdCasas = Fachada.getInstancia().getTamMatriz()
				* Fachada.getInstancia().getTamMatriz();
		casaInicio = new Drawable[qtdCasas];
		tamMatriz = Fachada.getInstancia().getTamMatriz();

		x = 0;
		y = 0;
		inicioClik = 0;
		fimClick = 0;

		for (int i = 0; i < casaInicio.length; i++) {
			casaInicio[i] = this.getResources().getDrawable(
					R.drawable.casainicio);
		}

		Fachada.getInstancia().carregarImagemInicio(casaInicio);
		casas = Fachada.getInstancia().recuperarCasas();
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		casas = Fachada.getInstancia().recuperarCasas();

		larguraCasa = canvas.getClipBounds().width() / tamMatriz;
		alturaCasa = canvas.getClipBounds().width() / tamMatriz;
		int indice = 0;
		x = 0;
		y = canvas.getClipBounds().height() / tamMatriz;

		for (int i = 0; i < tamMatriz; i++) {
			for (int j = 0; j < tamMatriz; j++) {

				casas.get(indice).getImagem()
						.setBounds(x, y, x + larguraCasa, y + alturaCasa);

				casas.get(indice).getImagem().draw(canvas);

				x += larguraCasa;
				indice++;
			}
			y += alturaCasa;
			x = 0;
		}
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int eventX = (int) event.getX();
		int eventY = (int) event.getY();

		int acao = event.getAction();
		switch (acao) {
		case 0:
			inicioClik = event.getEventTime();
			break;
		case 1:
			fimClick = event.getEventTime();
			break;
		default:
			break;
		}

		if (fimClick - inicioClik >= 600) {
			colocarBandeira(eventX, eventY);

		} else if (fimClick - inicioClik > 0 && fimClick - inicioClik < 1000) {
			abrirCasa(eventX, eventY);

		}
		return true;
	}

	/*
	 * Seta o valor boleano da casa com bandeira para true
	 */
	private void colocarBandeira(int x, int y) {
		Casa casa;
		int indice = acharIndiceCasa(x, y);
		if (indice > -1) {
			casa = casas.get(indice);
			if (!casa.isCasaAberta()) {
				casa.setImagem(this.getResources().getDrawable(
						R.drawable.bandeira));
				casa.setBandeira(true);
				trocarImagem(indice, casa);
				mudouBandeira();
				vibrar(100);
			}
		}
	}


	/*
	 * Abre uma casa e atribui a imagem e estado adequado
	 */
	private void abrirCasa(int x, int y) {
		Casa casa;
		int indice = acharIndiceCasa(x, y);
		if (indice > -1) {
			casa = casas.get(indice);
			if (!casa.isCasaAberta() && casa.isBandeira()) {

				casa.setImagem(this.getResources().getDrawable(
						R.drawable.casainicio));

				casa.setBandeira(false);
				trocarImagem(indice, casa);
				
				mudouBandeira(); //notificar Mudanca de bandeira

			} else if (!casa.isCasaAberta() && casa.isBomba()) {

				casa.setImagem(this.getResources().getDrawable(
						R.drawable.bomba_marcada));

				casa.setCasaAberta(true);
				trocarImagem(indice, casa);
				abrirTodasCasas();

				bombaEstourou(); //notificar estouro de bomba
				vibrar(1000);

			} else if (!casa.isCasaAberta() && !casa.isBomba()
					&& !casa.isBandeira()) {

				Drawable imagem = excolherImagemQtdBomba(casa
						.getQtdBombaProximo());

				casa.setImagem(imagem);
				casa.setCasaAberta(true);
				trocarImagem(indice, casa);

				if (casa.getQtdBombaProximo() == 0) {
					abrirClareira(indice);
				}
			}
		}
	}


	/*
	 * Busca por casa que nao sao bomba, ou possiveis 
	 * bombas(bandeiras), abre casas fora das condicoes acima ate
	 * achar uma bomba. As casas que foram abertas nesse processo
	 * realizam a mesma busca de forma recursiva.
	 */
	private void abrirClareira(int indice) {

		Casa casa;
		int i = 0;
		int[] vizinhos = Fachada.getInstancia().casasVizinhas(indice);

		while (i < vizinhos.length) {
			if (vizinhos[i] != -1) {
				casa = casas.get(vizinhos[i]);

				if (!casa.isBomba() && !casa.isBandeira()
						&& !casa.isCasaAberta()) {
					Drawable imagem = excolherImagemQtdBomba(casa
							.getQtdBombaProximo());

					casa.setImagem(imagem);
					casa.setCasaAberta(true);
					trocarImagem(vizinhos[i], casa);

					if (casa.getQtdBombaProximo() == 0) {
						abrirClareira(vizinhos[i]);
					}
				}
			}
			i++;
		}
	}

	/*
	 * Retorna uma imagem para ser colocada em uma casa a imagem sera um numero
	 * de 1 a 8 ou uma casa sem numero representando uma clareira
	 */
	private Drawable excolherImagemQtdBomba(int qtdBomba) {
		Drawable imagem;

		switch (qtdBomba) {
		case 1:
			imagem = this.getResources().getDrawable(R.drawable.casa_1);
			break;

		case 2:
			imagem = this.getResources().getDrawable(R.drawable.casa_2);
			break;

		case 3:
			imagem = this.getResources().getDrawable(R.drawable.casa_3);
			break;

		case 4:
			imagem = this.getResources().getDrawable(R.drawable.casa_4);
			break;
		case 5:
			imagem = this.getResources().getDrawable(R.drawable.casa_5);
			break;

		case 6:
			imagem = this.getResources().getDrawable(R.drawable.casa_6);
			break;

		case 7:
			imagem = this.getResources().getDrawable(R.drawable.casa_7);
			break;

		case 8:
			imagem = this.getResources().getDrawable(R.drawable.casa_8);
			break;

		default:
			imagem = this.getResources().getDrawable(R.drawable.clareira);
			break;
		}

		return imagem;
	}

	/*
	 * Muda a imagem que foi clicada
	 */
	private void trocarImagem(int indice, Casa casa) {
		casas.set(indice, casa);
		Fachada.getInstancia().atualizarCasas(casas);
		invalidate(); // atualiza tela
		trocouImagem(); //notificar troca de imagem
		if(Fachada.getInstancia().fimJogoVitoria()){
			fimJogoVitoria();
		}
	}


	/*
	 * Abre todas as Casas que ainda n�o foram abertas. � chamado quando o
	 * jogador clicou em uma bomba ou � fim de jogo por vitoria.
	 */
	private void abrirTodasCasas() {
		Casa casa;
		for (int i = 0; i < casas.size(); i++) {
			casa = casas.get(i);

			if (casa.isBomba() && !casa.isCasaAberta() && !casa.isBandeira()) {
				casa.setImagem(this.getResources()
						.getDrawable(R.drawable.bomba));

			} else if (!casa.isBomba() && casa.isBandeira()) {
				casa.setImagem(this.getResources().getDrawable(
						R.drawable.bandeira_marcada));

			} else if (casa.isBomba() && casa.isBandeira()) {
				casa.setCasaAberta(true);
			}
			casa.setCasaAberta(true);
			casas.set(i, casa);
		}
		invalidate(); // atualiza tela
	}

	/*
	 * Retorna o indice da imagem se ela existir ou -1 caso nao exista
	 */

	private int acharIndiceCasa(int x, int y) {

		boolean contem = false;
		Casa casa;
		for (int i = 0; i < casas.size(); i++) {
			casa = casas.get(i);
			contem = casa.getImagem().copyBounds().contains(x, y);

			if (contem == true) {
				return i;
			}
		}
		return -1;
	}

	
	public static void reiniciar() {
		Fachada.getInstancia().iniciar();

		for (int i = 0; i < casaInicio.length; i++) {
			casaInicio[i] = instancia.getResources().getDrawable(
					R.drawable.casainicio);
		}
		Fachada.getInstancia().carregarImagemInicio(casaInicio);
		
		casas = Fachada.getInstancia().recuperarCasas();
		instancia.invalidate();
		instancia.jogoReiniciou();
	}

	private void vibrar(int tempo) {
		Vibrator vibrator = (Vibrator) this.getContext().getSystemService(
				Context.VIBRATOR_SERVICE);
		vibrator.vibrate(tempo);
	}

	//metodos de observacao e notificacao
	private void mudouBandeira() {
		disparaMudouBandeira();
		
	}
	
	public void bombaEstourou() {
		disparaBombaEstourou();
	}

	public void jogoReiniciou() {
		disparaJogoReiniciou();
	}

	public void trocouImagem(){
		if(Fachada.getInstancia().isParadoRelogio()){
			disparaStartCronometro();
		}
	}
	
	private void fimJogoVitoria() {
		disparaFimJogoVitoria();
		abrirTodasCasas();
	}

	
	public synchronized void addGameViewListener(GameViewEventoListener l) {
		if (!gameViewListeners.contains(l)) {
			gameViewListeners.add(l);
		}
	}

	public synchronized void removeGameViewListener(GameViewEventoListener l) {
		gameViewListeners.remove(l);
	}

	private void disparaJogoReiniciou() {
		DispararGameViewEvento(JOGO_REINICIOU);
	}

	private void disparaBombaEstourou() {
		DispararGameViewEvento(PARTIDA_ACABOU);
	}
	
	private void disparaStartCronometro() {
		DispararGameViewEvento(START_CRONOMETRO);
	}
	
	private void disparaMudouBandeira(){
		DispararGameViewEvento(MUDOU_BANDEIRA);
	}
	
	private void disparaFimJogoVitoria(){
		DispararGameViewEvento(VITORIA);
	}

	private void DispararGameViewEvento(int constante) {
		Collection<GameViewEventoListener> gameListener;
		synchronized (this) {
			gameListener = (Collection) (((ArrayList) gameViewListeners).clone());
		}
		GameViewEvento evento = new GameViewEvento(this);

		switch (constante) {
		case PARTIDA_ACABOU:
			for (GameViewEventoListener l : gameListener) {
				l.partidaAcabou(evento);
			}
			break;

		case JOGO_REINICIOU:
			for (GameViewEventoListener l : gameListener) {
				l.jogoReiniciou(evento);
			}
			break;
		case START_CRONOMETRO:
			for (GameViewEventoListener l : gameListener) {
				l.jogodaInicialOcorreu(evento);
			}
			break;
			
		case MUDOU_BANDEIRA:
			for (GameViewEventoListener l : gameListener) {
				l.mudouQtdBandeira(evento);
			}
			break;
		
		case VITORIA:
			for (GameViewEventoListener l : gameListener) {
				l.vitoria(evento);
			}
			break;	
		default:
			break;
		}
	}
}