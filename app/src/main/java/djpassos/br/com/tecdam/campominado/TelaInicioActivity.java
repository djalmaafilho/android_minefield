package djpassos.br.com.tecdam.campominado;

import djpassos.br.com.tecdam.fachada.Fachada;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TelaInicioActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telainicio);
		
		((Button)findViewById(R.id.button1)).setOnClickListener(this);
        ((Button)findViewById(R.id.buttonRecordes)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
        if(v.getId() == R.id.button1){
            Intent it = new Intent(this, CampoMinadoActivity.class);
            startActivity(it);
            Fachada.getInstancia();

        }else{
            Intent it = new Intent(this, RecordesActivity.class);
            startActivity(it);
        }
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}