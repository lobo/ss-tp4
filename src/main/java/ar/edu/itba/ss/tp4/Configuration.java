package ar.edu.itba.ss.tp4;

/**
* <p>Esta clase representa la estructura del archivo de configuración,
* la cual será automáticamente completada durante el inicio del
* sistema. La clase debe ser completamente mapeable con respecto al
* archivo origen.</p>
*/

public final class Configuration {

	// Archivo de configuración origen:
	public static final String CONFIGURATION_FILENAME
		= "config.json";
	

	private Integer fps = 0;
	private Double deltat = 0.0;
	private String inputfile = "";
	private String integration = "";
	private String system = "";
		
	public static String getConfigurationFilename() {
		return CONFIGURATION_FILENAME;
	}

	public Integer getFPS() {
		return fps;
	}
	
	public String getInputfile() {
		return inputfile;
	}
	
	public String getIntegration() {
		return integration;
	}
	
	public String getSystem() {
		return system;
	}

	public Double getDeltat() {
		return deltat;
	}

}
