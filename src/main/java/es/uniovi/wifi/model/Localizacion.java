package es.uniovi.wifi.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "TLocalizacion")
public class Localizacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank
	@Column(name = "nombre")
	private String nombre;

	@OneToMany(mappedBy = "localizacion")
	@JoinColumn(name = "id_localizacion", nullable = false)
	private Set<Wifi> wifis;

	public Localizacion() {
	}

	public Localizacion(String nombre) {
		this.nombre = nombre;
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Wifi> getWifis() {
		return wifis;
	}

	public void setWifis(Set<Wifi> wifis) {
		this.wifis = wifis;
	}

}