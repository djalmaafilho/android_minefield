package djpassos.br.com.tecdam.persistencia;

import android.content.Context;

public class RepositorioRecordes {
	private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS recordes";

	private static final String SCRIPT_DATABASE_CREATE = "create table "
			+ "recordes(_id integer primary key autoincrement, nome text not null, "
			+ "tempo integer not null);";

	private static final String NOME_BANCO = "campo_minado_recordes";

	private static final int VERSAO_BANCO = 1;

	private SQLiteHelper dbHelper;

	public RepositorioRecordes(Context contexto) {
		dbHelper = new SQLiteHelper(contexto, NOME_BANCO, VERSAO_BANCO,
				SCRIPT_DATABASE_CREATE, SCRIPT_DATABASE_DELETE);
		dbHelper.getWritableDatabase();
	}

	public void fechar(){
		if(dbHelper!= null){
			dbHelper.close();
		}
	}	
}
