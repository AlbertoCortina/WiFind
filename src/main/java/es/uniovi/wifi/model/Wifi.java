package es.uniovi.wifi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "TWifi")
public class Wifi {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank
	@Column(name = "ssid")
	private String ssid;

	@NotNull
	@Column(name = "intensidad")
	private double intensidad;

	@ManyToOne
	@JoinColumn(name = "id_localizacion", nullable = false)
	private Localizacion localizacion;

	public Wifi() {
	}

	public Wifi(String ssid, double intensidad) {
		this.ssid = ssid;
		this.intensidad = intensidad;
	}

	public Long getId() {
		return id;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public double getIntensidad() {
		return intensidad;
	}

	public void setIntensidad(double intensidad) {
		this.intensidad = intensidad;
	}

	public Localizacion getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(Localizacion localizacion) {
		this.localizacion = localizacion;
	}

}