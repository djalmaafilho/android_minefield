package djpassos.br.com.tecdam.campominado;

import djpassos.br.com.tecdam.fachada.Fachada;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Classe que serve para dar boas Vindas ao usuario e depois de determinado
 * tempo vai para a tela de jogo.
 * 
 * @author djalma
 * 
 */
public class SplashActivity extends Activity implements Runnable {
	private final int DELAY = 4000; // tempo de espera em milisegundos pra
									// exibir proxima tela

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		int msgStr = R.string.msg_inicio;

		Toast.makeText(this, msgStr, Toast.LENGTH_LONG).show();

		Handler handler = new Handler();
		handler.postDelayed(this, DELAY);
	}

	@Override
	public void run() {
		Intent it = new Intent(this, TelaInicioActivity.class);
		startActivity(it);
		finish();
	}
}