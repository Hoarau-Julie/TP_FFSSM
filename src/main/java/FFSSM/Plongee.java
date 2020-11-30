/**
 * @(#) Plongee.java
 */
package FFSSM;

import java.time.LocalDate;
import java.util.LinkedList;

public class Plongee {

	public Site lieu;
	public Moniteur chefDePalanquee;
	public LocalDate date;
	public int profondeur;
	public int duree;
        public LinkedList<Plongeur> participants = new LinkedList<>();

	public Plongee(Moniteur chef){
            this.chefDePalanquee = chef;
        }
        
        public Plongee(Site lieu, Moniteur chefDePalanquee, LocalDate date, int profondeur, int duree) {
		this.lieu = lieu;
		this.chefDePalanquee = chefDePalanquee;
		this.date = date;
		this.profondeur = profondeur;
		this.duree = duree;
	}

	public void ajouteParticipant(Plongeur participant) throws Exception 
        {
                if(participants.contains(participant)){
                    throw new Exception("Ce plongeur a déjà été inscrit pour cette plongée");
                }    
                this.participants.add(participant);
	}

	public LocalDate getDate() {
		return date;
	}
        
        public void setDate(LocalDate date){
            this.date = date;
        }

	/**
	 * Détermine si la plongée est conforme. 
	 * Une plongée est conforme si tous les plongeurs de la palanquée ont une
	 * licence valide à la date de la plongée
	 * @return vrai si la plongée est conforme
	 */
	public boolean estConforme()
        {
            //si le chef de palanquee n'a pas de licence
            if(this.chefDePalanquee.getLicence() == null)
            {
                return false;
            }
            //si le chef de palanquee a une licence expirée
            if(!this.chefDePalanquee.getLicence().estValide(this.date))
            {
                return false;
            }
            //si l'un des participants n'a pas de licence ou une licence expirée
            if(!this.participants.isEmpty())
            {
                for(int i=0; i < participants.size(); i++)
                {
                    if(participants.get(i).getLicence() == null)
                    {
                        return false;
                    }
                    else if(!participants.get(i).getLicence().estValide(this.date))
                    {
                        return false;
                    }
                }
            }
                return true;
        }

}
