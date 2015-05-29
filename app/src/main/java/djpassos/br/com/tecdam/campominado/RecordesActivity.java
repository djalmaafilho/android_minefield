package djpassos.br.com.tecdam.campominado;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class RecordesActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telarecords);

		Intent it = getIntent();
		
		if (it!= null) {
			Bundle parametros = it.getExtras();
			int minuto = parametros.getInt("minuto");
			int segundo = parametros.getInt("segundo");
			String tempo = parametros.getString("tempo");
			((TextView)findViewById(R.telaRecordes.textView2)).setText(tempo);
		//	Toast.makeText(this, "Tempo=: Minutos "+minuto+":"+" segundos "+segundo, Toast.LENGTH_LONG).show();
		}
	}

}
