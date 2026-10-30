package tn.esprit.rh.achat.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import tn.esprit.rh.achat.dto.ProduitDto;

/** Appel REST vers product-service (decouvert par son nom dans Eureka). */
@FeignClient(name = "product-service")
public interface ProduitClient {

	@GetMapping("/produit/retrieve-produit/{produit-id}")
	ProduitDto retrieveProduit(@PathVariable("produit-id") Long produitId);
}
