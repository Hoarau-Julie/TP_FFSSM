
import FFSSM.Club;
import FFSSM.GroupeSanguin;
import FFSSM.Moniteur;
import FFSSM.Plongee;
import FFSSM.Plongeur;
import FFSSM.Site;
import java.time.LocalDate;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;



/**
 *
 * @author Hoarau Julie
 */
public class FFSSMJUnitTests {


    Plongeur P1;   
    Plongeur P2;  
    Plongeur P3;   
    Moniteur M1;  
    Moniteur M2;  
    Club WarnerBros;    
    Site Bermudes;
    Plongee Pl1;
    Plongee Pl2;
    
    
        
    @BeforeEach
    public void setUp() {
        P1 = new Plongeur("1990129480096","Bunny","Bugs","15 avenue Warner Bros, LA","0715248395",LocalDate.of(1999,1,23),GroupeSanguin.APLUS);
        P2 = new Plongeur("1990129480097","Duck","Daffy","15 avenue Warner Bros, LA","0762548673",LocalDate.of(1999,1,23),GroupeSanguin.BPLUS);
        P3 = new Plongeur("1990129480098","Marroon","Tazz","15 avenue Warner Bros, LA","0745620314",LocalDate.of(1999,1,23),GroupeSanguin.BPLUS);
        M1 = new Moniteur("1650829490102","Bunny","Lola","15 avenue Warner Bros, LA","0714756234",LocalDate.of(1965,8,22),GroupeSanguin.AMOINS,50349812);
        M2 = new Moniteur("1551029480012","Titi","Grominet","14 avenue Warner Bros, LA","0725489325",LocalDate.of(1955,10,11),GroupeSanguin.BMOINS,10452357);
        WarnerBros = new Club(M2,"Looney Tunes Scuba Diving","0123568478");
        Bermudes = new Site("Bermudes Los Angeles","La Toons squale");
        Pl1 = new Plongee(Bermudes, M1, LocalDate.of(2020,11,25), 37, 57);
        Pl2 = new Plongee(M1);
        
    }
    
    /**
     * Test l'ajout d'une licence à un plongeur
     * @throws Exception 
     */
    @Test
    public void testAjouteLicence() throws Exception{
        P2.setClub(WarnerBros);
        P2.ajouteLicence("125J15634D", LocalDate.of(2020,9,15));
        
        assertEquals(P2, P2.getLicence().getPossesseur(),
                "La licence n'a pas été ajoutée au bon plongeur");
        
        assertEquals(WarnerBros, P2.getLicence().getClub(),
                "La club délivrant la licence n'est pas le bon");
        
        assertEquals("125J15634D", P2.getLicence().getNumero(),
                "Le numéro de licence ajouté n'est pas le bon");
        
        assertEquals(LocalDate.of(2020,9,15),P2.getLicence().getDelivrance(),
                "La date de délivrance de la licence ajoutée n'est pas la bonne");
        
    }
    
    /**
     * Test l'organisation d'une plongee par un club
     */
    @Test
    public void testOrganisePlongee() throws Exception{
        WarnerBros.organisePlongee(Pl1);
        
        assertTrue(WarnerBros.plongees.contains(Pl1),
                "Ce club doit être organisateur de la plongée Pl1");
        
        //force le déclenchement d'une exception
        Exception thrown = assertThrows(Exception.class,
                () -> WarnerBros.organisePlongee(Pl1));
        
        //s'assure que le message d'exception affiché soit le bon
        assertEquals("Cette plongée existe déjà",
                thrown.getMessage(),
                "Une plongee ne peut être créée qu'une seule fois");
        
        WarnerBros.organisePlongee(Pl2);
        
        assertTrue(WarnerBros.plongees.contains(Pl2)); 
    }
    
    /**
     * Test d'ajout de articipants à une plongee
     * @java.lang.Exception 
     */
    @Test
    public void testAjouteParticipant()throws Exception{
        Pl2.ajouteParticipant(P1);
        
        assertTrue(Pl2.participants.contains(P1),
                "Ce participant n'a pas été ajouté à la plongée");
        
        
        //Test de l'exception
        Exception thrown = assertThrows(Exception.class,
                () -> Pl1.ajouteParticipant(P1));
        
        assertEquals("Ce plongeur est déjà inscrit pour cette plongée",
                thrown.getMessage(),
                "Un plongeur ne peut être inscrit qu'une seule fois à une même plongée");
        
        Pl1.ajouteParticipant(P3);
        assertTrue(Pl1.participants.contains(P3));
    }
    
