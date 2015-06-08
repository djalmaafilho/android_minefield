package djpassos.br.com.tecdam.campominado;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import djpassos.br.com.tecdam.entidades.Pontuacao;

public class RecordesActivity extends Activity implements View.OnClickListener{

    private ArrayList<Pontuacao> listaRecordes;
    private ListView listView;
    private SimpleAdapter adapter;
    private EditText txtNome;
    private Button btSalvar;
    private int minuto, segundo;
    private String tempo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telarecords);

        txtNome = (EditText)findViewById(R.id.editTextNome);
        btSalvar = (Button)findViewById(R.id.buttonSalvar);
        btSalvar.setOnClickListener(this);
        TextView txtTempo = ((TextView)findViewById(R.id.textView2));
        listView = (ListView)findViewById(R.id.listViewRecordes);
        TextView labelNome = (TextView)findViewById(R.id.textViewNome);
        TextView labelTempo = (TextView)findViewById(R.id.textViewTempo);

        Intent it = getIntent();
        if (it!= null && it.getExtras()!= null) {
            Bundle parametros = it.getExtras();
            minuto = parametros.getInt("minuto");
            segundo = parametros.getInt("segundo");
            tempo = parametros.getString("tempo");
            txtTempo.setText(tempo);
        }else{
            btSalvar.setVisibility(View.GONE);
            txtNome.setVisibility(View.GONE);
            txtTempo.setVisibility(View.GONE);
            labelNome.setVisibility(View.GONE);
            labelTempo.setVisibility(View.GONE);
        }

		ArrayList<HashMap<String, String>> lista = new
		ArrayList<HashMap<String, String>>();
        listaRecordes = getListaRecordes();

        int i = 0;
        for (Pontuacao pt:listaRecordes){
             HashMap<String, String> item = new HashMap<String, String>();
             item.put("contador", "Posição "+(++i));
             item.put("dados", pt.toString());
             lista.add(item);
        }

        String[] campos = { "contador", "dados"};
        int[] itens = new int[]{android.R.id.text1,android.R.id.text2};

		int layoutBase = android.R.layout.two_line_list_item;
        adapter = new SimpleAdapter(this, lista, layoutBase,
                campos, itens);

        listView.setAdapter(adapter);
	}


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonSalvar){
            salvarPontuacao();
        }
    }

    private ArrayList<Pontuacao> getListaRecordes() {
        ArrayList<Pontuacao> lista = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput("Recordes");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Pontuacao> listaAux = (ArrayList<Pontuacao>)ois.readObject();

            if(listaAux!= null){
                lista.addAll(listaAux);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return lista;
    }

    private void salvarPontuacao() {
            Pontuacao pt = new Pontuacao();
            pt.setJogador(txtNome.getText().toString());
            pt.setMinutos(minuto);
            pt.setSegundos(segundo);
            pt.setTempo(tempo);
            persistir(pt);

            btSalvar.setVisibility(View.GONE);
            txtNome.setEnabled(false);

            Intent it = new Intent(this, RecordesActivity.class);
            startActivity(it);
            finish();
    }

    private void persistir(Pontuacao pt){
        try {
            listaRecordes.add(pt);

            Collections.sort(listaRecordes, new Comparator<Pontuacao>() {
                @Override
                public int compare(Pontuacao lhs, Pontuacao rhs) {

                    int comparacao = lhs.getMinutos() -rhs.getMinutos();

                    if(comparacao == 0){
                        comparacao = lhs.getSegundos() -rhs.getSegundos();
                    }
                    return comparacao;
                }
            });

            FileOutputStream fos = openFileOutput("Recordes", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listaRecordes);
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}