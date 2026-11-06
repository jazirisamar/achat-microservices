package tn.esprit.rh.achat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Vue minimale d'un produit, recu depuis product-service. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProduitDto {

	private Long idProduit;
	private String libelleProduit;
	private float prix;

	public Long getIdProduit() { return idProduit; }
	public void setIdProduit(Long idProduit) { this.idProduit = idProduit; }
	public String getLibelleProduit() { return libelleProduit; }
	public void setLibelleProduit(String libelleProduit) { this.libelleProduit = libelleProduit; }
	public float getPrix() { return prix; }
	public void setPrix(float prix) { this.prix = prix; }
}