    /**
     * Test de création d'une plongée non conforme
     */
    @Test
    public void testPlongeeNonConforme() throws Exception{
        WarnerBros.organisePlongee(Pl2);
        Pl2.setDate(LocalDate.of(2020,11,29));
        
        //si le chef de palanquee n'a pas de licence
        assertFalse(Pl2.estConforme(),
                "Cette plongée ne doit pas être conforme");
        
        //si le chef de palanquée a une licence non valide
        M1.setClub(WarnerBros);
        M1.ajouteLicence("548A73628M", LocalDate.of(2018,8,12));
        
        assertFalse(Pl2.estConforme());
        
        //si le chef a une licence mais pas un des plongeurs
        M1.ajouteLicence("536A74368M", LocalDate.of(2020,8,12));
        Pl2.ajouteParticipant(P2);
                
        assertFalse(Pl2.estConforme(),
                "Cette plongée ne doit pas être conforme");
        
        //si un participant a une licence non valide
        P2.setClub(WarnerBros);
        P2.ajouteLicence("356P89152I", LocalDate.of(2015,3,6));
        
        assertFalse(Pl2.estConforme());
        
        assertTrue(WarnerBros.plongeesNonConformes().contains(Pl2),
                "Cette plongée devrait apparaître dans les plongées non conformes du club");
        
    }
    
    /**
     * Test plongee conforme
     */
    @Test
    public void testPlongeeConforme() throws Exception{
        WarnerBros.organisePlongee(Pl1);
        M1.setClub(WarnerBros);
        M1.ajouteLicence("536A74368M", LocalDate.of(2020,8,12));
        
        assertTrue(Pl1.estConforme());
        
        P1.setClub(WarnerBros);
        P1.ajouteLicence("231T54896K",LocalDate.of(2020,9,15));
        Pl1.ajouteParticipant(P1);
        
        assertTrue(Pl1.estConforme(),
                "Cette plongée doit être conforme");
        
        assertTrue(WarnerBros.plongeesNonConformes().isEmpty(),
                "ce club ne doit avoir aucune plongée non conforme");
        
    }
    
    /**
     * Test des geters des classes Site, Plongee, Plongeur
     */
    @Test
    public void testGet(){
        //Site.getDetails
        assertEquals("La Toons squale", Bermudes.getDetails());
        //Site.getNom
        assertEquals("Bermudes Los Angeles", Bermudes.getNom());
        //Plongee.getDate
        assertEquals(LocalDate.of(2020,11,25),Pl1.getDate());
        //Plongeur.getGroupe
        assertEquals(GroupeSanguin.APLUS, P1.getGroupe());
        //Plongeur.getClub
        P1.setClub(WarnerBros);
        assertEquals(WarnerBros, P1.getClub());
        //Plongeur.getNiveau
        P1.setNiveau(2);
        assertEquals(2, P1.getNiveau());
        //Personne.getNumeroINSE
        assertEquals("1551029480012",M2.getNumeroINSEE());
    }
    
    /**
     * Test des seters des classes Site, Personne, Club
     */
    @Test
    public void testSet(){
        //Site.setDetails
        Bermudes.setDetails("test détails");
        assertEquals("test détails", Bermudes.getDetails());
        //Site.setNom
        Bermudes.setNom("test nom");
        assertEquals("test nom", Bermudes.getNom());
        //Personne.setNaissance
        M2.setNaissance(LocalDate.of(1964,3,18));
        assertEquals(LocalDate.of(1964,3,18),M2.getNaissance());
        //Personne.setTelephone
        M2.setTelephone("0718033415");
        assertEquals("0718033415",M2.getTelephone());
        //Personne.setAdresse
        P3.setAdresse("test adresse");
        assertEquals("test adresse",P3.getAdresse());
        //Personne.setNom
        P3.setNom("test nom");
        assertEquals("test nom", P3.getNom());
        //Personne.setPrenom
        P3.setPrenom("test prenom");
        assertEquals("test prenom", P3.getPrenom());
        //Club.setTelephone
        WarnerBros.setTelephone("0607080910");
        assertEquals("0607080910",WarnerBros.getTelephone());
        //Club.setAdresse
        WarnerBros.setAdresse("test adresse");
        assertEquals("test adresse", WarnerBros.getAdresse());
        //Club.setNom
        WarnerBros.setNom("test nom");
        assertEquals("test nom",WarnerBros.getNom());
        //Club.setPresident
        WarnerBros.setPresident(M1);
        assertEquals(M1,WarnerBros.getPresident());
    }
    
    /**
     * Test des toString de Site et Club
     */
    public void testToString(){
        //Site.toString
        assertEquals("Site{nom=Bermudes Los Angeles, details=La Toons squale}",
                Bermudes.toString());
        //Club.toString
        WarnerBros.setAdresse("15 Quai Looney port, LA");
        assertEquals("Club{président=" + M1 + ", nom=Looney Tunes Scuba Diving, adresses=15 Quai Looney port, LA, telephone=0123568478}",
                WarnerBros.toString());
    }
}