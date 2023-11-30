package vn.fs.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hang")
public class Hang implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idhang;
	
	private String tenhang;


	public Hang(Long idhang, String tenhang) {
		super();
		this.idhang = idhang;
		this.tenhang = tenhang;
	}



	public Long getIdhang() {
		return idhang;
	}



	public void setIdhang(Long idhang) {
		this.idhang = idhang;
	}



	public String getTenhang() {
		return tenhang;
	}



	public void setTenhang(String tenhang) {
		this.tenhang = tenhang;
	}



	public Hang() {
		super();
	}
	
}
