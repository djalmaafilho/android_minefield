package djpassos.br.com.tecdam.entidades;

public class ControleRelogio {
	private Relogio relogio;
	private boolean parado;

	public ControleRelogio() {
		relogio = new Relogio();
		parado = true;
	}

	public String getTempo(){		
		return stringTempo(relogio.minuto, relogio.segundo);
	}
	
	public void zerarRelogio() {
		relogio.setMinuto(0);
		relogio.setSegundo(0);
	}

	public void reiniciarRelogio() {
		setParado(true);
		zerarRelogio();
	}

	public void pararRelogio() {
		setParado(true);
	}

	public void incrementarRelogio() {
		int minuto;
		int segundo;
		if (!parado) {
			minuto = relogio.getMinuto();
			segundo = relogio.getSegundo();

			if (segundo < 59) {
				relogio.setSegundo(segundo + 1);
			} else {
				relogio.setSegundo(0);
				if (minuto < 59) {
					relogio.setMinuto(minuto + 1);
				} else {
					relogio.setMinuto(0);
				}
			}
		}
	}

	public boolean isParado() {
		return parado;
	}

	public void setParado(boolean parado) {
		this.parado = parado;
	}

	public Relogio getRelogio() {
		return relogio;
	}

	public void setRelogio(Relogio relogio) {
		this.relogio = relogio;
	}
	
	/*
	 * Retorna uma string configurada no formato mm:ss
	 */
	private String stringTempo(int minuto, int segundo) {
		String str = "";
		if (minuto < 10) {
			str += "0" + minuto;
		}else {
			str += ":" + minuto	;
		}

		if (segundo < 10) {
			str += ":0" + segundo;
		} else {
			str += ":" + segundo;
		}

		return str;
	}
	
}