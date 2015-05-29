package djpassos.br.com.tecdam.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String CATEGORIA = "recorde";
	private String scriptSQLCreate;
	private String scriptDelete;

	public SQLiteHelper(Context contexto, String nomeBanco, int versaoBanco,
			String scriptDatabaseCreate, String scriptDatabaseDelete) {

		super(contexto, nomeBanco, null, versaoBanco);
		this.scriptSQLCreate = scriptDatabaseCreate;
		this.scriptDelete = scriptDatabaseDelete;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(scriptSQLCreate); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versao, int novaVersao) {
		db.execSQL(scriptDelete);
		onCreate(db);
	}
}
