package djpassos.br.com.tecdam.campominado;

import djpassos.br.com.tecdam.fachada.Fachada;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CampoMinadoActivity extends Activity implements OnClickListener,
		GameViewEventoListener, Runnable {
	public static final long TEMPO = (1000);
	protected static final int ATUALIZAR_RELOGIO = 0;
	private GameView gameView;
	private ImageButton faceBotao;
	private TextView contador;
	private TextView qtdBombas;
	private boolean on;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Fachada.getInstancia().iniciar();

		setContentView(R.layout.main);

		GameView gameView = (GameView) findViewById(R.id.gameView1);
		gameView.addGameViewListener(this);

		faceBotao = (ImageButton) findViewById(R.id.imageButton1);
		faceBotao.setOnClickListener(this);

		contador = (TextView) findViewById(R.id.textView1);
		qtdBombas = (TextView) findViewById(R.id.textView2);

		qtdBombas.setText(Integer
				.toString(Fachada.getInstancia().getQTDBomba()));

		handler = new Handler();
		handler.post(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		on = true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		on = false;
	}

	void atualizarRelogio() {
		Fachada.getInstancia().incrementarRelogio();
		((TextView) findViewById(R.id.textView1)).setText(Fachada
				.getInstancia().recuperarTempo());
	}

	void atualizaContatorBandeira() {
		String str = Integer.toString(Fachada.getInstancia().getQtdBandeira());
		((TextView) findViewById(R.id.textView2)).setText(str);
	}

	@Override
	public void onClick(View v) {
		if (v == faceBotao) {
			findViewById(R.id.imageButton1).setBackgroundResource(
					R.drawable.cara_feliz);
			GameView.reiniciar();
		}
	}

	@Override
	public void partidaAcabou(GameViewEvento e) {
		findViewById(R.id.imageButton1).setBackgroundResource(
				R.drawable.cara_triste);
		Fachada.getInstancia().pararRelogio();
	}

	@Override
	public void jogoReiniciou(GameViewEvento e) {
		Fachada.getInstancia().reiniciarRelogio();
		atualizaContatorBandeira();
	}

	@Override
	public void JogoComecou(GameViewEvento e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jogodaInicialOcorreu(GameViewEvento e) {
		Fachada.getInstancia().liberarRelogio();
	}

	@Override
	public void mudouQtdBandeira(GameViewEvento e) {
		atualizaContatorBandeira();
	}

	@Override
	public void run() {
		if (on) {
			atualizarRelogio();
			handler.postDelayed(this, 1000);
		}
	}

	@Override
	public void vitoria(GameViewEvento e) {
		// TODO Auto-generated method stub
		findViewById(R.id.imageButton1).setBackgroundResource(
				R.drawable.cara_oculos);
		Fachada.getInstancia().pararRelogio();
		
		Toast.makeText(this, this.getResources().getText(R.string.vitoria),
				Toast.LENGTH_SHORT).show();
		
		Bundle b = new Bundle();
		b.putInt("minuto", Fachada.getInstancia().recuperarRelogio().getMinuto());
		b.putInt("segundo", Fachada.getInstancia().recuperarRelogio().getSegundo());
		b.putString("tempo",Fachada.getInstancia().recuperarTempo());
		Intent it = new Intent(this, RecordesActivity.class);
		it.putExtras(b);
		
		startActivity(it);
	}	
}